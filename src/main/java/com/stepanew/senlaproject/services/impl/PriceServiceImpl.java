package com.stepanew.senlaproject.services.impl;

import com.stepanew.senlaproject.domain.entity.Price;
import com.stepanew.senlaproject.repository.PriceRepository;
import com.stepanew.senlaproject.services.PriceService;
import com.stepanew.senlaproject.utils.charts.ChartMaker;
import com.stepanew.senlaproject.utils.charts.average.AveragePriceChartMaker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    @Qualifier("priceChartMaker")
    private final ChartMaker<Price> priceChartMaker;

    private final List<AveragePriceChartMaker> averagePriceChartMakers;

    @Override
    public byte[] getPriceTrend(Long productId, Long storeId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Price> priceList = priceRepository.findAllByProduct_IdAndStore_IdAndCheckedDateBetween(
                productId,
                storeId,
                startDate,
                endDate
        );

        if (priceList.isEmpty()) {
            return new byte[0];
        }

        return priceChartMaker.createChart(priceList).toByteArray();
    }

    @Override
    public byte[] getAveragePriceBy(Long productId, ChronoUnit averageBy, LocalDateTime startDate, LocalDateTime endDate) {
        List<Price> priceList = priceRepository.findAllByProduct_IdAndCheckedDateBetween(
                productId,
                startDate,
                endDate
        );

        if (priceList.isEmpty()) {
            return new byte[0];
        }

        for (AveragePriceChartMaker averagePriceChartMaker: averagePriceChartMakers) {
            if (averagePriceChartMaker.getChronoUnit().equals(averageBy)) {
                return averagePriceChartMaker.createChart(priceList).toByteArray();
            }
        }

        return new byte[0];
    }

}
