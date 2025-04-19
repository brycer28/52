package graphics;

import javax.swing.*;

import graphics.panels.gameplay.GamePlayPanel;
import graphics.panels.LobbyPanel;
import graphics.panels.StatsPanel;
import client.PlayerClient;

import java.awt.CardLayout;

public class MainGameFrame extends JFrame {

    // Fields
    private PlayerClient playerClient;
    private LobbyPanel lobbyPanel;
    private GamePlayPanel gamePlayPanel;
    private StatsPanel statsPanel;
    private CardLayout cardLayout;

    // Default constructor
    public MainGameFrame() {
        super("Texas Hold'Em");
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        initUI();
        setupListeners();
    }

    // Overloaded constructor to accept PlayerClient
    public MainGameFrame(PlayerClient playerClient) {
        this(); // Call the default constructor
        this.playerClient = playerClient;
    }

    private void initUI() {
        statsPanel = new StatsPanel();
        add(statsPanel, "stats");

        gamePlayPanel = new GamePlayPanel();
        add(gamePlayPanel, "gamePlay");

        lobbyPanel = new LobbyPanel();
        add(lobbyPanel, "lobby");
    }

    public void setupListeners() {
        // Optional action hooks (can be implemented later)
    }

    public void setPanel(String panelName) {
        cardLayout.show(getContentPane(), panelName);
    }

    public LobbyPanel getLobbyPanel() {
        return lobbyPanel;
    }
}
