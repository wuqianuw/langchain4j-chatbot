package com.java1234.omnichat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java1234.omnichat.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 *
 * @author Java1234
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
