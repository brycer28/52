package Client;

import javax.swing.*;
import java.awt.*;

import account.CreateAccountControl;
import account.InitialControl;
import account.InitialPanel;
import account.LoginControl;
import graphics.*;

public class MainGameFrame extends JFrame {
    private JPanel cardPanel;         // The container for all panels (CardLayout)
    private CardLayout cardLayout;    // Layout manager


    // Panels for different views
    private JPanel loginPanel;
    private JPanel createAccountPanel;
    private JPanel lobbyPanel;
    private JPanel gamePlayPanel;
    private JPanel statsPanel;

    //view 1 = initial panel
    //view 2 = login panel
    //view 3 = createAccount panel
    //view 4 = game panel

    public MainGameFrame() {
        super("Texas Hold'em"); // Set window title
        //server.Game game = new Game();
        // Initialize layout and card panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        //lobbyPanel = new LobbyPanel();
        //gamePlayPanel = new TexasHoldemPanel();              // or whatever your gameplay panel is
        //statsPanel = new StatsPanel();

        //Controller classes
        InitialControl ic = new InitialControl(cardPanel, client);
        LoginControl lc = new LoginControl(cardPanel, client);
        CreateAccountControl cc = new CreateAccountControl(cardPanel, client);

        //Views
        JPanel initPanel = new InitialPanel(ic);
        loginPanel = new account.LoginPanel(lc);                     // assuming you have these classes
        createAccountPanel = new account.CreateAccountPanel(cc);

        cardPanel.add(initPanel, "initPanel");
        cardPanel.add(loginPanel, "login");
        cardPanel.add(createAccountPanel, "createAccountPanel");

        cardPanel.add(lobbyPanel, "lobby");
        cardPanel.add(gamePlayPanel, "texasHoldem");
        cardPanel.add(statsPanel, "stats");

        cardLayout.show(cardPanel, "initPanel");

        // Set the card panel as the content pane of the JFrame
        this.setContentPane(cardPanel);

        this.setSize(550,350);
        this.setVisible(true);
    }

    /**
     * Switches the visible panel using the CardLayout.
     *
     * @param panelName The name of the panel to show ("login", "lobby", etc.)
     */
    public void setPanel(String panelName) {
        cardLayout.show(cardPanel, panelName);
    }

    /**
     * Returns the main card panel so other classes can add or interact with it.
     */
    public JPanel getCardPanel() {
        return cardPanel;
    }
}

