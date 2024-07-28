package com.stepanew.senlaproject.services;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface PriceService {

    @Transactional(readOnly = true)
    byte[] getPriceTrend(Long productId, Long storeId, LocalDateTime startDate, LocalDateTime endDate);

}
