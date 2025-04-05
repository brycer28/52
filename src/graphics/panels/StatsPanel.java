package graphics.panels;

import javax.swing.*;
import java.awt.*;

public class StatsPanel extends JPanel {
    private JLabel potLabel;
    private JLabel betLabel;
    private JLabel playerChipsLabel;
    private JLabel dealerChipsLabel;
    private JLabel dealerDecisionLabel;

    public StatsPanel() {
        initUI();
    }

    private void initUI() {
        // Use a GridLayout with 5 rows and 1 column, with some gaps
        setLayout(new GridLayout(5, 1, 10, 5));
        setBackground(new Color(0, 150, 0)); // Optional background color

        // Initialize labels with default text
        potLabel = new JLabel("Current Pot: 0");
        betLabel = new JLabel("Current Bet: 0");
        playerChipsLabel = new JLabel("Player Chips: 0");
        dealerChipsLabel = new JLabel("Dealer Chips: 0");
        dealerDecisionLabel = new JLabel("Dealer Option: None");

        // Optionally, center-align text
        potLabel.setHorizontalAlignment(SwingConstants.CENTER);
        betLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerChipsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dealerChipsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dealerDecisionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add labels to the panel
        add(potLabel);
        add(betLabel);
        add(playerChipsLabel);
        add(dealerChipsLabel);
        add(dealerDecisionLabel);
    }

    /**
     * Updates the displayed statistics.
     *
     * @param pot The current pot amount.
     * @param bet The current bet amount.
     * @param playerChips The player's chip count.
     * @param dealerChips The dealer's chip count.
     * @param dealerDecision The dealer's current decision.
     */
    public void updateStats(int pot, int bet, int playerChips, int dealerChips, String dealerDecision) {
        potLabel.setText("Current Pot: " + pot);
        betLabel.setText("Current Bet: " + bet);
        playerChipsLabel.setText("Player Chips: " + playerChips);
        dealerChipsLabel.setText("Dealer Chips: " + dealerChips);
        dealerDecisionLabel.setText("Dealer Option: " + dealerDecision);
    }
}
