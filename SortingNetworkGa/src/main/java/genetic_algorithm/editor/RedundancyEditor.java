package genetic_algorithm.editor;

import genetic_algorithm.hybrid.Chromosome;
import genetic_algorithm.hybrid.Gene;
import genetic_algorithm.network.Network;
import genetic_algorithm.network.Utils;

import java.util.ArrayList;
import java.util.List;

public class RedundancyEditor implements IEditor {
    @Override
    public void edit(Chromosome offspring, boolean verbose) {
        if(verbose){System.out.print("Editing...");}
        List<List<Gene>> layersToBeDeleted = new ArrayList<>();
        Network offspringNet = offspring.getNetwork();
        for(List<Gene> layer:offspring.getParallelLayers()){
            if(Math.random()<0.4) {
                layer.removeAll(Utils.ConvertComparatorSetToGeneList(offspringNet.getRedundantComparators()));
            }
            if(layer.size()==0){
                layersToBeDeleted.add(layer);
            }
        }
        offspring.getParallelLayers().removeAll(layersToBeDeleted);
        offspring.addParallelLayers(layersToBeDeleted);
        if(verbose){System.out.println("Done");}
    }
}
