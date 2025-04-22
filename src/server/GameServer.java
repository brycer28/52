package server;
/*
 * GameServer handles client connections, tracks login status,
 * and updates the server GUI with the number of authenticated and unauthenticated users.
 */

import account.CreateAccountData;
import account.LoginData;
import account.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cards.Hand;
import logic.GameState;

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
    private int chipCount = 500;

    // Map of Users
    private HashMap<ConnectionToClient, User> clientUserMap = new HashMap<>();
    
    // Game Object
    private TexasHoldem game;

    // Constructor sets up server and connects GUI text areas
    public GameServer(int port) {
        super(port);

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
        client.setInfo("authenticated", true); // each ConnectionToClient should be directly mapped to a User object. Can handle creation wherever you guys like
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
        System.out.println("handleMessageFromClient() called");
        if (msg instanceof GameMessage<?>) {
            GameMessage<?> gm = (GameMessage<?>) msg;

            switch (gm.getType()) {
                case LOGIN -> {
                    LoginData data = (LoginData) ((GameMessage<?>) msg).getData();

                    if (database.verifyAccount(data.getUsername(), data.getPassword())) {
                        System.out.println("LOGIN VERIFIED");
                        User user = new User(data.getUsername(), 500);
                        clientUserMap.put(client, user);

                        handleSuccessfulLogin(client);

                        ArrayList<User> users = new ArrayList<>(clientUserMap.values());

                        GameState gs = new GameState(users, new Hand(), 0,0,0, null);
                        GameMessage<User> newUser = new GameMessage<>(GameMessage.MessageType.LOGIN_SUCCESS, user);

                        try {
                            client.sendToClient(newUser);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else {
                        try {
                            GameMessage<Error> loginResult = new GameMessage<>(GameMessage.MessageType.LOGIN, new Error());
                            System.out.println("loginUnsuccessful");
                            client.sendToClient(loginResult);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                case CREATE_ACC -> {
                    System.out.println("CASE CREATE_ACC REACHED");
                    CreateAccountData data = (CreateAccountData) ((GameMessage<?>) msg).getData();

                    if (database.createNewAccount(data.getUsername(), data.getPassword(), chipCount)) {
                        try {
                            GameMessage <CreateAccountData> createResult = new GameMessage<>(GameMessage.MessageType.CREATE_ACC, new CreateAccountData(null, null));
                            client.sendToClient(createResult);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try {
                            GameMessage <Error> createResult = new GameMessage<>(GameMessage.MessageType.CREATE_ACC, new Error());
                            client.sendToClient(createResult);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                case PLAYER_ACTION -> {
                    System.out.println("Player Action Recieved");
                    User user = clientUserMap.get(client);
                    Options opt = (Options) gm.getData();

                    if (!senderIsCurrentUser(client)) {
                        return;
                    }

                    try {
                        game.handleOption((Options) gm.getData(), user);

                        if (game.isRoundOver()) {
                            if (game.getGameState().getPhase() == GameState.GamePhase.SHOWDOWN) {
                                game.determineWinner();
                                game.resetGame();
                            } else {
                                game.nextPhase();
                            }
                        } else {
                            game.promptNextUser();
                        }

                        sendToAllClients(new GameMessage(GameMessage.MessageType.STATE_UPDATE, game.getGameState()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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

    public Map<ConnectionToClient, User> getClients() {
        return clientUserMap;
    }

    private boolean senderIsCurrentUser(ConnectionToClient client) {
        User current = game.getPlayerList().get(game.getCurrentPlayerIndex());
        return current.equals(clientUserMap.get(client));
    }

    public int getUnauthenticatedUsers() { return unauthenticatedUsers; }
    public int getAuthenticatedUsers() { return authenticatedUsers; }

    public void startGameFromServer() {
        game = new TexasHoldem(this);
        game.startGame();
    }

    // // Handles interaction with game server updating based on client input from handleMessageFromClient
    // private void handlePlayerAction(Options opt, ConnectionToClient client) {
    //     // update game state
    //     game.handleAction(opt);

    //     // broadcast updated state to clients
    //     GameMessage updateMessage = new GameMessage("GAME_STATE_UPDATE", game.getCurrentState());
    //     sendToAllClients(updateMessage);
    // }
}
