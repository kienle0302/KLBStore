package com.klbstore.dto;

import com.klbstore.model.SanPham;

@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class SanPhamDTO {
    private SanPham sanPham;
    private Boolean giamGia;
    private Double tongGia;
    private Integer phanTramGiamGia;
    private Integer soSao;
}
