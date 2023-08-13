package com.klbstore.dao;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.klbstore.model.MaXacNhan;

import jakarta.transaction.Transactional;

@Repository
public interface MaXacNhanDAO extends JpaRepository<MaXacNhan, Integer> {
    Optional<MaXacNhan> findByMaOtp(String token);

    @Transactional
    @Modifying
    @Query("UPDATE MaXacNhan c " +
            "SET c.daXacNhan = ?2 " +
            "WHERE c.maOtp = ?1")
    int updateConfirmed(String token,
                          Boolean isConfirm);
}
