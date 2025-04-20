package client;
import graphics.*;
import javax.swing.*;
import account.*;
import logic.GameMessage;
import logic.GameState;
import logic.TexasHoldem.*;
import ocsf.client.AbstractClient;
import java.io.IOException;

/**
 * Acts as the main controller for the client-side application.
 * Mediates between the UI (MainGameFrame) and network logic (PlayerClient).
 */

public class GameClient extends AbstractClient {
    public GameClient(String host, int port) throws IOException {
        super(host, port);
        openConnection();

//        try {
//            openConnection();
//            System.out.println("Connected to server at " + host + ":" + port);
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.exit(1); // may cause issues!!!?!?!
//        }
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        if (msg instanceof GameMessage) {
            GameMessage gameMessage = (GameMessage) msg;

            switch (gameMessage.getType()) {
                case LOGIN -> {
                    // try to login by sending request to database

                    // view.getLoginControl.success() ??
                }
                case CREATE_ACC -> {
                    // try to create account by requesting database

                    // view.getCreateAccControl.success() ??
                }
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

