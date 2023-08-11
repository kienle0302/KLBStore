package com.klbstore.delivery;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor

@JsonIgnoreProperties(ignoreUnknown = true)
public class Province {
    @JsonProperty("ProvinceID")
    private String provinceID;

    @JsonProperty("ProvinceName")
    private String provinceName;

    @JsonProperty("CountryID")
    private Integer countryID;
    
    @JsonProperty("NameExtension")
    private List<String> nameExtension;
    
    // @JsonProperty("Code")
    // private String code;


    // @JsonProperty("IsEnable")
    // private int isEnable;

    // @JsonProperty("RegionID")
    // private int regionID;

    // @JsonProperty("RegionCPN")
    // private int regionCPN;

    // @JsonProperty("UpdatedBy")
    // private Integer updatedBy;

    // @JsonProperty("UpdatedIP")
    // private String updatedIP;

    // @JsonProperty("UpdatedEmployee")
    // private String updatedEmployee;

    // @JsonProperty("UpdatedSource")
    // private String updatedSource;

    // @JsonProperty("UpdatedDate")
    // private String updatedDate;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS X")
    // @JsonProperty("CreatedAt")
    // private Date createdDate;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS X")
    // @JsonProperty("UpdatedAt")
    // private Date updatedAt;

    // @JsonProperty("CanUpdateCOD")
    // private boolean canUpdateCOD;

    // @JsonProperty("Status")
    // private int status;
}
