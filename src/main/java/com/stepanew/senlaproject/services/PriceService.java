package com.stepanew.senlaproject.services;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public interface PriceService {

    @Transactional(readOnly = true)
    byte[] getPriceTrend(Long productId, Long storeId, LocalDateTime startDate, LocalDateTime endDate);

    @Transactional(readOnly = true)
    byte[] getAveragePriceBy(Long productId, ChronoUnit averageBy, LocalDateTime startDate, LocalDateTime endDate);
}
