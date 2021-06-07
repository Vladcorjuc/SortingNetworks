package genetic_algorithm;

import genetic_algorithm.composite_objective.CompositeGeneticAlgorithm;
import genetic_algorithm.hybrid.crossover.SinglePointCrossover;
import genetic_algorithm.hybrid.editor.CorrectWireEditor;
import genetic_algorithm.hybrid.editor.RedundancyEditor;
import genetic_algorithm.hybrid.HybridGeneticAlgorithm;
import genetic_algorithm.hybrid.mutation.SimpleMutation;
import genetic_algorithm.network.Network;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        System.out.println(" ~Optimal Depth Sorting Network~  by Vlad Corjuc\n");
        System.out.println(" Select the search method: ");
        System.out.println("\t\t 1. Genetic Algorithm Hybrid (Local Optimizers)");
        System.out.println("\t\t 2. Composite Genetic Algorithm With Novelty");
        System.out.print("Please pick one: ");
        String line = in.nextLine();
        int selected=1;
        try {
            selected=Integer.parseInt(line.trim());
        }catch (Exception e){
            System.err.println("Please select a valid number");
            System.exit(-1);
        }

        System.out.print("Network wires: ");
        line = in.nextLine();
        int wires=9;
        int depth=7;
        try {
            wires = Integer.parseInt(line.trim());
            if(wires<=0){throw new Exception("negative wires");}
        }catch (Exception e){
            System.err.println("Please select a valid number for wires");
            System.exit(-2);
        }


        System.out.print("Desired Depth: ");
        line = in.nextLine();
        try {
            depth = Integer.parseInt(line.trim());
            if(depth<=0){throw new Exception("negative depth");}
        }catch (Exception e){
            System.err.println("Please select a valid number for depth");
            System.exit(-3);
        }

        if(selected==1) {

            HybridGeneticAlgorithm hybridGeneticAlgorithm = new HybridGeneticAlgorithm(100, 1,
                    new SinglePointCrossover(), new CorrectWireEditor(),
                    new SimpleMutation(0.03), null, false);
            Network network = hybridGeneticAlgorithm.getSortedNetwork(wires,depth,depth);
            System.out.println(network.getLayers().size() + " " + network.getComparators().size());
            System.out.println(Statistics.get());
        }
        else {
            CompositeGeneticAlgorithm compositeGeneticAlgorithm = new CompositeGeneticAlgorithm(1,1000,1000,
                    0.10,0.30,0.4);
            compositeGeneticAlgorithm.getSortedNetwork(wires,depth);
        }
    }
}
