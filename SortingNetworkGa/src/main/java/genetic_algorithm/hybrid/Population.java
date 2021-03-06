package genetic_algorithm.hybrid;

import genetic_algorithm.hybrid.editor.IEditor;
import genetic_algorithm.hybrid.editor.RedundancyEditor;
import genetic_algorithm.network.Network;
import genetic_algorithm.network.Utils;
import javafx.util.Pair;

import java.util.*;
import java.util.function.Function;

public class Population {

    private List<Chromosome> population;
    private List<Double>  fitnessValues;
    private double fitnessSum;

    private Function<Chromosome,Double> fitness;


    public Population(int n, Function<Chromosome, Double> fitness) {
        this.fitness=fitness;
        this.population=new ArrayList<>();
        this.fitnessValues=new ArrayList<>();
        this.fitnessSum=0;
    }
    public static Population initialize(int n, int parallelLayerSize,int d , int populationSize,
                                        Function<Chromosome, Double> fitness, IEditor editor, Fixer fixer,
                                        boolean verbose) {
        if(verbose){System.out.print("Initializing population...");System.out.flush();}
        Population population = new Population(n,fitness);
        Network filter = Network.createGreenFilter(n);
        for(int i=0;i<populationSize;i++){
            //Chromosome chromosome = new Chromosome(parallelLayerSize,n);
           Chromosome chromosome = new Chromosome(parallelLayerSize,n,d,filter.getLayers());
           editor.edit(chromosome,verbose);
           chromosome = fixer.repair(chromosome,verbose);
           population.addChromosome(chromosome);
        }
        if(verbose){System.out.println("Done");System.out.flush();}
        return population;
    }

    public void addChromosome(Chromosome chromosome){
        population.add(chromosome);
        Double value= fitness.apply(chromosome);
        fitnessValues.add(value);
        fitnessSum += value;
    }
    public List<Chromosome> getPopulation() {
        return population;
    }
    public Chromosome getIndividual(int index){
        return population.get(index);
    }

    public int getBestPosition() {
        Pair<Integer,Double> max= new Pair<>(-1, -1.0);

        for (int i=0;i<population.size();i++) {
            Chromosome chromosome = population.get(i);
            Double value = fitnessValues.get(i);
            if(value>max.getValue()){
                max= new Pair<>(i,value);
            }
        }
        return max.getKey();
    }
    public List<Double> getFitnessValues() {
        return fitnessValues;
    }

    public double getFitnessSum() {
        return fitnessSum;
    }

    public Pair<Chromosome, Double> getBest() {
        Pair<Chromosome,Double> max= new Pair<>(null, -1.0);

        for (int i=0;i<population.size();i++) {
            Chromosome chromosome = population.get(i);
            Double value = fitnessValues.get(i);
            if(value>max.getValue()){
                max= new Pair<>(chromosome,value);
            }
        }
        return max;
    }
    public Pair<Chromosome,Chromosome> getParents() {
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
        for (int i=0;i<population.size();i++) {

            cumulativeProbability += fitnessValues.get(i)/fitnessSum;
            if (p <= cumulativeProbability) {
                return population.get(i);
            }
        }
        return null;
    }

    public void replace(Pair<Chromosome,Chromosome> parents, Chromosome offspring,boolean  verbose) {
        if(verbose){System.out.print("Replace population...");}
        Double offSpringValue = fitness.apply(offspring);
        int parent0Index = population.indexOf(parents.getKey());
        int parent1Index = population.indexOf(parents.getValue());
        if(offSpringValue > fitnessValues.get(parent0Index)){
            replaceIndividual(parent0Index,offspring);
        }
        else if(offSpringValue > fitnessValues.get(parent1Index)){
            replaceIndividual(parent1Index,offspring);
        }
        else{
            int worstIndividualIndex = Utils.argMin(fitnessValues);
            replaceIndividual(worstIndividualIndex,offspring);
        }
        if(verbose){System.out.println("Done");}

    }
    public void replaceIndividual(int index,Chromosome offspring){
        fitnessSum -= fitnessValues.get(index);
        population.set(index,offspring);

        Double value= fitness.apply(offspring);
        fitnessValues.set(index,value);
        fitnessSum += value;
    }


    public Pair<Integer,Integer> worstAndBest(){
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for(Chromosome chromosome:population){
            int length = chromosome.length();
            if(length>max){
                max=length;
            }
            if(length<min){
                min=length;
            }
        }
        return new Pair<>(max,min);
    }

    @Override
    public String toString() {
        return "Population{" +
                "population=" + population +
                '}';
    }


    public static void main(String[] args) {
        Function<Chromosome,Double> fitness;
        IEditor editor;
        Fixer fixer;
        fitness = HybridGeneticAlgorithm::evaluate;
        editor = new RedundancyEditor();
        fixer = new Fixer(editor);
        Population population = Population.initialize(5, 4,4,5,fitness,editor,fixer,false);
        for(Chromosome chromosome:population.getPopulation()){
            System.out.println(chromosome);
        }

        Chromosome chromosome =new Chromosome(5,4);
        chromosome.addParallelLayer(new ArrayList<>());
        chromosome.addInParallelLayer(0, new Gene(0,0));
        population.replace(population.getParents(),chromosome,false);

        for(Chromosome _chromosome:population.getPopulation()){
            System.out.println(_chromosome);
        }
    }
}
