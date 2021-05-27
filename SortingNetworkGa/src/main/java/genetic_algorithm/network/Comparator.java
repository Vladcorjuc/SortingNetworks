package genetic_algorithm.network;


import java.util.List;
import java.util.Objects;

public class Comparator {
    private int startingWire;
    private int endingWire;

    public Comparator(int startingWire, int endingWire) {
        this.startingWire = startingWire;
        this.endingWire = endingWire;
    }

    public int getStartingWire() {
        return startingWire;
    }
    public int getEndingWire() {
        return endingWire;
    }
    public int getLength(){return endingWire-startingWire;}
    public boolean isIndependent(List<Comparator> comparators){
        for (Comparator comparator : comparators) {
            if (comparator.getStartingWire() == getStartingWire() || comparator.getStartingWire() == getEndingWire()
                    || comparator.getEndingWire() == getStartingWire() || comparator.getEndingWire() == getEndingWire()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comparator)) return false;
        Comparator that = (Comparator) o;
        return getStartingWire() == that.getStartingWire() &&
                getEndingWire() == that.getEndingWire();
    }
    @Override
    public int hashCode() {
        return Objects.hash(getStartingWire(), getEndingWire());
    }
    @Override
    public String toString() {
        return "Comparator{" + startingWire + "--" + endingWire +
                '}';
    }
}
