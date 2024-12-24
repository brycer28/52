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
    JButton quitButton = new JButton("Quit");
    final int CARD_WIDTH = 100;
    final int GAME_WIDTH = 1500;
    final int GAME_HEIGHT = 1000;
    final int HAND_WIDTH = 220;
    final int HAND_HEIGHT = 160;
    final int CC_WIDTH = 550;
    final int CC_HEIGHT = 150;
    final int BUTTON_WIDTH = 50;
    final int BUTTON_HEIGHT = 20;
    final int OPT_WIDTH = 500;
    final int OPT_HEIGHT = 70;
    final int STATS_WIDTH = 140;
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

        List<JPanel> handPanels = initHands();
        for (JPanel panel : handPanels) {
            add(panel);
        }

        optionsPanel = initOptions();
        optionsPanel.setBounds((GAME_WIDTH/2-OPT_WIDTH/2), 40, OPT_WIDTH, OPT_HEIGHT);
        optionsPanel.setBackground(new Color(0, 100, 0));
        add(optionsPanel);

        statsPanel.setBounds(20, 20, STATS_WIDTH, STATS_HEIGHT);
        statsPanel.setBackground(new Color(0, 150, 0));
        add(statsPanel);
        updateStats();

        quitPanel.setBounds(25, 40 + STATS_HEIGHT, QUIT_WIDTH, QUIT_HEIGHT);
        quitPanel.setBackground(new Color(0, 100, 0));
        quitButton.setPreferredSize(new Dimension(QUIT_WIDTH, QUIT_HEIGHT));
        quitButton.addActionListener(e -> { endGame(); });
        quitPanel.add(quitButton);
        add(quitPanel);
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
            logic.setPlayerOption(TexasHoldem.Options.CHECK);
        });

        // create call button
        JButton callButton = new JButton("Call");
        callButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        callButton.addActionListener(e -> {
            logic.setPlayerOption(TexasHoldem.Options.CALL);
        });

        // create fold button
        JButton foldButton = new JButton("Fold");
        foldButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        foldButton.addActionListener(e -> {
            logic.setPlayerOption(TexasHoldem.Options.FOLD);
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
                    logic.setRaiseAmount(raiseInt);
                    logic.setPlayerOption(TexasHoldem.Options.RAISE);
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

    public void updateStats() {
        SwingUtilities.invokeLater(() -> {
            statsPanel.removeAll();
            statsPanel.setLayout(new GridLayout(5,1,20,0));

            JLabel potLabel = new JLabel("Current Pot: " + logic.getPot());
            potLabel.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
            statsPanel.add(potLabel);

            JLabel betLabel = new JLabel("Current Bet: " + logic.getCurrentBet());
            betLabel.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
            statsPanel.add(betLabel);

            JLabel playerChipsLabel = new JLabel("Player Chips: " + logic.getPlayerChips());
            playerChipsLabel.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
            statsPanel.add(playerChipsLabel);

            JLabel dealerChipsLabel = new JLabel("Dealer Chips: " + logic.getDealerChips());
            dealerChipsLabel.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
            statsPanel.add(dealerChipsLabel);

            JLabel dealerDecisionLabel = new JLabel("Dealer Option: " + logic.getDealerOption());
            dealerDecisionLabel.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
            statsPanel.add(dealerDecisionLabel);

            repaint();
            revalidate();
        });
    }

    public void updateHands() {
        SwingUtilities.invokeLater(() -> {
            playerHandPanel.removeAll();
            dealerHandPanel.removeAll();

            for (Card card : logic.getPlayerHand()) {
                card.setFaceUp(true);
                CardPanel cardPanel = new CardPanel(card, CARD_WIDTH);
                playerHandPanel.add(cardPanel);
            }

            for (Card card : logic.getDealerHand()) {
                card.setFaceUp(true);
                CardPanel cardPanel = new CardPanel(card, CARD_WIDTH);
                dealerHandPanel.add(cardPanel);
            }

            repaint();
            revalidate();
        });
    }

    public void updateCommunityCards() {
        SwingUtilities.invokeLater(() -> {
            communityCardsPanel.removeAll();

            for (Card card : logic.getCommunityCards()) {
                card.setFaceUp(true);
                CardPanel cardPanel = new CardPanel(card, CARD_WIDTH);
                communityCardsPanel.add(cardPanel);
            }

            repaint();
            revalidate();
        });
    }

    public void showDealerHand() {
        SwingUtilities.invokeLater(() -> {
            dealerHandPanel.removeAll();

            for (Card card : logic.getDealerHand()) {
                card.setFaceUp(true);
                CardPanel cardPanel = new CardPanel(card, CARD_WIDTH);
                dealerHandPanel.add(cardPanel);
            }

            repaint();
            revalidate();
        });
    }

    public void resetGUI() {
        playerHandPanel.removeAll();
        dealerHandPanel.removeAll();
        communityCardsPanel.removeAll();
        updateStats();
        updateHands();
        updateCommunityCards();

        repaint();
        revalidate();
    }

    public boolean displayReplayPrompt() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Would you like to play another round?",
                "Play Again?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return choice == JOptionPane.YES_OPTION;
    }

    public void displayPlayerFolded(boolean isPlayer) {
        if (isPlayer) {
            JOptionPane.showMessageDialog(this, "You Folded. Dealer Wins!");
        } else {
            JOptionPane.showMessageDialog(this, "Dealer Folded. You Win!");
        }
    }

    public void displayWinner(boolean isPlayer) {
        if (isPlayer) {
            JOptionPane.showMessageDialog(this, "You Won!");
        } else {
            JOptionPane.showMessageDialog(this, "Dealer Won!");
        }
    }

    public void endGame() {
        JFrame parentFrame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this);
        if (parentFrame != null) {
            parentFrame.dispose();
        } else {
            System.out.println("Frame not found :(");
        }
        System.exit(0);
    }


}

