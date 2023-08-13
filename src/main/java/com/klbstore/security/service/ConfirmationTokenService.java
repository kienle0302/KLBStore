package com.klbstore.security.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.klbstore.dao.MaXacNhanDAO;
import com.klbstore.model.MaXacNhan;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    
    private final MaXacNhanDAO maXacNhanDAO;

    public void saveMaXacNhan(MaXacNhan maXacNhan) {
        maXacNhanDAO.save(maXacNhan);
    }

    public Optional<MaXacNhan> getToken(String token) {
        return maXacNhanDAO.findByMaOtp(token);
    }

    public int setConfirmed(String token) {
        return maXacNhanDAO.updateConfirmed(
                token, true);
    }
}
