package lex_analize;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Init {
    public static Set<Character> sigma = new HashSet<>(), allCharacters = new HashSet<>();
     static {
         Scanner sc = null;
         try {
             sc = new Scanner(new File("src/lex_analize/lex_config.txt"));
         } catch (FileNotFoundException e) {
             throw new RuntimeException(e);
         }
         String string = sc.nextLine();
        for (int i = 0; i < string.length(); i++) {
            sigma.add(string.charAt(i));
        }
    }
}
