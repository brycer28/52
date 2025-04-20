package account;
import javax.swing.*;

import client.GameClient;
import client.MainGameFrame;
import client.MainGameFrame.*;
import logic.GameMessage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginControl implements ActionListener {
    private MainGameFrame frame;
    private client.GameClient client;

    public LoginControl(MainGameFrame frame, GameClient client) {
        this.frame = frame;
        this.client = client;
    }

    public void actionPerformed(ActionEvent action) {
        String command = action.getActionCommand();

        if (command.equals("Cancel")) {
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "initialPanel");
            //frame.setPanel(View.INITIAL);
        }
        else if (command.equals("Login")) {
            LoginPanel loginPanel = (LoginPanel) frame.getCardPanel().getComponent(2);
            LoginData loginData = new LoginData(loginPanel.getUsername(), loginPanel.getPassword());

            if (loginData.getUsername().equals("") || loginData.getPassword().equals("")) {
                JOptionPane.showMessageDialog(null, "Username and Password must not be empty");
                return;
            }

            try {
                GameMessage<LoginData> msg = new GameMessage<>(GameMessage.MessageType.LOGIN, loginData);
                client.sendToServer(msg);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Something went wrong. Please try again.");
                e.printStackTrace(); // Optional: helps with debugging if something goes wrong
            }
        }
    }

    public void displayError(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
    // not sure about this ?
    public void success() {
        frame.setPanel(View.GAME);
    }
}
