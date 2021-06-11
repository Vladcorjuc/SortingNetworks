package gui;

import javafx.util.Pair;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwingWorkerCmo extends SwingWorker<Boolean, List<double[]>> {

    private final List<Double> composite1 = Collections.synchronizedList(new ArrayList<Double>());
    private final List<Double> composite2 = Collections.synchronizedList(new ArrayList<Double>());
    private final List<Double> composite3 = Collections.synchronizedList(new ArrayList<Double>());
    private final SwingWrapper<XYChart> sw;
    private final XYChart chart;

    public SwingWorkerCmo(SwingWrapper<XYChart> sw, XYChart chart) {
        this.sw = sw;
        this.chart = chart;
    }

    @Override
    protected Boolean doInBackground() throws Exception {

        while (!isCancelled()) {
            if(composite1.size() >500){
                composite1.remove(0);
            }
            if (composite2.size() > 500) {
                composite2.remove(0);
            }
            if (composite3.size() > 500) {
                composite3.remove(0);
            }


            double[] composite1Values = new double[composite1.size()];
            synchronized (composite1) {

                for (int i = 0; i < composite1.size(); i++) {
                    composite1Values[i] = composite1.get(i);
                }
            }
            double[] composite2Values = new double[composite2.size()];
            synchronized (composite2) {

                for (int i = 0; i < composite2.size(); i++) {
                    composite2Values[i] = composite2.get(i);
                }
            }
            double[] composite3Values = new double[composite3.size()];
            synchronized (composite3) {

                for (int i = 0; i < composite3.size(); i++) {
                    composite3Values[i] = composite3.get(i);
                }
            }

            List<double[]> array= new ArrayList<>();
            array.add(composite1Values);array.add(composite2Values);array.add(composite3Values);
            publish(array);

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
    protected void process(List<List<double[]>> chunks) {

        List<double[]> mostRecentDataSet = chunks.get(chunks.size() - 1);
        chart.updateXYSeries("composite 1", null, mostRecentDataSet.get(0), null);
        chart.updateXYSeries("composite 2", null, mostRecentDataSet.get(1), null);
        chart.updateXYSeries("composite 3", null, mostRecentDataSet.get(2), null);
        sw.repaintChart();

        long start = System.currentTimeMillis();
        long duration = System.currentTimeMillis() - start;
        try {
            Thread.sleep(40 - duration); // 40 ms ==> 25fps
            // Thread.sleep(400 - duration); // 40 ms ==> 2.5fps
        } catch (InterruptedException e) {
        }

    }

    public List<Double> getComposite1() {
        return composite1;
    }
    public List<Double> getComposite2() {
        return composite2;
    }
    public List<Double> getComposite3() {
        return composite3;
    }
}
