package com.java1234.omnichat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java1234.omnichat.entity.Message;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息Mapper接口
 *
 * @author Java1234
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
