package com.klbstore.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NguoiDung implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer nguoiDungId;

    @NotBlank(message = "Hãy nhập tên người dùng")
    @Pattern(message = "Sai định dạng tên người dùng", regexp = "^[a-zA-Z0-9_-]{3,16}$")
    @Column(length = 50)
    private String tenDangNhap;

    @NotBlank(message = "Hãy nhập mật khẩu")
    @Column(length = 100)
    private String matKhau;

    @NotBlank(message = "Hãy nhập email")
    @Email(message = "Email không không đúng định dạng")
    @Column(length = 100)
    private String email;

    @NotBlank(message = "Hãy nhập tên người dùng")
    @Column(length = 100)
    private String hoTen;

    @Temporal(TemporalType.DATE)
    @Column
    private Date ngaySinh;

    @Column
    private Boolean gioiTinh;

    @NotBlank(message = "Hãy nhập địa chỉ")
    @Column(length = 200)
    private String diaChi;

    @NotBlank(message = "Hãy nhập số điện thoại")
    @Pattern(message = "Sai định dạng số điện thoại", regexp = "^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$")
    @Column(length = 20)
    private String sdt;

    @Column
    private Boolean quyenDangNhap = false;

    @Temporal(TemporalType.DATE)
    @Column
    private Date ngayDangKy = new Date();

    @Column
    private boolean trangThaiKhoa = false;

    @JsonIgnore
    @OneToMany(mappedBy = "nguoiDung")
    private List<DonHang> nguoiDungDonHangs;

    @JsonIgnore
    @OneToMany(mappedBy = "nguoiDung")
    private List<SanPham> nguoiDungSanPhams;
    
    @JsonIgnore
    @OneToMany(mappedBy = "nguoiDung")
    private List<PhieuXuat> nguoiDungPhieuXuats;

    @JsonIgnore
    @OneToMany(mappedBy = "nguoiDung")
    private List<DanhGia> nguoiDungDanhGias;

    @JsonIgnore
    @OneToMany(mappedBy = "nguoiDung")
    private List<PhieuNhap> nguoiDungPhieuNhaps;

    @JsonIgnore
    @OneToMany(mappedBy = "nguoiDung")
    private List<GioHang> nguoiDungGioHangs;

    @JsonIgnore
    @OneToMany(mappedBy = "nguoiDung")
    private List<HoatDongDangNhap> nguoiDungHoatDongDangNhaps;

    @JsonIgnore
    @OneToMany(mappedBy = "nguoiDung")
    private List<HoatDongSaiMatKhau> nguoiDungHoatDongSaiMatKhaus;

}
