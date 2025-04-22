package client;

import cards.Card;
import cards.Hand;
import graphics.*;
import javax.swing.*;
import account.*;
import logic.GameControl;
import logic.GameMessage;
import logic.GameState;
import logic.TexasHoldem.*;
import ocsf.client.AbstractClient;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;


/**
 * Acts as the main controller for the client-side application.
 * Mediates between the UI (MainGameFrame) and network logic (PlayerClient).
 */

public class GameClient extends AbstractClient {
    private InitialControl initialControl;
    private LoginControl loginControl;
    private CreateAccountControl createAccountControl;
    private GameControl gameControl;
    private User user;

    public GameClient(String host, int port) throws IOException {
        super(host, port);
       // openConnection();

        try {
            openConnection();
            System.out.println("Connected to server at " + host + ":" + port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1); // may cause issues!!!?!?!
        }
    }

    public void setInitialControl(InitialControl initialControl) { this.initialControl = initialControl; }
    public void setLoginControl(LoginControl loginControl) {
        this.loginControl = loginControl;
    }
    public void setCreateAccountControl (CreateAccountControl createAccountControl) { this.createAccountControl = createAccountControl; }
    public void setGameControl(GameControl gameControl) { this.gameControl = gameControl; }


    @Override
    protected void handleMessageFromServer(Object msg) {
        System.out.println("handleMessageFromServer() called");
        if (msg instanceof GameMessage) {
            GameMessage gameMessage = (GameMessage) msg;

            switch (gameMessage.getType()) {
                case LOGIN -> {
                    System.out.println("Login");
                    if (gameMessage.getData() instanceof LoginData) {
                        loginControl.displayMessageWindow("Successfully Logged in!");
                    }
                    else if (gameMessage.getData() instanceof Error) {

                        loginControl.displayMessageWindow("The username or password is incorrect");
                    }
                }
                case LOGIN_SUCCESS -> {
                    System.out.println("Successfully Logged in!");
                    if (gameMessage.getData() instanceof User user) {
                        this.user = user;
                    } else if (gameMessage.getData() instanceof Error) {
                        loginControl.displayMessageWindow("LOGIN FAILED :(");
                    }
                }
                case CREATE_ACC -> {
                    System.out.println("createAccount() called");
                    if (gameMessage.getData() instanceof CreateAccountData) {
                        createAccountControl.displayMessageWindow("Account Created Successfully! Return to login page to log in");
                    }
                    else if (gameMessage.getData() instanceof Error) {
                        //display error if occurred
                        createAccountControl.displayMessageWindow("The username is already in use");
                    }
                }
                case START_GAME -> {
                    if (gameMessage.getData() instanceof GameState gs) {
                        System.out.println("startGame() called");
                        gameControl.startGame();
                        gameControl.resetGameGUI(gs, user);
                    }
                }
                case NOTIFY_TURN -> {
                    System.out.println("notifyTurn() called");
                    gameControl.toggleButtons(true);
                }
                case STATE_UPDATE -> {
                    if (gameMessage.getData() instanceof GameState gs) {
                        System.out.println("stateUpdate() called");
                        gameControl.resetGameGUI(gs, user);
                    }
                }
                case WINNER -> {
                    System.out.println("winner() called");
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
}

