package genetic_algorithm.composite_objective;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SelectionPool {

    private List<Chromosome> population;
    private List<Chromosome> topFraction;
    private int poolSize;
    private double multiplier;
    private Random random;

    public SelectionPool(int poolSize,double multiplier, List<Chromosome> chromosomes) {
        population = new ArrayList<>();
        topFraction = new ArrayList<>();
        this.multiplier = multiplier;
        this.poolSize = poolSize;
        random = new Random();

        int addedItems=0;
        while(addedItems<poolSize+poolSize*multiplier){
            int randomPosition = random.nextInt(chromosomes.size());
            population.add(chromosomes.get(randomPosition));

            addedItems++;
        }
    }
    public Chromosome get(){
        int position = random.nextInt(poolSize);
        return population.get(position);
    }
    public void sort(){
        System.out.println("Sorting");
        population.sort((a, b) -> Integer.compare(CompositeGeneticAlgorithm.noveltyScore(b, population),
                CompositeGeneticAlgorithm.noveltyScore(a, population)));
        for(int i=0;i<poolSize;i++){
            topFraction.add(population.get(i));
        }

        for(int i=poolSize;i<population.size();i++){
            Chromosome individual = population.get(i);
            Chromosome toReplace = CompositeGeneticAlgorithm.minimumNovelty(individual,topFraction);
            topFraction.remove(toReplace);
            topFraction.add(individual);
        }

    }

}
