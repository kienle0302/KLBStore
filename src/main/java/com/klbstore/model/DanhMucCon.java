package com.klbstore.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.io.Serializable;



import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DanhMucCon implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer danhMucConId;

    @Column(length = 100)
    private String tenDanhMucCon;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "danhMucSanPhamId")
    private DanhMucSanPham danhMucSanPham;

    @JsonIgnore
    @OneToMany(mappedBy = "danhMucCon")
    private List<SanPham> danhMucConSanPhams;

    @JsonIgnore
    @OneToMany(mappedBy = "danhMucCon")
    private List<GiamGiaDanhMucCon> danhMucConGiamGiaDanhMucCons;

}