package genetic_algorithm;

import genetic_algorithm.crossover.SinglePointCrossover;
import genetic_algorithm.editor.CorrectWireEditor;
import genetic_algorithm.editor.RedundancyEditor;
import genetic_algorithm.hybrid.GeneticAlgorithm;
import genetic_algorithm.hybrid.Helper;
import genetic_algorithm.hybrid.Statistics;
import genetic_algorithm.network.Network;

public class Main {
    public static void main(String[] args) {
        GeneticAlgorithm geneticAlgorithm=new GeneticAlgorithm(100,1,
                new SinglePointCrossover(),new RedundancyEditor(),null,false);
        Network network = geneticAlgorithm.getSortedNetwork(9,7);
        System.out.println(network.getLayers().size()+" "+network.getComparators().size());
        System.out.println(Statistics.get());
    }
}
