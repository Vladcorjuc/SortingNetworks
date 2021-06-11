package genetic_algorithm.hybrid.crossover;

import genetic_algorithm.hybrid.Chromosome;
import genetic_algorithm.hybrid.Gene;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultiPointCrossover implements ICrossover {
    @Override
    public Chromosome cross(Chromosome parent0, Chromosome parent1, boolean verbose) {

       if(verbose){ System.out.print("Crossing...");System.out.flush();}

        Chromosome chromosome=new Chromosome(parent0.getWires(),parent0.getTargetDepth());

        if(parent0.getParallelLayers().size() < parent1.getParallelLayers().size()) {
            Chromosome aux = parent0;
            parent0 = parent1;
            parent1 = aux;
        }

        for(int layer=0;layer<parent0.depth();layer++){

            List<Gene> genes = new ArrayList<>();
            List<Gene> parent0Layer = parent0.getParallelLayer(layer);
            if(parent1.depth()<=layer){
                for(Gene gene:parent0Layer){
                    genes.add(new Gene(gene));
                }
                chromosome.addParallelLayer(genes);
                continue;
            }
            List<Gene> parent1Layer = parent1.getParallelLayer(layer);


            int maxGenes = Math.max(parent0Layer.size(),parent1Layer.size());
            for(int gene=0;gene<maxGenes;gene++){
                Gene newGene = null;
                if(parent0Layer.size()>gene){
                    newGene = new Gene(parent0Layer.get(gene));
                }
                else {
                    newGene = new Gene(parent1Layer.get(gene));
                }
                if(parent1Layer.size()>gene){
                    if(Math.random()<0.20){
                        newGene = new Gene(parent1Layer.get(gene));
                    }
                }
                genes.add(newGene);

            }

            genes=clean(genes);
            chromosome.addParallelLayer(genes);
        }

        if(verbose){System.out.println("Done");System.out.flush();}
        return chromosome;

    }
    public List<Gene> clean(List<Gene> genes){
        Set<Integer> occupiedBuses = new HashSet<>();
        List<Gene> toDelete = new ArrayList<>();
        for(Gene gene:genes){
            if(occupiedBuses.contains(gene.getWire1()) || occupiedBuses.contains(gene.getWire0())){
                toDelete.add(gene);
                continue;
            }
            occupiedBuses.add(gene.getWire0());
            occupiedBuses.add(gene.getWire1());
        }
        genes.removeAll(toDelete);
        return genes;
    }

    public static void main(String[] args) {
        Chromosome chromosome1 = new Chromosome(10,10);
        chromosome1.addParallelLayer(new ArrayList<>());
        chromosome1.addInParallelLayer(0,new Gene(1,2));
        chromosome1.addInParallelLayer(0,new Gene(3,4));

        Chromosome chromosome2 = new Chromosome(chromosome1);
        chromosome2.addInParallelLayer(0,new Gene(5,6));
        chromosome2.addInParallelLayer(0,new Gene(7,8));

        System.out.println(chromosome1+"\n"+chromosome2);

        ICrossover crossover=new MultiPointCrossover();
        System.out.println(crossover.cross(chromosome1,chromosome2,false));
        System.out.println(chromosome1);
        System.out.println(chromosome2);
    }
}
