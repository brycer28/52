package AccountFiles;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    private JLabel errorLabel;
    private JTextField username;
    private JPasswordField passwordField;
    private JButton submitButton;
    private JButton cancelButton;

    public String getUsername() {
        return username.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public void setError(String error) {
        errorLabel.setText(error);
    }

    public LoginPanel() {

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        username = new JTextField(15);
        passwordField = new JPasswordField(15);
        submitButton = new JButton("Create Account");
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
        // adds confirm password label and field
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        // creating submit and cancel buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

    }

}
