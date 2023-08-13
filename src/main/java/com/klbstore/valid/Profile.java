package com.klbstore.valid;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Profile implements Serializable {

    @Id
    private String nguoiDungId;

    private String tenDangNhap;

    private String matKhau;

    private String email;

    @NotBlank(message = "Hãy nhập tên người dùng")
    @Column(length = 100)
    private String hoTen;

    @Temporal(TemporalType.DATE)
    @Column
    private java.sql.Date ngaySinh;

    @Column
    private Boolean gioiTinh;

    @Column(length = 200)
    private String diaChi;

    @NotBlank(message = "Hãy nhập số điện thoại")
    // @Pattern(message = "Sai định dạng số điện thoại", regexp =
    // "^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$")
    @Column(length = 20)
    private String sdt;
}