package com.klbstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.io.Serializable;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class HinhThucThanhToan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hinhThucThanhToanId;

    @Column(length = 100)
    private String tenHinhThucThanhToan;

    @Column(columnDefinition = "varchar(max)")
    private String moTa;

    @Column
    private Boolean hinhThuc;

    @OneToMany(mappedBy = "hinhThucThanhToan")
    private Set<DonHang> hinhThucThanhToanDonHangs;

}
