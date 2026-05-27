package com.teaching.controller;

import com.teaching.common.Result;
import com.teaching.entity.SysUser;
import com.teaching.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginForm) {
        String username = loginForm.get("username");
        String password = loginForm.get("password");
        Map<String, Object> data = authService.login(username, password);
        return Result.success("登录成功", data);
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody SysUser user) {
        authService.register(user);
        return Result.success("注册成功");
    }
}
