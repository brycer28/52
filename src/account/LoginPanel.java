package account;

import java.awt.*;
import java.util.Arrays;
import javax.swing.*;

public class LoginPanel extends JPanel {
    private JTextField username;
    private JPasswordField passwordField;
    private JButton submitButton;
    private JButton cancelButton;

    public LoginPanel(LoginControl loginControl) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        username = new JTextField(15);
        passwordField = new JPasswordField(15);
        submitButton = new JButton("Login");
        cancelButton = new JButton("Cancel");
        // adds username label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        add(username, gbc);
        // adds password label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // creating submit and cancel buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        // add controller for action listener
        submitButton.addActionListener(loginControl);
        cancelButton.addActionListener(loginControl);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;

        add(buttonPanel, gbc);
    }

    public String getUsername() {
        return username.getText();
    }

    public String getPassword() {
        //return Arrays.toString(passwordField.getPassword()); // getPassword returns a char[]
        return new String(passwordField.getPassword());
    }
}
