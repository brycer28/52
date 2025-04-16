package Client;
import ocsf.client.AbstractClient;
import AccountFiles.*;
import logic.TexasHoldem;
import AccountFiles.LoginControl;

import javax.swing.*;
// our game client class will handle the interaction between game logic, server comm, and ui
public class GameClient extends AbstractClient {
    private TexasHoldem gameState; //ui
    private JTextArea gameStateLog; // game state
    private LoginControl loginControl;
    private CreateAccountControl createControl;
    private PlayerClient client; // client used to comm w server

    // setters
    public void setLoginControl(LoginControl loginControl) {
        this.loginControl = loginControl;
    }
    public void setCreateAccountControl(CreateAccountControl createControl) {
        this.createControl = createControl;
    }
    public void setGameState(TexasHoldem gameState) {
        this.gameState = gameState;
    }
    public void setGameStateLog(JTextArea gameStateLog) {
        this.gameStateLog = gameStateLog;
    }

    public GameClient(PlayerClient client, String gameName) {
        super("localhost", 8300);
        this.client = client;

    }

    @Override
    protected void handleMessageFromServer(Object serverMessage) {

        if (serverMessage instanceof String message) {
            if (message.equals("login=successful"))
                loginControl.success();

            else if (message.equals("createAccount=successful"))
                createControl.createAccountSuccess();
        }
    }

    @Override
    public void connectionEstablished() {

    }

    @Override
    public void connectionClosed() {

    }

    public void connectionException(Throwable exception) {

    }

    // update game class
    // handle messages class
    // setup acton listeners
    //

}
