package AccountFiles;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.CardLayout;
import java.io.IOException;

import Client.*;

public class CreateAccountControl{
    private JPanel container;
    private GameClient client;

    public CreateAccountControl(JPanel container) {
        this.container = container;
    }
        public void actionPerformed(ActionEvent action) {

            String command = action.getActionCommand();

            if (command.equals("Cancel")) {
                CardLayout cardLayout = (CardLayout) container.getLayout();
                cardLayout.show(container, "InitialPanel");
            }

            else if (command.equals("Login")) {
                CreateAccountPanel createPanel = (CreateAccountPanel)container.getComponent(3);
                CreateAccountData data = new CreateAccountData(createPanel.getUsername(), createPanel.getPassword());

                if (data.getUsername().isBlank() || data.getPassword().isBlank()) {
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

        public void success() {
            CreateAccountPanel createPanel = (CreateAccountPanel)container.getComponent(2);
            CardLayout cardLayout = (CardLayout) container.getLayout();
            cardLayout.show(container, "TexasHoldemPanel");
        }
    }



