package com.java1234.omnichat.config;

import com.java1234.omnichat.common.BusinessException;
import com.java1234.omnichat.common.CurrentUser;
import com.java1234.omnichat.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 当前登录用户参数解析器
 * 将请求中的用户信息注入到Controller方法参数
 *
 * @author Java1234
 */
@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * 判断是否支持该参数类型
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && parameter.getParameterType().equals(User.class);
    }

    /**
     * 解析当前登录用户参数
     *
     * @param parameter     方法参数
     * @param mavContainer  模型视图容器
     * @param webRequest    Web请求
     * @param binderFactory 数据绑定工厂
     * @return 当前登录用户
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new BusinessException(401, "未登录");
        }
        User user = (User) request.getAttribute("currentUser");
        if (user == null) {
            throw new BusinessException(401, "未登录");
        }
        return user;
    }
}
