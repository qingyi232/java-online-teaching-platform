package com.teaching.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.teaching.entity.SysUser;

public interface UserService {

    IPage<SysUser> getUserPage(int page, int size, String username, String role);

    SysUser getUserById(Long id);

    void updateUser(SysUser user);

    void updatePassword(Long userId, String oldPassword, String newPassword);

    void updateStatus(Long id, Integer status);

    void deleteUser(Long id);

    void resetPassword(Long id);
}
