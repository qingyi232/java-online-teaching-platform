package com.teaching.config;

import com.teaching.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/auth/login",
                        "/api/auth/register",
                        "/api/resource/list",
                        "/api/resource/detail/**",
                        "/api/resource/category/list",
                        "/api/question/list",
                        "/api/question/detail/**",
                        "/api/question/reply/list/**",
                        "/api/comment/list/**",
                        "/api/statistics/dashboard"
                );
    }
}
