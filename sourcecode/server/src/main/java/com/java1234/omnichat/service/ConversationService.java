package com.java1234.omnichat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.java1234.omnichat.entity.Conversation;

import java.util.List;

/**
 * 会话服务接口
 *
 * @author Java1234
 */
public interface ConversationService extends IService<Conversation> {

    /**
     * 获取用户的会话列表
     *
     * @param userId 用户ID
     * @return 会话列表
     */
    List<Conversation> listByUserId(Long userId);

    /**
     * 创建新会话
     *
     * @param userId 用户ID
     * @param title  会话标题
     * @return 新会话
     */
    Conversation createConversation(Long userId, String title);
}
