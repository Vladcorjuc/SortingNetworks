package genetic_algorithm.crossover;

import genetic_algorithm.hybrid.Chromosome;

public interface ICrossover {
    Chromosome cross(Chromosome parent0, Chromosome parent1, boolean verbose);
}
