package com.klbstore.delivery;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class Leadtime {
    @JsonProperty("leadtime")
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private Date leadtime;

    @JsonProperty("order_date")
    @JsonDeserialize(using = UnixTimestampDeserializer.class)
    private Date orderDate;
}

