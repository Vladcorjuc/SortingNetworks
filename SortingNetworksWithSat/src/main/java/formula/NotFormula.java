package formula;

public class NotFormula extends Formula {
    public Formula p;

    public NotFormula(Formula p) {
        this.p = p;
    }

    @Override
    public String toString() {
        return "~"+p;
    }
} // negation
