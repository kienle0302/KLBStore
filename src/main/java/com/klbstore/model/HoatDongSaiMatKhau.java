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
public class HoatDongSaiMatKhau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hoatDongSaiMatKhauId;
    @Column
    private Integer soLanSaiMatKhau;

    @Column(columnDefinition = "datetime2")
    private OffsetDateTime thoiGianSaiMatKhauCuoi;

    @Column(columnDefinition = "datetime2")
    private OffsetDateTime thoiGianKhoaTaiKhoan;

    @Column(length = 50)
    private String diaChiIp;

    @ManyToOne
    @JoinColumn(name = "nguoiDungId")
    private NguoiDung nguoiDung;
}
