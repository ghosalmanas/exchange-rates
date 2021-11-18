package com.wholesale.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wholesale.model.entity.ExchangeRatesEntity;

public interface ExchangeRatesRepository extends JpaRepository<ExchangeRatesEntity, Long> {

    @Query("select u from ExchangeRatesEntity  u where u.date= :date")
    List<ExchangeRatesEntity> getExchangeRateSpecificDateFromDb(@Param("date") LocalDate date);

    @Query("select u from ExchangeRatesEntity  u where u.date between :fromDate and :toDate order by u.date asc ")
    List<ExchangeRatesEntity> getExchangeRateSpecificDateRangeFromDb(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

}
