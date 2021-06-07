package genetic_algorithm.hybrid.mutation;

import genetic_algorithm.hybrid.Chromosome;
import genetic_algorithm.hybrid.Gene;
import genetic_algorithm.Statistics;

import java.util.List;
import java.util.Random;

public class IndirectReplacementMutation implements IMutation {
    private Random random;
    private double _randomFactor;

    public IndirectReplacementMutation(double _randomFactor) {
        this._randomFactor = _randomFactor;
        random = new Random();
    }

    public void mutate(Chromosome chromosome,boolean verbose){
        if(verbose){System.out.print("Mutating...");}
        for(List<Gene> layer:chromosome.getParallelLayers()){
            for(Gene gene:layer) {
                if (random.nextDouble() <= _randomFactor) {
                    Statistics.mutationOccurred();
                    Gene randomGene = Gene.getRandom(chromosome.getWires());
                    gene.setWire0(randomGene.getWire0());
                    gene.setWire1(randomGene.getWire1());
                }
            }
        }
        if(verbose){System.out.println("Done");}
    }
}
