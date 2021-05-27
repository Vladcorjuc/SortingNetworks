import Util.Edge;
import Util.Graph;
import Util.Matching;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Subsumption {


    @NotNull
    public static Graph createGraph(Network C1, Network C2){
        Set<Integer> V=createVertices(C1);
        Set<Edge> E=createEdges(C1,C2);
        return new Graph(V,E);
    }
    @NotNull
    private static Set<Edge> createEdges(@NotNull Network C1, @NotNull Network C2) {
        Set<Edge> E =new HashSet<>();
        int n=C1.getWires();

        List<Set<Integer>> zerosC1=   C1.getAllZeros();
        List<Set<Integer>> zerosC2=   C2.getAllZeros();

        List<Set<Integer>> onesC1=    C1.getAllOnes();
        List<Set<Integer>> onesC2=    C2.getAllOnes();

        for(int i=1;i<=n;i++) {
            for(int j=1;j<=n;j++){
                boolean conditionZeros=true,conditionOnes=true;
                for(int p=0;p<=n;p++){
                    if(C1.getCluster(p).size()!=C2.getCluster(p).size()) {
                        continue;
                    }
                    if(zerosC1 == zerosC2 && zerosC1.get(p).contains(i) && !zerosC2.get(p).contains(j)) {
                        conditionZeros=false;
                    }
                    if(onesC1.size() == onesC2.size() && onesC1.get(p).contains(i) && !onesC2.get(p).contains(j)){
                        conditionOnes=false;
                    }
                    if(!conditionZeros || !conditionOnes){
                        break;
                    }
                }
                if(conditionOnes&&conditionZeros){
                    E.add(new Edge(i, j + n));
                }
            }
        }
        return E;

    }
    @NotNull
    private static Set<Integer> createVertices(@NotNull Network net) {
        Set<Integer> V=new HashSet<>();
        int n=net.getWires();
        for(int i=1;i<=n;i++){
            V.add(i);
            V.add(n+i);
        }
        return V;
    }

    public static boolean canSubsum(Network Ca,Network Cb){
        if(Ca.outputs().size()> Cb.outputs().size()){
            return false;
        }
        int n = Ca.getWires();
        for(int p=0;p<=n;p++){
            if(Ca.getCluster(p).size()>Cb.getCluster(p).size()){
                return false;
            }
            if(Ca.getOnes(p).size()>Ca.getOnes(p).size()||
            Ca.getZeros(p).size()>Cb.getZeros(p).size()){
                return false;
            }
        }
        return true;
    }

    public static List<Network> getSubsumedNetworks(Pair<Network,Graph> existentMatching) {
        List<Network> netList=new ArrayList<>();
        Graph G= existentMatching.getValue();
        //netList.add(existentMatching.getKey());

        Graph DG = Graph.getDecompositionGraph(G,G.getPerfectMatching());


        List<Network> toAdd = enumPerfectMatchingIter(DG, DG.getPerfectMatching());
        if(toAdd!=null) {
            netList.addAll(toAdd);
        }
        return netList;
    }
    private static List<Network> enumPerfectMatchingIter(Graph G, Matching matching){
        if(G.getEdges().size()==0){
            return null;
        }
        List<Network> netList = new ArrayList<>();

        int[] cycle = G.findCycle(G,matching);
        if(cycle==null){
            return  null;
        }

        Matching newMatching=new Matching(matching);

        int i = 1;
        while (i > 0) {
            int v = cycle[i];
            int w = i + 1 < cycle.length ? cycle[i + 1] : -1;
            if (w == -1) {
                w = cycle[0];
                i = 0;
            } else {
                i += 2;
            }
            newMatching.replace(new Edge(w,v));

        }


        if(newMatching.size()<matching.size()||matching.equals(newMatching)){
            return null;
        }

        Edge e = new Edge(cycle[0],cycle[1]);
        Graph GPlus = Graph.getGraphPlus(G,e);
        Graph GMinus = Graph.getGraphMinus(G,e);


        List<Network> minusList = enumPerfectMatchingIter(GMinus, newMatching);
        if(minusList!=null){
            netList.addAll(minusList);
        }

        List<Network> plusList = enumPerfectMatchingIter(GPlus, matching);
        if (plusList != null) {
            netList.addAll(plusList);
        }
        return netList;
    }

    public static void main(String[] args) {
        Network Ca=new Network(5);
        Network Cb=new Network(5);

        Ca.addComparator(new Comparator(1,2));
        Ca.addComparator(new Comparator(3,4));
        Ca.addComparator(new Comparator(2,4));
        Ca.addComparator(new Comparator(2,5));

        Cb.addComparator(new Comparator(1,2));
        Cb.addComparator(new Comparator(3,4));
        Cb.addComparator(new Comparator(1,4));
        Cb.addComparator(new Comparator(2,5));

        Graph g=createGraph(Ca,Cb);


    }
}
