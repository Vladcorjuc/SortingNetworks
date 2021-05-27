import Util.Constants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.awt.util.IdentityArrayList;

import java.util.*;

public class Network {
    private int wires;
    private List<Layer> layers;
    private int depth;
    private boolean dead=false;

    private Set<Sequence> outputs=null;

    private List<Set<Sequence>> clusters=null;
    private Set<Integer> bad=null;
    private List<Set<Integer>> ones=null;
    private List<Set<Integer>> zeros=null;

    //region Constructors
    public Network(int wires) {
        this.wires = wires;
        this.layers=new ArrayList<>();
        depth=0;
    }
    public Network(@NotNull Network network, int i, int j){
        this.layers=new ArrayList<>();
        for (Layer layer:network.layers) {
            this.layers.add(new Layer(layer));
        }
        this.depth=network.getDepth();
        this.wires=network.getWires();
        this.addComparator(new Comparator(i,j));

        if(network.outputs!=null) {
            this.outputs = new HashSet<>(network.outputs);
            this.removeImpossibleOutput(i, j);
        }
    }
    //endregion
    //region Getters, Setters and Updaters
    public Set<Sequence> outputs() {
        if(outputs==null){
            outputs=new HashSet<>();
            createOutputs();
        }
        return outputs;
    }
    public int getDepth(){
        return depth;
    }
    public int getWires() {
        return wires;
    }
    public List<Layer> getLayers() {
        return layers;
    }
    public int size(){
        int number=0;
        for (Layer l:layers) {
            number+=l.getSize();
        }
        return number;
    }
    public List<Comparator> getComparatorList(){
        List<Comparator> comparators=new ArrayList<>();
        for(Layer l:layers){
            comparators.addAll(l.getAll());
        }
        return comparators;
    }
    private void updateDepth() {
        depth++;
    }
    public boolean isEmpty(){
        return size()==0;
    }
    public boolean isSorting() {
        return outputs().size() == (wires+1);
    }
    public boolean isRedundant(int i, int j){
        for (Sequence x:this.outputs()) {
            if(x.areSwappable(i-1,j-1)){
                return false;
            }
        }
        return true;

    }
    public boolean isDead(){return dead;}
    public void die() {
        dead=true;
    }
    //endregion
    //region Comparators
    private boolean canUseComparator(@NotNull Comparator comparator, @NotNull Sequence input){
        return input.get(comparator.getStartingWire()-1) && !input.get(comparator.getEndingWire()-1);
    }
    @Nullable
    private Sequence useComparator(Comparator comparator, Sequence input){
        if(!canUseComparator(comparator,input)){
            return null;
        }
        input.swap(comparator.getStartingWire()-1,comparator.getEndingWire()-1);
        return input;
    }
    public boolean containsComparator(int i, int j) {
        for(Layer l:layers){
            if(l.containsComparator(new Comparator(i,j))){
                return true;
            }
        }
        return false;
    }
    public void addComparator(Comparator comparator){
        boolean found=false;
        if(layers.size()>0) {
            Layer layer = layers.get(layers.size() - 1);
            if(layer.getAll().size()>0){
                if(layer.get(layer.getAll().size()-1).getEndingWire()<comparator.getStartingWire()){
                    layer.addComparator(comparator);
                    layers.set(layers.size()-1,layer);
                    found = true;
                }
            }
            else {
                layer.addComparator(comparator);
                layers.set(layers.size()-1,layer);
                found = true;
            }

        }
        if(!found){
            Layer layer=new Layer(layers.size());
            layer.addComparator(comparator);
            layers.add(layer);
            updateDepth();
        }
    }
    public void addComparator(int i, int j){
        addComparator(new Comparator(i, j));
    }
    private boolean canAddComparator(Comparator comparator) {
        for(Layer layer:layers){
            if(layer.containsComparator(comparator)){
                return false;
            }
        }
        return true;
    }
    //endregion
    //region Outputs
    private void removeImpossibleOutput(int i, int j) {
        Set<Sequence> originalOutputs=new HashSet<>(outputs);

        for(Sequence s:originalOutputs){
            if(s.areSwappable(i-1,j-1)) {
                outputs.remove(s);
                outputs.add(Sequence.getSwapped(s,i-1,j-1));
            }
        }
    }
    private void createOutputs() {
        for (int value = 0; value < Constants.POW_2[wires+1]; value++) {
            Sequence input = new Sequence(Sequence.getInstance(wires, value));
            //System.out.println(input);
            Sequence sq=new Sequence(5,9);

            Sequence output=apply(input);
            //System.out.println(input+" ----  "+output);
            outputs.add(output);
        }
    }
    public Set<Sequence> getOutputsRestricted(int p) {
        Set<Sequence> restrictedOutputs=new HashSet<>();
        for(Sequence x:this.outputs()){
            if(x.getOnesNumber()==p){
                restrictedOutputs.add(x);
            }
        }
        return restrictedOutputs;
    }
    //endregion

    public Sequence apply(Sequence input){
        for(Layer layer:layers) {
            for (Comparator c : layer.getAll()) {
                Sequence output=useComparator(c,input);
                if(output!=null){
                    input=output;
                }
            }
        }
        return input;
    }

    //region Getters of algorithm needed sets
    public  Set<Sequence> getCluster(int p){
        List<Set<Sequence>> _clusters = getClusters();
        return _clusters.get(p);
    }
    public  Set<Integer>  getZeros(int p){
        List<Set<Integer>> _zeros = this.getAllZeros();
        return _zeros.get(p);
    }
    public Set<Integer> getOnes(int p){
        List<Set<Integer>> _ones = this.getAllOnes();
        return _ones.get(p);
    }

    public List<Set<Sequence>> getClusters(){
        if(clusters==null){
            this.generateClusters();
        }
        return clusters;
    }
    public List<Set<Integer>> getAllZeros(){
        if(this.zeros == null){
            this.generateZeros();
        }
        return zeros;
    }
    public  List<Set<Integer>> getAllOnes(){
        if(this.ones==null){
           this.generateOnes();
        }
        return this.ones;
    }
    public  Set<Integer> getBad(){
        if(bad==null) {
            this.generateBad();
        }
        return bad;
    }
    //endregion

    //region generate cache Clusters,Ones,Zeros and Bad
    private void generateOnes() {
        ones=new ArrayList<>();
        for(int i=0;i<=this.getWires();i++) {
            Set<Integer> onesSet = new HashSet<>();
            for (Sequence s : this.getCluster(i)) {

                onesSet.addAll(s.getOnes());
            }
            ones.add(onesSet);
        }
    }
    private void generateZeros() {
        zeros=new ArrayList<>();
        for(int i=0;i<=this.getWires();i++) {
            Set<Integer> zeroSet=new HashSet<>();
            for(Sequence s:this.getCluster(i)){
                zeroSet.addAll(s.getZeros());
            }
            zeros.add(zeroSet);
        }
    }
    private void generateClusters() {
        this.clusters=new ArrayList<>();
        for(int i=0;i<=this.getWires();i++){
            clusters.add(this.getOutputsRestricted(i));
        }
    }
    private void generateBad(){
        bad = new HashSet<>();
        int n = this.getWires();
        for (Sequence output : this.outputs()) {
            int p = output.getZerosNumber();
            for (int i = 0; i < p; i++) {
                if (output.get(i)) {
                    bad.add(i);
                }
                if (!output.get(n - i - 1)) {
                    bad.add(n - i - 1);
                }
            }
        }
    }
    //endregion


    @Override
    public String toString() {
        return "Network{" +
                "wires=" + wires +
                ", layers=" + layers +
                ", depth=" + depth +
                ", outputs=" + outputs +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Network)) return false;
        Network network = (Network) o;
        return getWires() == network.getWires() &&
                getDepth() == network.getDepth() &&
                getLayers().equals(network.getLayers());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getWires(), getLayers(), getDepth());
    }

    public static void main(String[] args) {
        Network network=new Network(5);
        network.addComparator(1,2);
        network.addComparator(4,5);
        //System.out.println(network.outputs());
        //System.out.println(network.getOutputsRestricted(1));


        Network newNetwork=new Network(5);
        newNetwork.addComparator(1,2);
        newNetwork.addComparator(3,4);

        System.out.println(newNetwork.isRedundant(2,3));
    }



}
