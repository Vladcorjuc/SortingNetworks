package genetic_algorithm.hybrid.mutation;

import genetic_algorithm.hybrid.Chromosome;
import genetic_algorithm.hybrid.Gene;
import genetic_algorithm.Statistics;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleMutation implements IMutation {
    private double _randomFactor;
    private Random random;
    public SimpleMutation(double _randomFactor) {
       this._randomFactor = _randomFactor;
        random=new Random();
    }
    @Override
    public void  mutate(Chromosome offspring, boolean verbose) {
        if(verbose){System.out.print("Mutating...");System.out.flush();}
        for(int layer=0;layer<offspring.getParallelLayers().size();layer++){
            List<Pair<Integer,Integer>> toCorrect = new ArrayList<>();
            for(Gene gene:offspring.getParallelLayer(layer)) {
                if(toCorrect.size()>0){
                    int correctionIndex=-1;
                    for(int i=0;i<toCorrect.size();i++){
                        Pair<Integer,Integer> correction = toCorrect.get(i);
                        if(gene.contains(correction.getKey())){
                            gene.replaceUsedWire(correction.getKey(),correction.getValue());
                            correctionIndex = i;
                        }
                    }
                    if(correctionIndex!=-1){
                        toCorrect.remove(correctionIndex);
                    }
                }

                if (random.nextDouble() <= _randomFactor) {
                    Statistics.mutationOccurred();
                    int newWire = getNewWire(gene, offspring.getWires());
                    int oldWire =-1;
                    if (random.nextDouble() <= 0.5) {
                        oldWire = gene.getWire0();
                        gene.setWire0(newWire);
                    } else {
                        oldWire=gene.getWire1();
                        gene.setWire1(newWire);
                    }
                    toCorrect.add(new Pair<>(newWire,oldWire));
                }
            }
        }
        if(verbose){System.out.println("Done");System.out.flush();}

    }
    private int getNewWire(Gene gene, int wires){
        int newWire = gene.getWire0();
        while(gene.contains(newWire)){
            newWire = random.nextInt(wires);
        }
        return newWire;
    }
}
