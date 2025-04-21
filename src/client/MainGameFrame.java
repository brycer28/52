package client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import account.*;
import graphics.TexasHoldemPanel;

public class MainGameFrame extends JFrame {
    private JPanel cardPanel;         // The container for all panels (CardLayout)
    private CardLayout cardLayout;    // Layout manager

    // Panels for different views
    private InitialPanel initialPanel;
    private JPanel loginPanel;
    private JPanel createAccountPanel;
    private TexasHoldemPanel gamePanel;

    public enum View {
        INITIAL, LOGIN, CREATE, GAME
    }

    private GameClient client;
    private static final int PORT = 8300;

    public MainGameFrame(GameClient client) {
        super("Texas Hold'em"); // Set window title
        this.client = client;

        // Initialize layout and card panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        //Controller classes
        InitialControl ic = new InitialControl(this);
        LoginControl lc = new LoginControl(this, client);
        CreateAccountControl cc = new CreateAccountControl(this, client);
        client.setLoginControl(lc);
        client.setCreateAccountControl(cc);

        //Views
        initialPanel = new InitialPanel(ic);
        loginPanel = new LoginPanel(lc);
        createAccountPanel = new CreateAccountPanel(cc);


//        gamePanel = new TexasHoldemPanel(); // this should take in a GameState

        cardPanel.add(initialPanel, "1");
        cardPanel.add(loginPanel, "2");
        cardPanel.add(createAccountPanel, "3");
//        cardPanel.add(gamePanel, "4");

        // Select which panel to show first
        cardLayout.show(cardPanel, "1");

        // Set the card panel as the content pane of the JFrame
        this.setContentPane(cardPanel);

        this.setSize(550,350);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameClient client = null;
            try {
                client = new GameClient("localhost", PORT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            new MainGameFrame(client);
        });
    }

    public void setPanel(View v) {
        switch (v) {
            case INITIAL -> cardLayout.show(cardPanel, "1");

            case LOGIN -> cardLayout.show(cardPanel, "2");

            case CREATE -> cardLayout.show(cardPanel, "3");

//            case GAME -> cardLayout.show(cardPanel, "4");
        }
    }

    public JPanel getCardPanel() { return cardPanel; }
    public GameClient getClient() { return client; }
}

