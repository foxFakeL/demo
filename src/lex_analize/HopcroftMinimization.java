package lex_analize;

import util.Pair;

import java.util.*;

public class HopcroftMinimization {
    private static boolean contain(Node[] a, Node n) {
        for (var node : a) {
            if (node.compareTo(n) == 0) {
                return true;
            }
        }
        return false;
    }
    private static Set<Node> getSet(Set<Node> s, Set<Set<Node>> p) {
        for (var set : p) {
            if (set.containsAll(s)) {
                return set;
            }
        }
        return null;
    }
    public static DFA minimize(DFA dfa) {
        // 将状态分为接受状态和非接受状态
        Set<Node> acceptStates = new HashSet<>();
        Set<Node> nonAcceptStates = new HashSet<>();
        acceptStates.addAll(Arrays.asList(dfa.endNode));
        for (var node : dfa.DFANodes) {
            if (!acceptStates.contains(node)) {
                nonAcceptStates.add(node);
            }
        }
        Set<Set<Node>> P = new HashSet<>();
        P.add(acceptStates);
        P.add(nonAcceptStates);

        // 迭代划分
        while (true) {
            boolean flag = false;
            Set<Set<Node>> newP = new HashSet<>();
            for (var S : P) {
                for (int i = 0; i < dfa.cstr.length; i++) {
                    Set<Node> X = new HashSet<>();
                    for (var node : S) {
                        for (var edge : node.edges) {
                            if (edge.first.equals(String.valueOf(dfa.cstr[i]))) {
                                X.add(edge.second);
                            }
                        }
                    }
                    Set<Set<Node>> T = new HashSet<>();
                    for (var Y : P) {
                        Set<Node> intersection = new HashSet<>(Y);
                        intersection.retainAll(X);
                        Set<Node> difference = new HashSet<>(Y);
                        difference.removeAll(X);
                        if (!intersection.isEmpty() && !difference.isEmpty()) {
                            T.add(intersection);
                            T.add(difference);
                        } else {
                            T.add(Y);
                        }
                    }
                    if (T.size() < P.size()) {
                        flag = true;
                        newP = T;
                        break;
                    }
                }
                if (flag) {
                    break;
                }
            }
            if (!flag) {
                break;
            }
            P = newP;
        }

        // 构建新的DFA
        Map<Set<Node>, Node> map = new HashMap<>();
        Node[] newNodes = new Node[P.size()];
        Node[] headnode = new Node[1];
        List<Node> endnode = new ArrayList<>();
        int index = 0;
        for (var set : P) {
            Node newNode = new Node();
            for (var node : set) {
                if (node == dfa.headNode[0]) { // 头节点
                    headnode[0] = newNode;
                    break;
                }
                if (contain(dfa.endNode, node)) { // 尾节点
                    newNode.isAccept = true;
                    endnode.add(newNode);
                }
            }
            newNodes[index++] = newNode;
            map.put(set, newNode);
        }
        for (var i:P) {
            for (int j = 0;j<dfa.cstr.length;j++) {
                Set<Node> dst =new HashSet<>();
                for (var node : i) {
                    for (var edge : node.edges) {
                        if (edge.first.equals(String.valueOf(dfa.cstr[j]))) {
                            dst.add(edge.second);
                        }
                    }
                }
                if (!dst.isEmpty()) {
                    map.get(i).edges.add(new Pair<>(String.valueOf(dfa.cstr[j]), map.get(getSet(dst, P))));
                }
            }
        }
        return new DFA(newNodes, headnode,endnode.toArray(new Node[0]),dfa.cstr);
    }
}