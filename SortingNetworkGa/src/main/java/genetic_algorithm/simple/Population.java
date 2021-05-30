package genetic_algorithm.simple;

import genetic_algorithm.network.Network;
import genetic_algorithm.network.Utils;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Population {
    List<Chromosome> chromosomes;
    List<Double> scoreValues;
    Double scoreSum;

    public Population(){
        chromosomes=new ArrayList<>();
        scoreValues=new ArrayList<>();
        scoreSum=0.0;
    }
    public static Population initialize(int n,int d, int populationSize){
        Population population = new Population();
        for(int i=0;i<populationSize;i++){
            Chromosome chromosome = new Chromosome(n,d,Network.createGreenFilter(n).getComparators());
            population.addChromosome(chromosome);
        }
        return population;
    }

    public void addChromosome(Chromosome chromosome){
        chromosomes.add(chromosome);
        Double value= score(chromosome);
        scoreValues.add(value);
        scoreSum += value;
    }
    public void replace(Pair<Chromosome ,Chromosome> parents, Chromosome offspring) {
        Double offSpringValue = score(offspring);
        int parent0Index = chromosomes.indexOf(parents.getKey());
        int parent1Index = chromosomes.indexOf(parents.getValue());
        if(offSpringValue > scoreValues.get(parent0Index)){
            replaceIndividual(parent0Index,offspring);
        }
        else if(offSpringValue > scoreValues.get(parent1Index)){
            replaceIndividual(parent1Index,offspring);
        }
        else{
            int worstIndividualIndex = Utils.argMin(scoreValues);
            replaceIndividual(worstIndividualIndex,offspring);
        }

    }

    private Double score( Chromosome individual) {
        Network network = individual.getNetwork();
        int n= individual.getWires();
        return Math.pow(2,n) - network.getBad().size();
    }

    public void replaceIndividual(int index, Chromosome offspring){
        scoreSum -= scoreValues.get(index);
        chromosomes.set(index,offspring);

        Double value= score(offspring);
        scoreValues.set(index,value);
        scoreSum += value;
    }

    public Pair<Chromosome, Chromosome> getParents() {
        Chromosome parent1 = getParent();
        Chromosome parent2 = getParent();
        if(parent1==null||parent2==null){
            return null;
        }
        return new Pair<>(parent1,parent2);
    }
    private Chromosome getParent() {

        double p = Math.random();
        double cumulativeProbability = 0.0;
        for (int i=0;i<chromosomes.size();i++) {

            cumulativeProbability += scoreValues.get(i)/scoreSum;
            if (p <= cumulativeProbability) {
                return chromosomes.get(i);
            }
        }
        return null;
    }

    public Pair<Chromosome, Double> getBest() {
        Pair<Chromosome,Double> max= new Pair<>(null, -1000.0);

        for (int i=0;i<chromosomes.size();i++) {
            Chromosome chromosome = chromosomes.get(i);
            Double value = scoreValues.get(i);
            if(value>max.getValue()){
                max= new Pair<>(chromosome,value);
            }
        }
        return max;
    }
}
