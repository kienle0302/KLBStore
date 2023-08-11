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
public class AnhSanPham implements Serializable {

    @Id
    @Column(insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long anhSanPhamId;

    @Column(length = 500)
    private String duongDan;

    @ManyToOne
    @JoinColumn(name = "sanPhamId")
    private SanPham sanPham;
}