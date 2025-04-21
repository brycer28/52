package graphics;

import graphics.panels.StatsPanel;
import graphics.panels.gameplay.CardPanel;
import logic.GameState;
import account.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import cards.Card;

public class TexasHoldemPanel extends JPanel {
    private GameState gameState;
    private User user;

    private JPanel playerHandPanel = new JPanel();
    private StatsPanel statsPanel;

    private final int CARD_WIDTH = 100;
    private final int GAME_WIDTH = 800;
    private final int GAME_HEIGHT = 600;
    private final int HAND_WIDTH = 220;
    private final int HAND_HEIGHT = 160;
    private final int STATS_WIDTH = 200;
    private final int STATS_HEIGHT = 100;

    public TexasHoldemPanel(GameState gameState, User user) {
        super();
        this.gameState = gameState;
        this.user = user;

        setLayout(null);
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setBackground(new Color(0, 100, 0));

        initPlayerHand();
        initStatsPanel();
        updateFromGameState(gameState, user);
    }

    private void initPlayerHand() {
        playerHandPanel.setBounds((GAME_WIDTH / 2 - HAND_WIDTH / 2), 400, HAND_WIDTH, HAND_HEIGHT);
        playerHandPanel.setBackground(new Color(0, 150, 0));
        add(playerHandPanel);
    }

    private void initStatsPanel() {
        statsPanel = new StatsPanel();
        statsPanel.setBounds(20, 20, STATS_WIDTH, STATS_HEIGHT);
        add(statsPanel);
    }

    public void updateFromGameState(GameState gameState, User user) {
        this.gameState = gameState;
        this.user = user;

        statsPanel.updateStats(
                gameState.getPot(),
                gameState.getCurrentBet(),
                user.getBalance()
        );

        updatePlayerHand(user.getHand());
    }

    private void updatePlayerHand(List<Card> cards) {
        playerHandPanel.removeAll();
        for (Card card : cards) {
            card.setFaceUp(true);
            playerHandPanel.add(new CardPanel(card, CARD_WIDTH));
        }
        playerHandPanel.revalidate();
        playerHandPanel.repaint();
    }
}
