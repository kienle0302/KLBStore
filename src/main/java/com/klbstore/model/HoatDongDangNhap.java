package com.klbstore.model;

import java.time.OffsetDateTime;

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
public class HoatDongDangNhap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hoatDongDangNhapId;

    @Column(columnDefinition = "datetime2")
    private OffsetDateTime thoiGianDangNhap;

    @Column(length = 50)
    private String diaChiIp;
    @Column
    private Boolean thanhCong;

    @ManyToOne
    @JoinColumn(name = "nguoiDungId")
    private NguoiDung nguoiDung;
}
