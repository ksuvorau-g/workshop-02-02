package com.example.currencyexchange.repository;

import com.example.currencyexchange.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    @Query("SELECT e FROM ExchangeRate e WHERE e.baseCurrency = :base " +
           "AND e.targetCurrency = :target AND e.rateDate = " +
           "(SELECT MAX(e2.rateDate) FROM ExchangeRate e2 " +
           "WHERE e2.baseCurrency = :base AND e2.targetCurrency = :target)")
    Optional<ExchangeRate> findLatestRate(@Param("base") String baseCurrency, 
                                          @Param("target") String targetCurrency);

    List<ExchangeRate> findByBaseCurrencyAndTargetCurrencyOrderByRateDateDesc(
        String baseCurrency, String targetCurrency);

    List<ExchangeRate> findByRateDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
