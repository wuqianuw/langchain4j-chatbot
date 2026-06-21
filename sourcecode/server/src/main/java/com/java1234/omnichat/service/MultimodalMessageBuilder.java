package com.java1234.omnichat.service;

import com.java1234.omnichat.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 构建 Qwen Omni OpenAI 兼容多模态 messages
 */
@Slf4j
@Component
public class MultimodalMessageBuilder {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.url-prefix}")
    private String urlPrefix;

    /**
     * 构建完整 messages 列表(含 system、历史、当前消息)
     */
    public List<Map<String, Object>> buildMessages(List<Message> history,
                                                   String content,
                                                   String contentType,
                                                   String fileUrl) {
        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content",
                "你是全模态智能助手，基于LangChain4j和Qwen3.5-Omni-Plus模型。你可以理解文本、图片、音频和视频内容，请用中文友好地回答用户问题。"));

        int start = Math.max(0, history.size() - 21);
        for (int i = start; i < history.size() - 1; i++) {
            Message msg = history.get(i);
            if ("user".equals(msg.getRole())) {
                messages.add(buildUserMessage(msg.getContent(), msg.getContentType(), msg.getFileUrl()));
            } else if ("assistant".equals(msg.getRole())) {
                messages.add(Map.of("role", "assistant", "content", msg.getContent() != null ? msg.getContent() : ""));
            }
        }

        messages.add(buildUserMessage(content, contentType, fileUrl));
        return messages;
    }

    private Map<String, Object> buildUserMessage(String content, String contentType, String fileUrl) {
        List<Map<String, Object>> parts = new ArrayList<>();

        // 媒体内容放在文本之前，与 Qwen 官方示例一致
        if (fileUrl != null && !fileUrl.isBlank()) {
            Map<String, Object> mediaPart = buildMediaPart(contentType, fileUrl);
            if (mediaPart != null) {
                parts.add(mediaPart);
            }
        }

        if (content != null && !content.isBlank()) {
            parts.add(Map.of("type", "text", "text", content));
        }

        if (parts.isEmpty()) {
            return Map.of("role", "user", "content", "你好");
        }

        if (parts.size() == 1 && parts.get(0).containsKey("text")) {
            return Map.of("role", "user", "content", parts.get(0).get("text"));
        }

        return Map.of("role", "user", "content", parts);
    }

    private Map<String, Object> buildMediaPart(String contentType, String fileUrl) {
        try {
            Path filePath = resolveFilePath(fileUrl);
            if (!Files.exists(filePath)) {
                log.warn("附件文件不存在: {}", filePath);
                return null;
            }
            byte[] bytes = Files.readAllBytes(filePath);
            String base64 = Base64.getEncoder().encodeToString(bytes);
            String lower = filePath.toString().toLowerCase();

            switch (contentType) {
                case "audio" -> {
                    String format = getAudioFormat(lower);
                    String dataUrl = "data:audio/" + format + ";base64," + base64;
                    Map<String, Object> inputAudio = new HashMap<>();
                    inputAudio.put("data", dataUrl);
                    inputAudio.put("format", format);
                    Map<String, Object> part = new HashMap<>();
                    part.put("type", "input_audio");
                    part.put("input_audio", inputAudio);
                    return part;
                }
                case "image" -> {
                    String mime = getImageMime(lower);
                    String dataUrl = "data:" + mime + ";base64," + base64;
                    Map<String, Object> part = new HashMap<>();
                    part.put("type", "image_url");
                    part.put("image_url", Map.of("url", dataUrl));
                    return part;
                }
                case "video" -> {
                    String mime = getVideoMime(lower);
                    String dataUrl = "data:" + mime + ";base64," + base64;
                    Map<String, Object> part = new HashMap<>();
                    part.put("type", "video_url");
                    part.put("video_url", Map.of("url", dataUrl));
                    return part;
                }
                default -> {
                    return null;
                }
            }
        } catch (Exception e) {
            log.warn("读取附件失败: {}", fileUrl, e);
            return null;
        }
    }

    private Path resolveFilePath(String fileUrl) {
        String normalizedUrl = fileUrl.replace("\\", "/");
        String relativePath = normalizedUrl;
        if (relativePath.startsWith(urlPrefix)) {
            relativePath = normalizedUrl.substring(urlPrefix.length());
            if (relativePath.startsWith("/")) {
                relativePath = relativePath.substring(1);
            }
        }
        return Paths.get(uploadPath, relativePath);
    }

    private String getAudioFormat(String fileName) {
        if (fileName.endsWith(".wav")) return "wav";
        if (fileName.endsWith(".mp3")) return "mp3";
        if (fileName.endsWith(".ogg")) return "ogg";
        if (fileName.endsWith(".aac")) return "aac";
        if (fileName.endsWith(".flac")) return "flac";
        if (fileName.endsWith(".m4a")) return "m4a";
        if (fileName.endsWith(".amr")) return "amr";
        return "mp3";
    }

    private String getImageMime(String fileName) {
        if (fileName.endsWith(".png")) return "image/png";
        if (fileName.endsWith(".gif")) return "image/gif";
        if (fileName.endsWith(".webp")) return "image/webp";
        return "image/jpeg";
    }

    private String getVideoMime(String fileName) {
        if (fileName.endsWith(".webm")) return "video/webm";
        if (fileName.endsWith(".avi")) return "video/x-msvideo";
        return "video/mp4";
    }
}
