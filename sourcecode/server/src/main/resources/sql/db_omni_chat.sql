-- =============================================
-- Java1234 基于LangChain4j的全模态聊天机器人系统
-- 数据库: db_omni_chat  MySQL8 端口3308
-- =============================================

CREATE DATABASE IF NOT EXISTS `db_omni_chat` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `db_omni_chat`;

-- ----------------------------
-- 用户表
-- ----------------------------
DROP TABLE IF EXISTS `t_login_log`;
DROP TABLE IF EXISTS `t_message`;
DROP TABLE IF EXISTS `t_conversation`;
DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username`    VARCHAR(50)  NOT NULL COMMENT '用户名',
    `password`    VARCHAR(64)  NOT NULL COMMENT '密码(MD5加密)',
    `nickname`    VARCHAR(50)  DEFAULT NULL COMMENT '昵称',
    `avatar`      VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `role`        VARCHAR(20)  NOT NULL DEFAULT 'USER' COMMENT '角色: ADMIN/USER',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 1启用 0禁用',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- 会话表
-- ----------------------------
CREATE TABLE `t_conversation` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`     BIGINT       NOT NULL COMMENT '所属用户ID',
    `title`       VARCHAR(200) NOT NULL DEFAULT '新对话' COMMENT '会话标题',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会话表';

-- ----------------------------
-- 消息表
-- ----------------------------
CREATE TABLE `t_message` (
    `id`               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `conversation_id`  BIGINT       NOT NULL COMMENT '会话ID',
    `user_id`          BIGINT       NOT NULL COMMENT '用户ID',
    `role`             VARCHAR(20)  NOT NULL COMMENT '角色: user/assistant',
    `content_type`     VARCHAR(20)  NOT NULL DEFAULT 'text' COMMENT '内容类型: text/image/audio/video',
    `content`          TEXT         DEFAULT NULL COMMENT '文本内容',
    `file_url`         VARCHAR(500) DEFAULT NULL COMMENT '附件URL',
    `tokens`           INT          DEFAULT 0 COMMENT '消耗Token数',
    `create_time`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_conversation_id` (`conversation_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';

-- ----------------------------
-- 登录日志表
-- ----------------------------
CREATE TABLE `t_login_log` (
    `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`     BIGINT      NOT NULL COMMENT '用户ID',
    `ip`          VARCHAR(50) DEFAULT NULL COMMENT '登录IP',
    `login_time`  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';

-- =============================================
-- 测试数据 (密码123456的MD5: e10adc3949ba59abbe56e057f20f883e)
-- =============================================

INSERT INTO `t_user` (`username`, `password`, `nickname`, `role`, `status`, `create_time`) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', 'ADMIN', 1, '2026-01-15 09:00:00'),
('zhangsan', 'e10adc3949ba59abbe56e057f20f883e', '张三', 'USER', 1, '2026-02-10 10:30:00'),
('lisi', 'e10adc3949ba59abbe56e057f20f883e', '李四', 'USER', 1, '2026-02-12 14:20:00'),
('wangwu', 'e10adc3949ba59abbe56e057f20f883e', '王五', 'USER', 1, '2026-03-05 16:45:00'),
('zhaoliu', 'e10adc3949ba59abbe56e057f20f883e', '赵六', 'USER', 0, '2026-03-20 11:00:00');

INSERT INTO `t_conversation` (`user_id`, `title`, `create_time`) VALUES
(2, '关于人工智能的讨论', '2026-03-01 10:00:00'),
(2, '图片识别测试', '2026-03-15 14:30:00'),
(3, '音频转写咨询', '2026-04-02 09:15:00'),
(4, '视频内容分析', '2026-05-10 16:00:00'),
(2, '日常闲聊', '2026-06-01 20:00:00');

INSERT INTO `t_message` (`conversation_id`, `user_id`, `role`, `content_type`, `content`, `file_url`, `tokens`, `create_time`) VALUES
(1, 2, 'user', 'text', '你好，请介绍一下LangChain4j框架', NULL, 0, '2026-03-01 10:00:05'),
(1, 2, 'assistant', 'text', 'LangChain4j是一个专为Java开发者设计的LLM应用开发框架，它提供了统一的API来集成各种大语言模型，支持聊天、嵌入、RAG等功能。', NULL, 120, '2026-03-01 10:00:15'),
(1, 2, 'user', 'text', '它支持哪些大模型？', NULL, 0, '2026-03-01 10:01:00'),
(1, 2, 'assistant', 'text', 'LangChain4j支持20多种主流LLM提供商，包括OpenAI、Google Vertex AI、Anthropic Claude、阿里云通义千问等，还支持Ollama本地模型。', NULL, 95, '2026-03-01 10:01:10'),
(2, 2, 'user', 'image', '请描述这张图片的内容', '/uploads7/demo/cat.jpg', 0, '2026-03-15 14:30:05'),
(2, 2, 'assistant', 'text', '这是一张可爱的猫咪照片，猫咪有着橙色的毛发，正坐在窗台上看着窗外。', NULL, 80, '2026-03-15 14:30:20'),
(3, 3, 'user', 'audio', '请帮我转写这段音频', '/uploads7/demo/audio.mp3', 0, '2026-04-02 09:15:05'),
(3, 3, 'assistant', 'text', '音频转写结果：今天天气很好，适合出去散步。', NULL, 60, '2026-04-02 09:15:25'),
(4, 4, 'user', 'video', '请分析这段视频的主要内容', '/uploads7/demo/video.mp4', 0, '2026-05-10 16:00:05'),
(4, 4, 'assistant', 'text', '视频展示了一段城市风景，包含高楼大厦和繁忙的街道，整体氛围现代都市。', NULL, 75, '2026-05-10 16:00:30'),
(5, 2, 'user', 'text', '今天心情不错', NULL, 0, '2026-06-01 20:00:05'),
(5, 2, 'assistant', 'text', '很高兴听到你心情不错！有什么我可以帮你的吗？', NULL, 40, '2026-06-01 20:00:12'),
(1, 2, 'user', 'text', '全模态模型有什么优势？', NULL, 0, '2026-06-10 11:00:00'),
(1, 2, 'assistant', 'text', '全模态模型可以同时理解和处理文本、图片、音频、视频等多种输入形式，实现更自然的人机交互体验。', NULL, 88, '2026-06-10 11:00:15');

INSERT INTO `t_login_log` (`user_id`, `ip`, `login_time`) VALUES
(1, '127.0.0.1', '2026-06-01 08:00:00'),
(1, '127.0.0.1', '2026-06-05 09:30:00'),
(1, '127.0.0.1', '2026-06-10 10:00:00'),
(2, '192.168.1.100', '2026-06-02 14:00:00'),
(2, '192.168.1.100', '2026-06-08 16:30:00'),
(3, '192.168.1.101', '2026-06-03 11:00:00'),
(3, '192.168.1.101', '2026-06-09 15:00:00'),
(4, '192.168.1.102', '2026-06-04 09:00:00'),
(2, '192.168.1.100', '2026-06-12 10:30:00'),
(3, '192.168.1.101', '2026-06-13 08:45:00');
