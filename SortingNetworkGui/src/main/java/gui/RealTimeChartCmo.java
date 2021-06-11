package gui;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.markers.SeriesMarkers;

import javax.swing.*;

public class RealTimeChartCmo implements Runnable {
    private final SwingWrapper<XYChart> sw;
    private final XYChart chart;
    private final SwingWorkerCmo swingWorkerCmo;

    public RealTimeChartCmo() {

        chart = new XYChartBuilder().width(600).height(400).xAxisTitle("Time").yAxisTitle("Fitness values").build();
        // Series
        chart.addSeries("composite 1", new double[] { 0}).setMarker(SeriesMarkers.NONE);
        chart.addSeries("composite 2", new double[] { 0}).setMarker(SeriesMarkers.NONE);
        chart.addSeries("composite 3", new double[] { 0}).setMarker(SeriesMarkers.NONE);

        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setXAxisTicksVisible(false);
        sw = new SwingWrapper<XYChart>(chart);
        swingWorkerCmo = new SwingWorkerCmo(sw, chart);
    }

    public SwingWorkerCmo getSwingWorkerCmo() {
        return swingWorkerCmo;
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
        swingWorkerCmo.execute();
    }
}
