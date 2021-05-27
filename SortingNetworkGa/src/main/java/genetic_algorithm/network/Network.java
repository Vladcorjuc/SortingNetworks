package genetic_algorithm.network;
import java.util.*;

public class Network {
    private int wires;
    private List<Layer> layers;

    private Set<Sequence> outputs = null;
    private Set<Sequence> bad=null;
    private Set<Comparator> redundantComparators = null;

    //region Constructors
    public Network(int wires) {
        this.wires = wires;
        this.layers = new ArrayList<>();
    }
    public Network(Network network, int i, int j){
        this.layers=new ArrayList<>();
        for (Layer layer:network.layers) {
            this.layers.add(new Layer(layer));
        }
        this.wires=network.getWires();
        this.addComparator(new Comparator(i,j));

        if(network.outputs!=null) {
            this.bad = new HashSet<>(network.bad);
            this.outputs = new HashSet<>(network.outputs());
            this.removeImpossibleOutput(i, j);
        }
    }
    //endregion
    //region Getters
    public int getWires() {
        return wires;
    }
    public List<Layer> getLayers() {
        return layers;
    }
    public List<Comparator> getComparators(){
        List<Comparator> comparators=new ArrayList<>();
        for(Layer l:layers){
            comparators.addAll(l.getAll());
        }
        return comparators;
    }
    public Set<Sequence> getBad(){
        if(bad==null){
            outputs=new HashSet<>();
            createOutputs();
        }
        return bad;
    }
    public Set<Sequence> outputs() {
        if (outputs == null) {
            outputs = new HashSet<>();
            createOutputs();
        }
        return outputs;
    }
    public boolean isRedundant(int i, int j) {
        for (Sequence x : this.outputs()) {
            if (x.areSwappable(i, j)) {
                return false;
            }
        }
        return true;

    }
    public boolean isRedundant(Comparator comparator){return isRedundant(comparator.getStartingWire(),comparator.getEndingWire());}
    public boolean isSorting() {
        //return outputs().size() == (wires+1);
        return getBad().size() == 0;
    }
    public Set<Comparator> getRedundantComparators(){
        if(redundantComparators==null){
            findRedundantComparators();
        }
        return redundantComparators;
    }

    private boolean canUseComparator(Comparator comparator,Sequence input) {
        return input.get(comparator.getStartingWire()) && !input.get(comparator.getEndingWire());
    }
    //endregion
    //region Setters
    public void addComparator(int i, int j) {
        addComparator(new Comparator(i, j));
    }
    public void addComparator(Comparator comparator) {
        //comparators.add(comparator);
        if(layers.size()>0) {
            boolean found = false;
            for(Layer layer:layers){
                if(comparator.isIndependent(layer.getAll())){
                    layer.addComparator(comparator);
                    found=true;
                    break;
                }
            }
            if(!found){
                Layer layer=new Layer(layers.size());
                layer.addComparator(comparator);
                layers.add(layer);
            }
        }
        else {
            Layer layer=new Layer(layers.size());
            layer.addComparator(comparator);
            layers.add(layer);
        }
    }
    public void addComparator(Comparator comparator, int layer){
        if(layers.size()<=layer){
            for(int i=layers.size();i<=layer;i++) {
                layers.add(new Layer(wires));
            }
        }
        layers.get(layer).addComparator(comparator);
    }
    private void createOutputs() {
        bad=new HashSet<>();
        for (int value = 0; value < Utils.POW_2[wires+1]; value++) {
            Sequence input = new Sequence(Objects.requireNonNull(Sequence.getInstance(wires, value)));
            Sequence output = apply(input);
            outputs.add(output);
            if(!output.isSorted()){
                bad.add(output);
            }
        }
    }
    private void removeImpossibleOutput(int i, int j) {
        Set<Sequence> originalOutputs=new HashSet<>(outputs);

        for(Sequence s:originalOutputs){
            if(s.areSwappable(i,j)) {
                outputs.remove(s);
                bad.remove(s);
                outputs.add(Sequence.getSwapped(s,i,j));
            }
        }
    }

    private void findRedundantComparators() {
        redundantComparators=new HashSet<>(getComparators());
        for (int value = 0; value < Utils.POW_2[wires+1]; value++) {
            Sequence input = new Sequence(Objects.requireNonNull(Sequence.getInstance(wires, value)));
            for(Layer layer:layers) {
                for (Comparator c : layer.getAll()) {
                    if(canUseComparator(c,input)){
                        redundantComparators.remove(c);
                    }
                    Sequence output = useComparator(c, input);
                    if (output != null) {
                        input = output;
                    }
                }
            }

        }
    }
    //endregion
    //region Apply
    public Sequence apply(Sequence input) {
        for(Layer layer:layers) {
            for (Comparator c : layer.getAll()) {
                Sequence output = useComparator(c, input);
                if (output != null) {
                    input = output;
                }
            }
        }
        return input;
    }
    private Sequence useComparator(Comparator comparator, Sequence input) {
        if (!canUseComparator(comparator, input)) {
            return null;
        }
        input.swap(comparator.getStartingWire(), comparator.getEndingWire());
        return input;
    }
    //endregion

    public static Network createGreenFilter(int n){
        Network filter = new Network(n);
        int length=1;
        while(length<n){

            for(int k=0;k<length;k++){
                for(int i=k;i<n-length;i+=2*length){
                    filter.addComparator(new Comparator(i,i+length));
                }
            }
            length *= 2;
        }
        return filter;
    }
    public String visualize(){
        List<List<Layer>> parallelLayers = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();


        for(Layer layer:getLayers()){
            List<Layer> iLayer = new ArrayList<>();
            Layer currentLayer = new Layer(wires);
            for(Comparator comparator:layer.getAll()){
                if(currentLayer.getSize()>0) {
                    if (comparator.getStartingWire() < currentLayer.get(currentLayer.getSize() - 1).getEndingWire()) {
                        iLayer.add(currentLayer);
                        currentLayer=new Layer(wires);
                        currentLayer.addComparator(comparator);
                        continue;
                    }
                }
                currentLayer.addComparator(comparator);
            }
            iLayer.add(currentLayer);
            parallelLayers.add(iLayer);
        }

        stringBuilder.append("\\begin{tikzpicture}\n");

        int last=0;
        int i=0;
        for(List<Layer> layerList:parallelLayers){
            for(Layer layer:layerList){
                i++;
                for(Comparator comparator:layer.getAll()){
                    last++;
                }

            }

            stringBuilder.append(String.format("\\draw[dashed] (%f,0.5) -- (%f,%f);\n",i+0.5,i+0.5,wires+0.5));
        }

        stringBuilder.append(String.format("\\foreach \\a in {1,...,%d}\n" +
                "  \\draw[thick] (0,\\a) -- ++(%d,0);\n",wires,i+1));

        stringBuilder.append("\\foreach \\x in {");


        i=0;
        int current=0;
        for(List<Layer> layerList:parallelLayers){
            for(Layer layer:layerList){
                for(Comparator comparator:layer.getAll()){
                    current++;
                    if(current==last){
                        stringBuilder.append("{").append(i  + 1).append(",").append(comparator.getEndingWire()+1).append("},");
                        stringBuilder.append("{").append(i + 1).append(",").append(comparator.getStartingWire()+1).append("}");
                    }
                    else {
                        stringBuilder.append("{").append(i  + 1).append(",").append(comparator.getEndingWire()+1).append("},");
                        stringBuilder.append("{").append(i + 1).append(",").append(comparator.getStartingWire()+1).append("},");
                    }

                }
                i++;
            }
        }

        stringBuilder.append("}\n\t \\filldraw (\\x) circle (1.5pt);");
        stringBuilder.append("\n");
        i=0;
        for(List<Layer> layerList:parallelLayers){
            for(Layer layer:layerList){
                for(Comparator comparator:layer.getAll()){
                    stringBuilder.append("\\draw[thick] (").append(i + 1).append(",").append(comparator.getStartingWire()+1)
                            .append(") -- (").append(i + 1).append(",")
                            .append(comparator.getEndingWire()+1).append(");").append("\n");
                }
                i++;
            }
        }
        stringBuilder.append("\\end{tikzpicture}");
        return stringBuilder.toString();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Network)) return false;
        Network network = (Network) o;
        return getWires() == network.getWires() &&
                Objects.equals(getLayers(), network.getLayers()) &&
                Objects.equals(outputs, network.outputs) &&
                Objects.equals(getBad(), network.getBad());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWires(), getLayers(), outputs, getBad());
    }

    @Override
    public String toString() {
        return "Network{" +
                "wires=" + wires +
                ", layers=" + layers +
                ", outputs=" + outputs +
                '}';
    }

    public static void main(String[] args) {
        Network network=new Network(5);
        network.addComparator(new Comparator(0,1));
        network.addComparator(new Comparator(2,3));
        network.addComparator(new Comparator(1,3));
        network.addComparator(new Comparator(1,4));
        System.out.println(network.visualize());
    }
}
