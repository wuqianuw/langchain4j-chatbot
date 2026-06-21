package com.java1234.omnichat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.java1234.omnichat.entity.LoginLog;

/**
 * 登录日志服务接口
 *
 * @author Java1234
 */
public interface LoginLogService extends IService<LoginLog> {

    /**
     * 记录登录日志
     *
     * @param userId 用户ID
     * @param ip     登录IP
     */
    void recordLogin(Long userId, String ip);
}
