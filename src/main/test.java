package main;

import cards.Card;
import cards.Deck;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class test {
    public static void main(String[] args) {
        printFiles("src/cardImages");
    }

    public static void printFiles(String packageName) {
        String packagePath = packageName.replace(".", File.separator);
        File packageDir = new File(packagePath);

        ArrayList<String> fileNames = new ArrayList<>();

        if (packageDir.exists() && packageDir.isDirectory()) {
            String[] files = packageDir.list();
            if (files != null) {
                for (String file : files) {
                        String cardName = file.substring(0, file.lastIndexOf("."));
                        fileNames.add(cardName);
                }
            } else {
                System.out.println("NONE FOUND");
            }
        } else {
            System.out.println("DIR NOT FOUND");
        }

//        for (String s : fileNames) {
//            System.out.println(s);
//        }

        ArrayList<String> cardNames = new ArrayList<>();

        Deck d = new Deck();
        for (Card c : d) {
            cardNames.add(c.toString());
        }

//        for (Card c : d) {
//           if (!(fileNames.contains(c))) {
//               System.out.println(c);
//           };
//        }

        for (String cardName : fileNames) {
            if (!(cardNames.contains(cardName))) {
                System.out.println(cardName);
            }
        }

        System.out.println(listsEqual(fileNames, cardNames));
    }

    public static boolean listsEqual(ArrayList<String> list1, ArrayList<String> list2) {
        return new HashSet<>(list1).equals(new HashSet<>(list2));
    }
}
