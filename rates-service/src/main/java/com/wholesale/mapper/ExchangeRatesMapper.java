package com.wholesale.mapper;

import com.wholesale.model.dto.ExchangeRatesDto;
import com.wholesale.model.entity.ExchangeRatesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExchangeRatesMapper {

    ExchangeRatesEntity fromDtoToEntity(ExchangeRatesDto ExchangeRatesDto);

    ExchangeRatesDto fromEntityToDto(ExchangeRatesEntity ExchangeRatesEntity);

}
