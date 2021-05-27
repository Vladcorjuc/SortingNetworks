package genetic_algorithm;

import genetic_algorithm.network.Layer;
import genetic_algorithm.network.Network;
import genetic_algorithm.network.Comparator;

import java.util.*;

public class Chromosome {

    private List<List<Gene>> parallelLayers;
    private List<Gene> supplementalLayer;
    private int wires;

    //region Constructors
    public Chromosome(int n){
        this.wires=n;
        parallelLayers=new ArrayList<>();
        supplementalLayer=new ArrayList<>();
    }
    public Chromosome(Chromosome chromosome) {
        parallelLayers = new ArrayList<>();
        for( List<Gene> layer : chromosome.parallelLayers) {
            List<Gene> layerList = new ArrayList<>();
            for(Gene gene:layer){
                layerList.add(new Gene(gene));
            }
            parallelLayers.add(layerList);
        }
        supplementalLayer = new ArrayList<>();
        for(Gene gene:chromosome.getSupplementalLayer()){
            supplementalLayer.add(new Gene(gene));
        }
        wires=chromosome.wires;
    }
    public Chromosome(int k,int n){
        this.wires=n;
        parallelLayers = new ArrayList<>(k);
        for(int i=0;i<k;i++){
            int genesNumber=new Random().nextInt(n/2 - n/4 + 1) + n/4;
            List<Gene> geneList = new ArrayList<>();
            for(int j=0;j<genesNumber;j++){
                Gene gene = Gene.getIndependent(n,geneList);
                geneList.add(gene);
            }
            parallelLayers.add(geneList);
        }
        supplementalLayer= new ArrayList<>();
    }
    public Chromosome(int k,int n, List<Layer> filter){
        this.wires=n;
        parallelLayers = new ArrayList<>(k+filter.size());
        for (Layer layer : filter) {
            List<Gene> geneList = new ArrayList<>();
            for (Comparator comparator : layer.getAll()) {
                Gene gene = new Gene(comparator.getStartingWire(), comparator.getEndingWire());
                geneList.add(gene);
            }
            parallelLayers.add(geneList);
        }
        for(int i=parallelLayers.size();i<k;i++){
            int genesNumber=new Random().nextInt(n/2 - n/4 + 1) + n/4;
            List<Gene> geneList = new ArrayList<>();
            for(int j=0;j<genesNumber;j++){
                Gene gene = Gene.getIndependent(n,geneList);
                geneList.add(gene);
            }
            parallelLayers.add(geneList);
        }

        supplementalLayer= new ArrayList<>();
    }

    public Chromosome(Chromosome offspring, List<Gene> exchangedLayer, List<Gene> currentLayer) {
        this.parallelLayers=new ArrayList<>();
        this.supplementalLayer=new ArrayList<>();

        for (List<Gene> layer:offspring.parallelLayers) {

            if(layer.equals(currentLayer)){
                List<Gene> layerList = new ArrayList<>();
                for(Gene gene:exchangedLayer){
                    layerList.add(new Gene(gene));
                }
                parallelLayers.add(layerList);
            }
            else {
                List<Gene> layerList = new ArrayList<>();
                for(Gene gene:layer){
                    layerList.add(new Gene(gene));
                }
                parallelLayers.add(layerList);
            }
        }

        for(Gene gene:offspring.supplementalLayer){
            this.supplementalLayer.add(new Gene(gene));
        }
        this.wires=offspring.wires;
    }

    //endregion
    //region Getters,Setters
    public int getWires() {
        return wires;
    }
    public int length() {
        int length=0;
        for (List<Gene> layer:parallelLayers) {
            length+=layer.size();
        }
        length+= supplementalLayer.size();
        return length;
    }
    public int depth(){
       int depth=0;
       for(List<Gene> layer:parallelLayers){
           if(layer.size()>0){
               depth++;
           }
       }
       return depth;
    }

    public List<List<Gene>> getParallelLayers() {
        return parallelLayers;
    }
    public List<Gene> getParallelLayer(int index) {
        return parallelLayers.get(index);
    }
    public List<Gene> getSupplementalLayer() {
        return supplementalLayer;
    }
    public Gene getSupplementalLayer(int index) {
        return supplementalLayer.get(index);
    }
    public Network getNetwork() {
        Network net = new Network(this.wires);
        for(int layer=0;layer<parallelLayers.size();layer++){
            for(Gene gene:parallelLayers.get(layer)){
                net.addComparator(new Comparator(gene.getWire0(),gene.getWire1()),layer);
            }
        }

        for(Gene gene:supplementalLayer) {
            net.addComparator(gene.getWire0(), gene.getWire1());
        }
        return net;
    }

    public void addParallelLayer(List<Gene> layer) {
        parallelLayers.add(layer);
    }
    public void addParallelLayers(List<List<Gene>> layers) {
        parallelLayers.addAll(layers);
    }
    public boolean canAddInParallelLayer(int layer,Gene gene){
        if(parallelLayers.get(layer).size() > wires/2){
            return false;
        }
        List<Gene> genes = parallelLayers.get(layer);
        for(Gene _gene:genes){
            if(gene.contains(_gene.getWire0())||gene.contains(_gene.getWire1())){
                return false;
            }
        }
        return true;
    }
    public void addSupplementalLayer(Gene gene) {
        supplementalLayer.add(gene);
    }
    public void addInParallelLayer(int layer, Gene gene) {
        parallelLayers.get(layer).add(gene);
    }
    //endregion

    public void append(Gene gene){
        for(List<Gene> layer:parallelLayers){
            if(layer.size()==0){
                if(!isRedundant(layer,gene)){
                    layer.add(gene);
                    return;
                }
            }
        }
        if(parallelLayers.get(parallelLayers.size()-1).size()<wires/2){
            if(!isRedundant(parallelLayers.get(parallelLayers.size()-1),gene)){
                addInParallelLayer(parallelLayers.size()-1,gene);
                return;
            }
        }
        //supplementalLayer.add(gene);
    }

    public boolean isRedundant(List<Gene> genes, Gene gene) {
        for(Gene gene1:genes){
            if(gene1.contains(gene.getWire0())||gene1.contains(gene.getWire1()))
                return true;
        }
        return false;
    }

    public static List<Integer> possibleWires(int wires, List<Gene> parallelLayer) {
        List<Integer> possibleValues = new ArrayList<>();
        for (int i = 0; i < wires; i++) {
            possibleValues.add(i);
        }
        for (Gene gene : parallelLayer) {
            possibleValues.remove(Integer.valueOf(gene.getWire0()));
            possibleValues.remove(Integer.valueOf(gene.getWire1()));
        }
        return possibleValues;
    }
    public void reset(Chromosome chromosome){
        parallelLayers = new ArrayList<>();
        for( List<Gene> layer : chromosome.parallelLayers) {
            parallelLayers.add(new ArrayList<>(layer));
        }
        supplementalLayer = new ArrayList<>(chromosome.supplementalLayer);
        wires=chromosome.wires;
    }
    @Override
    public String toString() {
        return "Chromosome{" +
                "parallelLayers=" + parallelLayers +
                ", supplementalLayer=" + supplementalLayer +
                ", wires=" + wires +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chromosome)) return false;
        Chromosome that = (Chromosome) o;
        return parallelLayers.equals(that.parallelLayers) &&
                Objects.equals(supplementalLayer, that.supplementalLayer);
    }
    @Override
    public int hashCode() {
        return Objects.hash(parallelLayers, supplementalLayer);
    }


    public static void main(String[] args) {
        Chromosome chromosome = new Chromosome(10);
        List<Gene> firstLayer = new ArrayList<Gene>(){{add(new Gene(0,1));}};
        List<Gene> secondLayer = new ArrayList<Gene>(){{add(new Gene(0,2));}};
        chromosome.addParallelLayer(firstLayer);
        chromosome.addParallelLayer(secondLayer);

        Chromosome chromosome1 = new Chromosome(chromosome);
        chromosome1.getParallelLayer(0).get(0).setWire0(6);
        chromosome1.addInParallelLayer(0,new Gene(2,3));

        System.out.println(chromosome);
        System.out.println(chromosome1);
    }


}
