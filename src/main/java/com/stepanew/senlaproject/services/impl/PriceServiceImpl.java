package com.stepanew.senlaproject.services.impl;

import com.stepanew.senlaproject.domain.dto.response.ComparisonResponseDto;
import com.stepanew.senlaproject.domain.dto.response.PriceComparisonResponseDto;
import com.stepanew.senlaproject.domain.dto.response.StoreWithPriceResponseDto;
import com.stepanew.senlaproject.domain.entity.Price;
import com.stepanew.senlaproject.exceptions.ParserException;
import com.stepanew.senlaproject.exceptions.PriceException;
import com.stepanew.senlaproject.repository.PriceRepository;
import com.stepanew.senlaproject.services.PriceService;
import com.stepanew.senlaproject.utils.charts.ChartMaker;
import com.stepanew.senlaproject.utils.charts.average.AveragePriceChartMaker;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    @Qualifier("priceChartMaker")
    private final ChartMaker<Price> priceChartMaker;

    private final List<AveragePriceChartMaker> averagePriceChartMakers;

    private final static String STATUS_MESSAGE =
            "В магазине %s по адресу %s дешевле чем в магазине %s по адресу %s на %.2f рублей или на %.2f процентов";

    private final static BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private final static String TITLE = "Price Dynamics";

    private final static String FIRST_COLUMN_TITLE = "Date";

    private final static String SECOND_COLUMN_TITLE = "Price";

    private final static byte FIRST_COLUMN_NUM = 0;

    private final static byte SECOND_COLUMN_NUM = 1;

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

        priceList.sort(Comparator.comparing(Price::getCheckedDate));

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

        for (AveragePriceChartMaker averagePriceChartMaker : averagePriceChartMakers) {
            if (averagePriceChartMaker.getChronoUnit().equals(averageBy)) {
                return averagePriceChartMaker.createChart(priceList).toByteArray();
            }
        }

        return new byte[0];
    }

    @Override
    public PriceComparisonResponseDto comparePrices(List<Long> productIds, Long firstStoreId, Long secondStoreId) {
        if (productIds.isEmpty()) {
            throw PriceException.CODE.EMPTY_DATA.get();
        }

        PriceComparisonResponseDto response = new PriceComparisonResponseDto(new ArrayList<>());

        for (Long productId : productIds) {
            Price firstPrice = findPriceByIdProductIdAndStoreId(productId, firstStoreId);
            Price secondPrice = findPriceByIdProductIdAndStoreId(productId, secondStoreId);

            StoreWithPriceResponseDto maxPrice = getMaxPrice(firstPrice, secondPrice);
            StoreWithPriceResponseDto minPrice = getMinPrice(firstPrice, secondPrice);

            String comparisonStatus = createStatus(minPrice, maxPrice);

            ComparisonResponseDto comparisonResult = new ComparisonResponseDto(
                    productId,
                    firstPrice.getProduct().getName(),
                    minPrice,
                    maxPrice,
                    comparisonStatus
            );

            response.comparison().add(comparisonResult);
        }

        return response;
    }

    @Override
    public byte[] getPriceReport(Long productId, Long storeId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Price> priceList = priceRepository.findAllByProduct_IdAndStore_IdAndCheckedDateBetween(
                productId,
                storeId,
                startDate,
                endDate
        );

        if (priceList.isEmpty()) {
            return new byte[0];
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(TITLE);

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(FIRST_COLUMN_NUM).setCellValue(FIRST_COLUMN_TITLE);
            headerRow.createCell(SECOND_COLUMN_NUM).setCellValue(SECOND_COLUMN_TITLE);

            int rowNum = 1;
            for (Price price : priceList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(FIRST_COLUMN_NUM).setCellValue(price.getCheckedDate().toString());
                row.createCell(SECOND_COLUMN_NUM).setCellValue(price.getPrice().toString());
            }

            for (int i = 0; i < 2; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw ParserException.CODE.SOMETHING_WRONG.get();
        }
    }

    private StoreWithPriceResponseDto getMaxPrice(
            Price firstPrice,
            Price secondPrice
    ) {
        return firstPrice.getPrice().compareTo(secondPrice.getPrice()) > 0 ?
                new StoreWithPriceResponseDto(
                        firstPrice.getStore().getId(),
                        firstPrice.getStore().getName(),
                        firstPrice.getStore().getAddress(),
                        firstPrice.getPrice()
                ) :
                new StoreWithPriceResponseDto(
                        secondPrice.getStore().getId(),
                        secondPrice.getStore().getName(),
                        secondPrice.getStore().getAddress(),
                        secondPrice.getPrice()
                );
    }

    private StoreWithPriceResponseDto getMinPrice(
            Price firstPrice,
            Price secondPrice
    ) {
        return firstPrice.getPrice().compareTo(secondPrice.getPrice()) <= 0 ?
                new StoreWithPriceResponseDto(
                        firstPrice.getStore().getId(),
                        firstPrice.getStore().getName(),
                        firstPrice.getStore().getAddress(),
                        firstPrice.getPrice()
                ) :
                new StoreWithPriceResponseDto(
                        secondPrice.getStore().getId(),
                        secondPrice.getStore().getName(),
                        secondPrice.getStore().getAddress(),
                        secondPrice.getPrice()
                );
    }

    private String createStatus(StoreWithPriceResponseDto minPrice, StoreWithPriceResponseDto maxPrice) {
        BigDecimal difference = maxPrice.price().subtract(minPrice.price());
        BigDecimal percentageDifference = difference
                .divide(maxPrice.price(), 2, RoundingMode.HALF_UP)
                .multiply(ONE_HUNDRED);

        return String.format(
                STATUS_MESSAGE,
                minPrice.name(),
                minPrice.address(),
                maxPrice.name(),
                maxPrice.address(),
                difference,
                percentageDifference
        );
    }

    private Price findPriceByIdProductIdAndStoreId(Long productId, Long storeId) {
        return priceRepository
                .findTopByProduct_IdAndStore_IdOrderByCheckedDateDesc(productId, storeId)
                .orElseThrow(PriceException.CODE.NO_SUCH_PRICE::get);
    }

}
