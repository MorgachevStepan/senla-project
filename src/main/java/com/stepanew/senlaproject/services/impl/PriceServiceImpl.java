package com.stepanew.senlaproject.services.impl;

import com.stepanew.senlaproject.domain.entity.Price;
import com.stepanew.senlaproject.repository.PriceRepository;
import com.stepanew.senlaproject.services.PriceService;
import com.stepanew.senlaproject.utils.charts.ChartMaker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    @Qualifier("priceChartMaker")
    private final ChartMaker<Price> priceCharMaker;

    @Override
    public byte[] getPriceTrend(Long productId, Long storeId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Price> priceList = priceRepository.findAllByProduct_IdAndStore_IdAndCheckedDateBetween(
                productId,
                storeId,
                startDate,
                endDate
        );

        priceList.forEach(System.out::println);

        return priceCharMaker.createChart(priceList).toByteArray();
    }

}
