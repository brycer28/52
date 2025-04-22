package graphics;

import cards.Hand;
import graphics.panels.StatsPanel;
import graphics.panels.gameplay.CardPanel;
import logic.GameControl;

import logic.GameState;
import account.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import cards.Card;

public class TexasHoldemPanel extends JPanel {
    private JPanel playerHandPanel = new JPanel();
    private JPanel communityCardsPanel;
    private StatsPanel statsPanel;

    private JPanel optionsPanel;
    private JButton checkButton = new JButton("Check");
    private JButton callButton = new JButton("Call");
    private JButton foldButton = new JButton("Fold");
    private JButton raiseButton = new JButton("Raise");

    private GameControl gameControl;

    private final int CARD_WIDTH = 50;
    private final int GAME_WIDTH = 800;
    private final int GAME_HEIGHT = 600;
    private final int HAND_WIDTH = 220;
    private final int HAND_HEIGHT = 160;
    private final int STATS_WIDTH = 200;
    private final int STATS_HEIGHT = 100;
    private GameState gameState;
    private User user;

    public TexasHoldemPanel(GameControl gc) {
        super();
        gameControl = gc;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setBackground(new Color(0, 100, 0));

        initHandsPanel();
        initStatsPanel();
        initOptionsPanel();
//        updateFromGameState();
    }

    private void initHandsPanel() {
        JPanel handPanel = new JPanel();
        handPanel.setLayout(new BoxLayout(handPanel, BoxLayout.Y_AXIS));
        GridBagConstraints gbc = new GridBagConstraints();
        handPanel.setBackground(new Color(0, 120, 0));

        communityCardsPanel = new JPanel();
        communityCardsPanel.setBorder(BorderFactory.createTitledBorder("Community Cards"));
        communityCardsPanel.setPreferredSize(new Dimension(300, 100));
        communityCardsPanel.setBackground(new Color(0, 120, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 2;
        gbc.insets = new Insets(10, 10,10,10);
        handPanel.add(communityCardsPanel, gbc);

        handPanel.add(Box.createVerticalStrut(20));

        playerHandPanel = new JPanel();
        playerHandPanel.setBorder(BorderFactory.createTitledBorder("Player Hand"));
        playerHandPanel.setPreferredSize(new Dimension(200, 100));
        playerHandPanel.setBackground(new Color(0, 120, 0));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        handPanel.add(playerHandPanel, gbc);

        add(handPanel, BorderLayout.CENTER);
    }

    public void initStatsPanel() {
        statsPanel = new StatsPanel();
        statsPanel.setBorder(BorderFactory.createTitledBorder("Stats"));
        statsPanel.setPreferredSize(new Dimension(STATS_WIDTH, STATS_HEIGHT));
        add(statsPanel, BorderLayout.WEST);
    }

    public void initOptionsPanel() {
        optionsPanel = new JPanel();
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Options"));
        optionsPanel.setPreferredSize(new Dimension(0, 100));
        optionsPanel.setBackground(new Color(0, 120, 0));

        // add game control
        checkButton.addActionListener(gameControl);
        callButton.addActionListener(gameControl);
        foldButton.addActionListener(gameControl);
        raiseButton.addActionListener(gameControl);

        // add buttons to panel
        optionsPanel.add(checkButton);
        optionsPanel.add(callButton);
        optionsPanel.add(foldButton);
        optionsPanel.add(raiseButton);

        // disable buttons
        toggleButtons(false);

        // SET PANEL BOUNDS
        add(optionsPanel, BorderLayout.SOUTH);
    }

    public void toggleButtons(boolean state) {
        checkButton.setEnabled(state);
        callButton.setEnabled(state);
        foldButton.setEnabled(state);
        raiseButton.setEnabled(state);
    }

    public void updateFromGameState(GameState gameState, User user) {
        SwingUtilities.invokeLater(() -> {
            playerHandPanel.removeAll();
            playerHandPanel.revalidate();
            playerHandPanel.repaint();

            this.gameState = gameState;
            this.user = this.gameState.getPlayers().get(gameState.getCurrentPlayerIndex());

            Hand hand = this.user.getHand();

            System.out.println("Hand size: " + hand.size());

            statsPanel.updateStats(
                    gameState.getPot(),
                    gameState.getCurrentBet(),
                    user.getBalance()
            );

            // update current players hand
            updatePlayerHand(hand);

            // update community cards
            updateCommunityCards(gameState.getCommunityCards());
        });
    }

    private void updatePlayerHand(Hand hand) {
        SwingUtilities.invokeLater(() -> {
            playerHandPanel.removeAll();

            for (Card card : hand) {
                card.setFaceUp(true);
                playerHandPanel.add(new CardPanel(card, CARD_WIDTH));
            }
            playerHandPanel.revalidate();
            playerHandPanel.repaint();
        });
    }

    private void updateCommunityCards(Hand communityCards) {
        SwingUtilities.invokeLater(() -> {
            communityCardsPanel.removeAll();

            for (Card card : communityCards) {
                card.setFaceUp(true);
                communityCardsPanel.add(new CardPanel(card, CARD_WIDTH));
            }
            communityCardsPanel.revalidate();
            communityCardsPanel.repaint();
        });

    }

    public ArrayList<JButton> getButtons() {
        ArrayList<JButton> buttons = new ArrayList<>();
        buttons.add(checkButton);
        buttons.add(callButton);
        buttons.add(foldButton);
        buttons.add(raiseButton);

        return buttons;
    }
}
