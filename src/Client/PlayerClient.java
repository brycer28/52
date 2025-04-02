package Client;

import AccountFiles.CreateAccountControl;
import AccountFiles.LoginControl;
import ocsf.client.AbstractClient;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Consumer;
import java.io.IOException;
import java.util.Arrays;

public class PlayerClient extends AbstractClient{
    private LoginControl loginControl;
    private CreateAccountControl createAccountControl;
    private GameClient gameclient;
    private LobbyControl lobbyControl;
    private User currentUser;
    private JPanel container;
    private List<User> players;
    private String username;
    private Map<String, Consumer<Message>> messageHandlers;
    private String serverName;
    private GamePanel gamePanel;

    public PlayerClient(string host, int port) throws IOException {
        super(host, port);
        this.players = new ArrayList<>();
        this.messageHandlers = new HashMap<>();
        this.serverName = "unknown server";
    }

    @Override
    protected void handleMessageFromServer(Object msg) {

    }

    public void setLoginControl(LoginControl loginControl) {
        this.loginControl = loginControl;
    }

    public void setCreateAccountControl(CreateAccountControl createAccountControl) {
        this.createAccountControl = createAccountControl;
    }

    public void setGameControl(GameControl gameControl) {
        this.GameControl = gameControl;
    }

    public void setLobbyControl(LobbyControl lobbyControl) {
        this.lobbyControl = lobbyControl;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void setContainer(JPanel container) {
        this.container = container;
    }

    private List<User> parsePlayersdata(String playersData) {


    }

    public List<User> getPlayers() {
        return players;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }

    public String getUsername() {
        return username;
    }














}

