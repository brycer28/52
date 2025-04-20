package account;
import client.MainGameFrame;
import client.MainGameFrame.*;
import logic.GameMessage;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class InitialControl implements ActionListener
{
    // Private data field for storing the container.
    private MainGameFrame frame;

    //view 1 = initial panel
    //view 2 = login panel
    //view 3 = createAccount panel
    //view 4 = game panel

    // Constructor for the initial controller.
    public InitialControl(MainGameFrame frame) {
        this.frame = frame;
    }

    // Handle button clicks.
    public void actionPerformed(ActionEvent ae)
    {
        // Get the name of the button clicked.
        String command = ae.getActionCommand();

        // The Login button takes the user to the login panel.
        if (command.equals("Login"))
        {
            frame.setPanel(View.LOGIN);
        }

        // The Create button takes the user to the create account panel.
        else if (command.equals("Create Account"))
        {
            frame.setPanel(View.CREATE);
        }
    }
}
