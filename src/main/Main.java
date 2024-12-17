package main;

import cards.*;
import graphics.CardPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1500, 1000));

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.gray);

        CardPanel cardPanel = new CardPanel(new Card(Card.Suit.SPADES, Card.Rank.ACE), 100);

        mainPanel.add(cardPanel);
        frame.add(mainPanel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
