package graphics.panels.test;

import javax.swing.*;

import client.GameClient;
import client.PlayerClient;
import account.User; // Replaces the removed CurrentClient

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

import graphics.panels.LobbyPanel;
import logic.TexasHoldem;
import server.ServerClass;

/**
 * IntegratedLobbyPanelTest demonstrates how the LobbyPanel
 * interacts with other parts of the system, such as the client, game logic, and server.
 */
public class IntegratedLobbyPanelTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Integrated Lobby Panel Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            LobbyPanel lobbyPanel = new LobbyPanel();
            lobbyPanel.updateGameRooms(Arrays.asList("Room A", "Room B", "Room C"));

            ServerClass server = new ServerClass(8300, "TestServer");

            PlayerClient playerClient = null;
            try {
                playerClient = new PlayerClient("localhost", 8300);
            } catch (IOException e) {
                e.printStackTrace();
            }

            GameClient gameClient = new GameClient(playerClient);

            TexasHoldem texasHoldem = new TexasHoldem();

            // Replacing CurrentClient with a test User
            User testUser = new User("TestUser", 1000);

            lobbyPanel.setJoinGameAction(e -> {
                String selectedRoom = lobbyPanel.getSelectedGameRoom();
                if (selectedRoom != null) {
                    JOptionPane.showMessageDialog(frame, "Join Game action triggered for: " + selectedRoom);
                    System.out.println("PlayerClient would now attempt to join game: " + selectedRoom);
                } else {
                    JOptionPane.showMessageDialog(frame, "No room selected. Please select a room.");
                }
            });

            lobbyPanel.setCreateGameAction(e -> {
                String newGameRoom = "Room " + (char) ('D' + (int)(Math.random() * 3));
                JOptionPane.showMessageDialog(frame, "Create Game action triggered. New room: " + newGameRoom);
                lobbyPanel.updateGameRooms(Arrays.asList(newGameRoom, "Room X", "Room Y"));
                System.out.println("Server (dummy) would broadcast a new game list including " + newGameRoom);
            });

            JPanel statusPanel = new JPanel(new GridLayout(4, 1));
            statusPanel.add(new JLabel("Server Name: " + server.getServerName()));
            statusPanel.add(new JLabel("User: " + testUser.getUsername() + " (Balance: $" + testUser.getBalance() + ")"));
            statusPanel.add(new JLabel("PlayerClient: Connected to localhost:8300"));
            statusPanel.add(new JLabel("GameClient: Managing game lobby"));

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(lobbyPanel, BorderLayout.CENTER);
            mainPanel.add(statusPanel, BorderLayout.SOUTH);

            frame.setContentPane(mainPanel);
            frame.setVisible(true);
        });
    }
}
