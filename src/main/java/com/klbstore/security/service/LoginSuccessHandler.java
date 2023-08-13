package com.klbstore.security.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.klbstore.dao.NguoiDungDAO;
import com.klbstore.service.CookieService;
import com.klbstore.service.ParamService;
import com.klbstore.service.SessionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    CookieService cookieService;
    @Autowired
    ParamService paramService;
    @Autowired
    SessionService sessionService;
    @Autowired
    NguoiDungDAO nguoiDungDAO;
    @Autowired
    JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // If user's logged
        if (authentication != null && authentication.isAuthenticated()) {
            var user = nguoiDungDAO.findByTenDangNhap(auth.getName())
                                .orElseThrow();
                var jwtToken = jwtService.generateToken(user);
            response.addHeader("Authorization", "Bearer " + jwtToken);
            System.out.println("Authorization header value: " + response.getHeader("Authorization"));
        }

        sessionService.set("usernameSS", auth.getName()); // Gán cho session user
        if (paramService.getBoolean("rememberMe", false)) {
            cookieService.add("usernameCK", String.valueOf(auth.getName()), 10);
        }

        System.out.println("Cumstomer's name: " + auth.getName());

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
