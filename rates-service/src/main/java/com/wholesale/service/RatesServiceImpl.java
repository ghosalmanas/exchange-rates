package com.wholesale.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wholesale.config.ConfigProperties;
import com.wholesale.exception.NotFoundException;
import com.wholesale.model.dto.ExchangeRatesApiResponse;
import com.wholesale.model.dto.ExchangeRatesDto;
import com.wholesale.model.entity.ExchangeRatesEntity;
import com.wholesale.repository.ExchangeRatesRepository;
import com.wholesale.utils.Util;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Log4j2
public class RatesServiceImpl implements RatesService {
    private final ExchangeRatesRepository exchangeRatesRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ConfigProperties configProperties;

    @Override
    public Consumer<List<ExchangeRatesEntity>> saveOrUpdateExchangeRates() {
        return exchangeRatesEntityList -> exchangeRatesRepository.saveAll(exchangeRatesEntityList);

    }

    public Supplier<Boolean> invokeExchangeRatesApiAndLoadRatesData() {

        return () -> {
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String dateBefore1Year = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            ExchangeRatesApiResponse exchangeRatesApiResponse = null;
            String exchangeRatesApiUrl = configProperties.getTimeSeriesUrl() + configProperties.getAccessKey() + "&start_date=" + dateBefore1Year + "&end_date=" + today + "&base=EUR&symbols=GBP,USD,HKD&format=1";
            try {
                exchangeRatesApiResponse = restTemplate.getForObject(exchangeRatesApiUrl, ExchangeRatesApiResponse.class);
            } catch (Exception e) {
                e.printStackTrace();

                /*Just Use next two lines only for testing with sample data in case of access issue with ExchangeRatesApi. Comment out on priority **/
                /*Test Code Start **/
                String sampleJsonToValidateFunctionality = Util.prepareJson();
                try {
                    exchangeRatesApiResponse = objectMapper.readValue(sampleJsonToValidateFunctionality, ExchangeRatesApiResponse.class);
                } catch (JsonProcessingException ex) {
                    log.error(ex.getLocalizedMessage());
                }
                /*Test Code End **/
            }
            if (exchangeRatesApiResponse != null && exchangeRatesApiResponse.isSuccess()) {
                List<ExchangeRatesEntity> entities = extractEntityFromApiResponse().apply(exchangeRatesApiResponse);
                saveOrUpdateExchangeRates().accept(entities);
                return true;
            }
            return false;
        };
    }


    @Override
    public Function<ExchangeRatesApiResponse, List<ExchangeRatesEntity>> extractEntityFromApiResponse() {
        return result ->
                result.getRates().entrySet().stream().map(rateEntry -> {
                    List<ExchangeRatesEntity> entitiesInner = ((Map<String, Double>) (rateEntry.getValue())).entrySet().stream().map(currEntry -> {
                        ExchangeRatesEntity exchangeRatesEntity = new ExchangeRatesEntity();
                        exchangeRatesEntity.setDate(LocalDate.parse(rateEntry.getKey(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        exchangeRatesEntity.setBase(result.getBase());
                        exchangeRatesEntity.setToCurr(currEntry.getKey());
                        exchangeRatesEntity.setRate(currEntry.getValue());
                        return exchangeRatesEntity;
                    }).collect(Collectors.toList());
                    return entitiesInner;
                }).flatMap(list -> list.stream()).collect(Collectors.toList());
    }


    @Override
    public Function<LocalDate, ExchangeRatesApiResponse> getExchangeRatesForSpecificDate() {
        return date -> {
            ExchangeRatesApiResponse exchangeRatesApiResponse = new ExchangeRatesApiResponse();
            Map<String, Object> rates = new LinkedHashMap<>();

            List<ExchangeRatesEntity> entities = exchangeRatesRepository.getExchangeRateSpecificDateFromDb(date);

            entities.stream().forEach(
                    exchangeRatesEntity -> {
                        rates.put(exchangeRatesEntity.getToCurr(), exchangeRatesEntity.getRate());
                        exchangeRatesApiResponse.setRates(rates);

                    });
            entities.stream().findFirst().ifPresentOrElse(exchangeRatesEntity -> {
                        exchangeRatesApiResponse.setBase(exchangeRatesEntity.getBase());
                        exchangeRatesApiResponse.setDate(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        exchangeRatesApiResponse.setSuccess(true);
                    },
                    () -> {
                        throw new NotFoundException(String.format("record for date '%s' not found", date));
                    });
            return exchangeRatesApiResponse;
        };
    }


    @Override
    public BiFunction<LocalDate, LocalDate, ExchangeRatesApiResponse> getAllExchangeSpecificDateRange() {
        return (fromDate, toDate) -> {
            ExchangeRatesApiResponse exchangeRatesApiResponse = new ExchangeRatesApiResponse();
            Map<String, Object> rates = new LinkedHashMap<>();

            exchangeRatesRepository.getExchangeRateSpecificDateRangeFromDb(fromDate, toDate).stream().forEach(
                    exchangeRatesEntity -> {
                        if (rates.containsKey(exchangeRatesEntity.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))) {
                            ((Map<String, Double>) (rates.get(exchangeRatesEntity.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))).put(exchangeRatesEntity.getToCurr(), exchangeRatesEntity.getRate());
                        } else {
                            Map<String, Double> innerRate = new LinkedHashMap<>();
                            innerRate.put(exchangeRatesEntity.getToCurr(), exchangeRatesEntity.getRate());
                            rates.put(exchangeRatesEntity.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), innerRate);
                        }

                    });
            Optional<ExchangeRatesEntity> exchangeRatesEntityOptional = exchangeRatesRepository.getExchangeRateSpecificDateRangeFromDb(fromDate, toDate).stream().findFirst();
            exchangeRatesEntityOptional.ifPresentOrElse(exchangeRatesEntity -> {
                        exchangeRatesApiResponse.setBase(exchangeRatesEntity.getBase());
                        exchangeRatesApiResponse.setSuccess(true);
                        exchangeRatesApiResponse.setDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        exchangeRatesApiResponse.setRates(rates);
                    },
                    () -> {
                        throw new NotFoundException(String.format("record for date range '%s' and '%s' not found", fromDate, toDate));
                    }
            );
            return exchangeRatesApiResponse;
        };
    }

    @Override
    public Function<String, Boolean> isValid() {
        return dateStr -> {
            try {
                LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                return false;
            }
            return true;
        };
    }

    @Override
    public Function<String, LocalDate> strToDate() {
        return date -> LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    public Supplier<List<ExchangeRatesDto>> getAllRecordsForTesting() {
        return () -> exchangeRatesRepository.findAll().stream().map(i -> ExchangeRatesDto.builder().rate(i.getRate()).base(i.getBase())
                .date(i.getDate().toString()).toCurr(i.getToCurr()).build()).collect(Collectors.toList());
    }

}
