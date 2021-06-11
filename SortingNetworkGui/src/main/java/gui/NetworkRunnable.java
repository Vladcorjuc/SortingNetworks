package gui;

import genetic_algorithm.composite_objective.CompositeGeneticAlgorithm;
import genetic_algorithm.hybrid.HybridGeneticAlgorithm;

public class NetworkRunnable implements Runnable {
    private CompositeGeneticAlgorithm algorithmCmo=null;
    private HybridGeneticAlgorithm algorithmHybrid=null;
    private SwingWorkerHybrid swingWorkerHybrid;
    private SwingWorkerCmo swingWorkerCmo;
    private int wires;
    private int depths;

    public NetworkRunnable(CompositeGeneticAlgorithm algorithmCmo, HybridGeneticAlgorithm algorithmHybrid,  SwingWorkerCmo swingWorkerCmo, SwingWorkerHybrid swingWorkerHybrid, int wires, int depths) {
        this.algorithmCmo = algorithmCmo;
        this.algorithmHybrid = algorithmHybrid;
        this.swingWorkerHybrid = swingWorkerHybrid;
        this.swingWorkerCmo = swingWorkerCmo;
        this.wires = wires;
        this.depths = depths;
    }

    @Override
    public void run() {
        if(algorithmCmo==null){
            this.algorithmHybrid.getSortedNetwork(wires,depths,depths,swingWorkerHybrid.getBestFitnessFifo(),
                    swingWorkerHybrid.getAverageFitnessFifo());
        }
        else {
            this.algorithmCmo.getSortedNetwork(wires,depths,swingWorkerCmo.getComposite1(),
                    swingWorkerCmo.getComposite2(),swingWorkerCmo.getComposite3());
        }
    }
}
