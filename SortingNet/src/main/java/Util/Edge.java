package Util;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Edge {
    protected Integer from;
    protected Integer to;

    public Edge(Integer from, Integer to) {
        this.from = from;
        this.to = to;
    }

    public Integer getFrom() {
        return from;
    }
    public Integer getTo() {
        return to;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }
    public void setTo(Integer to) {
        this.to = to;
    }
    public void reverse(){
        Integer aux = this.from;
        this.from=this.to;
        this.to=aux;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge edge = (Edge) o;
        return (getFrom().equals(edge.getFrom()) &&
                getTo().equals(edge.getTo()) )||(getFrom().equals(edge.getTo()) && getTo().equals(edge.getFrom()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFrom(), getTo()) + Objects.hash(getTo(),getFrom());
    }

    @Override
    public String toString() {
        return "Edge{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
