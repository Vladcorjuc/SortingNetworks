package genetic_algorithm.composite_objective;

import genetic_algorithm.network.Network;
import javafx.util.Pair;

import java.util.Comparator;
import java.util.List;

public class CompositeGeneticAlgorithm {
    private int runs;
    private int generations;
    private int popSize;
    private Crossover crossover;
    private IndirectReplacementMutation mutation;

    public CompositeGeneticAlgorithm(int runs, int generations, int populationSize,
                                     double crossoverRate, double crossoverProportion,
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
            Population parents = Population.initialize(n,d,popSize);
            Population offspring = new Population();
            while (generation<generations) {
                System.out.println(generation);
                Population currentPopulation = new Population(offspring.getChromosomes());
                System.out.println(currentPopulation.getChromosomes());
                System.out.println("non-dominated sort");
                List<List<Chromosome>> fronts = currentPopulation.nonDominatedSort();

                System.out.println("add possible fronts");
                int i=0;
                while(parents.getChromosomes().size() + fronts.get(i).size() < popSize){
                    List<Chromosome> front = crowdingDistance(fronts.get(i));
                    parents.addChromosomes(front);
                    i++;
                }
                System.out.println("sort fronts");
                fronts.get(i).sort((o1, o2) -> {
                    if (o1.equals(o2)) {
                        return 0;
                    }
                    if (o1.getRank() < o2.getRank() || (o1.getRank() == o2.getRank() || o1.getDistance() < o2.getDistance())) {
                        return -1;
                    }
                    return 1;
                });
                int j=0;
                while (parents.getChromosomes().size() != popSize){
                    parents.addChromosome(fronts.get(i).get(j));
                    j++;
                }
                System.out.println("make new population");
                offspring = Population.makeNewPopulation(parents,crossover,mutation,popSize);
                generation++;
            }
            for(Chromosome chromosome:offspring.getChromosomes()) {
                Network network = chromosome.getNetwork();
                if (network.isSorting()) {
                    System.out.println("FOUND ->>>" + network);
                }
            }
        }
    }

    public List<Chromosome> crowdingDistance(List<Chromosome> I){
        int l = I.size();
        for (Chromosome chromosome : I) {
            chromosome.setDistance(0);
        }
        for(int m=0;m<3;m++){
            int finalM = m;
            I.sort(Comparator.comparing(o -> o.getObjective(finalM)));
            I.get(0).setDistance(Integer.MAX_VALUE);
            I.get(l-1).setDistance(Integer.MAX_VALUE);
            Pair<Double,Double> maxMin=findMaxMin(I,m);
            for(int i=1;i<l-1;i++){
                I.get(i).setDistance((int) (I.get(i).getDistance() +
                        (I.get(i+1).getObjective(m) - I.get(i-1).getObjective(m))/
                                maxMin.getKey() - maxMin.getValue()));
            }
        }
        return I;
    }
    private Pair<Double, Double> findMaxMin(List<Chromosome> I,int m) {
        Pair<Double,Double> maxMin = new Pair<>(Double.MIN_VALUE,Double.MAX_VALUE);
        for(Chromosome chromosome:I){
            double score = chromosome.getObjective(m);
            if(score>maxMin.getKey()){
                maxMin = new Pair<>(score,maxMin.getValue());
            }
            if(score<maxMin.getValue()){
                maxMin = new Pair<>(maxMin.getKey(),score);
            }
        }
        return maxMin;
    }
    public static Double composite1(Chromosome individual){
        Network network = individual.getNetwork();
        int m = network.getBad().size();
        int l = network.getLayers().size();
        int c = network.getComparators().size();
        return 10000.0 *  m + 100*l + c ;
    }
    public static Double composite2(Chromosome individual){
        Network network = individual.getNetwork();
        int m = network.getBad().size();
        int l = network.getLayers().size();
        return m + 10.0 * l;
    }

    public static Double composite3(Chromosome individual){
        Network network = individual.getNetwork();
        int m = network.getBad().size();
        int c = network.getComparators().size();
        return m + 2.0 * c;
    }
    public static int noveltyScore(Chromosome individual, List<Chromosome> population){
        int sum = 0;
        int xiSwaps = individual.getNetwork().getSwaps();
        for(Chromosome chromosome:population){
            int xjSwaps = chromosome.getNetwork().getSwaps();
            sum+= (xiSwaps - xjSwaps);
        }
        return sum;
    }
    public static Chromosome minimumNovelty(Chromosome individual,List<Chromosome> population){
        Pair<Chromosome,Integer> min = new Pair<>(null, Integer.MAX_VALUE);
        int xiSwaps = individual.getNetwork().getSwaps();
        for(Chromosome chromosome:population){
            int xjSwaps = chromosome.getNetwork().getSwaps();
            if(chromosome.equals(individual)){continue;}
            if(xiSwaps - xjSwaps < min.getValue()){
                min = new Pair<>(chromosome,xiSwaps-xjSwaps);
            }
        }
        return min.getKey();
    }


    public static void main(String[] args) {
        CompositeGeneticAlgorithm compositeGeneticAlgorithm = new CompositeGeneticAlgorithm(1,1000,1000,
                0.10,0.30,0.4);
        compositeGeneticAlgorithm.getSortedNetwork(4,3);
    }

}
