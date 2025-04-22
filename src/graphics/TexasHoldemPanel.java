package graphics;

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
    private GameState gameState;
    private User user;

    private JPanel playerHandPanel = new JPanel();
    private StatsPanel statsPanel;
    private JPanel optionsPanel;
    private JButton checkButton = new JButton("Check");
    private JButton callButton = new JButton("Call");
    private JButton foldButton = new JButton("Fold");
    private JButton raiseButton = new JButton("Raise");

    private GameControl gameControl;

    private final int CARD_WIDTH = 100;
    private final int GAME_WIDTH = 800;
    private final int GAME_HEIGHT = 600;
    private final int HAND_WIDTH = 220;
    private final int HAND_HEIGHT = 160;
    private final int STATS_WIDTH = 200;
    private final int STATS_HEIGHT = 100;

    public TexasHoldemPanel(GameControl gc) {
        super();
        gameControl = gc;

        setLayout(null);
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setBackground(new Color(0, 100, 0));

        initPlayerHand();
        initStatsPanel();
        initOptionsPanel();
//        updateFromGameState();
    }

    private void initPlayerHand() {
        playerHandPanel.setBounds((GAME_WIDTH / 2 - HAND_WIDTH / 2), 400, HAND_WIDTH, HAND_HEIGHT);
        playerHandPanel.setBackground(new Color(0, 150, 0));
        add(playerHandPanel);
    }

    public void initStatsPanel() {
        statsPanel = new StatsPanel();
        statsPanel.setBounds(20, 20, STATS_WIDTH, STATS_HEIGHT);
        add(statsPanel);
    }

    public void initOptionsPanel() {
        optionsPanel = new JPanel();
        optionsPanel.setBackground(new Color(0, 120, 0));
        optionsPanel.setBounds(200, 200, 200, 200);
        optionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

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
        add(optionsPanel);
    }

    public void toggleButtons(boolean state) {
        checkButton.setEnabled(state);
        callButton.setEnabled(state);
        foldButton.setEnabled(state);
        raiseButton.setEnabled(state);
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

    public void resetGUI() {

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
