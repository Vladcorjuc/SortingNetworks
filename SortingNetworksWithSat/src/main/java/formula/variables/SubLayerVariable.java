package formula.variables;

import java.util.Objects;

public class SubLayerVariable extends Variable {
    private int layer;
    private int subLayer;
    private int sequence;

    public SubLayerVariable(int layer,int subLayer,int sequence) {
        super(true,createIdentifier(layer,subLayer,sequence));
        this.layer=layer;
        this.subLayer=subLayer;
        this.sequence = sequence;
    }
    private static String createIdentifier(int layer,int subLayer,int sequence) {
        return "p-" + layer + "-" + subLayer + "-" + sequence;
    }

    public int getLayer() {
        return layer;
    }
    public int getSubLayer() {
        return subLayer;
    }
    public int getSequence() {
        return sequence;
    }
    public boolean getSign(){return sign;}

    @Override
    public Variable opposite() {
        SubLayerVariable v = new SubLayerVariable(layer,subLayer,sequence);
        v.sign = !v.sign;
        return v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubLayerVariable)) return false;
        if (!super.equals(o)) return false;
        SubLayerVariable that = (SubLayerVariable) o;
        return getSign() == that.getSign() &&
                getLayer() == that.getLayer() &&
                getSubLayer() == that.getSubLayer() &&
                getSequence() == that.getSequence();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLayer(), getSubLayer(), getSequence(),getSign());
    }
}
