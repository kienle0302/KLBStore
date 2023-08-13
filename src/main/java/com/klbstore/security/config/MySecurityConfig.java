package com.klbstore.security.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.util.UrlPathHelper;

import com.klbstore.extensions.HashedPasswordArgon2;
import com.klbstore.security.JwtAuthenticationFilter;
import com.klbstore.security.service.CustomOAuth2UserService;
import com.klbstore.security.service.LoginSuccessHandler;
import com.klbstore.security.service.OAuth2LoginSuccessHandler;
import com.klbstore.service.CookieService;
import com.klbstore.service.ParamService;
import com.klbstore.service.SessionService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class MySecurityConfig {
        @Autowired
        HashedPasswordArgon2 hashedPassword;
        @Autowired
        CookieService cookieService;
        @Autowired
        ParamService paramService;
        @Autowired
        SessionService sessionService;
        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;
        // private final UserService userService;
        // private final BCryptPasswordEncoder bCryptPasswordEncoder;

        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf.disable()).cors(cors -> cors.disable());
                http
                                .authorizeHttpRequests(
                                                (authorizeRequests) -> authorizeRequests
                                                                .requestMatchers(
                                                                                "/",
                                                                                "/user/**",
                                                                                "/assets/**",
                                                                                "/oauth/**",
                                                                                "/api/v1/auth/**",
                                                                                "/api/v*/registration/**")
                                                                .permitAll()
                                                                .anyRequest().authenticated())

                                .formLogin(formLogin -> formLogin
                                                .loginPage("/user/login")
                                                .loginProcessingUrl("/user/login")
                                                .defaultSuccessUrl("/user/index", false)
                                                .successHandler(loginSuccessHandler)
                                                .failureUrl("/user/login/error"))

                                .sessionManagement(management -> management
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                                .exceptionHandling(handling -> handling
                                                .accessDeniedPage("/user/access/denied"))

                                .oauth2Login(oauth2Customize -> oauth2Customize
                                                .loginPage("/user/login")
                                                .defaultSuccessUrl("/user/index")
                                                .failureUrl("/user/login/error")
                                                .userInfoEndpoint()
                                                .userService(oAuth2UserService)
                                                .and()
                                                .successHandler(oAuth2LoginSuccessHandler))

                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .clearAuthentication(true)
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID", "refresh_token")
                                                .logoutSuccessHandler(new LogoutSuccessHandler() {
                                                        @Override
                                                        public void onLogoutSuccess(HttpServletRequest request,
                                                                        HttpServletResponse response,
                                                                        Authentication authentication)
                                                                        throws IOException, ServletException {
                                                                cookieService.remove("usernameCK");
                                                                sessionService.remove("usernameSS");

                                                                System.out.println("The user" + authentication.getName()
                                                                                + " has logged out.");

                                                                UrlPathHelper helper = new UrlPathHelper();
                                                                String context = helper.getContextPath(request);

                                                                response.sendRedirect(context + "/user/login");
                                                        }
                                                }).permitAll())

                                .userDetailsService(applicationConfig.userDetailsService());
                return http.build();
        }

        @Autowired
        private ApplicationConfig applicationConfig;

        @Autowired
        private CustomOAuth2UserService oAuth2UserService;

        @Autowired
        private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

        @Autowired
        private LoginSuccessHandler loginSuccessHandler;

}
