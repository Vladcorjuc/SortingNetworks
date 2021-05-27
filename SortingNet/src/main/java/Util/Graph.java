package Util;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Graph {

    private Set<Integer> V;
    private Set<Edge> E;
    private Matching perfectMatching=null;
    private Map<Integer,List<Integer>> neighbours=null;
    private Map<Integer,Integer> degrees=null;

    //region Constructors
    public Graph() {

        this.V=new HashSet<>();
        this.E=new HashSet<>();
    }
    public Graph(@NotNull Set<Integer> V, Set<Edge> E){
        this.V=V;
        this.E=E;
    }
    public Graph(Graph G){
        this.V = new HashSet<>(G.getVertices());

        this.E = new HashSet<>(G.getEdges());
        this.neighbours=new HashMap<>(G.getNeighbours());
        this.degrees=new HashMap<>(G.getDegrees());
        this.perfectMatching=null;
    }
    //endregion
    //region Getters
    public Set<Edge>  getEdges() {
        return E;
    }
    public Set<Integer> getVertices(){
        return V;
    }

    public Map<Integer,List<Integer>> getNeighbours(){
        if(neighbours==null||neighbours.size()==0){
            neighbours=new HashMap<>();

            for(Integer v:V){
               neighbours.put(v,new ArrayList<Integer>());
            }
            for(Edge e:E){

                int v = e.getFrom();
                int u = e.getTo();

                neighbours.get(v).add(e.getTo());
                neighbours.get(u).add(e.getFrom());

            }
        }
        return neighbours;
    }
    public List<Integer> getNeighbours(int node) {

        return this.getNeighbours().get(node);
    }

    public Map<Integer, Integer> getDegrees(){
        if(degrees==null||degrees.size()==0){
            degrees=new HashMap<>();
            for(int v:V){
                degrees.put(v,getNeighbours(v).size());
            }
        }
        return degrees;
    }
    public Integer getDegree(int node){
        return getDegrees().get(node);
    }

    public Matching getPerfectMatching(){
        if(perfectMatching==null){
            perfectMatching=fordFulkerson();
        }
        return perfectMatching;
    }
    public boolean hasPerfectMatching(){
        if(perfectMatching==null){
            perfectMatching=fordFulkerson();
        }
        return perfectMatching.size() == V.size()/2;
    }

    //endregion
    //region Setters
    private void addEdge(Edge edge) {
        this.E.add(edge);
        getNeighbours(edge.getFrom()).add(edge.getTo());
        getNeighbours(edge.getTo()).add(edge.getFrom());
        getDegrees().put(edge.getFrom(),getDegree(edge.getFrom())+1);
        getDegrees().put(edge.getTo(),getDegree(edge.getTo())+1);

    }
    private void removeEdge(Edge edge) {
        this.E.remove(edge);
        getNeighbours(edge.getFrom()).remove(edge.getTo());
        getNeighbours(edge.getTo()).remove(edge.getFrom());
        getDegrees().put(edge.getFrom(),getDegree(edge.getFrom())-1);
        getDegrees().put(edge.getTo(),getDegree(edge.getTo())-1);
    }
    //endregion
    //region Algorithms
    public int[] findCycle(Graph G, Matching matching) {
        int n = G.getVertices().size()/2;
        for (int u=1;u<=n;u++) {
            if(!V.contains(u)){
                continue;
            }
            if (G.getDegree(u)< 2) {
                continue;
            }

            Map<Integer,Integer> visited=new HashMap<>();
            for(int v:G.getVertices()){
                visited.put(v,0);
            }
            visited.put(u,1);

            int[] cycle= findCycleRec(G, matching, u, 1, visited);
            if (cycle != null) {
                return cycle;
            }
        }
        return null;
    }
    private int[] findCycleRec(Graph G, Matching matching, int node, int pos, Map<Integer,Integer> visited) {
        int n = G.getVertices().size()/2;
        int set = (pos-1)%2;
        if (set == 1) {
            for (Integer neighbour : G.getNeighbours(node)) {
                if (matching.getEdgeSet().contains(new Edge(node, neighbour))
                        || visited.get(neighbour) <= 0) {
                    continue;
                }

                int first = visited.get(neighbour);
                int[] cycle = new int[pos - first + 1];
                for (int i = 1; i <= n; i++) {
                    if (visited.get(i) >= first) {
                        cycle[visited.get(i) - first] = i;
                    }
                    if (visited.get(i+n) >= first) {
                        cycle[visited.get(i+n) - first] = i+n;
                    }
                }
                return cycle;


            }
        }

        List<Integer> neighboursCopy = new ArrayList<>(G.getNeighbours(node));
        for(Integer neighbour:neighboursCopy){
            if( visited.get(neighbour) < 0 ||G.getDegree(neighbour)<2
                    || (set==1 && matching.getEdgeSet().contains(new Edge(node,neighbour)))
                    || (set==0 && !matching.getEdgeSet().contains(new Edge(node,neighbour)))){
                continue;
            }

            visited.put(neighbour,pos+1);
            G.removeEdge(new Edge(node,neighbour));
            int[] cycle = findCycleRec(G,matching,neighbour,pos+1,visited);
            G.addEdge(new Edge(node,neighbour));
            if(cycle!=null){
                return cycle;
            }
            visited.put(neighbour,-1);

        }
        return null;
    }

    private Matching fordFulkerson(){
        int n=V.size()/2;

        int[] match = new int[n+1];
        for(int i = 0; i <= n; ++i)
            match[i] = -1;

        int result = 0;
        for (int u = 1; u <= n; u++){
            boolean[] seen =new boolean[n+1];
            for(int i = 1; i <= n; ++i)
                seen[i] = false;
            if (bpm(u, seen, match)){
                result++;
            }
        }
        Matching matching=new Matching();
        for(int i=1;i<=n;i++){
            if(match[i]==-1){
                continue;
            }

            matching.add(new  Edge(match[i],i+n));
        }
        return matching;

    }
    private boolean bpm(int u, boolean[] seen, int[] matchR) {
        int n=V.size()/2;
        for (int v = n+1; v <= 2*n; v++) {
            if (E.contains(new Edge(u,v)) && !seen[v-n]){
                seen[v-n] = true;

                if (matchR[v-n] < 0 || bpm(matchR[v-n], seen, matchR)) {
                    matchR[v-n] = u;
                    return true;
                }
            }
        }
        return false;
    }
    //endregion
    //region Graphs for Subsumption
    public static Graph getDecompositionGraph(Graph G,Matching M){
        Graph decompositionGraph = new Graph();
        decompositionGraph.V=G.V;


        Set<Edge> E = new HashSet<>(G.E);
        E.removeAll(M.getEdgeSet());

        for (Edge e:E) {
            e.reverse();
        }

        E.addAll(M.getEdgeSet());

        decompositionGraph.E = E;
        //decompositionGraph.neighbours = G.neighbours;
        decompositionGraph.perfectMatching=M;

        return decompositionGraph;
    }
    public static Graph getGraphPlus(Graph G, Edge edge){
        Graph GPlus = new Graph(G);
        Set<Edge> edgeSet=new HashSet<>();

        for(Edge e: G.getEdges()){
            if(e.getFrom().equals(edge.getFrom()) || e.getFrom().equals(edge.getTo())
            || e.getTo().equals(edge.getFrom()) || e.getTo().equals(edge.getTo())){
               continue;
            }
            edgeSet.add(e);
        }
        GPlus.E=edgeSet;
        return GPlus;
    }
    public static Graph getGraphMinus(Graph G, Edge edge){
        Graph GMinus = new Graph(G);
        GMinus.removeEdge(edge);
        return GMinus;
    }
    //endregion


    @Override
    public String toString() {
        return "Graph{" +
                "V=" + V +
                ", E=" + E +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Graph)) return false;
        Graph graph = (Graph) o;
        return V.equals(graph.V) &&
                E.equals(graph.E) &&
                Objects.equals(getPerfectMatching(), graph.getPerfectMatching()) &&
                Objects.equals(getNeighbours(), graph.getNeighbours()) &&
                Objects.equals(getDegrees(), graph.getDegrees());
    }
    @Override
    public int hashCode() {
        return Objects.hash(V, E, getPerfectMatching(), getNeighbours(), getDegrees());
    }
}
