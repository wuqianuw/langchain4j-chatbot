package com.java1234.omnichat.controller;

import com.java1234.omnichat.common.BusinessException;
import com.java1234.omnichat.common.CurrentUser;
import com.java1234.omnichat.common.RequireLogin;
import com.java1234.omnichat.common.Result;
import com.java1234.omnichat.entity.Conversation;
import com.java1234.omnichat.entity.Message;
import com.java1234.omnichat.entity.User;
import com.java1234.omnichat.service.ConversationService;
import com.java1234.omnichat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息控制器
 *
 * @author Java1234
 */
@RestController
@RequestMapping("/message")
@RequireLogin
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final ConversationService conversationService;

    /**
     * 获取会话的消息历史
     *
     * @param conversationId 会话ID
     * @param currentUser      当前用户
     * @return 消息列表
     */
    @GetMapping("/list/{conversationId}")
    public Result<List<Message>> list(@PathVariable Long conversationId,
                                      @CurrentUser User currentUser) {
        Conversation conv = conversationService.getById(conversationId);
        if (conv == null || !conv.getUserId().equals(currentUser.getId())) {
            throw new BusinessException("会话不存在");
        }
        return Result.success(messageService.listByConversationId(conversationId));
    }
}
