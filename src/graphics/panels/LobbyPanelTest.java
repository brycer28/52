package graphics.panels;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class LobbyPanelTest {

    public static void main(String[] args) {
        // Ensure the UI updates occur on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Create a test JFrame to hold the LobbyPanel
            JFrame frame = new JFrame("Lobby Panel Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null); // Center on screen

            // Create an instance of LobbyPanel
            LobbyPanel lobbyPanel = new LobbyPanel();

            // Update the game room list dynamically with sample room names
            lobbyPanel.updateGameRooms(Arrays.asList("Room A", "Room B", "Room C"));

            // Set up sample action listeners for testing purposes
            lobbyPanel.setJoinGameAction(e -> {
                String selectedRoom = lobbyPanel.getSelectedGameRoom();
                if (selectedRoom != null) {
                    JOptionPane.showMessageDialog(frame, "Join Game action triggered for: " + selectedRoom);
                } else {
                    JOptionPane.showMessageDialog(frame, "No room selected. Please select a room.");
                }
            });

            lobbyPanel.setCreateGameAction(e -> {
                JOptionPane.showMessageDialog(frame, "Create Game action triggered.");
            });

            // Add the LobbyPanel to the frame
            frame.getContentPane().add(lobbyPanel, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}
