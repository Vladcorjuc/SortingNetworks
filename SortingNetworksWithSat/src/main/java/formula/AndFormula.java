package formula;

public class AndFormula extends Formula {
    public Formula p;
    public Formula q;

    public AndFormula(Formula p, Formula q) {
        this.p = p;
        this.q = q;
    }

    @Override
    public String toString() {
        return  "(" +p +" ^ "+ q +")";
    }
}  // conjunction
