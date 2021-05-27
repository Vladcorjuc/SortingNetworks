import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;


import java.util.Arrays;
import java.util.List;

public class SatSolver {

    public static boolean solve(int maxVar, int clausesNumber, List<int[]> clauses) throws ContradictionException {
        ISolver solver=SolverFactory.newDefault();
        solver.newVar(maxVar);
        solver.setExpectedNumberOfClauses(clausesNumber);
        for (int i = 0;i<clausesNumber;i++){
            solver.addClause(new VecInt(clauses.get(i)));
        }
        try {
            if(solver.isSatisfiable()){
                System.out.println(Arrays.toString(solver.model()));
            }
            return solver.isSatisfiable();
        }catch (TimeoutException e){
            return false;
        }
    }




}
