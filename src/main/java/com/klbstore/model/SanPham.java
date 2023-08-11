package com.klbstore.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Controller
@Table(name = "SanPham")
public class SanPham implements Serializable {

    @Id
    @Column(insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sanPhamId;

    @Column
    private String tenSanPham;

    @Column
    private String moTa;

    @Column
    @NotBlank(message = "{NotBlank.Model.nullText}")
    private String xuatSu;

    @Column
    @Min(0)
    @NotNull(message = "Chưa Nhập Giá Bán!")
    private Double giaBan;

    @Column
    @Min(0)
    private Integer soLuotXem = 0;

    @Column
    private Boolean conHang;

    @Column
    private Boolean noiBat;

    @Column
    private Integer canNang;

    @Column
    private Double chieuDai;

    @Column
    private Double chieuRong;

    @Column
    private Double doDay;

    @Column
    private Boolean hienThi;

    @Column
    private LocalDate ngayTao;

    @ManyToOne
    @JoinColumn(name = "danhMucSanPhamId")
    private DanhMucSanPham danhMucSanPham;

    @ManyToOne
    @JoinColumn(name = "danhMucConId")
    private DanhMucCon danhMucCon;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "nguoiDungId")
    private NguoiDung nguoiDung;

    @JsonIgnore
    @OneToMany(mappedBy = "sanPham")
    private List<MauSac> sanPhamMauSacs;

    @OneToMany(mappedBy = "sanPham")
    private List<ChiTietSanPham> sanPhamChiTietSanPhams;
    @JsonIgnore
    @OneToMany(mappedBy = "sanPham")
    private List<CauHinhLaptop> sanPhamCauHinhLaptops;

    @OneToMany(mappedBy = "sanPham")
    private List<CauHinhDienThoai> sanPhamCauHinhDienThoais;
    @JsonIgnore
    @OneToMany(mappedBy = "sanPham")
    private List<ChiTietDonHang> sanPhamChiTietDonHangs;
    @JsonIgnore
    @OneToMany(mappedBy = "sanPham")
    private List<ChiTietPhieuXuat> sanPhamChiTietPhieuXuats;
    @OneToMany(mappedBy = "sanPham")
    private List<DanhGia> sanPhamDanhGias;
    @JsonIgnore
    @OneToMany(mappedBy = "sanPham")
    private List<AnhSanPham> sanPhamAnhSanPhams;
    @JsonIgnore
    @OneToMany(mappedBy = "sanPham")
    private List<ChiTietPhieuNhap> sanPhamChiTietPhieuNhaps;
    @JsonIgnore
    @OneToMany(mappedBy = "sanPham")
    private List<ChiTietGioHang> sanPhamChiTietGioHangs;
    @JsonIgnore
    @OneToMany(mappedBy = "sanPham")
    private List<GiamGiaSanPham> sanPhamGiamGiaSanPhams;
    @JsonIgnore
    @OneToMany(mappedBy = "sanPham")
    private List<GiamGiaTrucTiep> sanPhamGiamGiaTrucTieps;

}
