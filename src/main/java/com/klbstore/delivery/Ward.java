package com.klbstore.delivery;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ward {
    @JsonProperty("WardCode")
    private String wardCode;

    @JsonProperty("DistrictID")
    private String districtID;

    @JsonProperty("WardName")
    private String wardName;

    @JsonProperty("NameExtension")
    private List<String> nameExtension;

    // @JsonProperty("CanUpdateCOD")
    // private boolean canUpdateCOD;

    // @JsonProperty("SupportType")
    // private int supportType;

    // @JsonProperty("Status")
    // private int status;

    // @JsonProperty("CreatedDate")
    // private Date createdDate;

    // @JsonProperty("UpdatedDate")
    // private Date updatedDate;

    // @JsonProperty("PickType")
    // private int pickType;

    // @JsonProperty("DeliverType")
    // private int deliveryType;

    // @JsonProperty("WhiteListClient")
    // private Object whiteListClient;

    // @JsonProperty("WhiteListWard")
    // private Object whiteListWard;

    // @JsonProperty("ReasonCode")
    // private int reasonCode;    

    // @JsonProperty("ReasonMessage")
    // private String reasonMessage;

    // @JsonProperty("OnDates")
    // private Object onDates;

    // @JsonProperty("CreatedIP")
    // private String createdIP;

    // @JsonProperty("CreatedEmployee")
    // private String createdEmployee;

    // @JsonProperty("CreatedSource")
    // private String createdSource;

    // @JsonProperty("UpdatedIP")
    // private String updatedIP;

    // @JsonProperty("UpdatedEmployee")
    // private String updatedEmployee;

    // @JsonProperty("UpdatedSource")
    // private String updatedSource;

    // @JsonProperty("IsEnable")
    // private int isEnable;

    // @JsonProperty("UpdatedBy")
    // private Integer updatedBy;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS X")
    // @JsonProperty("CreatedAt")
    // private Date createdAt;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS X")
    // @JsonProperty("UpdatedAt")
    // private Date updatedAt;

}


