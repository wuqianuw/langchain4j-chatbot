package com.java1234.omnichat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.java1234.omnichat.entity.User;

/**
 * 用户服务接口
 *
 * @author Java1234
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户实体
     */
    User findByUsername(String username);
}
