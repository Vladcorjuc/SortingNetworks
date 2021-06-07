package genetic_algorithm.composite_objective;

import genetic_algorithm.hybrid.Gene;

import java.util.Random;

public class IndirectReplacementMutation {
    private Random random;
    private double _randomFactor;

    public IndirectReplacementMutation(double _randomFactor) {
        this._randomFactor = _randomFactor;
        random = new Random();
    }

    public void mutate(Chromosome chromosome){
        if(random.nextDouble()<_randomFactor) {
            int randomPosition = random.nextInt(chromosome.getComparators().size());
            chromosome.getComparators().set(randomPosition, Gene.getRandom(chromosome.getWires()));
        }
    }
}
