package com.java1234.omnichat.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java1234.omnichat.entity.User;
import com.java1234.omnichat.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 *
 * @author Java1234
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户实体
     */
    @Override
    public User findByUsername(String username) {
        return lambdaQuery().eq(User::getUsername, username).one();
    }
}
