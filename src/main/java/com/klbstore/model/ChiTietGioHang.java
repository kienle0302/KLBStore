package com.klbstore.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class ChiTietGioHang implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chiTietGioHangId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "gioHangId")
    private GioHang gioHang;
    @ManyToOne
    @JoinColumn(name = "mauSacId")
    private MauSac mauSac;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sanPhamId")
    private SanPham sanPham;
    @Column
    private Integer soLuong;

}
