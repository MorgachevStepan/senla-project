package com.stepanew.senlaproject.utils.charts.average;

import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class AverageByHourPriceChartMaker extends AveragePriceSmallChartMaker {

    private static final String DATA_FORMAT = "dd.MM.yyyy HH:mm";

    private static final String TITLE_EXTEND = "по часам";

    private static final ChronoUnit CHRONO_UNIT = ChronoUnit.HOURS;

    protected AverageByHourPriceChartMaker() {
        super(TITLE_EXTEND, DATA_FORMAT, CHRONO_UNIT);
    }

}
