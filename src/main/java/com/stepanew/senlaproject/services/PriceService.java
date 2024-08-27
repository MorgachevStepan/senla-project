package com.stepanew.senlaproject.services;

import com.stepanew.senlaproject.domain.dto.response.PriceComparisonResponseDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public interface PriceService {

    byte[] getPriceTrend(Long productId, Long storeId, LocalDateTime startDate, LocalDateTime endDate);

    byte[] getAveragePriceBy(Long productId, ChronoUnit averageBy, LocalDateTime startDate, LocalDateTime endDate);

    PriceComparisonResponseDto comparePrices(List<Long> productId, Long firstStoreId, Long secondStoreId);

    byte[] getPriceReport(Long productId, Long storeId, LocalDateTime startDate, LocalDateTime endDate);

}
