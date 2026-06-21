package com.java1234.omnichat.controller;

import com.java1234.omnichat.common.BusinessException;
import com.java1234.omnichat.common.CurrentUser;
import com.java1234.omnichat.common.RequireLogin;
import com.java1234.omnichat.common.Result;
import com.java1234.omnichat.entity.User;
import com.java1234.omnichat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 全模态聊天控制器 - SSE流式接口
 *
 * @author Java1234
 */
@RestController
@RequestMapping("/chat")
@RequireLogin
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * 流式全模态聊天接口(SSE)
     * 支持文本、图片、音频、视频输入理解
     *
     * @param conversationId 会话ID(新建时可为空)
     * @param content        文本内容
     * @param contentType    内容类型: text/image/audio/video
     * @param fileUrl        附件URL
     * @param currentUser    当前用户
     * @return SSE发射器
     */
    @GetMapping("/stream")
    public SseEmitter streamChat(
            @RequestParam(required = false) Long conversationId,
            @RequestParam(required = false) String content,
            @RequestParam(defaultValue = "text") String contentType,
            @RequestParam(required = false) String fileUrl,
            @CurrentUser User currentUser) {

        if ((content == null || content.isBlank()) && (fileUrl == null || fileUrl.isBlank())) {
            throw new BusinessException("消息内容不能为空");
        }

        SseEmitter emitter = new SseEmitter(120000L);
        chatService.streamChat(currentUser.getId(), conversationId, content,
                contentType, fileUrl, emitter);
        return emitter;
    }
}
