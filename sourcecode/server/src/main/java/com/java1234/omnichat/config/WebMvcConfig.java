package com.java1234.omnichat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置类 - 静态资源映射
 *
 * @author Java1234
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.url-prefix}")
    private String urlPrefix;

    /**
     * 配置上传文件的静态资源映射
     *
     * @param registry 资源处理器注册器
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = "file:" + uploadPath.replace("\\", "/") + "/";
        // context-path 为 /api 时，资源映射路径不应再包含 /api 前缀
        String resourcePath = urlPrefix.startsWith("/api") ? urlPrefix.substring(4) : urlPrefix;
        registry.addResourceHandler(resourcePath + "/**")
                .addResourceLocations(location);
    }
}
