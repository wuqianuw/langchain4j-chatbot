package com.java1234.omnichat.common;

import com.java1234.omnichat.entity.User;
import com.java1234.omnichat.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

/**
 * JWT认证拦截器
 * 校验请求令牌并注入当前用户信息
 *
 *
 */
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    /**
     * 请求前置处理 - 校验JWT令牌
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 检查是否需要登录
        boolean requireLogin = handlerMethod.hasMethodAnnotation(RequireLogin.class)
                || handlerMethod.getBeanType().isAnnotationPresent(RequireLogin.class);

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (token != null && jwtUtil.validateToken(token)) {
            Long userId = jwtUtil.getUserId(token);
            User user = userService.getById(userId);
            if (user != null && user.getStatus() == 1) {
                request.setAttribute("currentUser", user);
                request.setAttribute("userId", userId);
                request.setAttribute("userRole", user.getRole());

                // 角色权限校验
                RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
                if (requireRole == null) {
                    requireRole = handlerMethod.getBeanType().getAnnotation(RequireRole.class);
                }
                if (requireRole != null) {
                    boolean hasRole = Arrays.asList(requireRole.value()).contains(user.getRole());
                    if (!hasRole) {
                        throw new BusinessException(403, "无权限访问");
                    }
                }
                return true;
            }
        }

        if (requireLogin) {
            throw new BusinessException(401, "未登录或登录已过期");
        }
        return true;
    }
}
