-- 线上教学资源平台数据库初始化脚本
CREATE DATABASE IF NOT EXISTS teaching_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE teaching_platform;

-- 用户表
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码（加密）',
    real_name VARCHAR(50) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像URL',
    role VARCHAR(20) NOT NULL DEFAULT 'STUDENT' COMMENT '角色：STUDENT-学生，TEACHER-教师，ADMIN-管理员',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 资源分类表
DROP TABLE IF EXISTS resource_category;
CREATE TABLE resource_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID，0为顶级分类',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源分类表';

-- 教学资源表
DROP TABLE IF EXISTS teaching_resource;
CREATE TABLE teaching_resource (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '资源ID',
    title VARCHAR(200) NOT NULL COMMENT '资源标题',
    description TEXT COMMENT '资源描述',
    category_id BIGINT COMMENT '分类ID',
    file_url VARCHAR(500) COMMENT '文件存储路径（MinIO）',
    file_name VARCHAR(255) COMMENT '原始文件名',
    file_size BIGINT DEFAULT 0 COMMENT '文件大小（字节）',
    file_type VARCHAR(50) COMMENT '文件类型（pdf/doc/ppt/mp4等）',
    cover_url VARCHAR(500) COMMENT '封面图URL',
    upload_user_id BIGINT NOT NULL COMMENT '上传用户ID',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '审核状态：0-待审核，1-已通过，2-已拒绝',
    download_count INT DEFAULT 0 COMMENT '下载次数',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_category (category_id),
    KEY idx_upload_user (upload_user_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教学资源表';

-- 资源评论表
DROP TABLE IF EXISTS resource_comment;
CREATE TABLE resource_comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评论ID',
    resource_id BIGINT NOT NULL COMMENT '资源ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    content TEXT NOT NULL COMMENT '评论内容',
    parent_id BIGINT DEFAULT 0 COMMENT '父评论ID，0为顶级评论',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_resource (resource_id),
    KEY idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源评论表';

-- 资源收藏表
DROP TABLE IF EXISTS resource_favorite;
CREATE TABLE resource_favorite (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',
    resource_id BIGINT NOT NULL COMMENT '资源ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_resource_user (resource_id, user_id),
    KEY idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源收藏表';

-- 问答表
DROP TABLE IF EXISTS question_answer;
CREATE TABLE question_answer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '问题ID',
    title VARCHAR(200) NOT NULL COMMENT '问题标题',
    content TEXT NOT NULL COMMENT '问题内容',
    user_id BIGINT NOT NULL COMMENT '提问用户ID',
    resource_id BIGINT COMMENT '关联资源ID',
    status TINYINT DEFAULT 0 COMMENT '状态：0-未解决，1-已解决',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_user (user_id),
    KEY idx_resource (resource_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问答表';

-- 问答回复表
DROP TABLE IF EXISTS qa_reply;
CREATE TABLE qa_reply (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '回复ID',
    question_id BIGINT NOT NULL COMMENT '问题ID',
    user_id BIGINT NOT NULL COMMENT '回复用户ID',
    content TEXT NOT NULL COMMENT '回复内容',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_question (question_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问答回复表';

-- 资源浏览记录表
DROP TABLE IF EXISTS resource_view_log;
CREATE TABLE resource_view_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    resource_id BIGINT NOT NULL COMMENT '资源ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_resource (resource_id),
    KEY idx_user (user_id),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源浏览记录表';

-- 资源下载记录表
DROP TABLE IF EXISTS resource_download_log;
CREATE TABLE resource_download_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    resource_id BIGINT NOT NULL COMMENT '资源ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_resource (resource_id),
    KEY idx_user (user_id),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源下载记录表';

-- 初始数据：默认账号（密码均为 admin123，Hutool BCrypt加密）
INSERT INTO sys_user (username, password, real_name, role, status) VALUES
('admin', '$2a$10$Vye2eBQJuV7pZ/zSKJM9yerYCXU4r/ukFk1ZtlBTAfnls7QKFF74a', '系统管理员', 'ADMIN', 1),
('teacher', '$2a$10$D30kKBRup2Tw52/cTSCbzeTQK5aWEyrkUjZNsicZIH/7pHfhQFWHW', '张老师', 'TEACHER', 1),
('student', '$2a$10$1mzIAf52k1FDWqYLPWtVyu8sG8UKcFoYzhSnKHsi7bd/OJ4GRRto6', '李同学', 'STUDENT', 1);

-- 初始数据：资源分类
INSERT INTO resource_category (name, parent_id, sort) VALUES
('课件', 0, 1),
('视频', 0, 2),
('文档', 0, 3),
('试题', 0, 4),
('其他', 0, 5);
