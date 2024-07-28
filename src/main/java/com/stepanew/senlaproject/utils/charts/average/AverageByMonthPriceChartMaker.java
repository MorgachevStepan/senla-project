package com.stepanew.senlaproject.utils.charts.average;

import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class AverageByMonthPriceChartMaker extends AveragePriceBigChartMaker {

    private static final String DATA_FORMAT = "MM.yyyy";

    private static final String TITLE_EXTEND = "по месяцам";

    private static final ChronoUnit CHRONO_UNIT = ChronoUnit.MONTHS;

    protected AverageByMonthPriceChartMaker() {
        super(TITLE_EXTEND, DATA_FORMAT, CHRONO_UNIT);
    }
}
