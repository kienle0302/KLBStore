package com.klbstore.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.klbstore.model.DonHang;
import com.klbstore.model.NguoiDung;

public interface DonHangDAO extends JpaRepository<DonHang, Integer> {
        List<DonHang> findByNguoiDungAndTinhTrangThanhToan(NguoiDung nguoiDung, boolean tinhTrangThanhToan);

        List<DonHang> findByNguoiDung_NguoiDungId(Integer nguoiDungId);

        DonHang findTopByNguoiDungOrderByNgayDatHangDesc(NguoiDung nguoiDung);

    @Query(value = "EXEC TaoDonHang "
            + "@NguoiDungID = :nguoiDungID, "
            + "@HinhThucThanhToan = :hinhThucThanhToan, "
            + "@NgayGiaoHangDuKien = :ngayGiaoHangDuKien, "
            + "@GhiChu = :ghiChu, "
            + "@PhiVanChuyen = :phiVanChuyen", nativeQuery = true)
    String taoDonHang(
            @Param("nguoiDungID") Integer nguoiDungID,
            @Param("hinhThucThanhToan") Integer hinhThucThanhToan,
            @Param("ngayGiaoHangDuKien") Date ngayGiaoHangDuKien,
            @Param("ghiChu") String ghiChu,
            @Param("phiVanChuyen") Double phiVanChuyen
    );
    
    @Procedure(name = "CapNhatTrangThaiThanhToanDonHang")
    void capNhatTrangThaiThanhToanDonHang(
        @Param("DonHangID") Integer donHangId
    );

}
