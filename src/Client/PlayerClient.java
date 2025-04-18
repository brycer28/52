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
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;
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






    public PlayerClient(String host, int port) throws IOException {
        super(host, port);
        this.players = new ArrayList<User>();
        this.messageHandlers = new HashMap<>();
        this.serverName = "unknown server";

    }

    public void handleJoinGame(ActionEvent e, String selectedRoom) {
        // Check if a room was actually selected
        if (selectedRoom == null || selectedRoom.isEmpty()) {
            // Notify the user to select a room using the main frame as the parent for the dialog
            JOptionPane.showMessageDialog(mainGameFrame, "Please select a game room to join.");
            return;
        }

        // Create a join room message using your inner Message class
        Message joinMessage = new Message("joinRoom", selectedRoom);
        try {
            // Attempt to send the join room message to the server
            sendToServer(joinMessage);
            System.out.println("Join room message sent for: " + selectedRoom);
            mainGameFrame.setPanel("gamePlay");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(mainGameFrame, "Failed to send join room request for: " + selectedRoom);
            return;
        }

        // Assuming the join request is successful, switch the UI to the GamePlay panel.
        mainGameFrame.setPanel("GamePlay");
    }

    @Override
	@@ -47,7 +82,7 @@ public void setCreateAccountControl(CreateAccountControl createAccountControl) {
    }

    public void setGameControl(GameControl gameControl) {
        this.gameControl = gameControl;
    }

    public void setLobbyControl(LobbyControl lobbyControl) {
	@@ -63,8 +98,7 @@ public void setContainer(JPanel container) {
    }

    private List<User> parsePlayersdata(String playersData) {
        return null;
    }

    public List<User> getPlayers() {
	@@ -79,6 +113,44 @@ public String getUsername() {
        return username;
    }

    public void setMainGameFrame(MainGameFrame mainFrame) {
        this.mainGameFrame = mainFrame;
    }

    public void handleCreateGame(ActionEvent e) {

            // Generate a sample room name.
            String newGameRoom = "Room " + (char) ('D' + (int) (Math.random() * 3));

            // Use mainGameFrame as the parent for the message dialog.
            JOptionPane.showMessageDialog(mainGameFrame, "Create Game action triggered. New room: " + newGameRoom);

            // Update the lobby panel's room list via the mainGameFrame.
            mainGameFrame.getLobbyPanel().updateGameRooms(Arrays.asList(newGameRoom, "Room X", "Room Y"));

            System.out.println("Server (dummy) would broadcast a new game list including " + newGameRoom);

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