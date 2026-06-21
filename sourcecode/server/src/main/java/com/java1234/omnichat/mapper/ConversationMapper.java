package com.java1234.omnichat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java1234.omnichat.entity.Conversation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会话Mapper接口
 *
 * @author Java1234
 */
@Mapper
public interface ConversationMapper extends BaseMapper<Conversation> {
}
