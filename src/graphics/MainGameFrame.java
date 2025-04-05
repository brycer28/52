package graphics;

import javax.swing.*;

import graphics.panels.gameplay.GamePlayPanel;
import graphics.panels.LobbyPanel;
import graphics.panels.StatsPanel;
import java.awt.CardLayout;
import Client.PlayerClient;

/*
* Main GUI manager, responsible for:
* Initialize and lay out all sub-panels. initUI()
* Provide a simple way for the controller to switch between panels (e.g., from LoginPanel to LobbyPanel).
*   setUpListeners()
* Expose methods that the controller can call to update the interface as the game state changes (e.g., show/hide panels, refresh data, display feedback, etc.).
*
* */
public class MainGameFrame extends JFrame {

    // panel references
    private LobbyPanel lobbyPanel;
    private GamePlayPanel gamePlayPanel;
    private StatsPanel statsPanel;

    // layout manager
    private CardLayout cardLayout;

    private PlayerClient controller;


    // will have to have 'GameClient controller' as a parameter
    // entry into UI
    public MainGameFrame(PlayerClient controller){
        super("Texas Hold'Em");  // Set the window title
        this.controller = controller; // set gameclient as the logic controller for the gui
        cardLayout = new CardLayout(); // layout manager
        setLayout(cardLayout);
        initUI(); // initialize UI
        setupListeners();
    }

    private void initUI()
    {
        // Add Panels to UI
        statsPanel = new StatsPanel();
        add(statsPanel, "stats");
        gamePlayPanel = new GamePlayPanel();
        add(gamePlayPanel, "gamePlay");
        lobbyPanel = new LobbyPanel();
        add(lobbyPanel, "lobby");
    }

    // Method to switch which panel is visible
    public void setPanel(String panelName) {
        cardLayout.show(getContentPane(), panelName);
    }

    // Method to retrieve lobby panel (for switching views manually)
    public LobbyPanel getLobbyPanel() {
        return lobbyPanel;
    }

    // Method to retrieve stats panel (for switching views manually)
    public StatsPanel getStatsPanel() {
        return statsPanel;
    }

    // Method to retrieve gamePlay panel (for switching views manually)
    public GamePlayPanel getGamePlayPanel() {
        return gamePlayPanel;
    }

    public void setupListeners() {
        lobbyPanel.setJoinGameAction(e -> {
            // Extract the selected room from the LobbyPanel
            String selectedRoom = lobbyPanel.getSelectedGameRoom();
            // Pass both the ActionEvent and selectedRoom to the controller
            controller.handleJoinGame(e, selectedRoom);
            controller.handleCreateGame(e);
        });
    }


    }




