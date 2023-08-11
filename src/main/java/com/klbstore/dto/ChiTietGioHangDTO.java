package com.klbstore.dto;

import com.klbstore.model.ChiTietGioHang;
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class ChiTietGioHangDTO {
    ChiTietGioHang chiTietGioHang;
    Long sanPhamId;
    String tenSanPham;
    Double giaBan;
    Double tongGia;
}
