package com.klbstore.delivery;
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class FeeResponse {
    private int code;
    private String message;
    private Fee data;
}
