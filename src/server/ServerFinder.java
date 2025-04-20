package server;
/*
 * ServerFinder sends a UDP broadcast to find nearby poker servers
 * and returns a list of discovered servers.
 */

import java.net.*;
import java.util.*;

    public class ServerFinder {
    private static final int DISCOVERY_PORT = 8301;
    private static final String DISCOVERY_MESSAGE = "TXHOLDEM_SERVER_DISCOVERY";
    private List<ServerInfo> discoveredServers = new ArrayList<>();

    // Holds info about a discovered server
    public static class ServerInfo {
        private String name;
        private String address;
        private int port;

        public ServerInfo(String name, String address, int port) {
            this.name = name;
            this.address = address;
            this.port = port;
        }

        public String getName() { return name; }
        public String getAddress() { return address; }
        public int getPort() { return port; }
    }

    // Sends a discovery message and listens for responses from servers
    public List<ServerInfo> discoverServers() {
        discoveredServers.clear();

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);
            socket.setSoTimeout(1000);

            byte[] sendData = DISCOVERY_MESSAGE.getBytes();
            broadcastToNetwork(socket, sendData);

            byte[] receiveData = new byte[1024];
            long endTime = System.currentTimeMillis() + 2000;

            // Listen for responses for 2 seconds
            while (System.currentTimeMillis() < endTime) {
                try {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    socket.receive(receivePacket);

                    String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    String[] parts = response.split("\\|");

                    if (parts.length == 2) {
                        ServerInfo server = new ServerInfo(
                                parts[1],
                                receivePacket.getAddress().getHostAddress(),
                                Integer.parseInt(parts[0])
                        );
                        if (!discoveredServers.contains(server)) {
                            discoveredServers.add(server);
                        }
                    }
                } catch (SocketTimeoutException e) {
                    // Do nothing on timeout — just keep looping
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return discoveredServers;
    }

    // Broadcasts the discovery message to the network
    private void broadcastToNetwork(DatagramSocket socket, byte[] sendData) throws Exception {
        InetAddress subnet = InetAddress.getByName("255.255.255.255");
        DatagramPacket sendPacket = new DatagramPacket(
                sendData, sendData.length, subnet, DISCOVERY_PORT);
        socket.send(sendPacket);
    }
}
