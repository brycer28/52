package server;
/*
 * GameServer handles client connections, tracks login status,
 * and updates the server GUI with the number of authenticated and unauthenticated users.
 */

import account.LoginData;
import account.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import javax.swing.*;
import logic.TexasHoldem.*;
import logic.GameMessage;
import logic.TexasHoldem;

public class GameServer extends AbstractServer {
    private int unauthenticatedUsers = 0;
    private int authenticatedUsers = 0;
    private DatabaseClass database;

    // Text areas in the GUI for showing user counts

    // Map of Users
    private HashMap<ConnectionToClient, User> clientUserMap = new HashMap<>();
    
    // Game Object
    private TexasHoldem game;

    // Constructor sets up server and connects GUI text areas
    public GameServer(int port) {
        super(port);
        this.game = new TexasHoldem(this);

        updateCounterDisplays();
    }

    public void setDatabase(DatabaseClass database) {
        this.database = database;
    }

    // Called when a client connects
    @Override
    protected void clientConnected(ConnectionToClient client) {
        unauthenticatedUsers++;
        updateCounterDisplays();
    }

    // Called when a client disconnects
    @Override
    protected void clientDisconnected(ConnectionToClient client) {
        Boolean isAuthenticated = (Boolean) client.getInfo("authenticated");
        if (isAuthenticated != null && isAuthenticated) {
            authenticatedUsers--;
        } else {
            unauthenticatedUsers--;
        }
        clientUserMap.remove(client);
        updateCounterDisplays();
    }

    // Called when a client logs in successfully
    public void handleSuccessfulLogin(ConnectionToClient client) {
        unauthenticatedUsers--;
        authenticatedUsers++;
        client.setInfo("authenticated", true);
        clientUserMap.put(client, new User("foo", 500)); // each ConnectionToClient should be directly mapped to a User object. Can handle creation wherever you guys like
        updateCounterDisplays();
    }

    // Updates the user counts on the GUI
    private void updateCounterDisplays() {
        SwingUtilities.invokeLater(() -> {
//            unauthenticatedCountArea.setText("Unauthenticated Users: " + unauthenticatedUsers);
//            authenticatedCountArea.setText("Authenticated Users: " + authenticatedUsers);
        });
    }

    // Handles messages from clients (to be implemented)
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        if (msg instanceof GameMessage<?>) {
            GameMessage<?> gm = (GameMessage<?>) msg;

            switch (gm.getType()) {
                case LOGIN -> {
                    LoginData data = (LoginData) msg;
                    LoginData message = new LoginData("loginSuccessful", null);
                    //Error result = null;

                    if (database.verifyAccount(data.getUsername(), data.getPassword())) {
                        try {
                            GameMessage <LoginData> loginResult = new GameMessage<>(GameMessage.MessageType.LOGIN, message);
                            client.sendToClient(loginResult);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try {
                            GameMessage <Error> loginResult = new GameMessage<>(GameMessage.MessageType.ERROR, new Error("loginUnsuccessful", null));
                            client.sendToClient(loginResult);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                case PLAYER_ACTION -> {
                    User user = clientUserMap.get(client);
                    game.handleOption((Options) gm.getData(), user);

                    // needs to broadcast a state update

                    // after state update, prompt next player - may need to do prompt outside of this method though
                }
                // add more options here - currently only Client -> Server comm should be inputting options
            }
        }
    }

    // send a message to client by specifying corresponding user
    public void sendToUser(GameMessage msg, User user) throws IOException {
        for (Map.Entry<ConnectionToClient, User> entry : clientUserMap.entrySet()) {
            if (entry.getValue().equals(user)) {
                ConnectionToClient client = entry.getKey();
                client.sendToClient(msg);
                break;
            }
        }
    }

    // broadcast method
    public void sendToAllClients(GameMessage msg) throws IOException {
        for (Map.Entry<ConnectionToClient, User> entry : clientUserMap.entrySet()) {
            ConnectionToClient client = entry.getKey();
            client.sendToClient(msg);
        }
    }

    public ArrayList<ConnectionToClient> getClients() {
        ArrayList<ConnectionToClient> clients = new ArrayList<>();
        for (Map.Entry<ConnectionToClient, User> entry : clientUserMap.entrySet()) {
            ConnectionToClient client = entry.getKey();
            clients.add(client);
        }
        return clients;
    }

    public int getUnauthenticatedUsers() { return unauthenticatedUsers; }
    public int getAuthenticatedUsers() { return authenticatedUsers; }

    // // Handles interaction with game server updating based on client input from handleMessageFromClient
    // private void handlePlayerAction(Options opt, ConnectionToClient client) {
    //     // update game state
    //     game.handleAction(opt);

    //     // broadcast updated state to clients
    //     GameMessage updateMessage = new GameMessage("GAME_STATE_UPDATE", game.getCurrentState());
    //     sendToAllClients(updateMessage);
    // }
}
