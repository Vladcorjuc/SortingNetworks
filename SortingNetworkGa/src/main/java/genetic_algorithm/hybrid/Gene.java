package genetic_algorithm.hybrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Gene {
    private int wire0;
    private int wire1;

    public Gene(int wire0, int wire1) {
        if(wire0>wire1){
            int aux = wire0;
            wire0 = wire1;
            wire1 = aux;
        }
        this.wire0 = wire0;
        this.wire1 = wire1;
    }
    public Gene(Gene gene) {
        this.wire0=gene.getWire0();
        this.wire1=gene.getWire1();
    }

    public int getWire0() {
        return wire0;
    }
    public int getWire1() {
        return wire1;
    }

    public void setWire0(int wire0) {
        if(wire0>this.getWire1()){
            int aux = this.wire1;
            this.wire0 = wire1;
            this.wire1 = aux;
        }
        else {
            this.wire0=wire0;
        }

    }
    public void setWire1(int wire1) {
        if(wire1<this.getWire0()){
            int aux = this.wire0;
            this.wire0 = wire1;
            this.wire1 = aux;
        }
        else {
            this.wire1 = wire1;
        }
    }

    public boolean isIndependent(List<Gene> genes) {
        for (Gene gene : genes) {
            if (gene.wire0 == wire0 || gene.wire0 == wire1
                    || gene.wire1 == wire0 || gene.wire1 == wire1) {
                return false;
            }
        }
        return true;
    }
    public static Gene getIndependent(int n, List<Gene> geneList) {
        List<Integer> possibleValues = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            possibleValues.add(i);
        }
        for (Gene gene : geneList) {
            possibleValues.remove(Integer.valueOf(gene.getWire0()));
            possibleValues.remove(Integer.valueOf(gene.getWire1()));
        }

        if(possibleValues.size()<=0){
            return null;
        }
        Random random = new Random();
        int firstWire = possibleValues.get(random.nextInt(possibleValues.size()));
        possibleValues.remove(Integer.valueOf(firstWire));

        if(possibleValues.size()<=0){
            return null;
        }
        int secondWire = possibleValues.get(random.nextInt(possibleValues.size()));

        return new Gene(firstWire, secondWire);

    }
    public static Gene getRandom(int n){
        List<Integer> possibleValues = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            possibleValues.add(i);
        }
        Random random = new Random();
        int pos= random.nextInt(possibleValues.size());
        int firstWire = possibleValues.get(pos);
        possibleValues.remove(pos);
        pos = random.nextInt(possibleValues.size());
        int secondWire = possibleValues.get(pos);
        return new Gene(firstWire,secondWire);
    }

    public boolean contains(int wire) {
        return wire==wire0||wire==wire1;
    }
    public void replaceUsedWire(Integer usedWire, Integer newWire) {
        if(wire0==usedWire){
            wire0=newWire;
        }
        else if(wire1==usedWire){
            wire1=newWire;
        }
    }

    @Override
    public String toString() {
        return "Gene{" +
                wire0 +"--"+wire1+
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gene)) return false;
        Gene gene = (Gene) o;
        return getWire0() == gene.getWire0() &&
                getWire1() == gene.getWire1();
    }
    @Override
    public int hashCode() {
        return Objects.hash(getWire0(), getWire1());
    }


}
