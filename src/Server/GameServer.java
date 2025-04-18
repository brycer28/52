package server;
/*
 * GameServer handles client connections, tracks login status,
 * and updates the server GUI with the number of authenticated and unauthenticated users.
 */

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import javax.swing.*;

public class GameServer extends AbstractServer {

    // Number of users not logged in
    private int unauthenticatedUsers = 0;

    // Number of users logged in
    private int authenticatedUsers = 0;

    // Text areas in the GUI for showing user counts
    private JTextArea unauthenticatedCountArea;
    private JTextArea authenticatedCountArea;

    // Constructor sets up server and connects GUI text areas
    public GameServer(int port, JTextArea unauthenticatedCountArea, JTextArea authenticatedCountArea) {
        super(port);
        this.unauthenticatedCountArea = unauthenticatedCountArea;
        this.authenticatedCountArea = authenticatedCountArea;
        updateCounterDisplays();
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
        updateCounterDisplays();
    }

    // Called when a client logs in successfully
    public void handleSuccessfulLogin(ConnectionToClient client) {
        unauthenticatedUsers--;
        authenticatedUsers++;
        client.setInfo("authenticated", true);
        updateCounterDisplays();
    }

    // Updates the user counts on the GUI
    private void updateCounterDisplays() {
        SwingUtilities.invokeLater(() -> {
            unauthenticatedCountArea.setText("Unauthenticated Users: " + unauthenticatedUsers);
            authenticatedCountArea.setText("Authenticated Users: " + authenticatedUsers);
        });
    }

    // Handles messages from clients (to be implemented)
    @Override
    protected void handleMessageFromClient(Object o, ConnectionToClient connectionToClient) {
        // Game logic or chat handling goes here
    }
}
