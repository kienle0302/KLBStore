package com.klbstore.dao;
import org.springframework.data.jpa.repository.JpaRepository;

import com.klbstore.model.NguoiDung;

public interface NguoiDungDAO extends JpaRepository<NguoiDung, Integer> {
    NguoiDung findByTenDangNhap(String tenDangNhap);

    NguoiDung findByEmail(String email);

    NguoiDung findBySdt(String sdt);
}
