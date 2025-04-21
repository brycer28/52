package account;

import client.GameClient;
import client.MainGameFrame;
import org.junit.*;
import server.DatabaseClass;
import server.GameServer;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.io.IOException;

import static org.junit.Assert.*;

// need XAMPP to be running to run this test file
public class LoginTest {

    private LoginControl loginControl;
    private MainGameFrame frame;
    private GameClient gameClient;
    private LoginPanel loginPanel;
    private GameServer gameServer;
    private DatabaseClass db;

    @Before
    public void setUp() throws Exception {
        gameServer = new GameServer(8300);
        gameServer.listen();

        gameClient = new GameClient("localhost", 8300);
        frame = new MainGameFrame();
        loginControl = new LoginControl(frame, gameClient);

        loginPanel = new LoginPanel(loginControl);

        db = new DatabaseClass();
        gameServer.setDatabase(db);

        JPanel cardPanel = frame.getCardPanel();
        cardPanel.add(new JPanel());
        cardPanel.add(loginPanel);

    }

    @After
    public void tearDown() throws Exception {
        if (gameServer != null) {
            gameServer.close();
        }
    }

    @Test
    public void testLoginPanelInitialized() {
        assertNotNull("LoginPanel should be initialized",loginControl.getFrame());
    }

    @Test
    public void testSuccessfulLoginSendsMessage() {
        // Simulate Login button click
        loginPanel.getUsernameField().setText("thi");
        loginPanel.getPasswordField().setText("thi123");
        ActionEvent loginEvent = new ActionEvent(loginPanel, ActionEvent.ACTION_PERFORMED, "Login");
        loginControl.actionPerformed(loginEvent);

        assertTrue("Client should be connected", gameClient.isConnected());
    }

    @Test
    public void testLoginHandlesExceptionGracefully() throws IOException {
        loginPanel.getUsernameField().setText("thi");
        loginPanel.getPasswordField().setText("thi123");

        gameClient.closeConnection();

        ActionEvent loginEvent = new ActionEvent(loginPanel, ActionEvent.ACTION_PERFORMED, "Login");
        loginControl.actionPerformed(loginEvent);

        assertTrue("Test passed without crashing", true);
    }

}