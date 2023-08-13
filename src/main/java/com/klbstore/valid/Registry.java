package com.klbstore.valid;

import java.util.Date;

import com.klbstore.model.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Registry {

    @NotBlank(message = "Hãy nhập tên người dùng")
    // @Pattern(message = "Sai định dạng tên người dùng", regexp = "^[a-z0-9_-]{3,16}$")
    private String tenDangNhap;

    @NotBlank(message = "Hãy nhập mật khẩu")
    // @Pattern(message = "Sai định dạng mật khẩu", regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
    private String matKhau;

    @NotBlank(message = "Hãy nhập mật khẩu xác nhận")
    private String matKhauXN;

    @NotBlank(message = "Hãy nhập email")
    @Email(message = "Email không không đúng định dạng")
    private String email;

    @NotBlank(message = "Hãy nhập tên người dùng")
    private String hoTen;

    private Date ngaySinh;

    @NotBlank(message = "Hãy nhập số điện thoại")
    // @Pattern(message = "Sai định dạng số điện thoại", regexp = "^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$")
    private String sdt;

    Boolean quyenDangNhap;
    Role role;
}
