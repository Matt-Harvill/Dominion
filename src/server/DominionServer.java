package server;

import controller.Main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class DominionServer {

    private ServerSocket serverSocket;
    private int numClients, portNumber, maxNumPlayers;
    private String ipAddress;

    private List<ServerSideConnection> serverSideConnections;

    public DominionServer(int maxNumPlayers){
        numClients = 0;
        this.maxNumPlayers = maxNumPlayers;
        serverSideConnections = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(0);
            ipAddress = InetAddress.getLocalHost().getHostAddress();
            portNumber = serverSocket.getLocalPort();
        } catch (Exception ex) {
            System.out.println("IOException from DominionServer()");
        }
    }

    public void acceptConnections() {
        try {
            System.out.println("Accepting connections...");

            do {
                Socket s = serverSocket.accept();
                numClients++;
                serverSideConnections.add(new ServerSideConnection(s));
                System.out.println("There are " + numClients + " clients connected");
            }
            while (/*!Main.getServerController().getGameStart() &&*/ numClients<maxNumPlayers);
            System.out.println("No longer accepting connections");

        } catch (IOException ex) {
            System.out.println("IOException from acceptConnections()");
        }
    }
    public void closeServerSocket() throws IOException {
        serverSocket.close();
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
    public int getNumClients() {
        return numClients;
    }
    public int getMaxNumPlayers() {return maxNumPlayers;}
    public int getPortNumber() {
        return portNumber;
    }
    public String getIpAddress() {
        return ipAddress;
    }
    public List<ServerSideConnection> getServerSideConnections() {
        return serverSideConnections;
    }
}
