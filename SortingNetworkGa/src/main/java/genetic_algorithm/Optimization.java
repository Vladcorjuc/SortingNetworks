package genetic_algorithm;

import genetic_algorithm.network.Layer;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Optimization {
    private final Editor editor;
    private final Fixer fixer;
    public Optimization(Editor editor, Fixer fixer){
        this.editor = editor;
        this.fixer = fixer;
    }
    public Chromosome localOptimization(Chromosome offspring) {
        /*
        int n = offspring.getWires();
        int l = offspring.getParallelLayers().size();
        for(int i=0;i<l;i++){
            Set<Integer> Q = new HashSet<>();
            assert offspring != null;
            List<Gene> currentLayer = offspring.getParallelLayer(i);

            ArrayList<Gene>[][] computed = new GeneList[l][n];
            computed[i][0] = new GeneList(currentLayer);


            for(int j=1;j<n/2;j++){
                List<Gene> maxLayer = new ArrayList<>();
                Pair<Integer,Integer> maxAB = null;
                int maxValue = Integer.MIN_VALUE;
                for(int a = 0; a<n;a++) {
                    for (int b = 0; b < n; b++) {
                        if (Q.contains(a)) {
                            continue;
                        }
                        if (Q.contains(b)) {
                            continue;
                        }

                        List<Gene> exchangedLayer = exchange(computed[i][j - 1], a, b);
                        Chromosome exchangedIndividual = new Chromosome(offspring, exchangedLayer, currentLayer);

                        int individualModulo = modulo(exchangedIndividual);
                        if (individualModulo>maxValue){
                            maxLayer = exchangedLayer;
                            maxValue = individualModulo;
                            maxAB=new Pair<>(a,b);
                        }

                    }
                }
                assert maxAB != null;
                Q.add(maxAB.getKey());
                Q.add(maxAB.getValue());

                computed[i][j] = new GeneList(maxLayer);
            }

            Chromosome maxIndividual = null;
            int maxModulo = Integer.MIN_VALUE;
            for(int k=0;k<n/2;k++){
                Chromosome exchangedIndividual = new Chromosome(offspring, computed[i][k], currentLayer);

                int individualModulo = modulo(exchangedIndividual);
                if(individualModulo>maxModulo){
                    maxIndividual = exchangedIndividual;
                    maxModulo=individualModulo;
                }
            }

            offspring = maxIndividual;
        }

        assert offspring != null;
        editor.edit(offspring);
        return fixer.repair(offspring);
         */


        for(List<Gene> layer : offspring.getParallelLayers()){
            if(layer.size()< offspring.getWires()/2){
                layer.add(Gene.getIndependent(offspring.getWires(),layer));
            }
        }

        editor.edit(offspring,false);
        return fixer.repair(offspring,false);
    }

    private int modulo(Chromosome exchangedIndividual) {
        Chromosome editedChromosome = new Chromosome(exchangedIndividual);
        editor.edit(editedChromosome,false);
        return editedChromosome.length() + exchangedIndividual.getNetwork().getBad().size();
    }

    public List<Gene> exchange(List<Gene> genes, int a, int b) {
        List<Gene> copy = new ArrayList<>();
        for(Gene gene:genes){
            Gene newGene = new Gene(gene);
            if(gene.getWire0()==a){
                newGene.setWire0(b);
            }
            else if(gene.getWire0()==b){
                newGene.setWire0(a);
            }
            if(gene.getWire1()==a){
                newGene.setWire1(b);
            }
            else if(gene.getWire1()==b){
                newGene.setWire1(a);
            }
            copy.add(newGene);
        }
        return copy;
    }

    static class GeneList extends ArrayList<Gene>{
        public GeneList(List<Gene> currentLayer) {
            for(Gene gene:currentLayer){
                this.add(new Gene(gene));
            }
        }
    }
    public static void main(String[] args) {
        List<Gene> genes=new ArrayList<>();
        genes.add(new Gene(1,2));
        genes.add(new Gene(3,4));

        Optimization o = new Optimization(new Editor(),new Fixer(new Editor()));
        System.out.println(o.exchange(genes,2,3));
        System.out.println(genes);

    }


}
