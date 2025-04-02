package AccountFiles;

import javax.swing.*;

public class LoginPanel extends JPanel {

    private JTextField username;
    private JPasswordField password;
    private JLabel errorLabel;

    public String getUsername() {
        return username.getText();
    }

    public String getPassword() {
        return password.getText();
    }

    public void setError(String error) {
        errorLabel.setText(error);
    }

    public LoginPanel() {

    }

}
