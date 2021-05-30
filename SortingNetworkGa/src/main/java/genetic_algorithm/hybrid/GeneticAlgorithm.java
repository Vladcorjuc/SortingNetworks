package genetic_algorithm.hybrid;
import genetic_algorithm.crossover.ICrossover;
import genetic_algorithm.crossover.SinglePointCrossover;
import genetic_algorithm.editor.CorrectWireEditor;
import genetic_algorithm.editor.IEditor;
import genetic_algorithm.network.Utils;
import javafx.util.Pair;

import java.util.function.Function;
import genetic_algorithm.network.Network;

public class GeneticAlgorithm {
    private int populationSize;
    private int runs;
    private int generations;
    private boolean verbose;

    private final Function<Chromosome,Double> fitness;
    private final ICrossover crossover;
    private final Mutation mutation;
    private final IEditor editor;
    private final Fixer fixer;
    private final Helper helper;
    private final IEditor cleaner = new CorrectWireEditor();
    private final Optimization optimization;

    public GeneticAlgorithm(int populationSize, int runs,ICrossover crossover ,IEditor editor,
                            Helper helper,boolean verbose) {
        this.populationSize = populationSize;
        this.runs = runs;
        this.generations = Integer.MAX_VALUE;

        fitness = GeneticAlgorithm::evaluate;
        this.crossover = crossover;
        mutation = new Mutation(0.03);
        this.editor=editor;
        fixer = new Fixer(editor);
        optimization = new Optimization(editor, fixer);
        this.helper= helper;
        this.verbose=verbose;

        Statistics.initialize(runs);
    }
    public GeneticAlgorithm(int populationSize, int runs, int generations,ICrossover crossover ,
                            IEditor editor,Helper helper,boolean verbose) {
        this.populationSize = populationSize;
        this.runs = runs;
        this.generations = generations;

        fitness = GeneticAlgorithm::evaluate;
        this.crossover = crossover;
        mutation = new Mutation(0.03);
        this.editor=editor;
        fixer = new Fixer(editor);
        optimization = new Optimization(editor,fixer);

        this.helper= helper;
        this.verbose=verbose;

        Statistics.initialize(runs);
    }

    public Network getSortedNetwork(int n,int d){

        Pair<Chromosome,Double> maxFit=new Pair<>(null,0.0);
        Pair<Integer,Double> generationMaxFit = new Pair<>(-1,-1.0);

        for(int i=0;i<runs;i++) {
            System.out.println("Run: "+i);
            Population population = Population.initialize(n, d,d,populationSize,fitness,editor,fixer,verbose);
            long startTime = System.nanoTime();
            int generation = 0;
            while (generation < generations) {

                Pair<Chromosome,Chromosome> parents = population.getParents();
                Chromosome offspring = crossover.cross(parents.getKey(),parents.getValue(),verbose);
                mutation.mutate(offspring,verbose);
                editor.edit(offspring,verbose);
                offspring = fixer.repair(offspring,verbose);

                //cleaner.edit(offspring,verbose);
               // offspring = optimization.localOptimization(offspring);

                population.replace(parents,offspring,verbose);
                generation++;
                Network network = population.getBest().getKey().getNetwork();
                double max = evaluate(population.getBest().getKey());
                System.out.println("Generation: "+generation+"\tMax fitness: "+max);

                if(helper!=null) {
                    if (max > generationMaxFit.getValue()) {
                        generationMaxFit = new Pair<>(generation, max);
                    }

                    if (max == generationMaxFit.getValue() &&
                            generation - generationMaxFit.getKey() >= helper.patience()) {
                        helper.trim(population, 0.50);
                        generationMaxFit = new Pair<>(generation, max);

                    }
                }
                if(network.isSorting()){
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
            }

            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            System.out.println("Time :"+duration);

        }

        return maxFit.getKey().getNetwork();
    }

    public static Double evaluate(Chromosome individual){
        Network network = individual.getNetwork();
        int n= individual.getWires();

        if(individual.getComparatorsOutOfDepth()==0) {
            return 1.0 - ((1.0 / ((n + 1) * (Utils.POW_2[n] - 1))) *
                    ((network.getBad().size() + network.outputs().size()) - n - 1));
        }
        return 1.0 - ((1.0 / ((n + 1) * (Utils.POW_2[n] - 1))) *
                ((network.getBad().size() + network.outputs().size()) - n - 1)) -
                individual.getComparatorsOutOfDepth()/
                        ((individual.depth() - individual.getTargetDepth())* individual.getWires()/2.0);
    }

    public static void main(String[] args) {
        GeneticAlgorithm geneticAlgorithm=new GeneticAlgorithm(100,1,
                new SinglePointCrossover(),new CorrectWireEditor(),null,false);
        Network network = geneticAlgorithm.getSortedNetwork(9,7);
        System.out.println(network.getLayers().size()+" "+network.getComparators().size());
        System.out.println(Statistics.get());
    }

}
