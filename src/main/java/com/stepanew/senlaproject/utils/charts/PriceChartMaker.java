package com.stepanew.senlaproject.utils.charts;

import com.stepanew.senlaproject.domain.entity.Price;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PriceChartMaker extends ChartMaker<Price> {

    private static final String TITLE = "Динамика цен";

    private static final String O_X_NAME = "Дата";

    private static final String O_Y_NAME = "Цена";

    public PriceChartMaker() {
        super(O_X_NAME, O_Y_NAME);
    }

    @Override
    public ByteArrayOutputStream createChart(List<Price> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        for (Price point : data) {
            dataset.addValue(point.getPrice(), O_Y_NAME, formatter.format(point.getCheckedDate()));
        }

        return super.useDataset(dataset, changeTitle(data.get(0)));
    }

    @Override
    String changeTitle(Price title) {
        return TITLE +
                " на " +
                title.getProduct().getName() +
                " в магазине " +
                title.getStore().getName() +
                " по адресу " +
                title.getStore().getAddress();
    }
}
