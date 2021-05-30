package genetic_algorithm.simple;

import genetic_algorithm.hybrid.Statistics;
import genetic_algorithm.network.Network;
import genetic_algorithm.network.Utils;
import javafx.util.Pair;

public class GeneticAlgorithm {
    private int runs;
    private int generations;
    private int popSize;
    private Crossover crossover;
    private IndirectReplacementMutation mutation;

    public GeneticAlgorithm(int runs,int generations,int populationSize,
                            double crossoverRate,double crossoverProportion,
                            double mutationRate) {
        this.runs=runs;
        this.generations = generations;
        this.popSize = populationSize;
        this.crossover = new Crossover(crossoverRate,crossoverProportion);
        this.mutation = new IndirectReplacementMutation(mutationRate);
    }

    public void getSortedNetwork(int n, int d){
        for(int run=0;run<runs;run++){
            int generation=0;
            Population population = Population.initialize(n,d,popSize);
            boolean startCounting =false;
            int step=0;
            while (generation<generations){
                System.out.print("Generation: "+step);
                Pair<Chromosome,Chromosome> parents = population.getParents();
                Chromosome offspring = crossover.cross(parents.getKey(),parents.getValue());
                mutation.mutate(offspring);
                population.replace(parents,offspring);
                Pair<Chromosome,Double> best = population.getBest();
                Network network = best.getKey().getNetwork();
                System.out.println("\tFitness: "+best.getValue());


                if(network.isSorting()){
                    startCounting=true;
                }
                if(startCounting){
                    generation++;
                }
                step++;
            }
            Pair<Chromosome,Double> bestIndividual = population.getBest();

            Network network = bestIndividual.getKey().getNetwork();
            if(network.isSorting()){
                Statistics.foundInRound(run);
                System.out.println(bestIndividual.getKey());
                System.out.println("FOUND ->>>"+network);
            }
            System.out.println(network);
        }
    }
    public static Double evaluate(Chromosome individual){
        Network network = individual.getNetwork();
        int n= individual.getWires();
        return Math.pow(2,n) - network.getBad().size()  - 0.06 * network.getOverDepth(individual.getTargetDepth());
    }

    public static void main(String[] args) {
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(1,2000,300,
                0.10,0.30,0.4);
        geneticAlgorithm.getSortedNetwork(7,6);
    }

}
