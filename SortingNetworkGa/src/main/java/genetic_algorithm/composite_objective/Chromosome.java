package genetic_algorithm.composite_objective;

import genetic_algorithm.hybrid.Gene;
import genetic_algorithm.network.Comparator;
import genetic_algorithm.network.Network;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Chromosome {
    private List<Gene> comparators;
    private int wires;
    private int targetDepth;
    private int rank=-1;
    private List<Chromosome> dominates;
    private int dominatedBy=-1;
    private int distance;
    private Double objective1=-1.0;
    private Double objective2=-1.0;
    private Double objective3=-1.0;

    public Chromosome(int wires ,int targetDepth) {
        this.wires = wires;
        this.targetDepth = targetDepth;
        this.comparators=new ArrayList<>();

    }

    public Chromosome(Chromosome chromosome) {
        this.wires = chromosome.wires;
        this.targetDepth = chromosome.targetDepth;
        this.comparators=chromosome.comparators;
    }

    public Chromosome(int n, int d, List<Comparator> comparators) {
        this.wires = n;
        this.targetDepth = d;
        this.comparators=  new ArrayList<>();
        for(Comparator comparator:comparators){
            Gene gene = new Gene(comparator.getStartingWire(),comparator.getEndingWire());
            this.comparators.add(gene);
        }
        int fullSize = (targetDepth)*n/2 - comparators.size();
        for(int i=0;i<fullSize;i++){
            Gene gene  = Gene.getRandom(wires);
            this.comparators.add(gene);
        }
    }


    public int getWires() {
        return wires;
    }
    public int getTargetDepth() {
        return targetDepth;
    }
    public List<Gene> getComparators() {
        return comparators;
    }
    public void setComparators(List<Gene> comparators) {
        this.comparators = comparators;
    }
    public void addComparators(List<Gene> comparators){this.comparators.addAll(comparators);}
    public void addComparator(Gene comparator){this.comparators.add(comparator);}

    public Network getNetwork() {
        Network net = new Network(this.wires);
        for(Gene gene:comparators){
            net.addComparator(new Comparator(gene.getWire0(),gene.getWire1()));
        }
        return net;
    }

    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }

    public List<Chromosome> getDominates() {
        return dominates;
    }
    public void setDominates(List<Chromosome> dominates) {
        this.dominates = dominates;
    }

    public void setDominatedBy(int dominatedBy) {
        this.dominatedBy = dominatedBy;
    }

    public int getDominatedBy() {
        return dominatedBy;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public Double getObjective(int m) {
        switch (m) {
            case 0:
                return objective1;
            case 1:
                return objective2;
            default:
                return objective3;
        }
    }
    public void computeObjective(){
        this.objective1 = CompositeGeneticAlgorithm.composite1(this);
        this.objective2 = CompositeGeneticAlgorithm.composite2(this);
        this.objective3 = CompositeGeneticAlgorithm.composite3(this);
    }

    @Override
    public String toString() {
        return "Chromosome{" +
                "comparators=" + comparators +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chromosome)) return false;
        Chromosome that = (Chromosome) o;
        return getWires() == that.getWires() &&
                getComparators().equals(that.getComparators());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getComparators(), getWires());
    }
}
