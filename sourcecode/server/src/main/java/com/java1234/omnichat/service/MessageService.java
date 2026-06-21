package com.java1234.omnichat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.java1234.omnichat.entity.Message;

import java.util.List;

/**
 * 消息服务接口
 *
 * @author Java1234
 */
public interface MessageService extends IService<Message> {

    /**
     * 获取会话的消息列表
     *
     * @param conversationId 会话ID
     * @return 消息列表
     */
    List<Message> listByConversationId(Long conversationId);

    /**
     * 保存用户消息
     *
     * @param conversationId 会话ID
     * @param userId         用户ID
     * @param contentType    内容类型
     * @param content        文本内容
     * @param fileUrl        附件URL
     * @return 消息实体
     */
    Message saveUserMessage(Long conversationId, Long userId, String contentType, String content, String fileUrl);

    /**
     * 保存助手回复消息
     *
     * @param conversationId 会话ID
     * @param userId         用户ID
     * @param content        回复内容
     * @param tokens         消耗Token数
     * @return 消息实体
     */
    Message saveAssistantMessage(Long conversationId, Long userId, String content, Integer tokens);
}
