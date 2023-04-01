package lex_analize;

import util.Pair;

import java.util.*;

public class DFA {
    static class Row {
        public Set<Node> first;
        public Set<Node>[] second;

        public Row() {
            second = new Set[Init.allCharacters.size()];
            for (int i = 0; i < second.length; i++) {
                second[i] = new HashSet<>();
            }
        }
    }

    private static void getEnclouser(Node root, Set<Node> enclouser) {
        if (enclouser.contains(root)) return;
        enclouser.add(root);
        for (var edge : root.edges) {
            if (edge.first.equals("")) {
                getEnclouser(edge.second, enclouser);
            }
        }
    }

    private static void getEnclouser(Set<Node> root, Set<Node> enclouser) {
        for (var node : root) {
            getEnclouser(node, enclouser);
        }
    }

    private static void getEnclouser(Node root, Set<Node> enclouser, String str) {
        for (var edge : root.edges) {
            if (edge.first.equals(str)) {
                enclouser.add(edge.second);
            }
        }
    }

    private static void getEnclouser(Set<Node> root, Set<Node> enclouser, String str) {
        for (var node : root) {
            getEnclouser(node, enclouser, str);
        }
    }

    public List<Row> DFAmap;

    private void dfs(Set<Node> root, Set<Set<Node>> all, char[] allCharacters) {
        Row row = new Row();
        row.first = Set.copyOf(root);
        for (int i = 0; i < allCharacters.length; i++) {
            Set<Node> t = new HashSet<>();
            getEnclouser(root, t, String.valueOf(allCharacters[i]));
            getEnclouser(t, row.second[i]);
            if (!all.contains(row.second[i]) && !row.second[i].isEmpty()) {
                all.add(row.second[i]);
                dfs(row.second[i], all, allCharacters);
            }
        }
        DFAmap.add(row);
    }

    public Set<Node> head;
    public List<Set<Node>> end;
    public char[] cstr;

    public Node[] DFANodes, headNode, endNode;
    public DFA(Node[] dfaNodes, Node[] headNode, Node[] endNode, char[] cstr) {
        DFANodes = dfaNodes;
        this.headNode = headNode;
        this.endNode = endNode;
        this.cstr = cstr;
    }
    public DFA(NFA nfa) {
        DFAmap = new ArrayList<>();
        Set<Set<Node>> all = new HashSet<>();
        head = new HashSet<>();
        getEnclouser(nfa.head, head);
        all.add(head);
        var t = Init.allCharacters.toArray();
        cstr = new char[t.length];
        for (int i = 0; i < t.length; i++) {
            cstr[i] = (char) t[i];
        }
        dfs(head, all,cstr);
        end = new ArrayList<>();
        for (var row : DFAmap) {
            for (var node : row.first) {
                if (node.isAccept) {
                    end.add(row.first);
                    break;
                }
            }
        }
        DFANodes = new Node[DFAmap.size()];
        headNode = new Node[1];
        endNode = new Node[end.size()];
        Node.NUM = 0;
        Map<Set<Node>, Node> map = new HashMap<>();
        for (int i = 0; i < DFANodes.length; i++) {
            DFANodes[i] = new Node();
            map.put(DFAmap.get(i).first, DFANodes[i]);
        }
        for (int i=0;i< headNode.length;i++){
            headNode[i] = map.get(head);
        }
        for (int i=0;i< endNode.length;i++){
            endNode[i] = map.get(end.get(i));
            endNode[i].isAccept = true;
        }
        for (int i = 0; i < DFANodes.length; i++) {
            for (int j = 0; j < cstr.length; j++) {
                DFANodes[i].edges.add(new Pair<>(String.valueOf(cstr[j]), map.get(DFAmap.get(i).second[j])));
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("_|");
        for (var i : cstr) {
            sb.append(i).append("|");
        }
        sb.append('\n');
        sb.append("-|".repeat(Init.allCharacters.size()+1));
        sb.append('\n');
        for(var t:DFANodes){
            sb.append(t).append("|");
            for (var edge : t.edges) {
                sb.append(edge.second).append("|");
            }
            sb.append('\n');
        }
        sb.append("begin:").append(Arrays.toString(headNode)).append('\n');
        sb.append("end:").append(Arrays.toString(endNode)).append('\n');
        return sb.toString();
    }
}
