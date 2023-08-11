package com.klbstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.klbstore.dao.NhomSanPhamDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NhomSanPhamInterceptor implements HandlerInterceptor {

    private final NhomSanPhamDAO nhomSanPhamDAO;

    public NhomSanPhamInterceptor(NhomSanPhamDAO nhomSanPhamDAO) {
        this.nhomSanPhamDAO = nhomSanPhamDAO;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            modelAndView.addObject("nhomSanPhamList", nhomSanPhamDAO.findAll());
        }
    }
}

