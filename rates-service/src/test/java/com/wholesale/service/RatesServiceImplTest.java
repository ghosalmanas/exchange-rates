package com.wholesale.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wholesale.config.ConfigProperties;
import com.wholesale.model.entity.ExchangeRatesEntity;
import com.wholesale.repository.ExchangeRatesRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class RatesServiceImplTest {

    @InjectMocks
    RatesServiceImpl ratesService;

    @Mock
    private ExchangeRatesRepository exchangeRatesRepository;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ConfigProperties configProperties;

    ExchangeRatesEntity exchangeRatesEntity;

    @BeforeEach
    void setUp() {
        exchangeRatesEntity = ExchangeRatesEntity.builder().rate(1.1).base("EUR").date(LocalDate.now()).toCurr("USD").build();
    }

    @Test
    void saveOrUpdateExchangeRates() {
        given(exchangeRatesRepository.saveAll(any(List.class))).willReturn(List.of(exchangeRatesEntity));
        ratesService.saveOrUpdateExchangeRates().accept(List.of(exchangeRatesEntity));
    }

    @Test
    void invokeExchangeRatesApiAndLoadRatesData() {
    }

    @Test
    void extractEntityFromApiResponse() {

    }

    @Test
    void getExchangeRatesForSpecificDate() {
        given(exchangeRatesRepository.getExchangeRateSpecificDateFromDb(any())).willReturn(List.of(exchangeRatesEntity));
        ratesService.getExchangeRatesForSpecificDate().apply(LocalDate.now());
    }

    @Test
    void getAllExchangeSpecificDateRange() {
        given(exchangeRatesRepository.getExchangeRateSpecificDateRangeFromDb(any(), any())).willReturn(List.of(exchangeRatesEntity));
        ratesService.getAllExchangeSpecificDateRange().apply(LocalDate.now(), LocalDate.now());
    }

    @Test
    void isValid() {
        Assert.assertTrue(ratesService.isValid().apply("2012-05-01"));
    }

    @Test
    void strToDate() {
        Assert.assertEquals(ratesService.strToDate().apply("2012-05-01"),
                LocalDate.parse("2012-05-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    @Test
    void getAllRecordsForTesting() {
        given(exchangeRatesRepository.findAll()).willReturn(List.of(exchangeRatesEntity));
        ratesService.getAllRecordsForTesting().get();
    }
}