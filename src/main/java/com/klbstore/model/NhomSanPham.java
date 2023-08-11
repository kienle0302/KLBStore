package com.klbstore.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.io.Serializable;



import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NhomSanPham implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer nhomSanPhamId;

    @Column(length = 100)
    private String tenNhomSanPham;

    @JsonIgnore
    @OneToMany(mappedBy = "nhomSanPham")
    private Set<DanhMucSanPham> danhMucSanPhams;

}
