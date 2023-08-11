package com.klbstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.klbstore.dao.NguoiDungDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class GlobalInterceptor implements HandlerInterceptor {
    @Autowired
    NguoiDungDAO dao;

    @Override
    public boolean preHandle(HttpServletRequest request, 
    HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("uri", request.getRequestURI());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, 
    Object handler, @Nullable ModelAndView mv) throws Exception {
        req.setAttribute("nguoidung", dao.findAll());
    }
}
