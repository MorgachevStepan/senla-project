package com.stepanew.senlaproject.service;

import com.stepanew.senlaproject.domain.dto.response.PriceComparisonResponseDto;
import com.stepanew.senlaproject.domain.entity.Price;
import com.stepanew.senlaproject.domain.entity.Product;
import com.stepanew.senlaproject.domain.entity.Store;
import com.stepanew.senlaproject.exceptions.PriceException;
import com.stepanew.senlaproject.repository.PriceRepository;
import com.stepanew.senlaproject.services.impl.PriceServiceImpl;
import com.stepanew.senlaproject.utils.charts.ChartMaker;
import com.stepanew.senlaproject.utils.charts.average.AveragePriceChartMaker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PriceServiceImplTest {

    @Mock
    private PriceRepository priceRepository;

    @Mock
    private ChartMaker<Price> priceChartMaker;

    @Mock
    private List<AveragePriceChartMaker> averagePriceChartMakers;

    @InjectMocks
    private PriceServiceImpl priceService;

    private static final Long PRODUCT_ID = 1L;

    private static final Long FIRST_STORE_ID = 1L;

    private static final Long SECOND_STORE_ID = 2L;

    private static final LocalDateTime START_DATE = LocalDateTime.now().minusDays(30);

    private static final LocalDateTime END_DATE = LocalDateTime.now();

    private static final String DEFAULT_NAME = "NAME";

    private static final String DEFAULT_ADDRESS = "ADDRESS";

    private static final String DEFAULT_NUMBER = "88005553535";

    private static final String DEFAULT_DESCRIPTION = "DESCRIPTION";

    private static final BigDecimal PRICE = BigDecimal.valueOf(100);

    @Test
    void getPriceTrendTest() {
        List<Price> priceList = List.of(mock(Price.class));
        when(priceRepository.findAllByProduct_IdAndStore_IdAndCheckedDateBetween(
                PRODUCT_ID,
                FIRST_STORE_ID,
                START_DATE,
                END_DATE
        ))
                .thenReturn(priceList);
        when(priceChartMaker.createChart(priceList))
                .thenReturn(new ByteArrayOutputStream());

        byte[] result = priceService.getPriceTrend(
                PRODUCT_ID,
                FIRST_STORE_ID,
                START_DATE,
                END_DATE
        );

        assertNotNull(result);
        verify(priceChartMaker).createChart(priceList);
    }

    @Test
    void getPriceTrendWithEmptyResultTest() {
        when(priceRepository.findAllByProduct_IdAndStore_IdAndCheckedDateBetween(
                PRODUCT_ID,
                FIRST_STORE_ID,
                START_DATE,
                END_DATE
        ))
                .thenReturn(Collections.emptyList());

        byte[] result = priceService.getPriceTrend(
                PRODUCT_ID,
                FIRST_STORE_ID,
                START_DATE,
                END_DATE
        );

        assertEquals(0, result.length);
    }

    @Test
    void comparePricesTest() {
        List<Long> productIds = List.of(PRODUCT_ID);
        Price firstPrice = createPrice(FIRST_STORE_ID);
        Price secondPrice = createPrice(SECOND_STORE_ID);

        when(priceRepository.findTopByProduct_IdAndStore_IdOrderByCheckedDateDesc(
                PRODUCT_ID,
                FIRST_STORE_ID
        ))
                .thenReturn(Optional.of(firstPrice));
        when(priceRepository.findTopByProduct_IdAndStore_IdOrderByCheckedDateDesc(
                PRODUCT_ID,
                SECOND_STORE_ID
        ))
                .thenReturn(Optional.of(secondPrice));

        PriceComparisonResponseDto result = priceService.comparePrices(
                productIds,
                FIRST_STORE_ID,
                SECOND_STORE_ID
        );

        assertNotNull(result);
        assertEquals(1, result.comparison().size());
    }

    @Test
    void comparePricesTestThrowsException() {
        List<Long> productIds = Collections.emptyList();

        assertThrowsPriceException(() -> priceService.comparePrices(
                productIds,
                FIRST_STORE_ID,
                SECOND_STORE_ID
        ));
    }

    @Test
    void getPriceReportTest() {
        List<Price> priceList = List.of(createPrice(FIRST_STORE_ID));
        when(priceRepository.findAllByProduct_IdAndStore_IdAndCheckedDateBetween(
                PRODUCT_ID,
                FIRST_STORE_ID,
                START_DATE,
                END_DATE
        ))
                .thenReturn(priceList);

        byte[] result = priceService.getPriceReport(
                PRODUCT_ID,
                FIRST_STORE_ID,
                START_DATE,
                END_DATE
        );

        assertNotNull(result);
    }

    @Test
    void getPriceReportWithEmptyResultTest() {
        when(priceRepository.findAllByProduct_IdAndStore_IdAndCheckedDateBetween(
                PRODUCT_ID,
                FIRST_STORE_ID,
                START_DATE,
                END_DATE
        ))
                .thenReturn(Collections.emptyList());

        byte[] result = priceService.getPriceReport(
                PRODUCT_ID,
                FIRST_STORE_ID,
                START_DATE,
                END_DATE
        );

        assertEquals(0, result.length);
    }

    private Price createPrice(Long id) {
        Store store = new Store(
                id,
                DEFAULT_NAME,
                DEFAULT_NUMBER,
                DEFAULT_ADDRESS,
                new ArrayList<>()
        );

        Product product = new Product(
                PRODUCT_ID,
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION,
                null,
                new ArrayList<>()
        );

        return new Price(
                id,
                PRICE,
                END_DATE,
                product,
                store
        );
    }

    private void assertThrowsPriceException(Runnable action) {
        Assertions.assertThrows(
                PriceException.class, action::run
        );
    }
}
