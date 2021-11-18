package com.wholesale.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wholesale.config.ConfigProperties;
import com.wholesale.model.dto.ExchangeRatesApiResponse;
import com.wholesale.model.dto.ExchangeRatesDto;
import com.wholesale.service.RatesService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RatesController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class RatesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatesService ratesService;
    @MockBean
    private RestTemplate restTemplate;
    @MockBean
    private ConfigProperties configProperties;

    @Before
    public void setUp() {
        restTemplate = new RestTemplate();
    }

    @Test
    void loadExchangeRatesData() throws Exception {
        given(ratesService.invokeExchangeRatesApiAndLoadRatesData()).willReturn(() -> true);
        this.mockMvc.perform(get("/exchange-rates/load-exchange-rates"))
                .andExpect(status().isCreated());
    }

    @Test
    void getExchangeRatesForDateRange() throws Exception {
        ExchangeRatesApiResponse exchangeRatesApiResponse = ExchangeRatesApiResponse.builder().base("EUR").success(true).build();
        given(ratesService.getAllExchangeSpecificDateRange()).willReturn((t, t1) -> exchangeRatesApiResponse);
        given(ratesService.isValid()).willReturn(t -> true);
        given(ratesService.strToDate()).willReturn(dateStr -> LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        this.mockMvc.perform(get("/exchange-rates/date-range")
                        .param("fromDate", "2012-05-01")
                        .param("toDate", "2012-05-03")
                )
                .andExpect(status().isOk());
    }

    @Test
    void getExchangeRatesForSpecificDate() throws Exception {
        ExchangeRatesApiResponse exchangeRatesApiResponse = ExchangeRatesApiResponse.builder().base("EUR").success(true).build();
        given(ratesService.getExchangeRatesForSpecificDate()).willReturn(t -> exchangeRatesApiResponse);
        given(ratesService.isValid()).willReturn(t -> true);
        given(ratesService.strToDate()).willReturn(dateStr -> LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        this.mockMvc.perform(get("/exchange-rates/date/{date}", "2012-05-03")) //
                .andExpect(status().isOk());
    }

    @Test
    void getAll() throws Exception {
        given(ratesService.getAllRecordsForTesting()).willReturn(() -> List.of(ExchangeRatesDto.builder().base("EUR").toCurr("GBP").build()));
        this.mockMvc.perform(get("/exchange-rates/get-all")) //
                .andExpect(status().isOk());
    }
}