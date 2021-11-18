package com.wholesale.model.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRatesDto {
    private String base;
    private String toCurr;
    private String date;
    private Double rate;

}
