package Client;
import ocsf.server.ConnectiontoClient;

public class CurrentClient {
    private long clientId;
    private String username;
    private int balance;
    private boolean authenticated;
    private String activity;
    private ConnectiontoClient client;

    public CurrentClient(long clientId, String username, int balance, boolean authenticated, String activity) {
        this.clientId = clientId;
        this.username = username;
        this.balance = balance;
        this.authenticated = authenticated;
        this.activity = "Offline";

    }

    public ConnectiontoClient getClient() {
        return client;
    }

    public long getClientId() {return clientId;}
    public String getUsername() {return username;}
    public int getBalance() {return balance;}
    public boolean isAuthenticated() {return authenticated;}
    public String getActivity() {return activity;}

    public void setUsername(String username) {this.username = username;}
    public void setBalance(int balance) {this.balance = balance;}
    public void setAuthenticated(boolean authenticated) {this.authenticated = authenticated;}
    public void setActivity(String activity) {this.activity = activity;}
}
