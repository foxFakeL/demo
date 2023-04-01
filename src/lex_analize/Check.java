package lex_analize;

public class Check {
    public static boolean isRight(String s, DFA dfa) {
        var node = dfa.headNode[0];
        for (var i : s.toCharArray()) {
            for (var j : node.edges) {
                if (j.first.equals(String.valueOf(i))) {
                    node = j.second;
                    break;
                }
            }
            if (node == null) return false;
        }
        return node.isAccept;
    }
}
