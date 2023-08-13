package com.klbstore.security.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.klbstore.model.MaXacNhan;
// import com.klbstore.extensions.OtpService;
// import com.klbstore.model.NguoiDung;
// import com.klbstore.model.Role;
// import com.klbstore.security.registration.EmailValidator;
// import com.klbstore.security.registration.RegistrationRequest;
// import com.klbstore.service.MailerService;
import com.klbstore.model.NguoiDung;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;

    @Transactional
    public NguoiDung confirmToken(String token) {
        MaXacNhan confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getDaXacNhan()) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getHanHieuLucOtp();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmed(token);
        userService.enableAppUser(
                confirmationToken.getNguoiDung().getEmail());

        return confirmationToken.getNguoiDung();
    }

}
