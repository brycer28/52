package graphics;

import logic.TexasHoldem;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import logic.*;
import cards.*;

public class TexasHoldemPanel extends JPanel {
    private TexasHoldem logic;
    private ArrayList<JComponent> options;
    JPanel playerHandPanel = new JPanel();
    JPanel dealerHandPanel = new JPanel();
    JPanel communityCardsPanel= new JPanel();
    JPanel optionsPanel = new JPanel();
    JPanel statsPanel = new JPanel();
    JPanel quitPanel = new JPanel();
    JPanel quitButton = new JPanel();
    final int CARD_WIDTH = 100;
    final int GAME_WIDTH = 1500;
    final int GAME_HEIGHT = 1000;
    final int HAND_WIDTH = 220;
    final int HAND_HEIGHT = 150;
    final int CC_WIDTH = 550;
    final int CC_HEIGHT = 150;
    final int BUTTON_WIDTH = 50;
    final int BUTTON_HEIGHT = 20;
    final int OPT_WIDTH = 500;
    final int OPT_HEIGHT = 70;
    final int STATS_WIDTH = 120;
    final int STATS_HEIGHT = 200;
    final int LABEL_WIDTH = 100;
    final int LABEL_HEIGHT = 40;
    final int QUIT_WIDTH = 100;
    final int QUIT_HEIGHT = 30;

    public TexasHoldemPanel(TexasHoldem logic) {
        super();
        this.logic = logic;

        // initialize game panel
        setLayout(null);
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setBackground(new Color(0, 100, 0));

        // create and add hand panels and community cards
    }

    public List<JPanel> initHands() {
        playerHandPanel.setBounds((GAME_WIDTH/2-HAND_WIDTH/2), 600, HAND_WIDTH, HAND_HEIGHT);
        playerHandPanel.setBackground(new Color(0, 150, 0));
        add(playerHandPanel);

        dealerHandPanel.setBounds((GAME_WIDTH/2-HAND_WIDTH/2), 200, HAND_WIDTH, HAND_HEIGHT);
        dealerHandPanel.setBackground(new Color(0, 150, 0));
        add(dealerHandPanel);

        communityCardsPanel.setBounds((GAME_WIDTH/2-CC_WIDTH/2), 400, CC_WIDTH, CC_HEIGHT);
        communityCardsPanel.setBackground(new Color(0, 150, 0));
        add(communityCardsPanel);

        return Arrays.asList(playerHandPanel, dealerHandPanel, communityCardsPanel);
    }

    public JPanel initOptions() {
        options = new ArrayList<>();

        // create check button
        JButton checkButton = new JButton("Check");
        checkButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        checkButton.addActionListener(e -> {
            //logic.setPlayerDecision(0);
        });

        // create call button
        JButton callButton = new JButton("Call");
        callButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        callButton.addActionListener(e -> {
            //logic.setPlayerDecision(1);
        });

        // create fold button
        JButton foldButton = new JButton("Fold");
        foldButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        foldButton.addActionListener(e -> {
            //logic.setPlayerDecision(3);
        });

        // create raise field and button to submit
        JTextField raiseField = new JTextField(5);
        raiseField.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        // create submit raise field
        JButton raiseButton = new JButton("Raise");
        raiseButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        raiseButton.addActionListener(e -> {
            String raiseAmt = raiseField.getText();
            try {
                int raiseInt = Integer.parseInt(raiseAmt);
                if (raiseInt < 0) {
                    JOptionPane.showMessageDialog(null, "You can't raise a negative number", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    //logic.setRaiseAmount(raiseInt);
                    //logic.setPlayerDecision(2);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "You can't raise a negative number", "Error", JOptionPane.ERROR_MESSAGE);
            }
            raiseField.setText("");
        });

        options.add(checkButton);
        options.add(callButton);
        options.add(foldButton);
        options.add(raiseField);
        options.add(raiseButton);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(1, 5, 20, 0));
        for (JComponent option : options) {
            optionsPanel.add(option);
        }

        return optionsPanel;
    }

//    public void updateHands() {
//        SwingUtilities.invokeLater(() -> {
//            playerHandPanel.removeAll();
//            dealerHandPanel.removeAll();
//
//            for (Card card : logic.getPlayerHand()) {
//                card.setFaceUp();
//                CardPanel cardPanel = new CardPanel();
//            }
//        })
//    }
}

