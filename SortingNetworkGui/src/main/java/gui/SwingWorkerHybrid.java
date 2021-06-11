package gui;

import javafx.util.Pair;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SwingWorkerHybrid extends SwingWorker<Boolean, Pair<double[],double[]>> {

    private final List<Double> bestFitnessFifo = Collections.synchronizedList(new ArrayList<Double>());
    private final List<Double> averageFitnessFifo = Collections.synchronizedList(new ArrayList<Double>());
    private final SwingWrapper<XYChart> sw;
    private final XYChart chart;

    public SwingWorkerHybrid(SwingWrapper<XYChart> sw, XYChart chart) {
        this.sw = sw;
        this.chart = chart;
    }

    @Override
    protected Boolean doInBackground() throws Exception {

        while (!isCancelled()) {
            if(averageFitnessFifo.size() >500){
                averageFitnessFifo.remove(0);
            }
            if (bestFitnessFifo.size() > 500) {
                bestFitnessFifo.remove(0);
            }

            double[] bestFitness = new double[bestFitnessFifo.size()];
            synchronized (bestFitnessFifo) {

                for (int i = 0; i < bestFitnessFifo.size(); i++) {
                    bestFitness[i] = bestFitnessFifo.get(i);
                }
            }

            double[] averageFitness = new double[averageFitnessFifo.size()];
            synchronized (averageFitnessFifo) {

                for (int i = 0; i <averageFitnessFifo.size(); i++) {
                    averageFitness[i] = averageFitnessFifo.get(i);
                }
            }
            publish(new Pair<>(bestFitness,averageFitness));

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                // eat it. caught when interrupt is called
                //System.err.println("MySwingWorker shut down.");
            }

        }

        return true;
    }

    @Override
    protected void process(List<Pair<double[],double[]>> chunks) {

        Pair<double[],double[]> mostRecentDataSet = chunks.get(chunks.size() - 1);
        chart.updateXYSeries("best fitness", null, mostRecentDataSet.getKey(), null);
        chart.updateXYSeries("average fitness", null, mostRecentDataSet.getValue(), null);
        sw.repaintChart();

        long start = System.currentTimeMillis();
        long duration = System.currentTimeMillis() - start;
        try {
            Thread.sleep(40 - duration); // 40 ms ==> 25fps
            // Thread.sleep(400 - duration); // 40 ms ==> 2.5fps
        } catch (InterruptedException e) {
        }

    }

    public List<Double> getBestFitnessFifo() {
        return bestFitnessFifo;
    }

    public List<Double> getAverageFitnessFifo() {
        return averageFitnessFifo;
    }
}