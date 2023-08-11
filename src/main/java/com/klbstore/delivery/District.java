package com.klbstore.delivery;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class District {
    @JsonProperty("DistrictID")
    private String districtID;

    @JsonProperty("ProvinceID")
    private String provinceID;

    @JsonProperty("DistrictName")
    private String districtName;

    @JsonProperty("NameExtension")
    private List<String> nameExtension;
    
    // @JsonProperty("SupportType")
    // private int supportType;

    // @JsonProperty("IsEnable")  // Thêm trường IsEnable và ánh xạ với JSON bằng annotation
    // private int isEnable;

    // @JsonProperty("Status")
    // private int status;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS X")
    // @JsonProperty("CreatedAt")
    // private Date createdDate;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS X")
    // @JsonProperty("UpdatedAt")
    // private Date updatedAt;

    // @JsonProperty("Code")
    // private String code;

    // @JsonProperty("Type")
    // private int type;

    // @JsonProperty("UpdatedBy")
    // private Integer updatedBy;

    // @JsonProperty("CanUpdateCOD")
    // private boolean canUpdateCOD;

    // @JsonProperty("PickType")
    // private int pickType;

    // @JsonProperty("DeliverType")
    // private int deliveryType;

    // @JsonProperty("WhiteListClient")
    // private Object WhiteListClient;

    // @JsonProperty("WhiteListDistrict")
    // private Object WhiteListDistrict;

    // @JsonProperty("ReasonCode")
    // private int reasonCode;

    // @JsonProperty("ReasonMessage")
    // private String reasonMessage;

    // @JsonProperty("OnDates")
    // private Object onDates;

    // @JsonProperty("UpdatedIP")
    // private String updatedIP;

    // @JsonProperty("UpdatedEmployee")
    // private String updatedEmployee;

    // @JsonProperty("UpdatedSource")
    // private String updatedSource;

    // @JsonProperty("UpdatedDate")
    // private String updatedDate;
}
