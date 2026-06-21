package com.java1234.omnichat.controller;

import com.java1234.omnichat.common.BusinessException;
import com.java1234.omnichat.common.JwtUtil;
import com.java1234.omnichat.common.Md5Util;
import com.java1234.omnichat.common.Result;
import com.java1234.omnichat.dto.LoginRequest;
import com.java1234.omnichat.dto.LoginResponse;
import com.java1234.omnichat.dto.RegisterRequest;
import com.java1234.omnichat.entity.User;
import com.java1234.omnichat.service.LoginLogService;
import com.java1234.omnichat.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器 - 登录/注册
 *
 * @author Java1234
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final LoginLogService loginLogService;

    /**
     * 用户登录
     *
     * @param request     登录请求
     * @param httpRequest HTTP请求(获取IP)
     * @return 登录响应(含JWT令牌)
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        User user = userService.findByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        if (!Md5Util.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        loginLogService.recordLogin(user.getId(), getClientIp(httpRequest));

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        response.setRole(user.getRole());
        response.setAvatar(user.getAvatar());
        return Result.success("登录成功", response);
    }

    /**
     * 用户注册
     *
     * @param request 注册请求
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        if (userService.findByUsername(request.getUsername()) != null) {
            throw new BusinessException("用户名已存在");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(Md5Util.encrypt(request.getPassword()));
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
        user.setRole("USER");
        user.setStatus(1);
        userService.save(user);
        return Result.success("注册成功", null);
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
