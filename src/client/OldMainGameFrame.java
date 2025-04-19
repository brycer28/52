package client;

import javax.swing.*;
import java.awt.*;

public class OldMainGameFrame extends JFrame {
    private JPanel cardPanel;         // The container for all panels (CardLayout)
    private CardLayout cardLayout;    // Layout manager

    // Panels for different views
    private JPanel loginPanel;
    private JPanel accountCreationPanel;
    private JPanel lobbyPanel;
    private JPanel gamePlayPanel;
    private JPanel statsPanel;

    public OldMainGameFrame() {
        super("Texas Hold'em"); // Set window title

        // Initialize layout and card panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create and add your panels
        loginPanel = new account.LoginPanel();                     // assuming you have these classes
        accountCreationPanel = new account.CreateAccountPanel();
        //lobbyPanel = new LobbyPanel();
        //gamePlayPanel = new TexasHoldemPanel();              // or whatever your gameplay panel is
        //statsPanel = new StatsPanel();

        cardPanel.add(loginPanel, "login");
        cardPanel.add(accountCreationPanel, "accountCreation");
        cardPanel.add(lobbyPanel, "lobby");
        cardPanel.add(gamePlayPanel, "texasHoldem");
        cardPanel.add(statsPanel, "stats");

        // Set the card panel as the content pane of the JFrame
        this.setContentPane(cardPanel);
    }

    /**
     * Switches the visible panel using the CardLayout.
     *
     * @param panelName The name of the panel to show ("login", "lobby", etc.)
     */
    public void setPanel(String panelName) {
        cardLayout.show(cardPanel, panelName);
    }

    /**
     * Returns the main card panel so other classes can add or interact with it.
     */
    public JPanel getCardPanel() {
        return cardPanel;
    }
}

