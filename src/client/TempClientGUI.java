package client;

import server.ServerFinder;
import server.ServerFinder.ServerInfo;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class TempClientGUI extends JFrame {
    private JList<String> serverList;
    private DefaultListModel<String> serverListModel;
    private List<ServerInfo> discoveredServers;

    public TempClientGUI() {
        setTitle("Connect to Poker Server");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        serverListModel = new DefaultListModel<>();
        serverList = new JList<>(serverListModel);
        JScrollPane scrollPane = new JScrollPane(serverList);

        JButton refreshButton = new JButton("Refresh Servers");
        JButton connectButton = new JButton("Connect");

        refreshButton.addActionListener(e -> discoverServers());
        connectButton.addActionListener(e -> connectToSelectedServer());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(connectButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        discoverServers(); // auto-discover on launch
    }

    private void discoverServers() {
        serverListModel.clear();
        ServerFinder finder = new ServerFinder();
        discoveredServers = finder.discoverServers();

        if (discoveredServers.isEmpty()) {
            serverListModel.addElement("No servers found");
        } else {
            for (ServerInfo server : discoveredServers) {
                serverListModel.addElement(server.getName() + " @ " + server.getAddress() + ":" + server.getPort());
            }
        }
    }

    private void connectToSelectedServer() {
        int selectedIndex = serverList.getSelectedIndex();
        if (selectedIndex == -1 || discoveredServers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a server to connect to.");
            return;
        }

        ServerInfo selected = discoveredServers.get(selectedIndex);
        try {
            PlayerClient playerClient = new PlayerClient(selected.getAddress(), selected.getPort());
            GameClient gameClient = new GameClient(playerClient); // This starts the full GUI flow
            dispose(); // Close this window after connecting
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TempClientGUI gui = new TempClientGUI();
            gui.setVisible(true);
        });
    }
}
