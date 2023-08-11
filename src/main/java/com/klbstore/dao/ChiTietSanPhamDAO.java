package com.klbstore.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klbstore.model.ChiTietSanPham;;


public interface ChiTietSanPhamDAO extends JpaRepository<ChiTietSanPham, Integer> {
    ChiTietSanPham findByMauSac_MauSacId(Integer mauSacId);
}
