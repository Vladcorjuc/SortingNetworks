package formula.variables;

import java.util.Objects;

//used in previous try
@Deprecated
public class InputVariable extends Variable {
    private int sequence;
    private int layer;
    private int wire;

    public InputVariable(boolean sign,InputVariable variable){
        super(sign,variable.identifier);
        this.layer = variable.layer;
        this.wire = variable.wire;
        this.sequence=variable.sequence;

    }
    public InputVariable(String type,boolean sign, int wire, int layer){
        super(sign,type+"-"+wire+"-"+layer);
        this.layer = layer;
        this.wire = wire;
    }
    public InputVariable(int sequence, int wire, int layer){
        super(true,createIdentifier(wire, layer,sequence));
        this.layer = layer;
        this.wire = wire;
        this.sequence = sequence;
    }
    public InputVariable(String type,int sequence, int wire, int layer){
        super(true,createIdentifier(wire, layer,sequence)+"-"+type);
        this.layer = layer;
        this.wire = wire;
        this.sequence = sequence;
    }

    public int getSequence() {
        return sequence;
    }
    public int getLayer() {
        return layer;
    }
    public int getWire() {
        return wire;
    }

    private static String createIdentifier(int i,int l,int s) {
        return "i-" + s + "-" + l + "-" + i;
    }

    @Override
    public InputVariable opposite() {
        return new InputVariable(!sign,this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InputVariable)) return false;
        InputVariable that = (InputVariable) o;
        return getSequence() == that.getSequence() &&
                getLayer() == that.getLayer() &&
                getWire() == that.getWire();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSequence(), getLayer(), getWire());
    }
}
