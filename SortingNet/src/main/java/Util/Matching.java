package Util;

import java.util.HashSet;
import java.util.Set;

public class Matching {
    private Set<Edge> edgeSet;

    public Matching(){
        edgeSet=new HashSet<>();
    }
    public Matching(Set<Edge> _edgeSet){
        edgeSet=_edgeSet;
    }
    public Matching(Matching matching){
        this.edgeSet=new HashSet<>(matching.getEdgeSet());
    }


    public Set<Edge> getEdgeSet(){
        return edgeSet;
    }
    public Integer size(){
        return edgeSet.size();
    }

    public void add(Edge edge) {
        edgeSet.add(edge);
    }
    public void replace(Edge edge){
        Set<Edge> edgeSetCopy=new HashSet<>(getEdgeSet());
        for(Edge e:edgeSetCopy){
            if(e.getFrom().equals(edge.getFrom()) || e.getTo().equals(e.getFrom())){
                edgeSet.remove(e);
            }
        }
        edgeSet.add(edge);
    }




    @Override
    public String toString() {
        return "Matching{" +
                "edgeSet=" + edgeSet +
                '}';
    }



}
