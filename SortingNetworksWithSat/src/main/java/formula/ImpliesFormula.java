package formula;

public abstract class ImpliesFormula extends Formula {
    public Formula p;
    public Formula q;

    public ImpliesFormula(Formula p, Formula q) {
        this.p = p;
        this.q = q;
    }

    public static OrFormula convert(Formula p, Formula q){
        return new OrFormula(new NotFormula(p),q);

    }

    @Override
    public String toString() {
        return "formula.ImpliesFormula{" +
                "p=" + p +
                ", q=" + q +
                '}';
    }
} // if-then
