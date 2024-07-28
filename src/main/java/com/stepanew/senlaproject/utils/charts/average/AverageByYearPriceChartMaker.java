package com.stepanew.senlaproject.utils.charts.average;

import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class AverageByYearPriceChartMaker extends AveragePriceBigChartMaker {

    private static final String DATA_FORMAT = "yyyy";

    private static final String TITLE_EXTEND = "по годам";

    private static final ChronoUnit CHRONO_UNIT = ChronoUnit.YEARS;

    protected AverageByYearPriceChartMaker() {
        super(TITLE_EXTEND, DATA_FORMAT, CHRONO_UNIT);
    }

}
