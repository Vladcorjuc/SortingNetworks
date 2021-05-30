package genetic_algorithm.simple;


import java.util.Random;

public class Crossover {
    private Random random;
    private double crossFactor;
    private double crossProportion;

    public Crossover(double crossFactor, double crossProportion) {
        this.crossFactor = crossFactor;
        this.crossProportion = crossProportion;
        random= new Random();
    }

    public Chromosome cross(Chromosome parent1, Chromosome parent2){
        Chromosome offspring = new Chromosome(parent1.getWires(),parent1.getTargetDepth());
        if(random.nextDouble()<crossProportion){
            int min = Math.min(parent1.getComparators().size(),parent2.getComparators().size());
            for(int i=0;i<min;i++){
                if(random.nextDouble()<crossFactor){
                    offspring.addComparator(parent1.getComparators().get(i));
                }
                else {

                    offspring.addComparator(parent2.getComparators().get(i));
                }
            }
        }
        else {
            int point = random.nextInt(Math.min(parent1.getComparators().size(),parent2.getComparators().size()));
            for(int i=0;i<point;i++){
                offspring.addComparator(parent1.getComparators().get(i));
            }
            int parent2Size =parent2.getComparators().size();
            for(int i=point;i<parent2Size;i++){
                offspring.addComparator(parent2.getComparators().get(i));
            }

        }
        return offspring;
    }
}
