package com.klbstore.dao;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klbstore.model.DanhGia;
import com.klbstore.model.NguoiDung;
import com.klbstore.model.SanPham;


public interface DanhGiaDAO extends JpaRepository<DanhGia, Integer> {
    List<DanhGia> findByNguoiDungAndSanPham(NguoiDung nguoiDung, SanPham sanPham);
}
