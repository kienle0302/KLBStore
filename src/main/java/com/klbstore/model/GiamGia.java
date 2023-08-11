package com.klbstore.model;

import java.time.LocalDate;
import java.util.List;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.io.Serializable;



import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GiamGia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer giamGiaId;

    @Column(length = 100)
    @NotBlank (message = "{NotBlank.Model.nullText}")
    private String tenGiamGia;

    @Column(columnDefinition = "varchar(max)")
    private String moTa;

    @Temporal(TemporalType.DATE)
    @Column
    @NotNull (message = "{NotNull.Model.nullDate}")
    private LocalDate ngayBatDau;

    @Temporal(TemporalType.DATE)
    @Column
    @Future (message = "{FutureOrPresent.dateFuture}")
    @NotNull (message = "{NotNull.Model.nullDate}")
    private LocalDate ngayKetThuc;

    @Column(length = 100)
    @Min(0)
    private String mucGiamGia;

    @Column
    private Boolean hienThi;

    @OneToMany(mappedBy = "giamGia")
    private List<GiamGiaSanPham> giamGiaGiamGiaSanPhams;

    @OneToMany(mappedBy = "giamGia")
    private List<GiamGiaDanhMuc> giamGiaGiamGiaDanhMucs;

    @OneToMany(mappedBy = "giamGia")
    private List<GiamGiaDanhMucCon> giamGiaGiamGiaDanhMucCons;

}
