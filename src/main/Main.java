package main;

import logic.TexasHoldem;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1500, 1000));

        TexasHoldem texasHoldem = new TexasHoldem();

        frame.add(texasHoldem.getTexasHoldemPanel());

        frame.setVisible(true);
        frame.pack();
    }
}
