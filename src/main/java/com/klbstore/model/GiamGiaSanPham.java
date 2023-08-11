package com.klbstore.model;

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
public class GiamGiaSanPham implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer giamGiaSanPhamId;

    @ManyToOne
    @JoinColumn(name = "giamGiaId")
    private GiamGia giamGia;

    @ManyToOne
    @JoinColumn(name = "sanPhamId")
    private SanPham sanPham;

}
