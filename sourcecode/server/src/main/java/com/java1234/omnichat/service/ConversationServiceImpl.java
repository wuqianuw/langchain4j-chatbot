package com.java1234.omnichat.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java1234.omnichat.entity.Conversation;
import com.java1234.omnichat.mapper.ConversationMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会话服务实现类
 *
 * @author Java1234
 */
@Service
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper, Conversation> implements ConversationService {

    /**
     * 获取用户的会话列表(按更新时间倒序)
     *
     * @param userId 用户ID
     * @return 会话列表
     */
    @Override
    public List<Conversation> listByUserId(Long userId) {
        return lambdaQuery()
                .eq(Conversation::getUserId, userId)
                .orderByDesc(Conversation::getUpdateTime)
                .list();
    }

    /**
     * 创建新会话
     *
     * @param userId 用户ID
     * @param title  会话标题
     * @return 新会话
     */
    @Override
    public Conversation createConversation(Long userId, String title) {
        Conversation conversation = new Conversation();
        conversation.setUserId(userId);
        conversation.setTitle(title != null ? title : "新对话");
        save(conversation);
        return conversation;
    }
}
