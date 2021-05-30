package genetic_algorithm.simple;

import genetic_algorithm.hybrid.Gene;
import genetic_algorithm.network.Comparator;
import genetic_algorithm.network.Network;

import java.util.ArrayList;
import java.util.List;

public class Chromosome {
    private List<Gene> comparators;
    private int wires;
    private int targetDepth;

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
            Gene gene = new Gene(comparator.getStartingWire(),comparator.getStartingWire());
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


}
