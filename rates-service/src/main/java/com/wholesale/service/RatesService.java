package com.wholesale.service;

import com.wholesale.model.dto.ExchangeRatesApiResponse;
import com.wholesale.model.dto.ExchangeRatesDto;
import com.wholesale.model.entity.ExchangeRatesEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface RatesService {

    Supplier<Boolean> invokeExchangeRatesApiAndLoadRatesData();

    Consumer<List<ExchangeRatesEntity>> saveOrUpdateExchangeRates();

    Function<ExchangeRatesApiResponse, List<ExchangeRatesEntity>> extractEntityFromApiResponse();

    Function<LocalDate, ExchangeRatesApiResponse> getExchangeRatesForSpecificDate();

    Function<String, Boolean> isValid();

    Function<String,LocalDate> strToDate();

    BiFunction<LocalDate,LocalDate, ExchangeRatesApiResponse> getAllExchangeSpecificDateRange();

    Supplier<List<ExchangeRatesDto>> getAllRecordsForTesting();
}
