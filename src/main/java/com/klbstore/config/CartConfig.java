package com.klbstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.klbstore.dao.ChiTietGioHangDAO;
@Configuration
public class CartConfig implements WebMvcConfigurer{
    @Autowired
    private ChiTietGioHangDAO chiTietGioHangDAO;

    @Bean
    public Cart cartInterceptor() {
        return new Cart(chiTietGioHangDAO);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cartInterceptor());
    }
}
