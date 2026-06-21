package com.java1234.omnichat.service;

import com.java1234.omnichat.common.BusinessException;
import com.java1234.omnichat.entity.Conversation;
import com.java1234.omnichat.entity.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 全模态聊天服务
 * 通过 DashScope API 直接流式调用，正确处理音频 data URL 格式
 *
 * @author Java1234
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final QwenOmniStreamService qwenOmniStreamService;
    private final MultimodalMessageBuilder messageBuilder;
    private final ConversationService conversationService;
    private final MessageService messageService;

    /**
     * 执行流式全模态聊天
     */
    public void streamChat(Long userId, Long conversationId, String content,
                           String contentType, String fileUrl, SseEmitter emitter) {
        try {
            Conversation conversation;
            if (conversationId == null) {
                String title = content != null && !content.isBlank()
                        ? content.substring(0, Math.min(content.length(), 20))
                        : "新对话";
                conversation = conversationService.createConversation(userId, title);
                emitter.send(SseEmitter.event().name("conversation").data(conversation.getId()));
            } else {
                conversation = conversationService.getById(conversationId);
                if (conversation == null || !conversation.getUserId().equals(userId)) {
                    throw new BusinessException("会话不存在");
                }
            }

            Long convId = conversation.getId();
            messageService.saveUserMessage(convId, userId, contentType, content, fileUrl);

            List<Message> history = messageService.listByConversationId(convId);
            List<Map<String, Object>> messages = messageBuilder.buildMessages(history, content, contentType, fileUrl);

            AtomicReference<Integer> tokens = new AtomicReference<>(0);

            qwenOmniStreamService.streamChat(messages,
                    partial -> {
                        try {
                            emitter.send(SseEmitter.event().name("message").data(partial));
                        } catch (IOException e) {
                            log.error("SSE发送失败", e);
                        }
                    },
                    tokenCount -> tokens.set(tokenCount),
                    reply -> {
                        try {
                            messageService.saveAssistantMessage(convId, userId, reply, tokens.get());
                            conversationService.updateById(conversation);
                            emitter.send(SseEmitter.event().name("done").data("[DONE]"));
                            emitter.complete();
                        } catch (IOException e) {
                            log.error("SSE完成发送失败", e);
                            emitter.completeWithError(e);
                        }
                    },
                    error -> {
                        try {
                            emitter.send(SseEmitter.event().name("error").data(error));
                            emitter.complete();
                        } catch (IOException e) {
                            log.error("SSE错误发送失败", e);
                            emitter.completeWithError(e);
                        }
                    }
            );
        } catch (Exception e) {
            log.error("聊天处理异常", e);
            emitter.completeWithError(e);
        }
    }
}
