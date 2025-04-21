package account;

import client.GameClient;
import client.MainGameFrame;
import client.MainGameFrame.*;
import logic.GameMessage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class CreateAccountControl implements ActionListener {
    private MainGameFrame frame;
    private client.GameClient client;

    public CreateAccountControl(MainGameFrame frame, GameClient client) {
        this.frame = frame;
        this.client = client;
    }

    public void actionPerformed(ActionEvent action) {
        String command = action.getActionCommand();

        if (command.equals("Cancel")) {
            frame.setPanel(View.INITIAL);
        }

        else if (command.equals("Create Account")) {
            CreateAccountPanel createPanel = (CreateAccountPanel) frame.getCardPanel().getComponent(2);
            CreateAccountData data = new CreateAccountData(createPanel.getUsername(), createPanel.getPassword());

            // validate create account credentials
            if (data.getUsername().isBlank() || data.getPassword().isBlank()) {
                JOptionPane.showMessageDialog(null, "Username and Password must not be empty");
                return;
            }
            else if (!createPanel.getPassword().equals(createPanel.getConfirmPassword()))
            {
                JOptionPane.showMessageDialog(null, "The two passwords must match");
                return;
            }
            if (createPanel.getPassword().length() < 6)
            {
                JOptionPane.showMessageDialog(null, "The password must be at least 6 characters");
                return;
            }

            try {
                GameMessage<CreateAccountData> msg = new GameMessage<>(GameMessage.MessageType.CREATE_ACC, data);
                client.sendToServer(msg);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Something went wrong. Please try again.");
                e.printStackTrace(); // Optional but helpful for debugging
            }
        }
    }

    public void createAccountSuccess() {
        frame.setPanel(View.GAME);
    }
}
