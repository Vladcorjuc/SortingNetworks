package cnf;

import formula.*;
import formula.variables.Variable;

import java.util.*;

public class CNFFormula {
    private List<Clause> clauses;

    public CNFFormula() {
        clauses=new ArrayList<>();
    }
    public CNFFormula(List<Clause> clauses1, List<Clause> clauses2) {
        this.clauses = new ArrayList<>(clauses1);
        this.clauses.addAll(clauses2);
    }
    public void add(Clause clause){
        clauses.add(clause);
    }
    public void addClauses(List<Clause> _clauses){
        clauses.addAll(_clauses);
    }
    public CNFFormula(List<Clause> clauses) {
        this.clauses = clauses;
    }
    public CNFFormula(Clause clause) {
        this.clauses=new ArrayList<>();
        this.clauses.add(clause);
    }


    public List<Clause> getClauses() {
        return clauses;
    }

    public void setClauses(List<Clause> clauses) {
        this.clauses = clauses;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("CNF={");
        for(Clause clause:clauses){
            stringBuilder.append(clause);
            stringBuilder.append(" ^ ");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    //CNF for of a formula.Formula
    public static CNFFormula convert(Formula formula){
        if(formula instanceof Variable){
            Clause clause = new Clause();
            clause.addVariable((Variable) formula);
            return new CNFFormula(clause);
        }
        if(formula instanceof AndFormula){
            List<Clause> first = convert(((AndFormula) formula).p).getClauses();
            List<Clause> second = convert(((AndFormula) formula).q).getClauses();

            List<Clause> newClauses = new ArrayList<>(first);
            newClauses.addAll(second);
           return new CNFFormula(newClauses);
        }
        if(formula instanceof OrFormula){
            CNFFormula cnfFormulaP = convert(((OrFormula) formula).p);
            CNFFormula cnfFormulaQ = convert(((OrFormula) formula).q);


            if(cnfFormulaP.clauses.size()>1){
                Variable z = Variable.GetNewVariable();
                Variable notZ = z.opposite();

                Formula impliesFormula =ImpliesFormula.convert(z,((OrFormula) formula).p);
                Formula notImpliesFormula = ImpliesFormula.convert(notZ,((OrFormula) formula).q);

                AndFormula andFormula = new AndFormula(impliesFormula,notImpliesFormula);

                return convert(andFormula);
            }
            else {
                for(Clause clause:cnfFormulaQ.clauses){
                    for(Variable variable:cnfFormulaP.getClauses().get(0).getVariables()){
                        clause.addVariable(variable);
                    }
                }
                return cnfFormulaQ;
            }
        }

        if(formula instanceof  NotFormula){
            Formula childFormula = ((NotFormula)formula).p;
            if(childFormula instanceof Variable){
                Variable newVariable = ((Variable) childFormula).opposite();
                Clause clause = new Clause();
                clause.addVariable(newVariable);
                return new CNFFormula(clause);
            }
            if(childFormula instanceof NotFormula ){
                return convert(((NotFormula) childFormula).p);
            }

            if(childFormula instanceof AndFormula){
                Formula p = ((AndFormula)childFormula).p;
                Formula q= ((AndFormula)childFormula).q;

                NotFormula negatedP = new NotFormula(p);
                NotFormula negatedQ = new NotFormula(q);

                OrFormula negatedOrFormula = new OrFormula(negatedP,negatedQ);

                return convert(negatedOrFormula);
            }
            if(childFormula instanceof OrFormula){
                Formula p = ((OrFormula)childFormula).p;
                Formula q= ((OrFormula)childFormula).q;

                NotFormula negatedP = new NotFormula(p);
                NotFormula negatedQ = new NotFormula(q);
                AndFormula negatedAndFormula = new AndFormula(negatedP,negatedQ);
                return convert(negatedAndFormula);
            }
        }
        return null;

    }

    public static void main(String[] args) {
        Formula formula = new NotFormula(new AndFormula(new OrFormula(new NotFormula(
                new Variable(true,"P")),new Variable(true,"Q")),
                    new AndFormula(new Variable(true,"R"), ImpliesFormula.convert(new Variable(true,"P"),new Variable(true,"Q")))));

        System.out.println(convert(formula));

        Formula formula1 = new NotFormula(ImpliesFormula.convert(new OrFormula(new Variable(true,"P"),new Variable(true
        ,"Q")),new Variable(false,"Q")));

        System.out.println(convert(formula1));
    }
}
