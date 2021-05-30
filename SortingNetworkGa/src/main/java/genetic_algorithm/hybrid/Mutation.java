package genetic_algorithm.hybrid;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mutation {
    Random random;
    private double _randomFactor;

    public Mutation(double _randomFactor) {
        this._randomFactor = _randomFactor;
        random=new Random();
    }

    public void  mutate(Chromosome offspring,boolean verbose) {
        if(verbose){System.out.print("Mutating...");}
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

                if (Math.random() <= _randomFactor) {
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
        if(verbose){System.out.println("Done");}

    }

    private int getNewWire(Gene gene,int wires){
        int newWire = gene.getWire0();
        while(gene.contains(newWire)){
            newWire = random.nextInt(wires);
        }
        return newWire;
    }


    public static void main(String[] args) {
        Chromosome chromosome=new Chromosome(4,10);
        List<Gene> firstLayer = new ArrayList<Gene>(){{add(new Gene(0,1));}};
        List<Gene> secondLayer = new ArrayList<Gene>(){{add(new Gene(0,2));}};
        chromosome.addParallelLayer(firstLayer);
        chromosome.addParallelLayer(secondLayer);

        System.out.println(chromosome);
        Mutation mutation=new Mutation(1);
        mutation.mutate(chromosome,false);
        System.out.println(chromosome);

    }
}
