package com.klbstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.klbstore.dao.NguoiDungDAO;
import com.klbstore.model.NguoiDung;
import com.klbstore.service.CookieService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    com.klbstore.service.SessionService sessionService;
    @Autowired
    CookieService cookieService;
    @Autowired
    NguoiDungDAO nguoiDungDAO;

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();
        NguoiDung user = sessionService.get("user"); // lấy từ session

        if (cookieService.get("klb") != null) {
            int id = Integer.parseInt(cookieService.getValue("klb"));
            user = nguoiDungDAO.findById(id).get();
        }

        request.setAttribute("useHeader", user);

        String error = "";

        if (user == null) { // chưa đăng nhập
            error = "Please login!";
        } else if (user.isTrangThaiKhoa()) { // chưa đăng nhập
            error = "Your account is locked!";
        } else if (!user.getQuyenDangNhap() && uri.startsWith("/admin/")) { // Không đúng vai trò
            error = "Access denied!"; // Truy cập bị từ chối
        }
        if (error.length() > 0) { // Có lỗi
            sessionService.set("security-uri", uri);
            response.sendRedirect("/user/login-register?error=" + error);
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

        NguoiDung user = sessionService.get("user"); // lấy từ session

        if (cookieService.get("klb") != null) {
            int id = Integer.parseInt(cookieService.getValue("klb"));
            user = nguoiDungDAO.findById(id).get();
        }
        request.setAttribute("useHeader", user);
        if (modelAndView != null) {
            modelAndView.addObject("useHeader", "user");
        }
    }

}
