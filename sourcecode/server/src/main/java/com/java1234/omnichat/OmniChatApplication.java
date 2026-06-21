package com.java1234.omnichat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *  基于LangChain4j的全模态聊天机器人系统 - 启动类
 *
 * @author Java1234
 */
@SpringBootApplication
@MapperScan("com.java1234.omnichat.mapper")
public class OmniChatApplication {

    /**
     * 应用程序入口
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(OmniChatApplication.class, args);
    }
}
