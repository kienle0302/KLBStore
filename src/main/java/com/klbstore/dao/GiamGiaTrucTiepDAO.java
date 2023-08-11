package com.klbstore.dao;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.klbstore.model.GiamGiaTrucTiep;

public interface GiamGiaTrucTiepDAO extends JpaRepository<GiamGiaTrucTiep, Integer>{
@Query(value = "SELECT COALESCE((SELECT TOP 1 GiamGiaTrucTiep "
            + "FROM GiamGiaTrucTiep "
            + "WHERE SanPhamID = ?1 "
            + "AND ?2 BETWEEN NgayBatDau AND NgayKetThuc "
            + "ORDER BY GiamGiaTrucTiepId DESC), 0)", nativeQuery = true)
Double getGiamGiaTrucTiepBySanPhamIdAndDate(Long sanPhamId, LocalDateTime currentDate);

}
