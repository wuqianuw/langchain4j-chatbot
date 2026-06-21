<p align="center">
  <span style="display:inline-flex;align-items:center;justify-content:center;width:72px;height:72px;background:linear-gradient(135deg,#1a56db 0%,#4f46e5 50%,#7c3aed 100%);border-radius:20px;color:#fff;font-size:32px;font-weight:700;margin-bottom:16px">往</span>
</p>

<h1 align="center">往 · 全模态智能助手</h1>

<p align="center">
  基于 LangChain4j + Spring Boot 4 + Vue 3 的全模态聊天机器人系统
  <br>
  支持文本 · 图片 · 音频 · 视频多模态交互
</p>

---

## 📋 项目简介

「往」是一个企业级的全模态聊天机器人系统，后端基于 **LangChain4j** 框架集成大语言模型 API，前端采用 **Vue 3** 构建现代化交互界面。系统支持文本、图片、音频、视频等多种模态的输入与理解，提供流式对话、会话管理、用户管理、数据统计等完整功能。

## ✨ 功能特性

### 💬 智能对话
- **多模态输入** — 支持文本、图片、音频、视频文件上传与分析
- **流式输出** — SSE 实时流式响应，打字机效果
- **会话管理** — 多会话切换、新建、删除

### 🖥️ 管理后台
- **数据统计** — ECharts 可视化仪表盘（消息趋势、模态占比、活跃用户等）
- **用户管理** — 用户列表、新增、编辑、删除、角色与状态管理
- **会话管理** — 全平台会话概览与检索

### 🔐 安全体系
- **JWT 认证** — Token 鉴权与自动续期
- **角色控制** — 管理员/普通用户二级权限
- **请求拦截** — Axios 拦截器统一处理 401 自动跳转

---

## 🛠️ 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| **前端框架** | Vue 3 | ^3.5 |
| **构建工具** | Vite | ^6.0 |
| **UI 组件库** | Element Plus | ^2.9 |
| **状态管理** | Pinia | ^2.3 |
| **路由** | Vue Router | ^4.5 |
| **HTTP 客户端** | Axios | ^1.7 |
| **图表** | ECharts | ^5.6 |
| **日期处理** | Day.js | ^1.11 |
| **后端框架** | Spring Boot | 4.0 |
| **ORM** | MyBatis-Plus | 3.5 |
| **AI 框架** | LangChain4j | 1.15 |
| **认证** | JWT (jjwt) | 0.12 |
| **数据库** | MySQL | 8.x |

---

## 🚀 快速开始

### 前置要求

- **Node.js** >= 18
- **Java** >= 17
- **Maven** >= 3.6
- **MySQL** >= 8.0

### 1. 克隆项目

```bash
git clone https://github.com/wuqianuw/langchain4j-chatbot.git
cd langchain4j-chatbot
```

### 2. 初始化数据库

```sql
source db/db_omni_chat2.sql
```

### 3. 启动后端

```bash
cd sourcecode/server

# 修改 application.yml 中的数据库连接和 AI API 配置
# 配置 DashScope API Key (通义千问多模态模型)

./mvnw spring-boot:run
```

### 4. 启动前端

```bash
cd sourcecode/client
npm install
npm run dev
```

访问 `http://localhost:5173` 即可使用。

---

## ⚙️ 配置说明

### 后端配置 (`server/src/main/resources/application.yml`)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/omni_chat?useUnicode=true&characterEncoding=utf-8
    username: root
    password: your_password

langchain4j:
  open-ai:
    chat-model:
      api-key: your-dashscope-api-key
      model-name: qwen-vl-max
      base-url: https://dashscope.aliyuncs.com/compatible-mode/v1
```

### 前端配置 (`client/vite.config.js`)

前端默认通过 Vite 代理将 `/api` 请求转发到后端 `localhost:8080`，无需额外配置。

---

## 📁 项目结构

```
├── sourcecode/
│   ├── client/                          # Vue 3 前端
│   │   ├── src/
│   │   │   ├── api/                     # API 接口层
│   │   │   ├── router/                  # 路由配置
│   │   │   ├── store/                   # Pinia 状态管理
│   │   │   ├── utils/                   # 工具函数
│   │   │   ├── styles/                  # 全局样式
│   │   │   └── views/                   # 页面组件
│   │   │       ├── admin/               # 管理后台
│   │   │       ├── ChatView.vue         # 聊天主界面
│   │   │       ├── Login.vue            # 登录
│   │   │       └── Register.vue         # 注册
│   │   └── vite.config.js
│   └── server/                          # Spring Boot 后端
│       └── src/main/java/com/java1234/omnichat/
│           ├── common/                  # 公共工具（JWT、异常、结果封装）
│           ├── config/                  # 配置类（CORS、MyBatis-Plus、LangChain4j）
│           ├── controller/              # REST API 控制器
│           ├── dto/                     # 数据传输对象
│           ├── entity/                  # 实体类
│           ├── mapper/                  # MyBatis-Plus 数据访问
│           └── service/                 # 业务逻辑层
├── db/                                  # 数据库脚本
└── README.md
```

---

## 🧩 核心架构

```
┌─────────────┐     HTTP/SSE      ┌──────────────┐     API Call     ┌─────────────┐
│  Vue 3 SPA  │ ──────────────►   │ Spring Boot  │ ──────────────►  │  DashScope  │
│  (Element+) │ ◄──────────────   │  + LangChain4j│  ◄─────────────  │ (通义千问VL) │
│             │    JSON/Stream    │              │                  │             │
│  ECharts    │                   │  MyBatis-Plus │                  │  多模态大模型│
│  仪表盘     │                   │  ──── MySQL   │                  │             │
└─────────────┘                   └──────────────┘                  └─────────────┘
```

- **前端**：Vue 3 SPA，Element Plus UI，Pinia 状态管理
- **通信**：Axios HTTP + SSE 流式响应
- **后端**：Spring Boot 4 REST API，LangChain4j AI 编排
- **数据**：MyBatis-Plus + MySQL
- **AI**：通义千问 VL 多模态模型（DashScope API）

---

## 📄 开源协议

[MIT License](LICENSE)

---

<p align="center">
  Made with ❤️ — 往 · 全模态智能助手
</p>
