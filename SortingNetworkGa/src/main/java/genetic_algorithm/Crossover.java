package genetic_algorithm;

interface Crossover {
    Chromosome cross(Chromosome parent0, Chromosome parent1,boolean verbose);
}
