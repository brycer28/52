package AccountFiles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class LoginControl {

    private JPanel container;
    //private GameClient client;

    public LoginControl(JPanel container) { // Add GameClient to parameters
        this.container = container;
    }

    public void actionPerformed(ActionEvent action) {
        Properties prop = new Properties();

        try {
            FileInputStream fis = new FileInputStream("src/LoginFiles/loginCredentials.properties");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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
            }
            catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Something went wrong. Please try again");
            }
        }


    }

}
