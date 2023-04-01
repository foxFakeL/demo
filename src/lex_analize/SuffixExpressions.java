package lex_analize;

import java.util.Stack;

public class SuffixExpressions {
    static int priority(char c) {
        switch (c) {
            case '|':
            case '&':
                return 1;
            case '*':
                return 2;
            default:
                return 0;
        }
    }

    static int op_cmp(char op1, char op2) {
        if (op1 == '(' && op2 == ')') return 0;
        if (op1 == '(' || op2 == '(') return -1;
        if (op2 == ')' || op1 == ')') return 1;
        int t = priority(op1) - priority(op2);
        if (t >= 0)
            return 1;
        else
            return -1;
    }

    public static String suffix(String s) {
        char[] _s = s.toCharArray();
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < _s.length; i++) {
            if (i == 0)
                sBuilder.append(_s[i]);
            else {
                if (_s[i] == '|' || _s[i] == '*' || _s[i] == ')') {
                    sBuilder.append(_s[i]);
                } else {
                    if (_s[i - 1] == '(' || _s[i - 1] == '|')
                        sBuilder.append(_s[i]);
                    else {
                        sBuilder.append('&');
                        sBuilder.append(_s[i]);
                    }
                }
            }
        }
        s = sBuilder.toString();
        Stack<Character> op = new Stack<>();
        StringBuilder ret = new StringBuilder();
        op.add('(');
        s+=')';
        for (var i:s.toCharArray()) {
            if (Init.sigma.contains(i)) {
                ret.append(i);
            } else {
                while (op_cmp(op.peek(), i) > 0) {
                    ret.append(op.pop());
                }
                if (op_cmp(op.peek(), i) == 0) {
                    op.pop();
                } else {
                    op.push(i);
                }
            }
        }
        return ret.toString();
    }
}


