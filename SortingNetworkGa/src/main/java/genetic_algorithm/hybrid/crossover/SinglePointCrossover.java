package genetic_algorithm.hybrid.crossover;

import genetic_algorithm.hybrid.Chromosome;
import genetic_algorithm.hybrid.Gene;

import java.util.ArrayList;
import java.util.List;

public class SinglePointCrossover implements ICrossover {

    @Override
    public Chromosome cross(Chromosome parent0, Chromosome parent1, boolean verbose) {
        if(verbose){System.out.print("Crossing parents...");System.out.flush();}
        Chromosome chromosome=new Chromosome(parent0.getWires(),parent0.getTargetDepth());
        if(parent0.getTargetDepth() < parent1.getTargetDepth()) {
            Chromosome aux = parent0;
            parent0 = parent1;
            parent1 = aux;
        }

        for(int i=0;i<parent0.getTargetDepth();i++){
            List<Gene> layer = new ArrayList<>();

            List<Gene> parent0Layer = parent0.getParallelLayer(i);
            if(parent1.getParallelLayers().size()<=i){

                List<Gene> genes = new ArrayList<>();
                for(Gene gene:parent0Layer){
                    genes.add(new Gene(gene));
                }
                chromosome.addParallelLayer(genes);
                continue;
            }
            List<Gene> parent1Layer = parent1.getParallelLayer(i);


            for(int j=0;j<parent0Layer.size()/2;j++){
                layer.add(new Gene(parent0Layer.get(j)));
            }
            for (int j=parent1Layer.size()/2;j<parent1Layer.size();j++){
                Gene gene=parent1Layer.get(j);
                if(gene.isIndependent(layer)){
                    layer.add(new Gene(gene));
                }
            }

            chromosome.addParallelLayer(layer);
        }
        if(verbose){System.out.println("Done");System.out.flush();}
        return chromosome;
    }
}
