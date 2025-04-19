package client;
import graphics.*;
import javax.swing.*;
import account.*;
import java.io.IOException;
import java.util.List;

/**
 * Acts as the main controller for the client-side application.
 * Mediates between the UI (MainGameFrame) and network logic (PlayerClient).
 */

public class GameClient {
    private MainGameFrame mainFrame;
    private PlayerClient networkClient;
    private User currentUser;
    // private logic.TexasHoldem texasHoldemLogic;
    // private graphics.TexasHoldemPanel texasHoldemPanel;

    public GameClient(PlayerClient networkClient) {
        this.networkClient = networkClient;
        this.mainFrame = new MainGameFrame();

        // Connect the network client to this controller
        this.networkClient.setGameControl(this);
        this.networkClient.setContainer((JPanel) mainFrame.getContentPane());

        // Set up control listeners (Login, Account creation, etc.)
        setUpControls();

        // Show the frame
        SwingUtilities.invokeLater(() -> {
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.pack();
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setVisible(true);
        });
    }

    private void setUpControls() {
        LoginControl loginControl = new LoginControl((JPanel) mainFrame.getContentPane());
        CreateAccountControl createAccountControl = new CreateAccountControl((JPanel) mainFrame.getContentPane());

        networkClient.setLoginControl(loginControl);
        networkClient.setCreateAccountControl(createAccountControl);
    }

    public void loginSuccessful(User user) {
        this.currentUser = user;
        networkClient.setCurrentUser(user);
        mainFrame.setPanel("lobby");
    }

    public void showLoginPanel() {
        mainFrame.setPanel("login");
    }

    public void showAccountCreationPanel() {
        mainFrame.setPanel("accountCreation");
    }

    public void showLobbyPanel() {
        mainFrame.setPanel("lobby");
    }

    public void sendToServer(Object message) {
        try {
            networkClient.sendToServer(message);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame, "Failed to send message to server.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}

