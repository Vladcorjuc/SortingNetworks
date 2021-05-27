package cnf;
import formula.variables.Variable;

import java.util.ArrayList;
import java.util.List;

public class Clause {
    private List<Variable> variables;

    public Clause(List<Variable> variables) {
        this.variables = variables;
    }

    public Clause() {
        variables=new ArrayList<>();
    }

    public void addVariable(Variable v){
        variables.add(v);
    }

    public void addAll(List<Variable> incident) {
        variables.addAll(incident);
    }

    public List<Variable> getVariables() {
        return variables;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("( ");
        for(Variable variable:variables){
            stringBuilder.append(variable);
            stringBuilder.append(" v ");
        }
        stringBuilder.append(" )");
        return stringBuilder.toString();
    }
}
