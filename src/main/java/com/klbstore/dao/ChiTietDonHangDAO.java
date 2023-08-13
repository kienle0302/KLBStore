package com.klbstore.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.klbstore.model.ChiTietDonHang;
@Repository
public interface ChiTietDonHangDAO extends JpaRepository<ChiTietDonHang, Integer> {
    @Query("SELECT c FROM ChiTietDonHang c WHERE c.donHang.id = :donHangId")
    List<ChiTietDonHang> findByDonHangId(Integer donHangId);
}
