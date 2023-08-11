package com.klbstore.dto;

import java.util.List;

@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class AllChiTietGioHangDTO {
    List<ChiTietGioHangDTO> danhSachSanPhamTrongGioHang;
    Integer tongSoLuong;
    Double tongTien;
    Double tongPhiVanChuyen;
    String ngayGiaoHangDuKien;

}
