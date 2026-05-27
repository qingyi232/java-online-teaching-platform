# 基于Java的线上教学资源平台

## 项目简介
本项目是一个基于 Spring Boot + Vue + ElementUI 的线上教学资源管理平台，实现教学资源的统一管理、师生互动交流和数据统计分析功能。

## 技术栈

### 后端
- **Spring Boot 2.7.18** - 后端框架
- **MyBatis-Plus 3.5.3** - ORM框架
- **MySQL 8.0** - 关系型数据库
- **Redis** - 缓存（热门资源缓存，提升访问性能）
- **MinIO** - 分布式文件存储（课件、视频等非结构化资源）
- **JWT** - 用户认证与权限控制

### 前端
- **Vue 2.6** - 前端框架
- **ElementUI 2.15** - UI组件库
- **Axios** - HTTP请求
- **ECharts 5.4** - 数据可视化
- **Vuex** - 状态管理
- **Vue Router** - 路由管理

## 功能模块

### 1. 用户与权限管理模块
- 用户注册、登录（JWT认证）
- 三种角色：学生、教师、管理员
- 角色权限控制（拦截器+前端路由守卫）
- 用户管理（管理员：查看、禁用/启用、重置密码、删除）
- 个人中心（修改个人信息、修改密码）

### 2. 教学资源管理模块
- 多类型资源上传（PDF、PPT、Word、视频、图片等）
- 资源分类管理（管理员维护分类体系）
- 资源检索（按名称、分类、类型搜索）
- 资源审核（教师/管理员审核待发布资源）
- 资源下载（MinIO预签名URL）
- 我的资源（查看个人上传的资源）
- Redis缓存热门资源，减少数据库压力

### 3. 师生互动模块
- 资源评论（支持树形结构评论）
- 资源收藏/取消收藏
- 问答中心（提问、回复、标记已解决）
- 问题可关联资源

### 4. 资源统计分析模块
- 仪表盘概览（用户数、资源数、浏览量、下载量）
- 浏览趋势图（近7天/30天折线图）
- 下载趋势图（近7天/30天折线图）
- 分类浏览分布（饼图）
- 下载量TOP10资源（柱状图）

## 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- MinIO（对象存储服务）
- Node.js 14+

## 部署步骤

### 1. 数据库初始化
```bash
mysql -u root -p < sql/init.sql
```

### 2. 启动Redis
```bash
redis-server
```

### 3. 启动MinIO
```bash
minio server /data --console-address ":9001"
```
默认账号密码：minioadmin / minioadmin

### 4. 启动后端
```bash
cd backend
# 修改 application.yml 中的数据库、Redis、MinIO配置
mvn spring-boot:run
```
后端服务运行在 http://localhost:8088

### 5. 启动前端
```bash
cd frontend
npm install
npm run serve
```
前端服务运行在 http://localhost:8080

## 默认账号
| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |

## 项目结构
```
├── sql/                          # 数据库脚本
│   └── init.sql
├── backend/                      # 后端项目
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/teaching/
│       │   ├── TeachingPlatformApplication.java
│       │   ├── common/           # 通用类（Result、JWT、异常处理）
│       │   ├── config/           # 配置类（Redis、MinIO、CORS）
│       │   ├── controller/       # 控制器层
│       │   ├── entity/           # 实体类
│       │   ├── interceptor/      # 拦截器（权限校验）
│       │   ├── mapper/           # 数据访问层
│       │   └── service/          # 业务逻辑层
│       └── resources/
│           ├── application.yml
│           └── mapper/           # MyBatis XML映射
├── frontend/                     # 前端项目
│   ├── package.json
│   ├── vue.config.js
│   └── src/
│       ├── api/                  # API接口
│       ├── router/               # 路由配置
│       ├── store/                # Vuex状态管理
│       ├── utils/                # 工具类
│       └── views/                # 页面组件
│           ├── Login.vue
│           ├── Register.vue
│           ├── Layout.vue
│           ├── Home.vue
│           ├── resource/         # 资源管理页面
│           ├── question/         # 问答页面
│           ├── favorite/         # 收藏页面
│           ├── user/             # 用户管理页面
│           ├── category/         # 分类管理页面
│           └── statistics/       # 数据统计页面
└── README.md
```
