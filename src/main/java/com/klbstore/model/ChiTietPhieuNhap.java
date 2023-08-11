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
public class ChiTietPhieuNhap implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chiTietPhieuNhapId;

    @Column
    private Integer soLuong;

    @Column
    private Double donGia;

    @ManyToOne
    @JoinColumn(name = "sanPhamId")
    private SanPham sanPham;

    @ManyToOne
    @JoinColumn(name = "phieuNhapId")
    private PhieuNhap phieuNhap;

}