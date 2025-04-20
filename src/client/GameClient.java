package client;
import graphics.*;
import javax.swing.*;
import account.*;
import logic.GameMessage;
import logic.GameState;
import logic.TexasHoldem.*;
import ocsf.client.AbstractClient;

import java.io.IOException;
import java.util.List;

/**
 * Acts as the main controller for the client-side application.
 * Mediates between the UI (MainGameFrame) and network logic (PlayerClient).
 */

public class GameClient extends AbstractClient {
    private LoginControl loginControl;
    private CreateAccountControl createAccountControl;
    private MainGameFrame view;

    public GameClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        if (msg instanceof GameMessage) {
            GameMessage gameMessage = (GameMessage) msg;

            switch (gameMessage.getType()) {
                case START_GAME -> {
                    GameState gs = (GameState) gameMessage.getData();
                    // updateGameState(gs);
                }
                case NOTIFY_TURN -> {
                    // view.gamePanel.promptOption();
                }
                case STATE_UPDATE -> {
                    GameState gs = (GameState) gameMessage.getData();
                    // updateGameState(gs);
                }
                case WINNER -> {
                    User winner = (User) gameMessage.getData();
                    // view.gamePanel.notifyWinner(winner)
                }
            }
        }
    }

    public void sendMessageToServer(Object msg) {
        if (msg instanceof GameMessage) {
            GameMessage gm = (GameMessage) msg;

            if (gm.getData() instanceof Options) {
                try {
                    this.sendToServer(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateGameState(GameState gameState) {
        SwingUtilities.invokeLater(() -> {
            // fill in this
        });
    }
}

