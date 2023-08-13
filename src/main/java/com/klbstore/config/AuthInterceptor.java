package com.klbstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

        // String uri = request.getRequestURI();
        // NguoiDung user = sessionService.get("user"); // lấy từ session

        // if (cookieService.get("klb") != null) {
        // int id = Integer.parseInt(cookieService.getValue("klb"));
        // user = nguoiDungDAO.findById(id).get();
        // }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (!(auth instanceof AnonymousAuthenticationToken)) {
                String currentUserName = auth.getName();
                modelAndView.addObject("userLogged", currentUserName);
            } else {
                NguoiDung user = sessionService.get("usenameSS"); // lấy từ session

                if (cookieService.get("usenameCK") != null) {
                    int id = Integer.parseInt(cookieService.getValue("usenameCK"));
                    user = nguoiDungDAO.findById(id).get();
                }

                String currentUserName = auth.getName();
                modelAndView.addObject("userLogged", currentUserName);
                modelAndView.addObject("userLogged", user);
            }
        }

        // NguoiDung user = sessionService.get("user"); // lấy từ session

        // if (cookieService.get("klb") != null) {
        // int id = Integer.parseInt(cookieService.getValue("klb"));
        // user = nguoiDungDAO.findById(id).get();
        // }
        // request.setAttribute("useHeader", user);
        // // Lưu người dùng vào request.
        // if (modelAndView != null) {
        // modelAndView.addObject("useHeader", "user");
        // }

    }

}
