package com.example.currencyexchange.service;

import com.example.currencyexchange.dto.ExchangeRatesApiResponse;
import com.example.currencyexchange.model.ExchangeRate;
import com.example.currencyexchange.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final RestTemplate restTemplate;

    @Value("${exchange.api.fixer.url:https://api.exchangeratesapi.io/latest}")
    private String exchangeRatesApiUrl;

    @Value("${exchange.api.base-currency:EUR}")
    private String baseCurrency;

    @Scheduled(fixedRateString = "${exchange.fetch.rate:3600000}") // Default: every hour
    public void fetchExchangeRates() {
        log.info("Starting scheduled exchange rate fetch");
        
        try {
            fetchFromExchangeRatesApi();
        } catch (Exception e) {
            log.error("Error fetching exchange rates from exchangeratesapi.io", e);
        }
    }

    private void fetchFromExchangeRatesApi() {
        try {
            String url = exchangeRatesApiUrl + "?base=" + baseCurrency;
            log.info("Fetching exchange rates from: {}", url);
            
            ExchangeRatesApiResponse response = restTemplate.getForObject(url, ExchangeRatesApiResponse.class);
            
            if (response != null && response.getRates() != null) {
                List<ExchangeRate> rates = new ArrayList<>();
                LocalDateTime rateDate = response.getTimestamp() != null 
                    ? LocalDateTime.ofInstant(Instant.ofEpochSecond(response.getTimestamp()), ZoneId.systemDefault())
                    : LocalDateTime.now();
                
                for (Map.Entry<String, BigDecimal> entry : response.getRates().entrySet()) {
                    ExchangeRate rate = ExchangeRate.builder()
                        .baseCurrency(baseCurrency)
                        .targetCurrency(entry.getKey())
                        .rate(entry.getValue())
                        .rateDate(rateDate)
                        .source("exchangeratesapi.io")
                        .build();
                    rates.add(rate);
                }
                
                exchangeRateRepository.saveAll(rates);
                log.info("Successfully saved {} exchange rates", rates.size());
            }
        } catch (Exception e) {
            log.error("Error processing exchange rates", e);
            throw e;
        }
    }

    public List<ExchangeRate> getAllRates() {
        return exchangeRateRepository.findAll();
    }

    public ExchangeRate getLatestRate(String base, String target) {
        return exchangeRateRepository.findLatestRate(base, target).orElse(null);
    }

    public List<ExchangeRate> getRateHistory(String base, String target) {
        return exchangeRateRepository.findByBaseCurrencyAndTargetCurrencyOrderByRateDateDesc(base, target);
    }
}
