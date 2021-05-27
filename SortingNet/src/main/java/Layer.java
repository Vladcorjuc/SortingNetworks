import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Layer {
    private int number;
    private List<Comparator> comparators;

    public Layer(int number) {
        this.number = number;
        comparators=new ArrayList<>();
    }
    public Layer(Layer layer){
        this.number = layer.number;
        this.comparators = new ArrayList<>(layer.comparators);
    }

    //region Getters, Setters and Updaters
    public int getSize(){return comparators.size();}
    public List<Comparator> getAll(){
        return comparators;
    }
    public Comparator get(int i){
        return comparators.get(i);
    }
    //endregion
    //region Comparators
    public boolean containsComparator(Comparator comparator) {
        return comparators.contains(comparator);
    }
    public void addComparator(Comparator comparator){
        comparators.add(comparator);
    }
    public void removeComparator(Comparator comparator){
        comparators.remove(comparator);
    }
    //endregion

    @Override
    public String toString() {
        return "Layer{" +
                "comparators=" + comparators +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Layer)) return false;
        Layer layer = (Layer) o;
        return number == layer.number &&
                Objects.equals(comparators, layer.comparators);
    }
    @Override
    public int hashCode() {
        return Objects.hash(number, comparators);
    }


}
