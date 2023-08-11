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
public class EmailForm implements Serializable{
    @NotBlank(message = "Hãy nhập email của bạn")
    @jakarta.validation.constraints.Email(message = "Email không đúng định dạng")
    private String email;

    // @NotBlank(message = "Mã xác nhận ")
    private String mxn;
}

