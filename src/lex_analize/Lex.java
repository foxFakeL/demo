package lex_analize;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Lex {
    public static Map<String, DFA> m = new HashMap<>();
    public static Map<String, String> mss = new HashMap<>();

    private static String turn(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '[') {
                sb.append('(');
                i++;
                StringBuilder sb1 = new StringBuilder();
                while (str.charAt(i) != ']') {
                    sb1.append(str.charAt(i));
                    i++;
                }
                String s = mss.get(sb1.toString());
                sb.append(s).append(')');
            } else {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }

    private static String combine(String name, String value) {
        return '<' + name + ": " + value + '>';
    }

    public Lex(String path) throws FileNotFoundException {
        if (m.isEmpty() || mss.isEmpty()) {
            Scanner scanner = new Scanner(new File("src/lex_analize/lex.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] split = line.split(":");
                String name = split[0];
                String pre = turn(split[1]);
                mss.put(name, pre);
                String suffix = SuffixExpressions.suffix(pre);
                NFA nfa = NFA.generateNFA(suffix);
                DFA dfa = new DFA(nfa);
                m.put(name, dfa);
            }
        }
        Scanner input = new Scanner(new File(path));
        while (input.hasNextLine()) {
            String[] line = input.nextLine().split(" ");
            for (String s : line) {
                if (Check.isRight(s, m.get("reserved"))) {
                    System.out.print(combine("reserved", s) + " ");
                    continue;
                }
                for (var key : m.keySet()) {
                    if (Check.isRight(s, m.get(key))) {
                        System.out.print(combine(key, s) + " ");
                        break;
                    }
                }
            }
        }
    }
}
