package Server;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.io.IOException;

public class ServerGUI extends JFrame {
    // GUI components
    private JTextField portField;
    private JTextField serverNameField;
    private JButton startButton;
    private JButton stopButton;
    private JTextArea logArea;
    private ServerClass server;
    private JTable clientsTable;
    private DefaultTableModel tableModel;
    private Timer refreshTimer;

    public ServerGUI() {
        setTitle("Poker Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top control panel with server name, port, start/stop buttons
        JPanel topPanel = new JPanel();
        serverNameField = new JTextField("Poker Server", 15);
        portField = new JTextField("8300", 10);
        startButton = new JButton("Start Server");
        stopButton = new JButton("Stop Server");
        stopButton.setEnabled(false);

        // Split pane for logs (top) and client table (bottom)
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        logArea = new JTextArea(10, 50);
        logArea.setEditable(false);

        // Set up the client table with columns
        String[] columnNames = {"Client ID", "Username", "Balance", "Status", "Activity"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        clientsTable = new JTable(tableModel);
        clientsTable.getTableHeader().setReorderingAllowed(false);

        // Add components to top panel
        topPanel.add(new JLabel("Server Name:"));
        topPanel.add(serverNameField);
        topPanel.add(new JLabel("Port:"));
        topPanel.add(portField);
        topPanel.add(startButton);
        topPanel.add(stopButton);

        // Add components to split pane
        splitPane.setTopComponent(new JScrollPane(logArea));
        splitPane.setBottomComponent(new JScrollPane(clientsTable));
        splitPane.setDividerLocation(200);

        // Add panels to frame
        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        // Set button functionality
        startButton.addActionListener(e -> startServer());
        stopButton.addActionListener(e -> stopServer());

        // Timer to refresh client table every second
        refreshTimer = new Timer(1000, e -> updateClientsTable());
        refreshTimer.start();

        // Basic frame setup
        setPreferredSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo(null);

        // Stop server safely when window closes
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (server != null) {
                    try {
                        server.close();
                    } catch (Exception ex) {
                        // Ignore errors on shutdown
                    }
                }
                System.exit(0);
            }
        });
    }

    // Updates the clients table with data from the server (needs work from client peeps)
    private void updateClientsTable() {
        if (server != null) {
            List<ConnectedClient> clients = server.getConnectedClients();
            tableModel.setRowCount(0); // Clear table first
            for (ConnectedClient client : clients) {
                tableModel.addRow(new Object[]{
                        client.getClientId(),
                        client.getUsername() != null ? client.getUsername() : "Not logged in",
                        client.getBalance(),
                        client.isAuthenticated() ? "Authenticated" : "Unauthenticated",
                        client.getActivity() != null ? client.getActivity() : "Not logged in"
                });
            }
        }
    }

    // Starts the server and discovery service
    private void startServer() {
        try {
            int port = Integer.parseInt(portField.getText());
            String serverName = serverNameField.getText().trim();

            if (serverName.isEmpty()) {
                logArea.append("Error: Server name cannot be empty\n");
                return;
            }

            // Close existing server if already running
            if (server != null) {
                try {
                    server.close();
                } catch (Exception e) {}
            }

            server = new ServerClass(port, serverName);
            server.setLogArea(logArea);
            server.listen(); // Begin listening for clients

            InetAddress localHost = InetAddress.getLocalHost();
            logArea.append("Server '" + serverName + "' started\n");
            logArea.append("Local IPv4 Address: " + localHost.getHostAddress() + "\n");
            logArea.append("Port: " + port + "\n");

            // Update GUI states
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            portField.setEnabled(false);
            serverNameField.setEnabled(false);

            startDiscoveryService(port, serverName); // Respond to client discovery messages
        } catch (Exception e) {
            logArea.append("Error starting server: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    // Starts a background service to reply to UDP discovery messages
    private void startDiscoveryService(int gamePort, String serverName) {
        new Thread(() -> {
            try (DatagramSocket socket = new DatagramSocket(8301)) {
                byte[] receiveData = new byte[1024];
                while (!socket.isClosed() && server != null) {
                    try {
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        socket.receive(receivePacket);

                        String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        if (message.equals("POKER_SERVER_DISCOVERY")) {
                            String response = gamePort + "|" + serverName;
                            byte[] sendData = response.getBytes();
                            DatagramPacket sendPacket = new DatagramPacket(
                                    sendData,
                                    sendData.length,
                                    receivePacket.getAddress(),
                                    receivePacket.getPort()
                            );
                            socket.send(sendPacket); // Respond to the client
                        }
                    } catch (IOException e) {
                        if (!socket.isClosed()) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Stops the server and resets GUI components
    private void stopServer() {
        try {
            server.close();
            logArea.append("Server '" + server.getServerName() + "' stopped\n");
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            portField.setEnabled(true);
            serverNameField.setEnabled(true);
        } catch (Exception e) {
            logArea.append("Error stopping server: " + e.getMessage() + "\n");
        }
    }

    // Main method to launch the GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ServerGUI().setVisible(true);
        });
    }
}
