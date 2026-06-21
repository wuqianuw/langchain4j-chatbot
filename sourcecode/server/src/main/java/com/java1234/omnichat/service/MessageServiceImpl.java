package com.java1234.omnichat.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java1234.omnichat.entity.Message;
import com.java1234.omnichat.mapper.MessageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息服务实现类
 *
 * @author Java1234
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    /**
     * 获取会话的消息列表(按时间正序)
     *
     * @param conversationId 会话ID
     * @return 消息列表
     */
    @Override
    public List<Message> listByConversationId(Long conversationId) {
        return lambdaQuery()
                .eq(Message::getConversationId, conversationId)
                .orderByAsc(Message::getCreateTime)
                .list();
    }

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
    @Override
    public Message saveUserMessage(Long conversationId, Long userId, String contentType, String content, String fileUrl) {
        Message message = new Message();
        message.setConversationId(conversationId);
        message.setUserId(userId);
        message.setRole("user");
        message.setContentType(contentType);
        message.setContent(content);
        message.setFileUrl(fileUrl);
        message.setTokens(0);
        save(message);
        return message;
    }

    /**
     * 保存助手回复消息
     *
     * @param conversationId 会话ID
     * @param userId         用户ID
     * @param content        回复内容
     * @param tokens         消耗Token数
     * @return 消息实体
     */
    @Override
    public Message saveAssistantMessage(Long conversationId, Long userId, String content, Integer tokens) {
        Message message = new Message();
        message.setConversationId(conversationId);
        message.setUserId(userId);
        message.setRole("assistant");
        message.setContentType("text");
        message.setContent(content);
        message.setTokens(tokens != null ? tokens : 0);
        save(message);
        return message;
    }
}
