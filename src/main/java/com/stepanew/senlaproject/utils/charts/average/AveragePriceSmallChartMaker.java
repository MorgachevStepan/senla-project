package com.stepanew.senlaproject.utils.charts.average;

import com.stepanew.senlaproject.domain.entity.Price;
import com.stepanew.senlaproject.exceptions.ChartException;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

abstract class AveragePriceSmallChartMaker extends AveragePriceChartMaker {

    private final String DATA_FORMAT;


    protected AveragePriceSmallChartMaker(String titleExtend, String dataFormat, ChronoUnit chronoUnit) {
        super(titleExtend, chronoUnit);
        DATA_FORMAT = dataFormat;
    }

    @Override
    public ByteArrayOutputStream createChart(List<Price> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATA_FORMAT);

        Map<LocalDateTime, List<Price>> pricesByDate;

        try {
            //группировка
            pricesByDate = data
                    .stream()
                    .collect(Collectors.groupingBy(price -> price.getCheckedDate().truncatedTo(getChronoUnit())));
        } catch (UnsupportedTemporalTypeException e) {
            throw ChartException.CODE.UNIT_TOO_LARGE.get();
        }

        //посчитали среднюю стоимость
        Map<LocalDateTime, BigDecimal> averagePrice = pricesByDate
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> super.calculateAverage(entry.getValue())
                ));

        //отсортировали и поместили в датасет
        averagePrice
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(
                        element -> dataset.addValue(
                                element.getValue(), O_Y_NAME, formatter.format(element.getKey())
                        )
                );

        return super.useDataset(dataset, changeTitle(data.get(0)));
    }
}
