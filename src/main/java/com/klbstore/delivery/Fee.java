package com.klbstore.delivery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fee {
    @JsonProperty("total")
    private Double total;
    // @JsonProperty("service_fee")
    // private int serviceFee;

    // @JsonProperty("insurance_fee")
    // private int insuranceFee;

    // @JsonProperty("pick_station_fee")
    // private int pickStationFee;

    // @JsonProperty("coupon_value")
    // private int couponValue;

    // @JsonProperty("r2s_fee")
    // private int r2sFee;

    // @JsonProperty("return_again")
    // private int returnAgain;

    // @JsonProperty("document_return")
    // private int documentReturn;

    // @JsonProperty("double_check")
    // private int doubleCheck;

    // @JsonProperty("cod_fee")
    // private int codFee;

    // @JsonProperty("pick_remote_areas_fee")
    // private int pickRemoteAreasFee;

    // @JsonProperty("deliver_remote_areas_fee")
    // private int deliverRemoteAreasFee;

    // @JsonProperty("cod_failed_fee")
    // private int codFailedFee;
}
