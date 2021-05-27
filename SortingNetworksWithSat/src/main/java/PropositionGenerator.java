import cnf.CNFFormula;
import cnf.Clause;
import formula.EquivFormula;
import formula.Formula;
import formula.OrFormula;
import formula.variables.ComparatorVariable;
import formula.variables.SubLayerVariable;
import formula.variables.Variable;

public class PropositionGenerator {

    /*
    private final Network network;
    private List<formula.variables.ComparatorVariable> allComparatorsVariables;
    private formula.variables.ComparatorVariable[][][] comparatorVariables;
    private formula.variables.Variable[][] usedWires;

    private cnf.CNFFormula phiUsed;
    private cnf.CNFFormula phiValid;
    private cnf.CNFFormula phiSort;


    public PropositionGenerator(Network network) {
        this.network = network;
    }

    public List<formula.variables.ComparatorVariable> getIncident(int l,int k){
        List<formula.variables.ComparatorVariable> incident= new ArrayList<>();
        if(allComparatorsVariables ==null){
            createComparatorsVariables();
        }
        for(formula.variables.ComparatorVariable v: allComparatorsVariables){
            if(v.getLayer()==l&& (v.getI()==k||v.getJ()==k)){
                incident.add(v);
            }
        }
        return incident;
    }
    private formula.Formula getAtMostOne(List<formula.variables.ComparatorVariable> incident) {
        int n = incident.size();
        formula.Formula previousFormula=null;
        for(int i=0;i<n-1;i++){
            for(int j=i+1;j<n;j++){
                formula.variables.ComparatorVariable bi = incident.get(i).opposite();
                formula.variables.ComparatorVariable bj = incident.get(j).opposite();

                formula.Formula f = new formula.OrFormula(bi,bj);

                if(previousFormula ==null){
                    previousFormula = f;
                }
                else {
                    previousFormula = new formula.AndFormula(bi,bj);
                }
            }
        }
        return previousFormula;
    }


    public cnf.CNFFormula getPhiUsed() {
        if(phiUsed==null){
            createPhyUsed();
        }
        return phiUsed;
    }
    public cnf.CNFFormula getPhiValid() {
        if(phiValid==null){
            createPhyValid();
        }
        return phiValid;
    }


    public cnf.CNFFormula getPhiSort(){
        if(phiSort==null){
            createPhySort();
        }
        return phiSort;
    }
    public List<formula.variables.ComparatorVariable> getComparators(){
        if(allComparatorsVariables == null){
            createComparatorsVariables();
        }
        return allComparatorsVariables;
    }
    private formula.variables.Variable[][] getUsedWires(){
        if(usedWires ==null){
            createUsedWires();
        }
        return usedWires;
    }
    public cnf.CNFFormula getProposition(){
        return new cnf.CNFFormula(getPhiUsed().getClauses(),new cnf.CNFFormula(getPhiValid().getClauses(),getPhiSort().getClauses()).getClauses());
    }

    private void createUsedWires() {
        int n = network.getWires();
        int d = network.getDesiredDepth();

        usedWires = new formula.variables.Variable[d][n];
        for(int l=0;l<d;l++){
            for(int k=0;k<n;k++){
                usedWires[l][k] = new formula.variables.InputVariable("u",true,k,l);
            }
        }
    }
    private void createComparatorsVariables() {
        int n = network.getWires();
        int d = network.getDesiredDepth();

        allComparatorsVariables = new ArrayList<>();
        comparatorVariables = new formula.variables.ComparatorVariable[d][n][n];

        for(int l=0;l<d;l++){
            for(int i=0;i<n-1;i++){
                for(int j=i+1;j<n;j++){
                    formula.variables.ComparatorVariable v = new formula.variables.ComparatorVariable(true,l,i,j);
                    comparatorVariables[l][i][j] = v;
                    allComparatorsVariables.add(v);
                }
            }
        }
    }
    private void createPhyUsed() {
        int n = network.getWires();
        int d = network.getDesiredDepth();
        getUsedWires();

        cnf.CNFFormula previousFormula = null;
        for (int i = 0; i < n; i++) {
            for(int l=0;l<d;l++){

                cnf.CNFFormula currentFormula = cnf.CNFFormula.convert(formula.EquivFormula.convert(usedWires[l][i],orOfList(getIncident(l,i))));
                
                if(previousFormula==null){
                    previousFormula = currentFormula;
                }
                else {
                    previousFormula = new cnf.CNFFormula(previousFormula.getClauses(),currentFormula.getClauses());
                }

            }
        }
        phiUsed = previousFormula;
    }
    private void createPhyValid() {
        int n = network.getWires();
        int d = network.getDesiredDepth();

        cnf.CNFFormula previousFormula =null;
        for (int i = 0; i < n; i++) {
            for(int l=0;l<d;l++){

                cnf.CNFFormula current = cnf.CNFFormula.convert(getAtMostOne(getIncident(l,i)));
                if(previousFormula==null){
                    previousFormula=current;
                }
                else {
                    previousFormula = new cnf.CNFFormula(previousFormula.getClauses(),current.getClauses());

                }            }
        }
        phiValid = previousFormula;
    }






    private void createPhySort() {

        phiSort = null;

        cnf.CNFFormula previousFormula=null;
        int b=0;
        for(Sequence s:network.getUnsorted()){
            b++;
            cnf.CNFFormula current = createPhySort(b);
            if(previousFormula==null){
                previousFormula = current;
            }
            else {
                previousFormula = new cnf.CNFFormula(previousFormula.getClauses(),current.getClauses());
            }
        }
        phiSort = previousFormula;

    }

    private cnf.CNFFormula createPhySort(int b) {
        int n = network.getWires();
        int d = network.getDesiredDepth();

        cnf.CNFFormula phiSortB = null;

        List<formula.variables.Variable> previous = new ArrayList<>();
        for(int i=0;i<n;i++){
            previous.add(new formula.variables.InputVariable(b,i,-1));
        }

        for(int l=0;l<d;l++){
            List<formula.variables.Variable> current = new ArrayList<>();
            for(int i=0;i<n;i++){
                current.add(new formula.variables.InputVariable(b,i,l));
            }
            cnf.CNFFormula currentFormula = phi(l,previous,current);

            if(phiSortB ==null){
                phiSortB = currentFormula;
            }
            else {
                phiSortB = new cnf.CNFFormula(phiSortB.getClauses(),currentFormula.getClauses());
            }

            previous=current;

        }

        for(int i=0;i<n;i++){
            formula.variables.Variable x_i = new formula.variables.InputVariable(b,i,d);
            formula.variables.Variable b_i = new formula.variables.InputVariable("b",b,i,-2);
            cnf.CNFFormula currentFormula =cnf.CNFFormula.convert(formula.EquivFormula.convert(x_i,b_i));
            phiSortB = new cnf.CNFFormula(phiSortB.getClauses(),currentFormula.getClauses());
        }


        return phiSortB;
    }
    private cnf.CNFFormula phi(int l, List<formula.variables.Variable> x, List<formula.variables.Variable> y) {
        int n = network.getWires();
        cnf.CNFFormula phi = null;
        for(int i = 0;i<n-1;i++){
            for(int j=i+1;j<n;j++){

                formula.Formula secondPartFormula = new formula.AndFormula(formula.EquivFormula.convert(y.get(i),new formula.AndFormula(x.get(i),x.get(j)))
                        ,formula.EquivFormula.convert(y.get(j),new formula.OrFormula(x.get(i),x.get(j))));
                cnf.CNFFormula currentFormula = cnf.CNFFormula.convert(formula.ImpliesFormula.convert(comparatorVariables[l][i][j],secondPartFormula));

                if(phi==null){
                    phi = currentFormula;
                }
                else {
                    phi = new cnf.CNFFormula(phi.getClauses(),currentFormula.getClauses());
                }

            }
        }

        for(int k=0;k<n;k++){
            cnf.CNFFormula b = cnf.CNFFormula.convert(formula.ImpliesFormula.convert(new formula.NotFormula(usedWires[l][k]),formula.EquivFormula.convert(x.get(k),y.get(k))));
            phi = new cnf.CNFFormula(phi.getClauses(),b.getClauses());
        }

        return phi;
    }



    private formula.Formula orOfList(List<formula.variables.ComparatorVariable> incident) {
        formula.Formula previousFormula = null;
        for(formula.variables.ComparatorVariable variable:incident){
            if(previousFormula==null){
                previousFormula= variable;
            }
            else {
                previousFormula = new formula.OrFormula(previousFormula,variable);
            }
        }
        return previousFormula;
    }
     */
    private int n;
    private int d;
    private Variable[][][] comparatorsVariables;
    private Variable[][][] subLayerVariables;

    public PropositionGenerator(int n, int d) {
        this.n = n;
        this.d = d;
        createComparatorVariables();
        createSubLayerVariables();
    }

    private void createSubLayerVariables() {
        int pow = (int) Math.pow(2,n);
        subLayerVariables = new Variable[d+1][n][pow];
        for(int k=0;k<=d;k++){
            for(int i=1;i<n;i++){
                for (int m=0;m<pow;m++){
                    subLayerVariables[k][i][m] = new SubLayerVariable(k,i,m);
                }
            }
        }
    }
    private void createComparatorVariables() {
        comparatorsVariables = new Variable[d+1][n+1][n+1];
        for(int k=1;k<=d;k++){
            for(int i=1;i<n;i++){
                for(int j=i+1;j<=n;j++){
                    comparatorsVariables[k][i][j] = new ComparatorVariable(k,i,j);
                }
            }
        }
    }

    public CNFFormula getProposition(){
        CNFFormula cnfFormula = new CNFFormula();
        cnfFormula.addClauses(getValidDepth().getClauses());
        cnfFormula.addClauses(getForwardDepth().getClauses());
        cnfFormula.addClauses(getAllInputs().getClauses());
        cnfFormula.addClauses(getNoUnsortedOutputs().getClauses());

        return cnfFormula;
    }

    public CNFFormula getValidDepth(){
        CNFFormula valid = null;
        for(int k = 1; k<=d;k++){
            for(int i=1;i<=n;i++){
                CNFFormula current = atMostOneDepth(k,i);
                if(valid==null){
                    valid = current;
                }
                else {
                    valid = new CNFFormula(valid.getClauses(),current.getClauses());
                }
            }
        }
        return valid;
    }
    private CNFFormula atMostOneDepth(int k, int i) {
        CNFFormula atMostOneDepth= new CNFFormula();
        for(int j=1;j<=n;j++){
            if(i==j){continue;}
            for(int l=1;l<=n;l++){
                if(l==i||l==j){continue;}
                Variable firstVariable = comparatorsVariables[k][Math.min(i,j)][Math.max(i,j)].opposite();

                Variable secondVariable = comparatorsVariables[k][Math.min(i,l)][Math.max(i,l)].opposite();

                Clause clause = new Clause();
                clause.addVariable(firstVariable);
                clause.addVariable(secondVariable);

                CNFFormula current = new CNFFormula(clause);
                atMostOneDepth.addClauses(current.getClauses());
            }
        }
        return atMostOneDepth;
    }

    public CNFFormula getForwardDepth(){
        CNFFormula forwardDepth = new CNFFormula();
        int pow = (int) Math.pow(2,n);
        for(int k=1;k<=d;k++){
            for(int i=1;i<n;i++){
                for (int m=0;m<pow;m++){
                    forwardDepth.addClauses(getFwdSubLayerUpdate(k,i,m).getClauses());
                }
            }
        }
        return forwardDepth;
    }
    private CNFFormula getFwdSubLayerUpdate(int k, int i, int m) {
        CNFFormula fwdSubLayerUpdate = new CNFFormula();

        Formula subLayer = getSubLayer(i,k);
        Formula fwd0 = getFwd0(k,i,m);
        CNFFormula firstPart = CNFFormula.convert(new OrFormula(subLayer,fwd0));
        fwdSubLayerUpdate.addClauses(firstPart.getClauses());

        for(int j=i+1;j<=n;j++){
            Formula fwd = getFwd(k,i,j,m);
            CNFFormula secondPart = CNFFormula.convert(new OrFormula(comparatorsVariables[k][i][j].opposite(),fwd));
            fwdSubLayerUpdate.addClauses(secondPart.getClauses());
        }
        return fwdSubLayerUpdate;
    }
    private Formula getFwd(int k, int i, int j, int m) {
        Formula fwd = null;
        int pow = (int) Math.pow(2,n);

        //TRY to find w such that m = c i,j ( w )
        int w = -1;
        for(int wi=0;wi<pow;wi++) {
            Sequence s = Sequence.getInstance(n, wi);
            assert s != null;
            s.swap(i - 1, j - 1);
            if (Sequence.getInstance(n, m).equals(s)) {
                w = wi;
                break;
            }
        }

        boolean mRemainsSame =false;
        Sequence s = Sequence.getInstance(n, m);
        assert s != null;
        mRemainsSame = !s.areSwappable(i-1,j-1);

        if(i==1){
            if(w!=-1){
                return EquivFormula.convert(subLayerVariables[k][i][m],
                        new OrFormula(subLayerVariables[k-1][n-1][m],subLayerVariables[k-1][n-1][w]));
            }
            if(mRemainsSame){
                    return EquivFormula.convert(subLayerVariables[k][i][m], subLayerVariables[k - 1][n - 1][m]);
            }
        }
        if(i>1){
            if(w!=-1) {
                return EquivFormula.convert(subLayerVariables[k][i][m],
                        new OrFormula(subLayerVariables[k][i-1][m],subLayerVariables[k][i-1][w]));
            }
            if(mRemainsSame){
                return EquivFormula.convert(subLayerVariables[k][i][m], subLayerVariables[k][i - 1][m]);
            }
        }
        return subLayerVariables[k][i][m].opposite();
    }
    private Formula getFwd0(int k, int i, int m) {
        if(i==1){
            return EquivFormula.convert(subLayerVariables[k][i][m],subLayerVariables[k-1][n-1][m]);
        }
        else {
            return EquivFormula.convert(subLayerVariables[k][i][m],subLayerVariables[k][i-1][m]);
        }
    }
    private Formula getSubLayer(int i, int k) {
        Formula formula =null;
        for(int j=i+1;j<=n;j++){
            Variable current = comparatorsVariables[k][i][j];
            if(formula==null){
                formula=current;
            }
            else {
               formula = new OrFormula(formula,current);
            }
        }
        return  formula;

    }


    public CNFFormula getAllInputs(){
        CNFFormula all = new CNFFormula();
        int pow = (int) Math.pow(2,n);
        for(int m=0;m<pow;m++){
            Clause clause=new Clause();
            clause.addVariable(subLayerVariables[0][n-1][m]);
            all.add(clause);
        }
        return all;
    }
    public CNFFormula getNoUnsortedOutputs(){
        CNFFormula all = new CNFFormula();
        int pow = (int) Math.pow(2,n);
        for(int m=0;m<pow;m++){
            if(Sequence.getInstance(n,m).isSorted()){
                continue;
            }
            Clause clause=new Clause();
            clause.addVariable(subLayerVariables[d][n-1][m].opposite());
            all.add(clause);
        }
        return all;
    }
}
