package com.klbstore.valid;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.io.Serializable;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Registry implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer nguoiDungId;

    @NotBlank(message = "Hãy nhập tên người dùng")
    @Pattern(message = "Sai định dạng tên người dùng", regexp = "^[a-z0-9_-]{3,16}$")
    @Column(length = 50)
    private String tenDangNhap;

    @NotBlank(message = "Hãy nhập mật khẩu")
    @Pattern(message = "Sai định dạng mật khẩu", regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
    @Column(length = 50)
    private String matKhau;

    private String matKhauXN;

    @NotBlank(message = "Hãy nhập email")
    @Email(message = "Email không không đúng định dạng")
    @Column(length = 100)
    private String email;

    @NotBlank(message = "Hãy nhập tên người dùng")
    @Column(length = 100)
    private String hoTen;

    @Column
    private Date ngaySinh;

    @Column
    private Boolean gioiTinh;

    @Column(length = 200)
    private String diaChi;

    @NotBlank(message = "Hãy nhập số điện thoại")
    @Pattern(message = "Sai định dạng số điện thoại", regexp = "^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$")
    @Column(length = 20)
    private String sdt;
}
