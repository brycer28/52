package account;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class InitialControl implements ActionListener
{
    // Private data field for storing the container.
    private JPanel container;
    private client.GameClient client;

    //view 1 = initial panel
    //view 2 = login panel
    //view 3 = createAccount panel
    //view 4 = game panel


    // Constructor for the initial controller.
    public InitialControl(JPanel container, client.GameClient client)
    {
        this.container = container;
        this.client = client;
    }

    // Handle button clicks.
    public void actionPerformed(ActionEvent ae)
    {
        // Get the name of the button clicked.
        String command = ae.getActionCommand();

        // The Login button takes the user to the login panel.
        if (command.equals("Login"))
        {
            LoginPanel loginPanel = (LoginPanel)container.getComponent(1);
            CardLayout cardLayout = (CardLayout)container.getLayout();
            cardLayout.show(container, "2");

        }

        // The Create button takes the user to the create account panel.
        else if (command.equals("Create Account"))
        {
            CreateAccountPanel createAccountPanel = (CreateAccountPanel)container.getComponent(2);
            CardLayout cardLayout = (CardLayout)container.getLayout();
            cardLayout.show(container, "3");
        }
    }
}
