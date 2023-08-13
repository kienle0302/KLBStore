package com.klbstore.security.registration;

import java.util.Date;

import groovy.transform.builder.Builder;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    private String username;
    private String password;
    private String email;
    private String fullname;
    private String phone;
    @Temporal(TemporalType.DATE)
    public Date birthday;
}
