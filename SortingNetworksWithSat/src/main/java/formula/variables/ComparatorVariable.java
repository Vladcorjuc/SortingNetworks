package formula.variables;

import java.util.Objects;

public class ComparatorVariable extends Variable {
    private int layer;
    private int i;
    private int j;

    public ComparatorVariable(boolean value, int layer, int i, int j) {
        super(value,createIdentifier(layer,i,j));
        this.sign = value;
        this.layer = layer;
        this.i = i;
        this.j = j;
    }

    public ComparatorVariable(int layer, int i, int j) {
        super(true, createIdentifier(layer,i,j));
        this.layer = layer;
        this.i = i;
        this.j = j;
    }

    private static String createIdentifier(int layer, int i, int j) {
        return new String("c-"+layer+"-"+i+"-"+j);
    }

    public boolean getValue() {
        return sign;
    }

    public int getLayer() {
        return layer;
    }
    public int getI() {
        return i;
    }
    public int getJ() {
        return j;
    }

    @Override
    public ComparatorVariable opposite() {
        return  new ComparatorVariable(!sign,layer,i,j);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComparatorVariable)) return false;
        ComparatorVariable variable = (ComparatorVariable) o;
        return getValue() == variable.getValue() &&
                getLayer() == variable.getLayer() &&
                getI() == variable.getI() &&
                getJ() == variable.getJ();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue(), getLayer(), getI(), getJ());
    }
}
