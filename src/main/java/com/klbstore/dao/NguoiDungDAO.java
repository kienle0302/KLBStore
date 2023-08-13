package com.klbstore.dao;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.klbstore.model.NguoiDung;

@Repository
@Transactional(readOnly = true)
public interface NguoiDungDAO extends JpaRepository<NguoiDung, Integer> {
    Optional<NguoiDung> findByTenDangNhap(String tenDangNhap);

    Optional<NguoiDung> findByEmail(String email);

    Optional<NguoiDung> findBySdt(String sdt);

    @Transactional
    @Modifying
    @Query("UPDATE NguoiDung a " +
            "SET a.quyenDangNhap = TRUE, ngayDangKy = ?2 WHERE a.email = ?1")
    int enableAppUser(String email, Date now);
}
