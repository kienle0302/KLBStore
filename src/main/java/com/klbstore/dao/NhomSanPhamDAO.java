package com.klbstore.dao;
import org.springframework.data.jpa.repository.JpaRepository;

import com.klbstore.model.NhomSanPham;


public interface NhomSanPhamDAO extends JpaRepository<NhomSanPham, Integer> {
}
