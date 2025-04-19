package graphics.panels.test;

import graphics.MainGameFrame;
import client.PlayerClient;
import graphics.panels.LobbyPanel;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;

public class IntegratedMainGameFrameTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Create a dummy PlayerClient (mocked connection to localhost:8300)
                PlayerClient playerClient = new PlayerClient("localhost", 8300);

                // Create the MainGameFrame with PlayerClient
                MainGameFrame mainFrame = new MainGameFrame(playerClient);

                // Optionally let the client know about the frame
                playerClient.setMainGameFrame(mainFrame);

                // Frame settings
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setSize(800, 600);
                mainFrame.setLocationRelativeTo(null);

                // Access and update the LobbyPanel
                LobbyPanel lobbyPanel = mainFrame.getLobbyPanel();
                lobbyPanel.updateGameRooms(Arrays.asList("Room A", "Room B", "Room C"));

                // Show the lobby panel
                mainFrame.setPanel("lobby");

                // Display the frame
                mainFrame.setVisible(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
