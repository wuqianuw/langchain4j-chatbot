package com.java1234.omnichat.config;

import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatRequestParameters;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * LangChain4j大模型配置类
 *
 * @author Java1234
 */
@Configuration
public class LangChainConfig {

    @Value("${langchain4j.openai.base-url}")
    private String baseUrl;

    @Value("${langchain4j.openai.api-key}")
    private String apiKey;

    @Value("${langchain4j.openai.model-name}")
    private String modelName;

    @Value("${langchain4j.openai.timeout}")
    private Duration timeout;

    /**
     * 创建OpenAI兼容的流式聊天模型Bean
     * 用于调用阿里云DashScope Qwen3.5-Omni-Plus全模态模型
     *
     * @return 流式聊天模型
     */
    @Bean
    public StreamingChatModel streamingChatModel() {
        // Qwen Omni 默认可能返回音频流，LangChain4j 仅解析文本 delta，需指定仅输出文本
        Map<String, Object> customParameters = Map.of(
                "modalities", List.of("text")
        );

        return OpenAiStreamingChatModel.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .modelName(modelName)
                .timeout(timeout)
                .logRequests(true)
                .logResponses(true)
                .defaultRequestParameters(OpenAiChatRequestParameters.builder()
                        .customParameters(customParameters)
                        .build())
                .build();
    }
}
