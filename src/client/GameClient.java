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

    private LoginControl loginControl;
    private CreateAccountControl createAccountControl;

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

    public void setLoginControl(LoginControl loginControl) {
        this.loginControl = loginControl;
    }

    public void setCreateAccountControl (CreateAccountControl createAccountControl) {
        this.createAccountControl = createAccountControl;
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        if (msg instanceof GameMessage) {
            GameMessage gameMessage = (GameMessage) msg;

            switch (gameMessage.getType()) {
                case LOGIN -> {
                    // try to log in by sending request to database
                    if (gameMessage.getData() instanceof LoginData) {
                        // view.getLoginControl.success() ??
                        loginControl.success();
                    }
                    else if (gameMessage.getData() instanceof Error) {
                        //display error if occurred
                        loginControl.displayError("The username or password is incorrect");
                    }
                }
                case CREATE_ACC -> {
                    // try to create account by requesting database
                    if (msg.equals("createAccountSuccessful")) {
                        // view.getCreateAccControl.success() ??
                        createAccountControl.createAccountSuccess();
                    }
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

