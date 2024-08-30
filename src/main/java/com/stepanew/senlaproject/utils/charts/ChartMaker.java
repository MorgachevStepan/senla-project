package com.stepanew.senlaproject.utils.charts;

import com.stepanew.senlaproject.exceptions.ChartException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class ChartMaker<T> {

    private final String O_X_NAME;

    private final String O_Y_NAME;

    private static final String FONT = "Arial";

    private static final Integer TITLE_SIZE = 20;

    private static final Integer NORMAL_SIZE = 16;

    protected JFreeChart lineChart;

    protected ChartMaker(String oXName, String oYName) {
        O_X_NAME = oXName;
        O_Y_NAME = oYName;
    }

    public abstract ByteArrayOutputStream createChart(java.util.List<T> data);

    public abstract String changeTitle(T title);

    protected ByteArrayOutputStream useDataset(DefaultCategoryDataset dataset, String title) {
        initializeChart(dataset, title);

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            ChartUtils.writeChartAsPNG(result, lineChart, 1600, 600);
        } catch (IOException e) {
            throw ChartException.CODE.SOMETHING_WRONG.get();
        }

        return result;
    }

    private void initializeChart(DefaultCategoryDataset dataset, String title) {
        lineChart = ChartFactory.createLineChart(
                title,
                O_X_NAME,
                O_Y_NAME,
                dataset,
                PlotOrientation.VERTICAL,
                false,  // Легенда
                true,   // Tooltips
                false   // URLs
        );

        lineChart.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        lineChart.getTitle().setFont(new Font(FONT, Font.BOLD, TITLE_SIZE));
        lineChart.getCategoryPlot().getDomainAxis().setLabelFont(new Font(FONT, Font.PLAIN, NORMAL_SIZE));
        lineChart.getCategoryPlot().getRangeAxis().setLabelFont(new Font(FONT, Font.PLAIN, NORMAL_SIZE));
    }

}
