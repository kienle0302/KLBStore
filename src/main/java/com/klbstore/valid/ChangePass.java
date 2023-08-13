package com.klbstore.valid;

import java.io.Serializable;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Validated
@Data
public class ChangePass implements Serializable {
    
    @NotBlank(message = "Hãy nhập mật khẩu hiện tại")
    private String curPass;

    @NotBlank(message = "Hãy nhập mật khẩu mới")
    private String newPass;

    @NotBlank(message = "Hãy nhập mật khẩu xác nhận")
    private String subPass;
}
