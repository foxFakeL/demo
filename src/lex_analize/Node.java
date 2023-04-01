package lex_analize;

import java.util.ArrayList;
import java.util.List;

import util.Pair;

public class Node {
    public List<Pair<String, Node>> edges;
    public boolean isAccept;
    public static int NUM = 0;
    public int sequence;
    public String toString(){
        return String.valueOf(sequence);
    }
    public int compareTo(Node o){
        return sequence - o.sequence;
    }
    public Node() {
        edges = new ArrayList<>();
        isAccept = false;
        sequence = NUM++;
    }
    public Node(Node _n) {
        edges = new ArrayList<>();
        isAccept = _n.isAccept;
        sequence = NUM++;
        for (var edge : _n.edges) {
            edges.add(new Pair<>(edge.first, edge.second));
        }
    }
}
