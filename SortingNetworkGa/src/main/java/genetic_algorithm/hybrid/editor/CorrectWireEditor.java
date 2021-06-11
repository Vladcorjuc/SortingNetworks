package genetic_algorithm.hybrid.editor;

import genetic_algorithm.hybrid.Chromosome;
import genetic_algorithm.hybrid.Gene;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CorrectWireEditor implements IEditor {
    @Override
    public void edit(Chromosome offspring, boolean verbose) {
        if(verbose){System.out.print("Editing...");System.out.flush();}
        for(List<Gene> layer:offspring.getParallelLayers()){
            Set<Integer> usedWires = new HashSet<>();
            List<Gene> toDelete = new ArrayList<>();
            for(Gene gene:layer){
                if(usedWires.contains(gene.getWire1())||usedWires.contains(gene.getWire0())){
                    toDelete.add(gene);
                    continue;
                }
                usedWires.add(gene.getWire0());
                usedWires.add(gene.getWire1());
            }
            layer.removeAll(toDelete);
        }
        if(verbose){System.out.println("Done");System.out.flush();}
    }
}
