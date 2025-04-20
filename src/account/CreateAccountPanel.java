package account;

import java.awt.*;
import javax.swing.*;

public class CreateAccountPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton submitButton;
    private JButton cancelButton;

    public CreateAccountPanel(CreateAccountControl createAccountControl) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        confirmPasswordField = new JPasswordField(15);
        submitButton = new JButton("Create Account");
        cancelButton = new JButton("Cancel");
        // adds username label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        add(usernameField, gbc);
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
        add(confirmPasswordField, gbc);

        // creating submit and cancel buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        // add controller as action listener
        submitButton.addActionListener(createAccountControl);
        cancelButton.addActionListener(createAccountControl);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getConfirmPassword() {
        return new String(confirmPasswordField.getPassword());
    }

    public JButton getSubmitButton() {
        return submitButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }
}
