package com.java1234.omnichat.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java1234.omnichat.entity.LoginLog;
import com.java1234.omnichat.mapper.LoginLogMapper;
import org.springframework.stereotype.Service;

/**
 * 登录日志服务实现类
 *
 * @author Java1234
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    /**
     * 记录登录日志
     *
     * @param userId 用户ID
     * @param ip     登录IP
     */
    @Override
    public void recordLogin(Long userId, String ip) {
        LoginLog log = new LoginLog();
        log.setUserId(userId);
        log.setIp(ip);
        save(log);
    }
}
