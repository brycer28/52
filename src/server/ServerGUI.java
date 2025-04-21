package server;

import javax.swing.*;
import java.awt.*;
import java.net.UnknownHostException;
import java.net.InetAddress;

public class ServerGUI extends JFrame {
    // GUI components
    private JTextArea unauthenticatedCountArea;
    private JTextArea authenticatedCountArea;
    private JTextArea logArea;
    private JButton startButton;
    private JButton stopButton;

    private GameServer server;
    private DatabaseClass database;
    private final int PORT = 8300;

    public ServerGUI() {
        super("Texas Hold'em Server");

        server = new GameServer(PORT);
        database = new DatabaseClass();
        database.setConnection("src/server/db.properties");
        server.setDatabase(database);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // create user count panel
        unauthenticatedCountArea = new JTextArea("Unauthenticated Users: " + server.getNumberOfClients(), 1, 20);
        authenticatedCountArea = new JTextArea("Authenticated Users: " + server.getNumberOfClients(), 1, 20);
        unauthenticatedCountArea.setEditable(false);
        authenticatedCountArea.setEditable(false);

        JPanel userCountPanel = new JPanel(new GridLayout(2,1));
        userCountPanel.add(unauthenticatedCountArea);
        userCountPanel.add(authenticatedCountArea);

        // create log area
        logArea = new JTextArea(10, 40);
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);

        // create buttons to start/stop server and a panel to contain them
        startButton = new JButton("Start Server");
        stopButton = new JButton("Stop Server");
        stopButton.setEnabled(false); // disable button until server starts

        startButton.addActionListener(e -> startServer());
        stopButton.addActionListener(e -> stopServer());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);


        // configure ServerGUI layout
        this.setLayout(new BorderLayout());
        this.add(userCountPanel, BorderLayout.NORTH);
        this.add(logScrollPane, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    // Starts the server and discovery service
    private void startServer() {
        // Close existing server if already running
        if (server != null) {
            try {
                server.close();
            } catch (Exception e) {}
        }

        // open server to listening for clients
        try {
            server = new GameServer(PORT);
            server.listen();

            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        } catch (Exception e) {
            log("ERROR: Failed to start server on port: " + PORT);
        }

        // get host address to log
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
            log("Server started on " + localHost.getHostName() + ":" + PORT);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    // Stops the server and resets GUI components
    private void stopServer() {
        try {
            server.close();
            log("Server stopped");
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        } catch (Exception e) {
            log("Error stopping server: " + e.getMessage() );
        }
    }

    public void log(String logMsg) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(logMsg + "\n");
        });
    }

    // Main method to launch the GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ServerGUI::new);
    }
}
