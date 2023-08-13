package com.klbstore.valid;

import java.io.Serializable;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Login implements Serializable {
    @NotBlank(message = "Hãy nhập số điện thoại")
    private String sdt;

    @NotBlank(message = "Hãy nhập mật khẩu")
    private String matKhau;
}
