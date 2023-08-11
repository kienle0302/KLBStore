package com.klbstore.valid;

import java.io.Serializable;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Validated
@Data
public class UnlockAcc implements Serializable{
    @NotBlank(message = "Hãy nhập mật khẩu mới")
    private String newPass;
    @NotBlank(message = "Hãy nhập nhập mật khẩu xác nhận")
    private String subPass;
    @NotBlank(message = "Hãy nhập mã xác nhận")
    private String subCode;
}
