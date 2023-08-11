package com.klbstore.dao;
import org.springframework.data.jpa.repository.JpaRepository;

import com.klbstore.model.GioHang;


public interface GioHangDAO extends JpaRepository<GioHang, Integer> {
    GioHang findByNguoiDung_NguoiDungId(Integer nguoiDungId);
}
