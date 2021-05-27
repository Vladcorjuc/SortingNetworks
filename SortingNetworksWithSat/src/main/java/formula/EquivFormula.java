package formula;

public abstract class EquivFormula extends Formula {
    public Formula p;
    public Formula q;

    public EquivFormula(Formula p, Formula q) {
        this.p = p;
        this.q = q;
    }

    public static AndFormula convert(Formula p,Formula q){
        return new AndFormula(new OrFormula(p,new NotFormula(q)),new OrFormula(new NotFormula(p),q));
    }

    @Override
    public String toString() {
        return "formula.EquivFormula{" +
                "p=" + p +
                ", q=" + q +
                '}';
    }
} // equiv
