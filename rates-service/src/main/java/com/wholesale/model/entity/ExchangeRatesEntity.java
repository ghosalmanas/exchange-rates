package com.wholesale.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "exchange_rates")
public class ExchangeRatesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "base_curr")
    private String base;

    @Column(name = "to_curr")
    private String toCurr;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "rate")
    private Double rate;


}
