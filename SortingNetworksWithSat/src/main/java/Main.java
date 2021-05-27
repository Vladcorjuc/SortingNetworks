import cnf.CNFFormula;
import org.sat4j.specs.ContradictionException;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int n = 16;
        int d = 2;

        /*
        PropositionGenerator generator = new PropositionGenerator(network);
        cnf.CNFFormula cnfFormula = generator.getProposition();
        SatSolver satSolver = new SatSolver();
        DimacsRepresentation representation = cnf.CNFFormula.convertToDimacs(cnfFormula);
        System.out.println(representation);
        System.out.println(representation.getMaxVar() +" "+representation.getMaxClauses());
        try {
            System.out.println(satSolver.solve(representation.getMaxVar(),representation.getMaxClauses(),representation.getClauses()));
        } catch (ContradictionException e) {
            e.printStackTrace();
        }
         */

        PropositionGenerator generator = new PropositionGenerator(n,d);
        CNFFormula cnfFormula = generator.getProposition();
        SatSolver satSolver = new SatSolver();
        DimacsRepresentation representation = DimacsRepresentation.convert(cnfFormula);
        System.out.println(representation);
        System.out.println(representation.getMaxVar() +" "+representation.getMaxClauses());
        try {
            System.out.println(SatSolver.solve(representation.getMaxVar(),representation.getMaxClauses(),representation.getClauses()));
        } catch (ContradictionException e) {
            e.printStackTrace();
        }



    }
}
