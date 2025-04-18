package graphics.panels.test;

import javax.swing.*;

import client.PlayerClient;

import java.util.Arrays;

import graphics.MainGameFrame;
import graphics.panels.LobbyPanel;

/**
 * IntegratedMainGameFrameTest demonstrates the integrated setup of the UI with its controller.
 *
 * In this test, we:
 *   - Create a dummy PlayerClient (acting as the controller).
 *   - Create the MainGameFrame (the top-level view) with a CardLayout managing sub-panels.
 *   - Update the LobbyPanel with a sample list of game rooms.
 *   - Rely on MainGameFrame's internal setUpListeners() method to wire up the "Join Game" button.
 *   - Initially display the LobbyPanel.
 */
public class IntegratedMainGameFrameTest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Create a dummy PlayerClient. (This would normally handle server communication.)
                PlayerClient playerClient = new PlayerClient("localhost", 8300);

                // Create the main game frame, passing the PlayerClient as the controller.
                MainGameFrame mainFrame = new MainGameFrame(playerClient);

                // Optionally, set the reference in the PlayerClient so it can access the main frame.
                // (This is necessary if PlayerClient.handleJoinGame() uses mainGameFrame.)
                playerClient.setMainGameFrame(mainFrame); // Ensure you have a setter in PlayerClient.

                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setSize(800, 600);
                mainFrame.setLocationRelativeTo(null); // Center the window

                // Update the LobbyPanel with some sample game room names.
                LobbyPanel lobbyPanel = mainFrame.getLobbyPanel();
                lobbyPanel.updateGameRooms(Arrays.asList("Room A", "Room B", "Room C"));

                // Set the initial visible panel to the lobby.
                mainFrame.setPanel("lobby");

                // Display the MainGameFrame.
                mainFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
