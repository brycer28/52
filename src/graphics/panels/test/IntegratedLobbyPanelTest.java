package graphics.panels.test;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import Client.PlayerClient;
import Client.GameClient;
import Client.CurrentClient;
import graphics.panels.LobbyPanel;
import logic.TexasHoldem;
import Server.ServerClass;

/**
 * IntegratedLobbyPanelTest demonstrates how the LobbyPanel
 * interacts with other parts of the system, such as the client, game logic, and server.
 * This test creates a simple GUI with a lobby panel and a status panel showing dummy data
 * from PlayerClient, GameClient, CurrentClient, TexasHoldem, and ServerClass.
 */
public class IntegratedLobbyPanelTest {
    public static void main(String[] args) {
        // Ensure Swing components are created and updated on the EDT.
        SwingUtilities.invokeLater(() -> {
            // Create the main application frame.
            JFrame frame = new JFrame("Integrated Lobby Panel Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null); // Center on screen

            // Create an instance of LobbyPanel.
            LobbyPanel lobbyPanel = new LobbyPanel();
            // Update the lobby panel with sample game room names.
            lobbyPanel.updateGameRooms(Arrays.asList("Room A", "Room B", "Room C"));

            // Create a dummy ServerClass instance.
            ServerClass server = new ServerClass(8300, "TestServer");

            // Create a dummy PlayerClient instance.
            PlayerClient playerClient = null;
            try {
                playerClient = new PlayerClient("localhost", 8300);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Create a dummy GameClient instance using the PlayerClient.
            GameClient gameClient = new GameClient(playerClient, "TestGame");

            // Create a dummy TexasHoldem instance.
            TexasHoldem texasHoldem = new TexasHoldem();

            // Create a dummy CurrentClient instance for demonstration.
            CurrentClient currentClient = new CurrentClient(1, "TestUser", 1000, true, "Online");

            // Set up the LobbyPanel's "Join Game" action.
            lobbyPanel.setJoinGameAction(e -> {
                String selectedRoom = lobbyPanel.getSelectedGameRoom();
                if (selectedRoom != null) {
                    JOptionPane.showMessageDialog(frame, "Join Game action triggered for: " + selectedRoom);
                    // For demonstration: simulate player joining the game.
                    System.out.println("PlayerClient would now attempt to join game: " + selectedRoom);
                } else {
                    JOptionPane.showMessageDialog(frame, "No room selected. Please select a room.");
                }
            });

            // Set up the LobbyPanel's "Create Game" action.
            lobbyPanel.setCreateGameAction(e -> {
                String newGameRoom = "Room " + (char) ('D' + (int)(Math.random() * 3)); // Generate a sample room name.
                JOptionPane.showMessageDialog(frame, "Create Game action triggered. New room: " + newGameRoom);
                // For demonstration: update the lobby panel's room list.
                lobbyPanel.updateGameRooms(Arrays.asList(newGameRoom, "Room X", "Room Y"));
                System.out.println("Server (dummy) would broadcast a new game list including " + newGameRoom);
            });

            // Create a status panel to display dummy information from the other classes.
            JPanel statusPanel = new JPanel(new GridLayout(4, 1));
            statusPanel.add(new JLabel("Server Name: " + server.getServerName()));
            statusPanel.add(new JLabel("Current Client: " + currentClient.getUsername() + " (Balance: " + currentClient.getBalance() + ")"));
            statusPanel.add(new JLabel("PlayerClient: Connected to localhost:8300"));
            statusPanel.add(new JLabel("GameClient: Managing game: TestGame"));

            // Combine the LobbyPanel and the status panel in the main panel.
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(lobbyPanel, BorderLayout.CENTER);
            mainPanel.add(statusPanel, BorderLayout.SOUTH);

            frame.setContentPane(mainPanel);
            frame.setVisible(true);
        });
    }
}