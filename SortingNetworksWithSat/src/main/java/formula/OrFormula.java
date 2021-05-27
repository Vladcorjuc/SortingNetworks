package formula;

public class OrFormula extends Formula {
    public Formula p;
    public Formula q;

    public OrFormula(Formula p, Formula q) {
        this.p = p;
        this.q = q;
    }

    @Override
    public String toString() {
        return "("+p +" v "+ q+")";
    }
}  // disjunction
