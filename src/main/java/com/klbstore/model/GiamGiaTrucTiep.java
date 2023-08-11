package com.klbstore.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class GiamGiaTrucTiep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer giamGiaTrucTiepId;

    @Column(precision = 7, scale = 2)
    private BigDecimal giamGiaTrucTiep;

    @Column
    private LocalDateTime ngayBatDau;

    @Column
    private LocalDateTime ngayKetThuc;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sanPhamId")
    private SanPham sanPham;
}
