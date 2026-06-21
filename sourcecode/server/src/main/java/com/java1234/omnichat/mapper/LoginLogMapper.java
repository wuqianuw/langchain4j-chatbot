package com.java1234.omnichat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java1234.omnichat.entity.LoginLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录日志Mapper接口
 *
 * @author Java1234
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {
}
