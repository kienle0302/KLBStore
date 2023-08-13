package com.klbstore.security.auth;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.klbstore.dao.NguoiDungDAO;
import com.klbstore.extensions.OtpService;
import com.klbstore.model.MaXacNhan;
import com.klbstore.model.NguoiDung;
import com.klbstore.model.Role;
import com.klbstore.security.registration.RegistrationRequest;
import com.klbstore.security.service.ConfirmationTokenService;
import com.klbstore.security.service.JwtService;
import com.klbstore.valid.Registry;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
        private final NguoiDungDAO nguoiDungDAO;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;
        private final ConfirmationTokenService confirmationTokenService;
        private final OtpService mailerService;

        public AuthenticationResponse register(Registry request) {
                var user = NguoiDung.builder()
                                .tenDangNhap(request.getTenDangNhap())
                                .matKhau(passwordEncoder.encode(request.getMatKhau()))
                                .hoTen(request.getHoTen())
                                .email(request.getEmail())
                                .ngaySinh(request.getNgaySinh())
                                .sdt(request.getSdt())
                                .role(Role.USER)
                                .quyenDangNhap(false)
                                .trangThaiKhoa(false)
                                .build();

                boolean userExist = nguoiDungDAO
                                .findByTenDangNhap(user.getTenDangNhap())
                                .isPresent();
                if (userExist) {
                        throw new IllegalStateException("username already taken");
                }
                boolean userExist2 = nguoiDungDAO
                                .findBySdt(user.getSdt())
                                .isPresent();
                if (userExist2) {
                        throw new IllegalStateException("phone number already taken");
                }
                boolean userExist3 = nguoiDungDAO
                                .findByEmail(user.getEmail())
                                .isPresent();
                if (userExist3) {
                        throw new IllegalStateException("email already taken");
                }

                nguoiDungDAO.save(user);

                String token = UUID.randomUUID().toString();
                var jwtToken = jwtService.generateToken(user);

                // TODO: Send confirmation token
                MaXacNhan maXacNhan = new MaXacNhan(
                                user,
                                token,
                                false,
                                LocalDateTime.now(),
                                LocalDateTime.now().plusMinutes(15));

                confirmationTokenService.saveMaXacNhan(maXacNhan);

                // TODO: Send email
                String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;

                mailerService.generateAndSendOtp(
                                request.getEmail(),
                                "THIS IS OTP TO ACTIVE ACCOUNT " + request.getTenDangNhap(),
                                link);

                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getUsername(),
                                                request.getPassword()));
                var user = nguoiDungDAO.findByTenDangNhap(request.getUsername())
                                .orElseThrow();
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build();
        }

        public String authenticateAndGetToken(String username, String password) {
                // Tạo request body cho yêu cầu đăng nhập
                AuthenticationRequest request = new AuthenticationRequest(username, password);
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getUsername(),
                                                request.getPassword()));
                var user = nguoiDungDAO.findByTenDangNhap(request.getUsername())
                                .orElseThrow();
                var jwtToken = jwtService.generateToken(user);
                // Lấy JWT token từ response
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build().getToken();
        }
}
