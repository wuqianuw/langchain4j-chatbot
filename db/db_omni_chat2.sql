/*
SQLyog Community v13.2.0 (64 bit)
MySQL - 8.1.0 : Database - db_omni_chat2
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`db_omni_chat2` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `db_omni_chat2`;

/*Table structure for table `t_conversation` */

DROP TABLE IF EXISTS `t_conversation`;

CREATE TABLE `t_conversation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '所属用户ID',
  `title` varchar(200) NOT NULL DEFAULT '新对话' COMMENT '会话标题',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='会话表';

/*Data for the table `t_conversation` */

insert  into `t_conversation`(`id`,`user_id`,`title`,`create_time`,`update_time`) values 
(1,2,'关于人工智能的讨论','2026-03-01 10:00:00','2026-06-14 13:56:13'),
(2,2,'图片识别测试','2026-03-15 14:30:00','2026-06-14 13:56:13'),
(3,3,'音频转写咨询','2026-04-02 09:15:00','2026-06-14 13:56:13'),
(4,4,'视频内容分析','2026-05-10 16:00:00','2026-06-14 13:56:13'),
(8,2,'你是谁？','2026-06-14 14:50:20','2026-06-14 14:50:20'),
(9,6,'你是谁？','2026-06-14 14:56:27','2026-06-14 14:56:27'),
(10,6,'你好啊','2026-06-14 15:04:40','2026-06-14 15:04:40'),
(12,6,'识别下mp3内容','2026-06-14 15:10:36','2026-06-14 15:10:36'),
(13,6,'识别下mp3音频内容','2026-06-14 15:11:49','2026-06-14 15:11:49'),
(14,6,'识别下Mp3音频内容','2026-06-14 15:18:55','2026-06-14 15:18:55');

/*Table structure for table `t_login_log` */

DROP TABLE IF EXISTS `t_login_log`;

CREATE TABLE `t_login_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `ip` varchar(50) DEFAULT NULL COMMENT '登录IP',
  `login_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='登录日志表';

/*Data for the table `t_login_log` */

insert  into `t_login_log`(`id`,`user_id`,`ip`,`login_time`) values 
(1,1,'127.0.0.1','2026-06-01 08:00:00'),
(2,1,'127.0.0.1','2026-06-05 09:30:00'),
(3,1,'127.0.0.1','2026-06-10 10:00:00'),
(4,2,'192.168.1.100','2026-06-02 14:00:00'),
(5,2,'192.168.1.100','2026-06-08 16:30:00'),
(6,3,'192.168.1.101','2026-06-03 11:00:00'),
(7,3,'192.168.1.101','2026-06-09 15:00:00'),
(8,4,'192.168.1.102','2026-06-04 09:00:00'),
(9,2,'192.168.1.100','2026-06-12 10:30:00'),
(10,3,'192.168.1.101','2026-06-13 08:45:00'),
(11,1,'127.0.0.1','2026-06-14 13:57:31'),
(12,2,'127.0.0.1','2026-06-14 13:58:35'),
(13,2,'127.0.0.1','2026-06-14 13:58:51'),
(14,2,'127.0.0.1','2026-06-14 14:49:53'),
(15,6,'127.0.0.1','2026-06-14 14:56:12'),
(16,6,'127.0.0.1','2026-06-14 15:01:23'),
(17,6,'127.0.0.1','2026-06-14 15:04:20'),
(18,1,'127.0.0.1','2026-06-14 15:19:39'),
(19,1,'127.0.0.1','2026-06-14 15:20:10'),
(20,1,'127.0.0.1','2026-06-14 15:20:21'),
(21,1,'127.0.0.1','2026-06-14 20:49:43');

/*Table structure for table `t_message` */

DROP TABLE IF EXISTS `t_message`;

CREATE TABLE `t_message` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `conversation_id` bigint NOT NULL COMMENT '会话ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role` varchar(20) NOT NULL COMMENT '角色: user/assistant',
  `content_type` varchar(20) NOT NULL DEFAULT 'text' COMMENT '内容类型: text/image/audio/video',
  `content` text COMMENT '文本内容',
  `file_url` varchar(500) DEFAULT NULL COMMENT '附件URL',
  `tokens` int DEFAULT '0' COMMENT '消耗Token数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_conversation_id` (`conversation_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='消息表';

/*Data for the table `t_message` */

insert  into `t_message`(`id`,`conversation_id`,`user_id`,`role`,`content_type`,`content`,`file_url`,`tokens`,`create_time`) values 
(1,1,2,'user','text','你好，请介绍一下LangChain4j框架',NULL,0,'2026-03-01 10:00:05'),
(2,1,2,'assistant','text','LangChain4j是一个专为Java开发者设计的LLM应用开发框架，它提供了统一的API来集成各种大语言模型，支持聊天、嵌入、RAG等功能。',NULL,120,'2026-03-01 10:00:15'),
(3,1,2,'user','text','它支持哪些大模型？',NULL,0,'2026-03-01 10:01:00'),
(4,1,2,'assistant','text','LangChain4j支持20多种主流LLM提供商，包括OpenAI、Google Vertex AI、Anthropic Claude、阿里云通义千问等，还支持Ollama本地模型。',NULL,95,'2026-03-01 10:01:10'),
(5,2,2,'user','image','请描述这张图片的内容','/uploads7/demo/cat.jpg',0,'2026-03-15 14:30:05'),
(6,2,2,'assistant','text','这是一张可爱的猫咪照片，猫咪有着橙色的毛发，正坐在窗台上看着窗外。',NULL,80,'2026-03-15 14:30:20'),
(7,3,3,'user','audio','请帮我转写这段音频','/uploads7/demo/audio.mp3',0,'2026-04-02 09:15:05'),
(8,3,3,'assistant','text','音频转写结果：今天天气很好，适合出去散步。',NULL,60,'2026-04-02 09:15:25'),
(9,4,4,'user','video','请分析这段视频的主要内容','/uploads7/demo/video.mp4',0,'2026-05-10 16:00:05'),
(10,4,4,'assistant','text','视频展示了一段城市风景，包含高楼大厦和繁忙的街道，整体氛围现代都市。',NULL,75,'2026-05-10 16:00:30'),
(11,5,2,'user','text','今天心情不错',NULL,0,'2026-06-01 20:00:05'),
(12,5,2,'assistant','text','很高兴听到你心情不错！有什么我可以帮你的吗？',NULL,40,'2026-06-01 20:00:12'),
(13,1,2,'user','text','全模态模型有什么优势？',NULL,0,'2026-06-10 11:00:00'),
(14,1,2,'assistant','text','全模态模型可以同时理解和处理文本、图片、音频、视频等多种输入形式，实现更自然的人机交互体验。',NULL,88,'2026-06-10 11:00:15'),
(15,5,2,'user','image','识别下这个图片','/api/uploads7/2026/06/14/62d6d8b49d20424ca6c7f5fe8237442b.png',0,'2026-06-14 14:36:59'),
(16,5,2,'assistant','text','这张图片展示的是一堆新鲜红润的苹果，摆放在一块白色做旧风格的木制托盘上。苹果表面带有晶莹的水珠，显得格外清新诱人，部分苹果还带着翠绿的叶子，增添了自然生机。背景是温暖的木质桌面和模糊的绿叶，整体光线柔和明亮，营造出一种温馨、健康、丰收的氛围。\n\n右下角有“豆包AI生成”字样，说明这是一张由人工智能生成的图像，而非真实拍摄。\n\n✅ 总结：  \n**内容**：新鲜带水珠的红苹果 + 绿叶 + 白色木托盘  \n**风格**：写实风、高饱和度、暖色调、静物摄影感  \n**来源**：AI 生成（豆包AI）  \n**用途建议**：可用于水果广告、健康饮食宣传、电商主图、美食博客配图等\n\n需要我帮你分析构图、色彩搭配，或者生成类似风格的提示词吗？ ?',NULL,2784,'2026-06-14 14:37:09'),
(17,5,2,'user','audio','识别下这个 mp3内容','/api/uploads7/2026/06/14/c795448a1d2d4604acc713c24f899973.mp3',0,'2026-06-14 14:37:48'),
(18,5,2,'assistant','text','',NULL,0,'2026-06-14 14:37:52'),
(19,5,2,'user','text','这个能识别吗',NULL,0,'2026-06-14 14:39:03'),
(20,5,2,'assistant','text','',NULL,0,'2026-06-14 14:39:07'),
(21,6,2,'user','video','识别下这个视频','/api/uploads7/2026/06/14/0fff2a80fdd64d3098b99b08bb69a00d.mp4',0,'2026-06-14 14:39:46'),
(22,6,2,'assistant','text','这段视频展现了一个充满诗意的瞬间：\n\n一颗饱满红润的苹果挂在枝头，夕阳的金光穿透叶片洒在它身上，表面还挂着晶莹的水珠。突然，苹果从树上脱落，在空中划出一道优美的弧线，最后轻轻落在翠绿的草地上。整个过程在逆光中显得格外梦幻，光影交错间充满了自然的美感。',NULL,6155,'2026-06-14 14:39:51'),
(23,7,2,'user','audio','识别下这个Mp3音频内容','/api/uploads7/2026/06/14/1ac56ef2900041a5b24a15216c5566e3.mp3',0,'2026-06-14 14:40:27'),
(24,7,2,'assistant','text','',NULL,0,'2026-06-14 14:40:28'),
(25,5,2,'user','text','你是谁？',NULL,0,'2026-06-14 14:50:02'),
(26,5,2,'assistant','text','',NULL,0,'2026-06-14 14:50:07'),
(27,8,2,'user','text','你是谁？',NULL,0,'2026-06-14 14:50:20'),
(28,8,2,'assistant','text','你好！我是 **Java1234 全模态智能助手**。\n\n我基于强大的 **LangChain4j** 框架和最新的 **Qwen3.5-Omni-Plus** 模型构建而成。作为一个“全模态”助手，我不只局限于文字交流，还能像人类一样感知和理解多种信息形式：\n\n*    **文本**：解答编程问题、撰写代码、逻辑推理或日常聊天。\n*   ?️ **图片**：识别图像内容、分析图表数据或解读截图中的代码报错。\n*   ? **音频**：理解语音指令、分析音频内容或进行语音交互。\n*   ? **视频**：解析视频画面、总结视频要点或回答关于视频内容的问题。\n\n无论你是需要解决复杂的 Java 技术难题，还是想让我帮你分析一张架构图、一段会议录音甚至是一个教学视频，我都能随时为你提供帮助！\n\n有什么我可以帮你的吗？',NULL,272,'2026-06-14 14:50:26'),
(29,9,6,'user','text','你是谁？',NULL,0,'2026-06-14 14:56:27'),
(30,9,6,'assistant','text','你好！我是 **Java1234 全模态智能助手**。\n\n我基于强大的 **LangChain4j** 框架和最新的 **Qwen3.5-Omni-Plus** 模型构建而成。作为一个全能型的 AI 伙伴，我不仅能流畅地处理文字对话，还具备深度的多模态理解能力：\n\n*    **看懂图片**：识别图表、代码截图或生活场景。\n*   ? **听懂音频**：解析语音内容或声音细节。\n*   ? **理解视频**：分析视频中的动作、情节及关键信息。\n*   ? **精通代码**：特别是在 Java 生态及相关开发领域，我能提供从基础语法到架构设计的全面支持。\n\n无论是需要解答技术难题、分析多媒体内容，还是日常聊天，我都随时准备着用中文为你提供最友好、最精准的帮助！有什么我可以帮你的吗？',NULL,261,'2026-06-14 14:56:33'),
(31,9,6,'user','image','识别下图片','/api/uploads7/2026/06/14/5ccea468357d4c63868f78eb296ad68d.png',0,'2026-06-14 14:57:06'),
(32,9,6,'assistant','text','这是一张非常诱人的**红苹果静物摄影图**。\n\n以下是图片中的详细内容识别：\n\n1.  **主体内容**：\n    *   画面中心是一堆新鲜饱满的**红苹果**，数量大约有9-10个。\n    *   苹果表皮光滑油亮，呈现出鲜艳的红色，部分区域带有自然的黄绿色过渡，看起来非常成熟。\n    *   每个苹果上都点缀着晶莹剔透的**水珠**，仿佛刚被清洗过，极大地增强了新鲜感和食欲。\n    *   许多苹果还保留着翠绿的**叶子**和短小的果柄，增添了自然生机。\n\n2.  **摆放与背景**：\n    *   苹果们随意地堆叠在一块**白色的木质托盘或砧板**上。这块木板有做旧的质感，边缘可见磨损和露出的原木色，风格质朴温馨。\n    *   背景是温暖的**浅棕色木质桌面**，并且做了虚化处理（浅景深），使得焦点完全集中在前景的苹果上。\n    *   左上角隐约可见一些模糊的绿色植物枝叶，丰富了画面层次。\n\n3.  **光影与氛围**：\n    *   光线柔和明亮，像是从侧上方打来的自然光，在苹果表面形成了漂亮的高光和阴影，突出了其圆润的立体感。\n    *   整体色调温暖、和谐，营造出一种**健康、天然、丰收**的愉悦氛围。\n\n4.  **其他信息**：\n    *   图片右下角有“**豆包AI生成**”的水印，表明这张精美的图片是由人工智能生成的，而非真实拍摄。\n\n**总结来说**：这是一张由AI生成的高质量美食/静物摄影图，主题是新鲜多汁的红苹果，构图精美，色彩诱人，旨在展现水果的新鲜与自然之美。',NULL,3158,'2026-06-14 14:57:22'),
(33,10,6,'user','text','你好啊',NULL,0,'2026-06-14 15:04:40'),
(34,10,6,'assistant','text','你好呀！? 很高兴见到你。\n\n我是 **Java1234 全模态智能助手**，基于强大的 LangChain4j 框架和 Qwen3.5-Omni-Plus 模型构建而成。我不光能和你畅聊文字，还能帮你：\n\n*    **看懂图片**：分析图表、识别物体或解读截图中的代码。\n*   ? **听懂音频**：处理语音指令或分析录音内容。\n*   ? **理解视频**：提取视频关键信息或解答相关疑问。\n*   ? **搞定代码**：特别是 Java 相关的编程问题、架构设计或调试建议。\n\n有什么想聊的，或者需要我帮忙处理的文件（图片、音视频等），随时告诉我哦！',NULL,236,'2026-06-14 15:04:46'),
(35,10,6,'user','video','识别下这个视频内容','/api/uploads7/2026/06/14/7e1df3f2ae6046e8bbbcc66b118faa17.mp4',0,'2026-06-14 15:04:59'),
(36,10,6,'assistant','text','收到，正在为您分析这段视频... ?\n\n这是一个非常唯美且充满诗意的**AI生成短片**。画面主要展示了一颗红苹果从树上成熟到落下的全过程，充满了自然之美。\n\n以下是我观察到的详细内容：\n\n1.  **晨光中的特写** ?\n    视频开始于一个清晨的果园，阳光透过树叶洒下斑驳的光影。镜头聚焦在一颗挂在枝头的**红苹果**上，果皮上布满了晶莹剔透的露珠，在逆光下闪闪发光，看起来非常新鲜诱人。\n\n2.  **自然的坠落** ?\n    随后，苹果似乎熟透了，轻轻脱离了树枝。它在空中缓慢旋转下落，背景是虚化的绿色草地和温暖的阳光（Bokeh效果），画面极具电影感。\n\n3.  **回归大地** ☀️\n    最后，苹果轻柔地落在翠绿的草地上，激起微小的尘土或水雾，静静地躺在那里，享受着阳光的沐浴。\n\n? **特别发现**：\n我在视频的角落看到了“**豆包AI生成**”的水印，这说明这段精美的视频是由人工智能技术创作的，而非实拍，展现了目前AI视频生成的高水准。\n\n整体氛围宁静、治愈，让人感受到丰收的喜悦和大自然的静谧。您觉得这个视频怎么样？需要我基于这个画面帮您写一段Java代码来模拟这种物理下落效果，还是想聊聊AI视频生成的技术原理呢？',NULL,6558,'2026-06-14 15:05:11'),
(37,11,6,'user','audio','识别下这个mp3音频内容','/api/uploads7/2026/06/14/34cf06be53c0415381069410fbe8a75f.mp3',0,'2026-06-14 15:05:43'),
(38,11,6,'assistant','text','',NULL,0,'2026-06-14 15:05:44'),
(39,12,6,'user','audio','识别下mp3内容','/api/uploads7/2026/06/14/c953417ee70940259f5148d1c5f48415.mp3',0,'2026-06-14 15:10:36'),
(40,13,6,'user','audio','识别下mp3音频内容','/api/uploads7/2026/06/14/f4211c98f25a4069893a57b6de159f7f.mp3',0,'2026-06-14 15:11:49'),
(41,14,6,'user','audio','识别下Mp3音频内容','/api/uploads7/2026/06/14/95e05577184e437e98b94d8ffdbb19e6.mp3',0,'2026-06-14 15:18:55'),
(42,14,6,'assistant','text','晓峰老师您好！很高兴为您服务。\n\n经过对您提供的音频内容进行识别，其中的文字内容如下：\n\n**“我是小峰老师，今天很开心讲这个课程。”**\n\n如果您需要针对这段课程内容进行总结、翻译，或者想基于此生成相关的教学大纲、代码示例（毕竟我是 Java1234 助手嘛 ?），随时告诉我哦！',NULL,171,'2026-06-14 15:18:59');

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码(MD5加密)',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `role` varchar(20) NOT NULL DEFAULT 'USER' COMMENT '角色: ADMIN/USER',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态: 1启用 0禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='用户表';

/*Data for the table `t_user` */

insert  into `t_user`(`id`,`username`,`password`,`nickname`,`avatar`,`role`,`status`,`create_time`,`update_time`) values 
(1,'java1234','e10adc3949ba59abbe56e057f20f883e','系统管理员',NULL,'ADMIN',1,'2026-01-15 09:00:00','2026-06-14 20:48:01'),
(2,'zhangsan','e10adc3949ba59abbe56e057f20f883e','张三',NULL,'USER',1,'2026-02-10 10:30:00','2026-06-14 13:56:13'),
(3,'lisi','e10adc3949ba59abbe56e057f20f883e','李四',NULL,'USER',1,'2026-02-12 14:20:00','2026-06-14 13:56:13'),
(4,'wangwu','e10adc3949ba59abbe56e057f20f883e','王五',NULL,'USER',1,'2026-03-05 16:45:00','2026-06-14 13:56:13'),
(5,'zhaoliu','e10adc3949ba59abbe56e057f20f883e','赵六',NULL,'USER',0,'2026-03-20 11:00:00','2026-06-14 13:56:13'),
(6,'jack','e10adc3949ba59abbe56e057f20f883e','',NULL,'USER',1,'2026-06-14 14:55:56','2026-06-14 14:55:56');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
