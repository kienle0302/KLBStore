package com.klbstore.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.klbstore.model.CauHinhDienThoai;


public interface CauHinhDienThoaiDAO extends JpaRepository<CauHinhDienThoai, Long> {
    @Query(value = "SELECT chdt FROM CauHinhDienThoai chdt WHERE sanPham.sanPhamId = ?1")
    public CauHinhDienThoai getMaNguoiDung(Integer id);
}
