package genetic_algorithm.composite_objective;

import genetic_algorithm.network.Comparator;
import genetic_algorithm.network.Network;

import java.util.*;

public class Population {
    private List<Chromosome> chromosomes;

    public Population(){
        chromosomes=new ArrayList<>();
    }

    public Population(List<Chromosome> chromosomes, List<Chromosome> chromosomes1) {
        this.chromosomes = new ArrayList<>(chromosomes);
        this.chromosomes.addAll(chromosomes1);
        computeObjective();
    }

    public Population(List<Chromosome> chromosomes) {
        this.chromosomes = new ArrayList<>(chromosomes);
        computeObjective();
    }

    public static Population initialize(int n,int d, int populationSize){
        Population population = new Population();
        List<Comparator> filter = Network.createGreenFilter(n).getComparators();
        for(int i=0;i<populationSize;i++){
            Chromosome chromosome = new Chromosome(n,d,filter);
            population.addChromosome(chromosome);
        }
        population.computeObjective();
        return population;
    }

    public static Population makeNewPopulation(Population parents,Crossover crossover,
                                               IndirectReplacementMutation mutation,int N) {
        Population children = new Population();
        for(int i=0;i<N/10;i++){
            children.addChromosome(parents.getChromosomes().get(i));
        }
        Random random = new Random();

        double multiplier = random.nextDouble()+0.1;
        SelectionPool pool = new SelectionPool(parents.getChromosomes().size()/2,
                multiplier ,parents.getChromosomes());
        pool.sort();

        while(children.getChromosomes().size()<N){

            Chromosome k1= pool.get();
            Chromosome k2= pool.get();

            if(smaller(k1,k2)){
                k1 = k2;
            }

            Chromosome k3= pool.get();
            Chromosome k4= pool.get();

            if(smaller(k3,k4)){
                k3=k4;
            }

            Chromosome child = crossover.cross(k1,k3);
            mutation.mutate(child);
            children.addChromosome(child);
        }
        return children;

    }

    private static boolean smaller(Chromosome o1 ,Chromosome o2) {
        return o1.getRank() < o2.getRank() || (o1.getRank() == o2.getRank() || o1.getDistance() < o2.getDistance());
    }

    private void computeObjective() {
        for(Chromosome chromosome:chromosomes){
                chromosome.computeObjective();
        }
    }

    public void addChromosomes(List<Chromosome> front) {
        for(Chromosome chromosome:front){
            addChromosome(chromosome);
        }
    }
    public List<Chromosome> getChromosomes() {
        return chromosomes;
    }
    public void addChromosome(Chromosome chromosome){
        chromosome.computeObjective();
        chromosomes.add(chromosome);
    }
    public void replaceIndividual(int index, Chromosome offspring){
        chromosomes.set(index,offspring);
    }


    public List<List<Chromosome>> nonDominatedSort(){
        List<List<Chromosome>> F = new ArrayList<>();
        List<Chromosome> F1 = new ArrayList<>();
        int i=0;
        for(Chromosome p: chromosomes){
            i++;
            List<Chromosome> Sp = new ArrayList<>();
            int np = 0;

            for(Chromosome q:chromosomes){
                if(dominates(p,q)){
                    Sp.add(q);
                }
                else if(dominates(q,p)){
                    np++; }
            }

            if(np==0){
                p.setRank(1);
                F1.add(p);
            }
            p.setDominates(Sp);
            p.setDominatedBy(np);
        }
        F.add(F1);
        i=0;
        while (F.get(i).size()>0){
            List<Chromosome> Q = new ArrayList<>();

            for (Chromosome p:F.get(i)) {
                for(Chromosome q:p.getDominates()){
                    q.setDominatedBy(q.getDominatedBy()-1);
                    if(q.getDominatedBy()==0){
                        q.setRank(q.getRank()+1);
                        Q.add(q);
                    }
                }
            }
            i++;
            F.add(Q);
        }
        return F;

    }
    private boolean dominates(Chromosome p, Chromosome q) {
        if(CompositeGeneticAlgorithm.composite1(p)<= CompositeGeneticAlgorithm.composite1(q) && CompositeGeneticAlgorithm.composite2(p)<= CompositeGeneticAlgorithm.composite2(q)
        && CompositeGeneticAlgorithm.composite3(p)<= CompositeGeneticAlgorithm.composite3(q) &&
                (CompositeGeneticAlgorithm.composite1(p)< CompositeGeneticAlgorithm.composite1(q)||
                CompositeGeneticAlgorithm.composite2(p)< CompositeGeneticAlgorithm.composite2(q)||
                CompositeGeneticAlgorithm.composite3(p)< CompositeGeneticAlgorithm.composite3(q))){
            return true;
        }
        return false;

    }


}
