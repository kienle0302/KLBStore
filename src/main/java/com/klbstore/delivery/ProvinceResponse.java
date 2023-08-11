package com.klbstore.delivery;

import java.util.List;
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class ProvinceResponse {
    private int code;
    private String message;
    private List<Province> data;

    // Constructors, getters, setters
}

