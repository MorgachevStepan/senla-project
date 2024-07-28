package com.stepanew.senlaproject.utils.charts.average;

import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class AverageByDayPriceChartMaker extends AveragePriceSmallChartMaker {

    private static final String DATA_FORMAT = "dd.MM.yyyy";

    private static final String TITLE_EXTEND = "по дням";

    private static final ChronoUnit CHRONO_UNIT = ChronoUnit.DAYS;

    protected AverageByDayPriceChartMaker() {
        super(TITLE_EXTEND, DATA_FORMAT, CHRONO_UNIT);
    }

}
