USE teaching_platform;

-- 演示资源数据（status=1 表示已审核通过）
INSERT INTO teaching_resource (title, description, category_id, file_name, file_size, file_type, upload_user_id, status, download_count, view_count) VALUES
('Java程序设计基础课件', '涵盖Java基本语法、面向对象、异常处理等核心知识点的教学课件', 1, 'Java基础课件.pptx', 2048000, 'ppt', 4, 1, 35, 128),
('数据结构与算法导论', '详细介绍常用数据结构（链表、树、图）及经典算法的教学PPT', 1, '数据结构导论.pptx', 3072000, 'ppt', 4, 1, 22, 95),
('Spring Boot入门教程', '从零开始学习Spring Boot框架，包含项目搭建、配置、常用注解等', 3, 'SpringBoot入门.pdf', 1536000, 'pdf', 4, 1, 48, 210),
('MySQL数据库设计规范', '数据库设计的最佳实践，包括范式、索引优化、SQL编写规范', 3, 'MySQL设计规范.pdf', 1024000, 'pdf', 4, 1, 31, 156),
('Vue.js前端开发实战', '前端Vue框架实战教程，涵盖组件开发、路由、状态管理', 3, 'Vue实战教程.pdf', 2560000, 'pdf', 4, 1, 27, 142),
('计算机网络期末复习题', '计算机网络课程期末考试复习题库，含选择题、简答题和分析题', 4, '计网复习题.docx', 512000, 'doc', 4, 1, 56, 230),
('操作系统原理视频课程', '操作系统核心概念讲解：进程管理、内存管理、文件系统', 2, '操作系统原理.mp4', 524288000, 'mp4', 4, 1, 18, 87),
('Python数据分析入门', 'Python数据分析基础教程，包含NumPy、Pandas、Matplotlib', 1, 'Python数据分析.pptx', 4096000, 'ppt', 4, 1, 41, 189),
('软件工程课程设计指南', '软件工程课程设计规范，包含需求分析、系统设计、测试方案模板', 3, '软工课设指南.pdf', 768000, 'pdf', 4, 1, 15, 63),
('Web前端HTML+CSS基础', 'Web前端入门课件，详细讲解HTML标签、CSS样式、页面布局', 1, 'HTML+CSS基础.pptx', 1792000, 'ppt', 4, 1, 33, 175);

-- 演示浏览记录（近7天，给统计图表提供数据）
INSERT INTO resource_view_log (resource_id, user_id, create_time) VALUES
(1, 5, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(2, 5, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(3, 5, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, 5, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(3, 5, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(4, 5, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(5, 5, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1, 5, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(2, 5, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(6, 5, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(7, 5, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(8, 5, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(3, 5, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(1, 5, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(4, 5, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(9, 5, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(10, 5, DATE_SUB(NOW(), INTERVAL 6 DAY)),
(2, 5, DATE_SUB(NOW(), INTERVAL 6 DAY)),
(5, 5, DATE_SUB(NOW(), INTERVAL 6 DAY)),
(3, 5, DATE_SUB(NOW(), INTERVAL 7 DAY));

-- 演示下载记录
INSERT INTO resource_download_log (resource_id, user_id, create_time) VALUES
(3, 5, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(6, 5, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, 5, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(3, 5, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(8, 5, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(4, 5, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(6, 5, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(1, 5, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(5, 5, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(2, 5, DATE_SUB(NOW(), INTERVAL 6 DAY)),
(3, 5, DATE_SUB(NOW(), INTERVAL 7 DAY));

-- 演示问答数据
INSERT INTO question_answer (title, content, user_id, status) VALUES
('Spring Boot如何配置多数据源？', '项目中需要同时连接MySQL和PostgreSQL两个数据库，请问Spring Boot中如何配置多数据源？', 5, 0),
('Java中HashMap和ConcurrentHashMap的区别？', '面试中经常被问到这个问题，请详细解释一下它们在线程安全、性能、实现原理上的区别', 5, 1),
('Vue组件之间如何传递数据？', '父子组件、兄弟组件之间如何高效传递数据？除了props和emit还有什么方式？', 5, 0);

-- 演示回复
INSERT INTO qa_reply (question_id, user_id, content) VALUES
(2, 4, 'HashMap是非线程安全的，ConcurrentHashMap通过分段锁（JDK7）或CAS+synchronized（JDK8）实现线程安全。性能上ConcurrentHashMap在多线程环境下更优，因为它允许多个线程同时读写不同的段。');

-- 演示评论
INSERT INTO resource_comment (resource_id, user_id, content) VALUES
(1, 5, '课件内容很详细，对Java基础学习很有帮助！'),
(3, 5, 'Spring Boot教程写得非常好，跟着做了一遍项目搭建成功了'),
(6, 5, '复习题很全面，感谢老师整理分享');
