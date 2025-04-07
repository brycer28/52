package graphics;

import javax.swing.*;

import graphics.panels.gameplay.GamePlayPanel;
import graphics.panels.LobbyPanel;
import graphics.panels.StatsPanel;
import java.awt.CardLayout;


/*
* Main GUI manager, responsible for:
* Initialize and lay out all sub-panels. initUI()
* Provide a simple way for the controller to switch between panels (e.g., from LoginPanel to LobbyPanel).
*   setUpListeners()
* Expose methods that the controller can call to update the interface as the game state changes (e.g., show/hide panels, refresh data, display feedback, etc.).
*
*
*
* */
public class MainGameFrame extends JFrame {

    // panel references
    private LobbyPanel lobbyPanel;
    private GamePlayPanel gamePlayPanel;
    private StatsPanel statsPanel;

    // layout manager
    private CardLayout cardLayout;

    // private GameClient controller;


    // will have to have 'GameClient controller' as a parameter
    // entry into UI
    public MainGameFrame(){
        super("Texas Hold'Em");  // Set the window title
        // this.controller = controller;
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        initUI();
        setupListeners();
    }

    private void initUI()
    {
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


    // centralized listener wiring using the controller
    // how GameClient will respond when input is received on Panels
    public void setupListeners() {
        // lobbyPanel.setJoinGameAction(e -> controller.handleJoinGame());
        // gamePlayPanel.setBetAction(e -> controller.handleBet());
        // etc.
    }


    }




