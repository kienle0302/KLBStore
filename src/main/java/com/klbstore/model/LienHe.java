package com.klbstore.model;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LienHe implements Serializable{
    @NotBlank(message = "Tên không được bỏ trống")
    private String name;
    @NotBlank(message = "Địa chỉ Email không được bỏ trống")
    @Email(message = "Địa chỉ Email không hợp lệ")
    private String email;
    @NotBlank(message = "Chủ đề không được bỏ trống")
    private String subject;
    @NotBlank(message = "Nội dung không được bỏ trống")
    private String message;
    public LienHe(String name, String email) {
        this.name = name;
        this.email = email;
    }
    
}
