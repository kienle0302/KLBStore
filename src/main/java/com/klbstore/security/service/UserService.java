package com.klbstore.security.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.klbstore.dao.NguoiDungDAO;
import com.klbstore.model.NguoiDung;

import lombok.AllArgsConstructor;

import com.klbstore.model.AuthenticationProvider;
import com.klbstore.model.MaXacNhan;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private NguoiDungDAO nguoiDungDAO;

    private BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void createUserAfterOAuthLogin(String email, String name, String phone, AuthenticationProvider provider) {
        NguoiDung newUser = new NguoiDung();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        newUser.setTenDangNhap(userName);
        newUser.setEmail(email);
        newUser.setHoTen(name);
        newUser.setSdt(phone);
        newUser.setQuyenDangNhap(true);
        newUser.setNgayDangKy(new Date());
        newUser.setTrangThaiKhoa(false);

        nguoiDungDAO.save(newUser);

    }

    public void updateUserAfterOAuthLogin(NguoiDung nguoiDung, String phone, String name,
            AuthenticationProvider provider) {

        nguoiDung.setHoTen(name);
        nguoiDung.setSdt(phone);

        nguoiDungDAO.save(nguoiDung);

    }

    public String signUpUser(NguoiDung nguoiDung) {

        boolean userExist = nguoiDungDAO
                .findByEmail(nguoiDung.getUsername())
                .isPresent();
        if (userExist) {
            throw new IllegalStateException("username already taken");
        }
        boolean userExist2 = nguoiDungDAO
                .findBySdt(nguoiDung.getSdt())
                .isPresent();
        if (userExist2) {
            throw new IllegalStateException("phone number already taken");
        }
        boolean userExist3 = nguoiDungDAO
                .findByEmail(nguoiDung.getEmail())
                .isPresent();
        if (userExist3) {
            throw new IllegalStateException("email already taken");
        }

        String encodePassword = passwordEncoder().encode(nguoiDung.getPassword());

        nguoiDung.setMatKhau(encodePassword);

        nguoiDungDAO.save(nguoiDung);

        String token = UUID.randomUUID().toString();
        // TODO: Send confirmation token
        MaXacNhan maXacNhan = new MaXacNhan(
                nguoiDung,
                token,
                false,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15));

        confirmationTokenService.saveMaXacNhan(maXacNhan);

        return token;
    }

    private final ConfirmationTokenService confirmationTokenService;

    public int enableAppUser(String email) {
        return nguoiDungDAO.enableAppUser(email, new Date());
    }

}
