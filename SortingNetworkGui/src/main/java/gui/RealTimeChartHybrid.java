package gui;

import javafx.util.Pair;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.markers.SeriesMarkers;

import javax.swing.*;
import java.util.*;

public class RealTimeChartHybrid implements Runnable {
    private final SwingWrapper<XYChart> sw;
    private final XYChart chart;
    private final SwingWorkerHybrid swingWorkerHybrid;

    public SwingWorkerHybrid getSwingWorkerHybrid() {
        return swingWorkerHybrid;
    }

    public RealTimeChartHybrid() {

        chart = new XYChartBuilder().width(600).height(400).xAxisTitle("Time").yAxisTitle("Fitness").build();
        // Series
        chart.addSeries("best fitness", new double[] { 0}).setMarker(SeriesMarkers.NONE);
        chart.addSeries("average fitness", new double[] { 0}).setMarker(SeriesMarkers.NONE);
        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setXAxisTicksVisible(false);
        sw = new SwingWrapper<XYChart>(chart);
        swingWorkerHybrid = new SwingWorkerHybrid(sw, chart);
    }

    public SwingWrapper<XYChart> getSw() {
        return sw;
    }
    public XYChart getChart() {
        return chart;
    }

    @Override
    public void run() {
        sw.displayChart().setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        swingWorkerHybrid.execute();
    }

}
