package formula.variables;

import formula.Formula;

import java.util.Objects;

public class Variable extends Formula {

    static int random=0;
    protected boolean sign;
    protected String identifier;

    public Variable(boolean sign, String identifier) {
        this.sign = sign;
        this.identifier = identifier;
    }

    public Variable opposite(){
        return new Variable(!this.sign,this.identifier);
    }

    public static Variable GetNewVariable() {
        String id= new String("random_"+random);
        random++;
        return new Variable(true,id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Variable)) return false;
        Variable variable = (Variable) o;
        return sign == variable.sign &&
                identifier.equals(variable.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sign, identifier);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if(!sign){
            stringBuilder.append("-");
        }
        stringBuilder.append(identifier);
        return stringBuilder.toString();
    }
}
