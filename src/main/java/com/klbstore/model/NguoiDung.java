package com.klbstore.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NguoiDung implements UserDetails {

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

    // New column - ROLE
    @Enumerated(EnumType.STRING)
    private Role role;

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

    public NguoiDung(
            String tenDangNhap,
            String matKhau,
            String email,
            String hoTen,
            Date ngaySinh,
            String sdt,
            Role role) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.email = email;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.sdt = sdt;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getUsername() {
        return tenDangNhap;
    }

    @Override
    public String getPassword() {
        return matKhau;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !trangThaiKhoa;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return quyenDangNhap;
    }

}
