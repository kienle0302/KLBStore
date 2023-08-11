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
public class CauHinhLaptop implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long laptopId;

    @Column(length = 100)
    private String cpu;

    @Column(length = 100)
    private String ram;

    @Column(length = 50)
    private String oCung;

    @Column(length = 100)
    private String manHinh;

    @Column(length = 100)
    private String cardDoHoa;
    @Column(length = 100)
    private String pin;

    @ManyToOne
    @JoinColumn(name = "sanPhamId")
    private SanPham sanPham;
}

