package com.example.currencyexchange.controller;

import com.example.currencyexchange.model.ExchangeRate;
import com.example.currencyexchange.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exchange-rates")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping
    public ResponseEntity<List<ExchangeRate>> getAllRates() {
        return ResponseEntity.ok(exchangeRateService.getAllRates());
    }

    @GetMapping("/latest")
    public ResponseEntity<ExchangeRate> getLatestRate(
            @RequestParam String base,
            @RequestParam String target) {
        ExchangeRate rate = exchangeRateService.getLatestRate(base, target);
        if (rate != null) {
            return ResponseEntity.ok(rate);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/history")
    public ResponseEntity<List<ExchangeRate>> getRateHistory(
            @RequestParam String base,
            @RequestParam String target) {
        return ResponseEntity.ok(exchangeRateService.getRateHistory(base, target));
    }
}
