package genetic_algorithm.hybrid.mutation;

import genetic_algorithm.hybrid.Chromosome;

public interface IMutation {

    public void  mutate(Chromosome offspring, boolean verbose);
}
