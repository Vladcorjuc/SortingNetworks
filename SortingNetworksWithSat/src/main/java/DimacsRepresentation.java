import cnf.CNFFormula;
import cnf.Clause;
import formula.variables.Variable;

import java.util.*;

public class DimacsRepresentation {

    private List<int[]> clauses;
    private int maxVar;
    private int maxClauses;

    public DimacsRepresentation() {
        clauses = new ArrayList<>();
    }

    public void setClauses(ArrayList<int[]> ints) {
        clauses = new ArrayList<>(ints);
    }
    public void setMaxVar(int i) {
        maxVar=i;
    }
    public void setMaxClauses(int size) {
        maxClauses = size;
    }

    public List<int[]> getClauses() {
        return clauses;
    }

    public int getMaxVar() {
        return maxVar;
    }

    public int getMaxClauses() {
        return maxClauses;
    }

    public static DimacsRepresentation convert(CNFFormula formula){
        DimacsRepresentation representation = new DimacsRepresentation();
        List<int[]> clauses = new ArrayList<>();
        Map<Variable,Integer> variableDictionary = new HashMap<>();
        int i=0;
        for(Clause clause:formula.getClauses()){
            int[] variables = new int[clause.getVariables().size()];
            int j=0;
            for(Variable variable:clause.getVariables()){
                if(variableDictionary.containsKey(variable)) {
                    variables[j] = variableDictionary.get(variable);
                    j++;
                    continue;
                }
                if(variableDictionary.containsKey(variable.opposite())){
                    variables[j] = -variableDictionary.get(variable.opposite());
                    variableDictionary.put(variable,-variableDictionary.get(variable.opposite()));
                    j++;
                    continue;
                }
                i++;
                variableDictionary.put(variable,i);
                variables[j] = i;
                j++;
            }
            clauses.add(variables);
        }
        List<Integer> a = new ArrayList<>(variableDictionary.values());
        Collections.sort(a);
        System.out.println(a);
        System.out.println(variableDictionary.values());
        representation.setClauses(new ArrayList<>(clauses));
        representation.setMaxVar(i);
        representation.setMaxClauses(clauses.size());
        return representation;

    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("[");
        for(int[] clause:clauses){
            stringBuilder.append("[");
            for(Integer v:clause){
                stringBuilder.append(v).append(", ");
            }
            stringBuilder.append("]");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
