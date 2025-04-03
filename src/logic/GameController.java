package logic;

import graphics.TexasHoldemPanel;

import java.util.List;
import javax.swing.*;

// our game controller class will manage the interaction between game logic, server comm, and the game ui
public class GameController {
    private TexasHoldemPanel gamepanel;

    // setup initial state and message handlers (client, game)
    public GameController() {

        // setup listeners and handlers
    }
    // handler for messages sent from server
    private void setupMessageHandlers() {

        // handle noti for player turn
        // handle result of player action
        // handle any errors sent by server

    }
    // set up listeners for call, raise, fold, check
    private void setupActionListeners() {

    }
    // update game state in the ui
    private void updateGame() {

    }

    // handles result of action sent by server
    private void handleActionResult() {

    }

    public TexasHoldemPanel getGamepanel() {
        return gamepanel;
    }
}
