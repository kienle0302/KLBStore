package com.klbstore.security.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.klbstore.dao.NguoiDungDAO;
import com.klbstore.model.AuthenticationProvider;
import com.klbstore.model.NguoiDung;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private NguoiDungDAO nguoiDungDAO;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        DefaultOidcUser oauthUser = (DefaultOidcUser) authentication.getPrincipal();
        
        String email = oauthUser.getAttribute("email");
        NguoiDung nguoiDung = nguoiDungDAO.findByEmail(email).get();
        String name = oauthUser.getFullName();
        String phone = oauthUser.getPhoneNumber();
        System.out.println("Phone: "+phone);

        if (nguoiDung == null) {
            // if (phone == null) {
            //     response.sendRedirect("/user/update-phone-number/"+oauthUser.getName());
            // }
            userService.createUserAfterOAuthLogin(email, name, phone, AuthenticationProvider.GOOGLE);
        } else {
            userService.updateUserAfterOAuthLogin(nguoiDung, phone, name, AuthenticationProvider.GOOGLE);
        }

        System.out.println("Cumstomer's Email: " + email);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
