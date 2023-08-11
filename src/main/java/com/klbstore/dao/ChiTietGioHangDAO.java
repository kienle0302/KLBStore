package com.klbstore.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.klbstore.dto.ChiTietGioHangDTO;
import com.klbstore.model.ChiTietGioHang;
import com.klbstore.model.GioHang;

public interface ChiTietGioHangDAO extends JpaRepository<ChiTietGioHang, Integer> {
    List<ChiTietGioHang> findAllByGioHang(GioHang gioHang);

    @Procedure(name = "KiemTraVaXoaSanPhamKhongHopLeTrongGioHang")
    void kiemTraVaXoaSanPhamKhongHopLeTrongGioHang(@Param("nguoiDungId") Integer nguoiDungId);

    @Procedure(name = "XoaSanPhamKhoiGioHang")
    void xoaSanPhamKhoiGioHang(@Param("mauSacId") Integer mauSacId, @Param("nguoiDungId") Integer nguoiDungId);

    @Query(value = "DECLARE @gioHangId INT; " +
            "DECLARE @thongBao NVARCHAR(MAX); " +
            "EXEC ThemSanPhamVaoGioHang " +
            "    @mauSacId = :mauSacId, " +
            "    @soLuong = :soLuong, " +
            "    @nguoiDungId = :nguoiDungId, " +
            "    @gioHangId = @gioHangId OUTPUT, " +
            "    @thongBao = @thongBao OUTPUT; " +
            "SELECT @gioHangId AS GioHangID, @thongBao AS ThongBao", nativeQuery = true)
            Map<String, Object> themSanPhamVaoGioHang(
                @Param("mauSacId") Integer mauSacId,
            @Param("soLuong") Integer soLuong,
            @Param("nguoiDungId") Integer nguoiDungId);
            
            @Query(value = "DECLARE @gioHangId INT; " +
                    "DECLARE @thongBao NVARCHAR(MAX); " +
                    "EXEC CapNhatSanPhamTrongGioHang " +
                    "    @mauSacId = :mauSacId, " +
                    "    @soLuong = :soLuong, " +
                    "    @nguoiDungId = :nguoiDungId, " +
                    "    @gioHangId = @gioHangId OUTPUT, " +
                    "    @thongBao = @thongBao OUTPUT; " +
                    "SELECT @gioHangId AS GioHangID, @thongBao AS ThongBao", nativeQuery = true)
                    Map<String, Object> capNhatSanPhamTrongGioHang(
                        @Param("mauSacId") Integer mauSacId,
                    @Param("soLuong") Integer soLuong,
                    @Param("nguoiDungId") Integer nguoiDungId);
    @Query("SELECT new com.klbstore.dto.ChiTietGioHangDTO(" +
            "ctgh, " +
            "ctgh.sanPham.sanPhamId, " +
            "ctgh.sanPham.tenSanPham, " +
            "CASE " +
            "  WHEN (ggTrucTiep IS NOT NULL " +
            "    AND ggTrucTiep.ngayBatDau <= CURRENT_TIMESTAMP " +
            "    AND ggTrucTiep.ngayKetThuc >= CURRENT_TIMESTAMP) " +
            "  THEN (CASE " +
            "    WHEN ctgh.sanPham.giaBan IS NOT NULL " +
            "    THEN (ctgh.sanPham.giaBan * (1 - COALESCE(ggTrucTiep.giamGiaTrucTiep, 0))) " +
            "    ELSE 0 " +
            "  END) " +
            "  ELSE ctgh.sanPham.giaBan " +
            "END, " +
            "CASE " +
            "  WHEN (ggTrucTiep IS NOT NULL " +
            "    AND ggTrucTiep.ngayBatDau <= CURRENT_TIMESTAMP " +
            "    AND ggTrucTiep.ngayKetThuc >= CURRENT_TIMESTAMP) " +
            "  THEN (CASE " +
            "    WHEN ctgh.sanPham.giaBan IS NOT NULL " +
            "    THEN (ctgh.sanPham.giaBan * (1 - COALESCE(ggTrucTiep.giamGiaTrucTiep, 0))) " +
            "    ELSE 0 " +
            "  END * ctgh.soLuong) " +
            "  ELSE ctgh.sanPham.giaBan * ctgh.soLuong " +
            "END) " +
            "FROM ChiTietGioHang ctgh " +
            "JOIN ctgh.gioHang gh " +
            "LEFT JOIN ctgh.sanPham.sanPhamGiamGiaTrucTieps ggTrucTiep " +
            "WHERE gh.nguoiDung.nguoiDungId = :nguoiDungId")
    List<ChiTietGioHangDTO> layDanhSachSanPhamTrongGioHangTheoNguoiDung(@Param("nguoiDungId") Integer nguoiDungId);

    @Query("(SELECT " +
            "SUM(CASE WHEN (ggTrucTiep IS NOT NULL " +
            "AND ggTrucTiep.ngayBatDau <= CURRENT_TIMESTAMP " +
            "AND ggTrucTiep.ngayKetThuc >= CURRENT_TIMESTAMP) THEN (CASE WHEN ctgh.sanPham.giaBan IS NOT NULL THEN (ctgh.sanPham.giaBan * (1 - COALESCE(ggTrucTiep.giamGiaTrucTiep, 0))) ELSE 0 END * ctgh.soLuong) ELSE ctgh.sanPham.giaBan * ctgh.soLuong END) "
            +
            "FROM ChiTietGioHang ctgh " +
            "JOIN ctgh.gioHang gh " +
            "LEFT JOIN ctgh.sanPham.sanPhamGiamGiaTrucTieps ggTrucTiep " +
            "WHERE gh.nguoiDung.nguoiDungId = :nguoiDungId) ")
    Double tinhTongTienTrongGioHang(@Param("nguoiDungId") Integer nguoiDungId);

    @Query("(SELECT " +
            "COUNT(ctgh) " +
            "FROM ChiTietGioHang ctgh " +
            "JOIN ctgh.gioHang gh " +
            "LEFT JOIN ctgh.sanPham.sanPhamGiamGiaTrucTieps ggTrucTiep " +
            "WHERE gh.nguoiDung.nguoiDungId = :nguoiDungId) ")
    Integer tongSoSanPhamTrongGioHang(@Param("nguoiDungId") Integer nguoiDungId);

    @Query("SELECT SUM(ctgh.mauSac.sanPham.canNang * ctgh.soLuong) " +
       "FROM ChiTietGioHang ctgh " +
       "JOIN ctgh.gioHang gh " +
       "LEFT JOIN ctgh.sanPham sp " +
       "LEFT JOIN sp.sanPhamGiamGiaTrucTieps ggTrucTiep " +
       "WHERE gh.nguoiDung.nguoiDungId = :nguoiDungId")
   Integer tongCanNangTrongGioHang(@Param("nguoiDungId") Integer nguoiDungId);
   
   @Query("SELECT SUM(ctgh.mauSac.sanPham.chieuDai * ctgh.soLuong) " +
      "FROM ChiTietGioHang ctgh " +
      "JOIN ctgh.gioHang gh " +
      "LEFT JOIN ctgh.sanPham sp " +
      "LEFT JOIN sp.sanPhamGiamGiaTrucTieps ggTrucTiep " +
      "WHERE gh.nguoiDung.nguoiDungId = :nguoiDungId")
  Integer tongChieuDaiTrongGioHang(@Param("nguoiDungId") Integer nguoiDungId);

   @Query("SELECT SUM(ctgh.mauSac.sanPham.chieuRong * ctgh.soLuong) " +
      "FROM ChiTietGioHang ctgh " +
      "JOIN ctgh.gioHang gh " +
      "LEFT JOIN ctgh.sanPham sp " +
      "LEFT JOIN sp.sanPhamGiamGiaTrucTieps ggTrucTiep " +
      "WHERE gh.nguoiDung.nguoiDungId = :nguoiDungId")
  Integer tongChieuRongTrongGioHang(@Param("nguoiDungId") Integer nguoiDungId);
  
   @Query("SELECT SUM(ctgh.mauSac.sanPham.doDay * ctgh.soLuong) " +
      "FROM ChiTietGioHang ctgh " +
      "JOIN ctgh.gioHang gh " +
      "LEFT JOIN ctgh.sanPham sp " +
      "LEFT JOIN sp.sanPhamGiamGiaTrucTieps ggTrucTiep " +
      "WHERE gh.nguoiDung.nguoiDungId = :nguoiDungId")
  Integer tongDoDayTrongGioHang(@Param("nguoiDungId") Integer nguoiDungId);



}
