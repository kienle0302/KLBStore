package com.klbstore.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class MaXacNhan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "nguoidungid")
    private NguoiDung nguoiDung;

    @Column(nullable = false)
    private String maOtp;

    @Column(nullable = false)
    private Boolean daXacNhan = false;

    @Column(nullable = false)
    private LocalDateTime ngayTaoOtp;

    @Column(nullable = false)
    private LocalDateTime hanHieuLucOtp; // 5 phút

    public MaXacNhan(NguoiDung nguoiDung, String maOtp, Boolean daXacNhan, LocalDateTime ngayTaoOtp, LocalDateTime hanHieuLucOtp) {
        this.nguoiDung = nguoiDung;
        this.maOtp = maOtp;
        this.daXacNhan = daXacNhan;
        this.ngayTaoOtp = ngayTaoOtp;
        this.hanHieuLucOtp = hanHieuLucOtp;
    }
}
