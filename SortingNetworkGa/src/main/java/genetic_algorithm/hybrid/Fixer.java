package genetic_algorithm.hybrid;

import genetic_algorithm.Statistics;
import genetic_algorithm.hybrid.editor.IEditor;
import genetic_algorithm.hybrid.editor.RedundancyEditor;
import genetic_algorithm.network.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Fixer {
    private final IEditor editor;
    private final Random random;

    public Fixer(IEditor editor){
        this.editor=editor;
        random=new Random();
    }

    private Pair<Chromosome, Network> append(Chromosome offspring, Network network){
        Comparator selectedComparator=null;
        Network newNetwork=null;
        int min = Integer.MAX_VALUE;

        for(int i=0;i<offspring.getWires();i++){
            for(int j=0;j<offspring.getWires();j++){
                if(i==j){continue;}
                if(network.isRedundant(i,j)){continue;}

                newNetwork = new Network(network,i,j);
                int bads = newNetwork.getBad().size();
                if(bads < min){
                    min = bads;
                    selectedComparator = new Comparator(i,j);
                }
            }
        }

        //System.out.println(offspring);
        offspring.append(new Gene(selectedComparator.getStartingWire(),selectedComparator.getEndingWire()));
        Statistics.ComparatorAdded();
        return new Pair<>(offspring,newNetwork);
    }
    private Chromosome insert(Chromosome offspring, Network network,boolean verbose){

        int layers= offspring.getParallelLayers().size();
        int networkComparators = network.getComparators().size();
        int networkBads = network.getBad().size();
        int minBads = Integer.MAX_VALUE;
        List<List<Pair<Integer,Gene>>> priorities = new ArrayList<>();
        for(int i=0;i<4;i++){
            priorities.add(new ArrayList<>());
        }

        for(int layer=0;layer<layers;layer++){

            List<Integer> possibleWires = Chromosome.possibleWires(offspring.getWires(),offspring.getParallelLayer(layer));
            for(int i:possibleWires){
                for(int j:possibleWires){
                    if(i==j){continue;}
                    Chromosome newChromosome = new Chromosome(offspring);
                    newChromosome.addInParallelLayer(layer,new Gene(i,j));

                    editor.edit(newChromosome,verbose);
                    Network newNetwork = newChromosome.getNetwork();
                    int bads = newNetwork.getBad().size();
                    int comparators = newNetwork.getComparators().size();
                    if(bads<networkBads){
                        priorities.get(0).add(new Pair<>(layer,new Gene(i,j)));
                    }
                    else if(bads==minBads){
                        priorities.get(1).add(new Pair<>(layer,new Gene(i,j)));
                    }
                    /*
                    if(comparators<networkComparators && bads<networkBads){
                        priorities.get(0).add(new Pair<>(layer,new Gene(i,j)));
                    }
                    else if(comparators<networkComparators && bads == networkBads){
                        priorities.get(1).add(new Pair<>(layer,new Gene(i,j)));
                    }
                    else if(comparators == networkComparators && bads<networkBads){
                        priorities.get(2).add(new Pair<>(layer,new Gene(i,j)));
                    }
                    else if(bads<minBads){
                        minBads = bads;
                        priorities.get(3).clear();
                        priorities.get(3).add(new Pair<>(layer,new Gene(i,j)));
                    }
                    else if(bads==minBads){
                        priorities.get(3).add(new Pair<>(layer,new Gene(i,j)));
                    }
                     */
                }
            }
        }
        int layer = -1;
        Gene gene = null;
        for (List<Pair<Integer,Gene>> priority:priorities) {
            for(Pair<Integer,Gene> toAdd:priority){
                layer = toAdd.getKey();
                gene = toAdd.getValue();
                if(offspring.canAddInParallelLayer(layer,gene)){
                    Statistics.ComparatorAdded();
                    offspring.addInParallelLayer(layer,gene);
                }

            }
        }
        //editor.edit(offspring);
        /*
        for(int i=0;i<offspring.getWires();i++){
            for(int j=0;j<offspring.getWires();j++){
                if(i==j){continue;}
                for(int layer=0;layer<offspring.getParallelLayers().size();layer++){
                    boolean contained=false;
                    for(Gene gene:offspring.getParallelLayer(layer)){
                        if(gene.contains(i)||gene.contains(j)){
                            contained=true;
                        }
                    }
                    if(!contained){
                        Chromosome chromosome=new Chromosome(offspring);
                        chromosome.addInParallelLayer(layer,new Gene(i,j));
                        if(chromosome.getNetwork().getBad().size()< network.getBad().size()){
                            offspring.addInParallelLayer(layer,new Gene(i,j));
                        }
                        break;
                    }

                }
            }
        }

         */
        /*for(Sequence sequence:network.getBad()){
            List<Pair<Integer,Integer>> badPos = sequence.getBadPositions();
            for(Pair<Integer,Integer> bad: badPos){
                for(int layer=0;layer<offspring.getParallelLayers().size();layer++){
                    Gene gene= new Gene(bad.getKey(),bad.getValue());
                    if(offspring.canAddInParallelLayer(layer,gene)){
                        offspring.addInParallelLayer(layer,gene);
                        break;
                    }
                }
            }
        }

         */
        editor.edit(offspring,false);
        return offspring;

    }



    public Chromosome repair(Chromosome offspring,boolean verbose) {
        if(verbose){System.out.print("Repairing...");System.out.flush();}
        Network network = offspring.getNetwork();
        Pair<Chromosome,Network> newIndividual = append(offspring,network);
        if(verbose){System.out.println("Done");System.out.flush();}
        return insert(newIndividual.getKey(),newIndividual.getValue(),verbose);

    }

    public static void main(String[] args) {
        Chromosome chromosome=new Chromosome(4,4);
        List<Gene> firstLayer = new ArrayList<Gene>(){{add(new Gene(0,1));}};
        List<Gene> secondLayer = new ArrayList<Gene>(){{add(new Gene(0,2));}};
        chromosome.addParallelLayer(firstLayer);
        chromosome.addParallelLayer(secondLayer);

        System.out.println(chromosome);
        Fixer fixer=new Fixer(new RedundancyEditor());
        fixer.repair(chromosome,false);
        System.out.println(chromosome);

    }
}
