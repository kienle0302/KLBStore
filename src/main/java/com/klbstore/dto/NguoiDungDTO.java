package com.klbstore.dto;

import java.util.Date;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class NguoiDungDTO {
    @NotBlank(message = "Hãy nhập tên người dùng")
    @Pattern(message = "Sai định dạng tên người dùng", regexp = "^[a-zA-Z0-9_-]{3,16}$")
    private String tenDangNhap;

    @NotBlank(message = "Hãy nhập email")
    @Email(message = "Email không không đúng định dạng")
    private String email;

    @NotBlank(message = "Hãy nhập tên người dùng")
    private String hoTen;

    @NotBlank(message = "Hãy chọn ngày sinh")
    private String ngaySinh;

    @NotNull(message = "Hãy chọn giới tính")
    private Boolean gioiTinh;

    @NotBlank(message = "Hãy nhập địa chỉ")
    private String diaChi;

    @NotBlank(message = "Hãy nhập số điện thoại")
    @Pattern(message = "Sai định dạng số điện thoại", regexp = "^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$")
    private String sdt;    
    
    @NotBlank(message = "Hãy chọn tỉnh thành")
    private String tinhThanh;

    @NotBlank(message = "Hãy chọn quận huyện")
    private String quanHuyen;

    @NotBlank(message = "Hãy chọn phường xã")
    private String xaPhuong;

    @AssertTrue(message = "Phải chọn giới tính")
    private boolean isGioiTinhValid() {
        return gioiTinh != null;
    }

    @AssertTrue(message = "Phải chọn tỉnh thành")
    private boolean isTinhThanhValid() {
        return tinhThanh != null && !tinhThanh.isEmpty();
    }

    @AssertTrue(message = "Phải chọn quận huyện")
    private boolean isQuanHuyenValid() {
        return quanHuyen != null && !quanHuyen.isEmpty();
    }

    @AssertTrue(message = "Phải chọn phường xã")
    private boolean isPhuongXaValid() {
        return xaPhuong != null && !xaPhuong.isEmpty();
    }
}
