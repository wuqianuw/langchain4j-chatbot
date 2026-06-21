package com.java1234.omnichat.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 直接调用阿里云 DashScope Qwen Omni 流式接口
 * 音频需使用 data:audio/xxx;base64,... 格式，LangChain4j 裸 base64 不被 Qwen 接受
 */
@Slf4j
@Service
public class QwenOmniStreamService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    @Value("${langchain4j.openai.base-url}")
    private String baseUrl;

    @Value("${langchain4j.openai.api-key}")
    private String apiKey;

    @Value("${langchain4j.openai.model-name}")
    private String modelName;

    @Value("${langchain4j.openai.timeout}")
    private Duration timeout;

    /**
     * 流式调用 Qwen Omni
     *
     * @param messages      OpenAI 兼容 messages 数组
     * @param onPartial     文本片段回调
     * @param onTokens      token 用量回调
     * @param onComplete    完成回调(返回完整文本)
     * @param onError       错误回调
     */
    public void streamChat(List<Map<String, Object>> messages,
                           Consumer<String> onPartial,
                           Consumer<Integer> onTokens,
                           Consumer<String> onComplete,
                           Consumer<String> onError) {
        try {
            ObjectNode body = objectMapper.createObjectNode();
            body.put("model", modelName);
            body.set("messages", objectMapper.valueToTree(messages));
            body.put("stream", true);
            ArrayNode modalities = objectMapper.createArrayNode();
            modalities.add("text");
            body.set("modalities", modalities);
            ObjectNode streamOptions = objectMapper.createObjectNode();
            streamOptions.put("include_usage", true);
            body.set("stream_options", streamOptions);

            String url = baseUrl.endsWith("/") ? baseUrl + "chat/completions" : baseUrl + "/chat/completions";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(timeout)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString(), StandardCharsets.UTF_8))
                    .build();

            log.debug("Qwen Omni 请求: model={}, messages={}", modelName, messages.size());

            HttpResponse<java.io.InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() != 200) {
                String errBody = new String(response.body().readAllBytes(), StandardCharsets.UTF_8);
                log.error("Qwen API HTTP {}: {}", response.statusCode(), errBody);
                onError.accept(parseApiError(errBody));
                return;
            }

            StringBuilder fullText = new StringBuilder();
            Integer totalTokens = null;

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.body(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith("data:")) {
                        continue;
                    }
                    String data = line.substring(5).trim();
                    if (data.isEmpty() || "[DONE]".equals(data)) {
                        continue;
                    }

                    JsonNode root = objectMapper.readTree(data);
                    if (root.has("error")) {
                        String msg = root.path("error").path("message").asText("API调用失败");
                        log.error("Qwen API 错误: {}", msg);
                        onError.accept(msg);
                        return;
                    }

                    JsonNode choices = root.get("choices");
                    if (choices != null && choices.isArray() && !choices.isEmpty()) {
                        JsonNode delta = choices.get(0).get("delta");
                        if (delta != null && delta.has("content") && !delta.get("content").isNull()) {
                            String chunk = delta.get("content").asText();
                            if (!chunk.isEmpty()) {
                                fullText.append(chunk);
                                onPartial.accept(chunk);
                            }
                        }
                    }

                    JsonNode usage = root.get("usage");
                    if (usage != null && usage.has("total_tokens")) {
                        totalTokens = usage.get("total_tokens").asInt();
                    }
                }
            }

            if (totalTokens != null) {
                onTokens.accept(totalTokens);
            }

            String reply = fullText.toString();
            if (reply.isBlank()) {
                onError.accept("AI未返回有效内容，请重试");
            } else {
                onComplete.accept(reply);
            }
        } catch (Exception e) {
            log.error("Qwen Omni 流式调用异常", e);
            onError.accept("大模型调用失败: " + e.getMessage());
        }
    }

    private String parseApiError(String body) {
        try {
            JsonNode root = objectMapper.readTree(body);
            if (root.has("error")) {
                return root.path("error").path("message").asText("API调用失败");
            }
        } catch (Exception ignored) {
            // ignore
        }
        return body.length() > 200 ? body.substring(0, 200) : body;
    }
}
