package com.klbstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterConfig implements WebMvcConfigurer{

    
    @Autowired
    AuthInterceptor auth;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(auth)
        .addPathPatterns("/admin/**", "/user/wishlist/**", "/user/profile/**")
        .excludePathPatterns("/assets/**", "/admin/login", "/admin/log-out", "/user/index", "/user/shopping-cart", "/user/404");
    }
}
