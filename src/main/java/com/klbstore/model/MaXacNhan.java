package com.klbstore.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class MaXacNhan implements Serializable {
    @Id
    @Column(name = "nguoiDungId")
    private Integer nguoiDungId;
    
    @Column
    private String maOtp;

    @Column
    private Boolean daXacNhan = false;

    @Column
    private Date ngayTaoOtp = new Date();

    @Column
    private Date hanHieuLucOtp = new Date(ngayTaoOtp.getTime() + 300000); // 5 phút

}
