package account;

import java.io.Serializable;

public class LoginData implements Serializable {
    private String username;
    private String password;
    private boolean success;
    private User user;

    public LoginData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginData(boolean success, User user) {
        this.success = success;
        this.user = user;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public User getUser() {return user;}
    public boolean isSuccess() {return success;}

}
