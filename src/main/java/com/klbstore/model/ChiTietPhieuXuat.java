package com.klbstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.io.Serializable;



import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChiTietPhieuXuat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chiTietPhieuXuatId;

    @Column
    private Integer soLuongXuat;

    @Column
    private Double giaBan;

    @ManyToOne
    @JoinColumn(name = "donHangId")
    private DonHang donHang;

    @ManyToOne
    @JoinColumn(name = "phieuXuatId")
    private PhieuXuat phieuXuat;

    @ManyToOne
    @JoinColumn(name = "sanPhamId")
    private SanPham sanPham;

}
