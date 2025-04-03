package Client;
import ocsf.client.AbstractClient;
import AccountFiles.*;
import logic.TexasHoldem;
import AccountFiles.LoginControl;

import javax.swing.*;

public class GameClient extends AbstractClient {
    private TexasHoldem gameState;
    private JTextArea gameStateLog;
    private LoginControl loginControl;
    private CreateAccountControl createControl;

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

    public GameClient() {
        super("localhost", 8300);
    }

    @Override
    protected void handleMessageFromServer(Object serverMessage) {

        if (serverMessage instanceof String) {
            String message = (String) serverMessage;

            if (message.equals("login=successful"))
                loginControl.success();
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

}
