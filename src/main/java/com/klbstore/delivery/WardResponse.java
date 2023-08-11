package com.klbstore.delivery;

import java.util.List;
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class WardResponse {
    private int code;
    private String message;
    private List<Ward> data;
}
