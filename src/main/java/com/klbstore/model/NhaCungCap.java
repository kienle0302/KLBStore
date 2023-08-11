package com.klbstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.io.Serializable;



import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NhaCungCap implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer nhaCungCapId;

    @Column(length = 100)
    @NotBlank (message = "{NotBlank.Model.nullText}")
    private String tenNhaCungCap;

    @Column(length = 100)
    @NotBlank (message = "{NotBlank.Model.nullText}")
    private String tenGiaoDich;

    @Column(length = 20)
    @NotBlank (message = "{NotBlank.Model.nullText}")
    @Size(min = 10, max = 10, message = "{Size.Model.sdt}")
    private String sdt;

    @Column(length = 100)
    @NotBlank (message = "{NotBlank.Model.nullText}")
    @Email (message = "{Email.Model.email}")
    private String email;

    @Column
    private Boolean hienThi;

    @OneToMany(mappedBy = "nhaCungCap")
    private Set<PhieuNhap> phieuNhaps;

}
