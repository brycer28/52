package Client;

import AccountFiles.User;
import Client.controllers.GameControl;
import Client.controllers.LobbyControl;

import graphics.MainGameFrame;
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

public class PlayerClient extends AbstractClient{
    private LoginControl loginControl;
    private CreateAccountControl createAccountControl;
    private GameClient gameclient;
    private LobbyControl lobbyControl;
    private GameControl gameControl;
    private User currentUser;
    private JPanel container;
    private List<User> players;
    private String username;
    private Map<String, Consumer<Message>> messageHandlers;
    private String serverName;
    private MainGameFrame mainGameFrame;
  //  private GamePanel gamePanel;

    // cali making edits:
    // may want to put this in lobbycontrol?
  //  private MainGameFrame mainGameFrame;

    public void handleJoinGame(Event e) {
        // Retrieve the selected game room from the LobbyPanel
        String selectedRoom = mainGameFrame.getLobbyPanel().getSelectedGameRoom();
        // Check if a room was actually selected
        if (selectedRoom == null || selectedRoom.isEmpty()) {
            // Notify the user to select a room
            JOptionPane.showMessageDialog(mainGameFrame, "Please select a game room to join.");
            return;
        }
    }
        /* Attempt to join the selected game room through the model (or via a server call)
boolean joined = TexasHoldem.joinRoom(selectedRoom); // Assume joinRoom returns a boolean

        if (joined) {
            // If joining was successful, switch to the GamePlayPanel in the view
            mainGameFrame.setPanel("GamePlay");
        } else {
            // Otherwise, display an error message
            JOptionPane.showMessageDialog(mainGameFrame, "Failed to join room: " + selectedRoom);
        }
    } */
    // ----- cali done

    public PlayerClient(String host, int port) throws IOException {
        super(host, port);
        this.players = new ArrayList<User>();
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
        this.gameControl = gameControl;
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
        return null;
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

    // Skeleton Message class to get rid of errors
    public class Message {
        // Define fields (like a type, payload, etc.), constructors, getters, and setters
        private String type;
        private Object payload;

        public Message(String type, Object payload) {
            this.type = type;
            this.payload = payload;
        }

        public String getType() {
            return type;
        }

        public Object getPayload() {
            return payload;
        }
    }













}

