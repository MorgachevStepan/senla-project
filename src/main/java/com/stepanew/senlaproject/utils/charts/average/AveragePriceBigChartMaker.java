package com.stepanew.senlaproject.utils.charts.average;

import com.stepanew.senlaproject.domain.entity.Price;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

abstract class AveragePriceBigChartMaker extends AveragePriceChartMaker {

    private final String DATA_FORMAT;


    protected AveragePriceBigChartMaker(String titleExtend, String dataFormat, ChronoUnit chronoUnit) {
        super(titleExtend, chronoUnit);
        DATA_FORMAT = dataFormat;
    }


    @Override
    public ByteArrayOutputStream createChart(List<Price> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATA_FORMAT);
        Map<LocalDate, List<Price>> prices = null;

        // Группировка по годам и месяцам
        switch (getChronoUnit()) {
            case MONTHS -> prices = data.stream()
                    .collect(Collectors.groupingBy(price -> {
                        LocalDateTime dateTime = price.getCheckedDate();
                        return LocalDate.of(dateTime.getYear(), dateTime.getMonth(), 1);
                    }));
            case YEARS ->
                prices = data.stream()
                        .collect(Collectors.groupingBy(price -> {
                            LocalDateTime dateTime = price.getCheckedDate();
                            return LocalDate.of(dateTime.getYear(), 1, 1);
                        }));

        }

        // Посчитать среднюю стоимость
        Map<LocalDate, BigDecimal> averagePrice = prices.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> calculateAverage(entry.getValue())
                ));

        // Отсортировать и добавить в датасет
        averagePrice.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(element -> dataset.addValue(
                        element.getValue(), O_Y_NAME, formatter.format(element.getKey())
                ));

        return super.useDataset(dataset, changeTitle(data.get(0)));
    }

}
