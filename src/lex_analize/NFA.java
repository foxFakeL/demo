package lex_analize;

import util.Pair;

import java.util.Stack;

public class NFA {
    public Node head, back;

    public static NFA add(NFA a, NFA b) {
        a.back.edges.add(new Pair<String, Node>("", b.head));
        return new NFA(a.head, b.back);
    }

    public static NFA or(NFA a, NFA b) {
        Node head = new Node(), back = new Node();
        head.edges.add(new Pair<String, Node>("", a.head));
        head.edges.add(new Pair<String, Node>("", b.head));
        a.back.edges.add(new Pair<String, Node>("", back));
        b.back.edges.add(new Pair<String, Node>("", back));
        return new NFA(head, back);
    }
    public static NFA star(NFA a) {
        a.back.edges.add(new Pair<>("", a.head));
        return a;
    }
    public NFA(Node head, Node back) {
        this.head = head;
        this.back = back;
    }
    public static NFA generateNFA(String pre){
        Stack<NFA> nfaStack = new Stack<>();
        for(var i: pre.toCharArray()){
            if (Init.sigma.contains(i)) {
                Init.allCharacters.add(i);
                Node head = new Node(), back = new Node();
                head.edges.add(new Pair<>(String.valueOf(i), back));
                nfaStack.push(new NFA(head, back));
            } else {
                switch (i) {
                    case '|':
                        nfaStack.push(or(nfaStack.pop(), nfaStack.pop()));
                        break;
                    case '*':
                        nfaStack.push(star(nfaStack.pop()));
                        break;
                    case '&':
                        var a = nfaStack.pop();
                        var b = nfaStack.pop();
                        nfaStack.push(add(b, a));
                        break;
                }
            }
        }
        var t = nfaStack.pop();
        t.back.isAccept = true;
        return t;
    }
}
