package com.teaching.service;

import com.teaching.entity.SysUser;

import java.util.Map;

public interface AuthService {

    Map<String, Object> login(String username, String password);

    void register(SysUser user);
}
