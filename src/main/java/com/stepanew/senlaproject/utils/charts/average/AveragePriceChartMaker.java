package com.stepanew.senlaproject.utils.charts.average;

import com.stepanew.senlaproject.domain.entity.Price;
import com.stepanew.senlaproject.utils.charts.ChartMaker;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.List;

public abstract class AveragePriceChartMaker extends ChartMaker<Price> {

    private static final String TITLE = "Средняя цена";

    private static final String O_X_NAME = "Дата";

    protected static final String O_Y_NAME = "Цена";

    private final String TITLE_EXTEND;

    private final ChronoUnit CHRONO_UNIT;


    protected AveragePriceChartMaker(String titleExtend, ChronoUnit chronoUnit) {
        super(O_X_NAME, O_Y_NAME);
        TITLE_EXTEND = titleExtend;
        CHRONO_UNIT = chronoUnit;
    }

    public abstract ByteArrayOutputStream createChart(java.util.List<Price> data);

    protected BigDecimal calculateAverage(List<Price> value) {
        BigDecimal total = value
                .stream()
                .map(Price::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(BigDecimal.valueOf(value.size()), 2, RoundingMode.HALF_UP.ordinal());
    }

    @Override
    public String changeTitle(Price title) {
        return TITLE +
                " на " +
                title.getProduct().getName() +
                " " +
                TITLE_EXTEND;
    }

    public ChronoUnit getChronoUnit() {
        return CHRONO_UNIT;
    }
}
