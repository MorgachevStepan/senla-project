package com.stepanew.senlaproject.services;

import com.stepanew.senlaproject.domain.dto.response.PriceComparisonResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public interface PriceService {

    @Transactional(readOnly = true)
    byte[] getPriceTrend(Long productId, Long storeId, LocalDateTime startDate, LocalDateTime endDate);

    @Transactional(readOnly = true)
    byte[] getAveragePriceBy(Long productId, ChronoUnit averageBy, LocalDateTime startDate, LocalDateTime endDate);

    @Transactional(readOnly = true)
    PriceComparisonResponseDto comparePrices(List<Long> productId, Long firstStoreId, Long secondStoreId);

    @Transactional(readOnly = true)
    byte[] getPriceReport(Long productId, Long storeId, LocalDateTime startDate, LocalDateTime endDate);

}
