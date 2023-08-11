package com.klbstore.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThongKeDoanhThu implements Serializable{
    @Id
    Serializable ngayDat;
    Integer soLuong;

}
