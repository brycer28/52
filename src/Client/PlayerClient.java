package Client;

import AccountFiles.CreateAccountControl;
import AccountFiles.LoginControl;
import AccountFiles.CreateAccountData;
import AccountFiles.LoginData;
import AccountFiles.User;
import ocsf.client.AbstractClient;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PlayerClient extends AbstractClient {
    private LoginControl loginControl;
    private CreateAccountControl createAccountControl;
    private GameClient gameClient;
    private User currentUser;
    private JPanel container;
    private List<User> players;
    private String username;
    private Map<String, Consumer<String>> messageHandlers;
    private String serverName;

    public PlayerClient(String host, int port) throws IOException {
        super(host, port);
        this.players = new ArrayList<>();
        this.messageHandlers = new HashMap<>();
        this.serverName = "unknown server";
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        if (msg instanceof LoginData loginData) {
            if (loginData.isSuccess()) {
                this.currentUser = loginData.getUser();
                gameClient.loginSuccessful(currentUser);
            } else {
                JOptionPane.showMessageDialog(container, "Login failed. Please try again.");
            }
        } else if (msg instanceof CreateAccountData accountData) {
            if (accountData.isSuccess()) {
                JOptionPane.showMessageDialog(container, "Account created successfully. Please log in.");
                gameClient.showLoginPanel();
            } else {
                JOptionPane.showMessageDialog(container, "Username already exists. Please choose another.");
            }
        } else if (msg instanceof String message) {
            System.out.println("Received string message: " + message);
            // Handle additional string-based server messages here if needed
        }
    }

    public void setLoginControl(LoginControl loginControl) {
        this.loginControl = loginControl;
    }

    public void setCreateAccountControl(CreateAccountControl createAccountControl) {
        this.createAccountControl = createAccountControl;
    }

    public void setGameControl(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void setContainer(JPanel container) {
        this.container = container;
    }

    private List<User> parsePlayersData(String playersData) {
        return new ArrayList<>(); // placeholder
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

    public void setUsername(String username) {
        this.username = username;
    }
}

