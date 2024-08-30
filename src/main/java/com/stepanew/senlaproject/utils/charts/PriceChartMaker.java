package com.stepanew.senlaproject.utils.charts;

import com.stepanew.senlaproject.domain.entity.Price;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PriceChartMaker extends ChartMaker<Price> {

    private static final String TITLE = "Динамика цен";

    private static final String O_X_NAME = "Дата";

    private static final String O_Y_NAME = "Цена";

    private static final Integer LABEL_DENSITY = 15;

    public PriceChartMaker() {
        super(O_X_NAME, O_Y_NAME);
    }

    @Override
    public ByteArrayOutputStream createChart(List<Price> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        LocalDate startDate = data.get(0).getCheckedDate().toLocalDate();
        LocalDate endDate = data.get(data.size() - 1).getCheckedDate().toLocalDate();

        Map<LocalDate, BigDecimal> pricesByDate = new HashMap<>();
        BigDecimal lastPrice = null;

        for (Price point : data) {
            LocalDate date = point.getCheckedDate().toLocalDate();
            pricesByDate.put(date, point.getPrice());
            lastPrice = point.getPrice();
        }

        int numberOfDays = (int) java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        int step = Math.max(1, numberOfDays / LABEL_DENSITY);

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            BigDecimal price = pricesByDate.get(date);
            if (price == null) {
                price = lastPrice;
            } else {
                lastPrice = price;
            }

            if (pricesByDate.containsKey(date)) {
                dataset.addValue(price, O_Y_NAME, formatter.format(date));
            } else if (ChronoUnit.DAYS.between(startDate, date) % step == 0 || date.equals(endDate)) {
                dataset.addValue(price, O_Y_NAME, formatter.format(date));
            } else {
                dataset.addValue(price, O_Y_NAME, "");
            }
        }

        return super.useDataset(dataset, changeTitle(data.get(0)));
    }


    @Override
    public String changeTitle(Price title) {
        return TITLE +
                " на " +
                title.getProduct().getName() +
                " в магазине " +
                title.getStore().getName() +
                " по адресу " +
                title.getStore().getAddress();
    }
}
