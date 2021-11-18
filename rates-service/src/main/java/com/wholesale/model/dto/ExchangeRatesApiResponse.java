package com.wholesale.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExchangeRatesApiResponse {

    private boolean success;
    private String base;
    private String date;
    private String start_date;
    private String end_date;
    private Long timestamp;
    private Boolean timeseries;
    private Map<String, Object> rates;
}
