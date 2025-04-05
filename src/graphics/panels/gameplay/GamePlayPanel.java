package graphics.panels.gameplay;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import cards.Card; // Ensure your Card class is in the 'cards' package

/**
 * GamePlayPanel represents the main game play area. It is organized using a vertical BoxLayout
 * and contains subpanels for the dealer's hand, community cards, player's hand, game options, and quit controls.
 */
public class GamePlayPanel extends JPanel {

    // Sub-panels
    private JPanel dealerHandPanel;
    private JPanel communityCardsPanel;
    private JPanel playerHandPanel;
    private JPanel optionsPanel;
    private JPanel quitPanel;

    // A quit button for the quit panel.
    private JButton quitButton;

    // Constants for preferred sizes (adjust as needed)
    private final int PANEL_WIDTH = 800;
    private final int DEALER_PANEL_HEIGHT = 150;
    private final int COMMUNITY_PANEL_HEIGHT = 150;
    private final int PLAYER_PANEL_HEIGHT = 150;
    private final int OPTIONS_PANEL_HEIGHT = 100;
    private final int QUIT_PANEL_HEIGHT = 50;

    // Constant for card width used in CardPanel creation
    private final int CARD_WIDTH = 100;

    public GamePlayPanel() {
        initUI();
    }

    private void initUI() {
        // Use a vertical BoxLayout to stack sub-panels
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(PANEL_WIDTH,
                DEALER_PANEL_HEIGHT + COMMUNITY_PANEL_HEIGHT + PLAYER_PANEL_HEIGHT + OPTIONS_PANEL_HEIGHT + QUIT_PANEL_HEIGHT));
        setBackground(new Color(0, 100, 0)); // Dark green background

        // Create the dealer hand panel
        dealerHandPanel = new JPanel();
        dealerHandPanel.setBackground(new Color(0, 150, 0));
        dealerHandPanel.setPreferredSize(new Dimension(PANEL_WIDTH, DEALER_PANEL_HEIGHT));
        dealerHandPanel.setBorder(BorderFactory.createTitledBorder("Dealer Hand"));

        // Create the community cards panel
        communityCardsPanel = new JPanel();
        communityCardsPanel.setBackground(new Color(0, 150, 0));
        communityCardsPanel.setPreferredSize(new Dimension(PANEL_WIDTH, COMMUNITY_PANEL_HEIGHT));
        communityCardsPanel.setBorder(BorderFactory.createTitledBorder("Community Cards"));

        // Create the player hand panel
        playerHandPanel = new JPanel();
        playerHandPanel.setBackground(new Color(0, 150, 0));
        playerHandPanel.setPreferredSize(new Dimension(PANEL_WIDTH, PLAYER_PANEL_HEIGHT));
        playerHandPanel.setBorder(BorderFactory.createTitledBorder("Your Hand"));

        // Create the options panel for game actions (Check, Call, Fold, Raise)
        optionsPanel = new JPanel();
        optionsPanel.setBackground(new Color(0, 100, 0));
        optionsPanel.setPreferredSize(new Dimension(PANEL_WIDTH, OPTIONS_PANEL_HEIGHT));
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        // Example buttons (the controller can later attach listeners)
        JButton checkButton = new JButton("Check");
        JButton callButton = new JButton("Call");
        JButton foldButton = new JButton("Fold");
        JTextField raiseField = new JTextField(5);
        JButton raiseButton = new JButton("Raise");
        optionsPanel.add(checkButton);
        optionsPanel.add(callButton);
        optionsPanel.add(foldButton);
        optionsPanel.add(raiseField);
        optionsPanel.add(raiseButton);

        // Create the quit panel with a quit button
        quitPanel = new JPanel();
        quitPanel.setBackground(new Color(0, 100, 0));
        quitPanel.setPreferredSize(new Dimension(PANEL_WIDTH, QUIT_PANEL_HEIGHT));
        quitPanel.setBorder(BorderFactory.createTitledBorder("Quit"));
        quitButton = new JButton("Quit");
        quitPanel.add(quitButton);

        // Add all sub-panels to the GamePlayPanel
        add(dealerHandPanel);
        add(communityCardsPanel);
        add(playerHandPanel);
        add(optionsPanel);
        add(quitPanel);
    }

    // Getters for sub-panels to allow the controller to update them.
    public JPanel getDealerHandPanel() {
        return dealerHandPanel;
    }

    public JPanel getCommunityCardsPanel() {
        return communityCardsPanel;
    }

    public JPanel getPlayerHandPanel() {
        return playerHandPanel;
    }

    public JPanel getOptionsPanel() {
        return optionsPanel;
    }

    public JPanel getQuitPanel() {
        return quitPanel;
    }

    public JButton getQuitButton() {
        return quitButton;
    }

    /**
     * Updates the dealer hand panel with a list of cards.
     * Each card is wrapped in a CardPanel.
     *
     * @param cards List of cards to display in the dealer's hand.
     */
    public void updateDealerHand(List<Card> cards) {
        SwingUtilities.invokeLater(() -> {
            dealerHandPanel.removeAll();
            for (Card card : cards) {
                // Ensure the card is visible
                card.setFaceUp(true);
                // Create a CardPanel to visually represent the card.
                CardPanel cp = new CardPanel(card, CARD_WIDTH);
                dealerHandPanel.add(cp);
            }
            dealerHandPanel.revalidate();
            dealerHandPanel.repaint();
        });
    }

    /**
     * Updates the player's hand panel with a list of cards.
     * Each card is wrapped in a CardPanel.
     *
     * @param cards List of cards to display in the player's hand.
     */
    public void updatePlayerHand(List<Card> cards) {
        SwingUtilities.invokeLater(() -> {
            playerHandPanel.removeAll();
            for (Card card : cards) {
                card.setFaceUp(true);
                CardPanel cp = new CardPanel(card, CARD_WIDTH);
                playerHandPanel.add(cp);
            }
            playerHandPanel.revalidate();
            playerHandPanel.repaint();
        });
    }

    /**
     * Updates the community cards panel with a list of cards.
     * Each card is wrapped in a CardPanel.
     *
     * @param cards List of cards to display in the community cards area.
     */
    public void updateCommunityCards(List<Card> cards) {
        SwingUtilities.invokeLater(() -> {
            communityCardsPanel.removeAll();
            for (Card card : cards) {
                card.setFaceUp(true);
                CardPanel cp = new CardPanel(card, CARD_WIDTH);
                communityCardsPanel.add(cp);
            }
            communityCardsPanel.revalidate();
            communityCardsPanel.repaint();
        });
    }
}
