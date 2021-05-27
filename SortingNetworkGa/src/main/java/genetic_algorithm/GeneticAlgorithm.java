package genetic_algorithm;
import genetic_algorithm.network.Utils;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import genetic_algorithm.network.Network;

public class GeneticAlgorithm {
    private int populationSize;
    private int runs;
    private int generations;

    private final Function<Pair<Chromosome,Population>,Double> fitness;
    private final Crossover crossover;
    private final Mutation mutation;
    private final Editor editor;
    private final Fixer fixer;
    private final Optimization optimization;

    public GeneticAlgorithm(int populationSize, int runs) {
        this.populationSize = populationSize;
        this.runs = runs;
        this.generations = Integer.MAX_VALUE;

        fitness = GeneticAlgorithm::evaluate;
        crossover = new MultiPointCrossover();
        //crossover = new SinglePointCrossover();
        mutation = new Mutation(0.03);
        editor = new Editor();
        fixer = new Fixer(editor);
        optimization = new Optimization(editor, fixer);

        Statistics.initialize(runs);
    }
    public GeneticAlgorithm(int populationSize, int runs, int generations) {
        this.populationSize = populationSize;
        this.runs = runs;
        this.generations = generations;

        fitness = GeneticAlgorithm::evaluate;
        crossover = new SinglePointCrossover();
        mutation = new Mutation(0.05);
        editor = new Editor();
        fixer = new Fixer(editor);
        optimization = new Optimization(editor,fixer);

        Statistics.initialize(runs);
    }

    public Network getSortedNetwork(int n,int d){

        Pair<Chromosome,Double> maxFit=new Pair<>(null,0.0);

        for(int i=0;i<runs;i++) {
            System.out.println("Run: "+i);
            Population population = Population.initialize(n, d,populationSize,fitness,editor,fixer,false);

            long startTime = System.nanoTime();
            int generation = 0;
            while (generation < generations) {
               // System.out.println("Generations: "+generation);

                Pair<Chromosome,Chromosome> parents = population.getParents();
                Chromosome offspring = crossover.cross(parents.getKey(),parents.getValue(),false);
                mutation.mutate(offspring,false);
                editor.edit(offspring,false);
                offspring = fixer.repair(offspring,false);
                //offspring = optimization.localOptimization(offspring);
                population.replace(parents,offspring,false);
                generation++;

                Network network = population.getBest().getKey().getNetwork();
                double max = evaluate(new Pair<>(population.getBest().getKey(),population));
                System.out.println(max);

                if(network.isSorting()){
                    //System.out.println("FOUND ->>>"+network);
                    //System.out.println(network.getLayers().size());
                    break;
                }
            }
            Pair<Chromosome,Double> bestIndividual = population.getBest();
            if(maxFit.getKey() == null || bestIndividual.getValue() > maxFit.getValue()){
                maxFit = bestIndividual;
            }

            Network network = maxFit.getKey().getNetwork();
            if(network.isSorting()){
                Statistics.foundInRound(i);
                System.out.println(maxFit.getKey());
                System.out.println("FOUND ->>>"+network);
                //System.out.println(maxFit.getKey());
                //break;
            }
            System.out.println(network);

            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            System.out.println("Time :"+duration);

        }

        return maxFit.getKey().getNetwork();
    }

    public static Double evaluate(Pair<Chromosome,Population> individual){
        Chromosome chromosome=individual.getKey();
        Network network = individual.getKey().getNetwork();
        int n= chromosome.getWires();
        return 1.0 - ((1.0 / (( n + 1) * (Utils.POW_2[n] - 1))) *
                ((network.getBad().size() + network.outputs().size()) - n - 1));

    }

    public static void main(String[] args) {
        //GeneticAlgorithm geneticAlgorithm=new GeneticAlgorithm(50,1,10);
        GeneticAlgorithm geneticAlgorithm=new GeneticAlgorithm(100,1);
        Network network = geneticAlgorithm.getSortedNetwork(10,7);
        System.out.println(network.getLayers().size()+" "+network.getComparators().size());
        System.out.println(Statistics.get());
    }

}
