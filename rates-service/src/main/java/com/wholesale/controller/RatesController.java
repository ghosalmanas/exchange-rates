package com.wholesale.controller;

import com.wholesale.model.dto.ExchangeRatesApiResponse;
import com.wholesale.model.dto.ExchangeRatesDto;
import com.wholesale.service.RatesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exchange-rates")
@AllArgsConstructor
@Slf4j
public class RatesController {

    private final RatesService ratesService;


    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("/load-exchange-rates")
    public boolean loadExchangeRatesData() {
        return ratesService.invokeExchangeRatesApiAndLoadRatesData().get();

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/date-range")
    public ExchangeRatesApiResponse getExchangeRatesForDateRange(
            @RequestParam(name = "fromDate") String fromDate,
            @RequestParam(name = "toDate") String toDate
    ) {
        Assert.notNull(fromDate, "invalid input fromDate as null");
        Assert.notNull(toDate, "invalid input toDate as null");
        Assert.isTrue(ratesService.isValid().apply(fromDate), "invalid input fromDate");
        Assert.isTrue(ratesService.isValid().apply(toDate), "invalid input toDate");

        return ratesService.getAllExchangeSpecificDateRange().apply(ratesService.strToDate().apply(fromDate), ratesService.strToDate().apply(toDate));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/date/{date}")
    public ExchangeRatesApiResponse getExchangeRatesForSpecificDate(@PathVariable String date) {
        Assert.notNull(date, "invalid input date as null");
        Assert.isTrue(ratesService.isValid().apply(date), "Invalid Date");
        return ratesService.getExchangeRatesForSpecificDate().apply(ratesService.strToDate().apply(date));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/get-all")
    public List<ExchangeRatesDto> getAll() {
        return ratesService.getAllRecordsForTesting().get();
    }

}
