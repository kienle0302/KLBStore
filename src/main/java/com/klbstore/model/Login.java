package com.klbstore.model;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Login implements Serializable{
    @NotBlank(message = "Địa chỉ Email không được bỏ trống")
    @Email(message = "Địa chỉ Email không hợp lệ")
    private String email;
    
    @NotBlank(message = "Mật khẩu không được bỏ trống")
    private String password;
}
