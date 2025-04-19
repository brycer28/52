package account;
import javax.swing.*;
import ocsf.client.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class LoginControl {

    private JPanel container;
    private client.GameClient client;


    public LoginControl(JPanel container) { // Add GameClient to parameters
        this.container = container;
    }

    public void actionPerformed(ActionEvent action) {

        String command = action.getActionCommand();

        if (command.equals("Cancel")) {
            CardLayout cardLayout = (CardLayout) container.getLayout();
            cardLayout.show(container, "InitialPanel");
        }

        else if (command.equals("Login")) {
            LoginPanel loginPanel = (LoginPanel)container.getComponent(2);
            LoginData data = new LoginData(loginPanel.getUsername(), loginPanel.getPassword());

            if (data.getUsername().equals("") || data.getPassword().equals("")) {
                JOptionPane.showMessageDialog(null, "Username and Password must not be empty");
                return;
            }

            try {
                client.sendToServer(data);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Something went wrong. Please try again.");
                e.printStackTrace(); // Optional: helps with debugging if something goes wrong
            }

        }
    }

    public void success() {
        LoginPanel loginPanel = (LoginPanel)container.getComponent(1);
        CardLayout cardLayout = (CardLayout) container.getLayout();
        cardLayout.show(container, "TexasHoldemPanel");
    }

}
