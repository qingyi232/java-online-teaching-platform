USE teaching_platform;
UPDATE sys_user SET real_name = '系统管理员' WHERE username = 'admin';
UPDATE sys_user SET real_name = '张老师' WHERE username = 'teacher';
UPDATE sys_user SET real_name = '李同学' WHERE username = 'student';
