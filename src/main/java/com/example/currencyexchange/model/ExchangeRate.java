package com.example.currencyexchange.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_rates", indexes = {
    @Index(name = "idx_base_target_date", columnList = "base_currency,target_currency,rate_date")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "base_currency", nullable = false, length = 3)
    private String baseCurrency;

    @Column(name = "target_currency", nullable = false, length = 3)
    private String targetCurrency;

    @Column(name = "rate", nullable = false, precision = 19, scale = 6)
    private BigDecimal rate;

    @Column(name = "rate_date", nullable = false)
    private LocalDateTime rateDate;

    @Column(name = "source", nullable = false, length = 50)
    private String source;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
