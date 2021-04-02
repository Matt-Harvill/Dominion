package server;

import controller.Main;
import model.card.ActionCard;
import model.card.Card;

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

    private final List<ServerSideConnection> serverSideConnections;

    private List<ActionCard> actionCardsInGame;

    public DominionServer(){
        numClients = 0;
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
            while (numClients<maxNumPlayers);
            System.out.println("Max players reached");
            startGame();
        } catch (IOException ex) {
            System.out.println("IOException @DS_acceptConnections()");
        }
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
    public void setMaxNumPlayers(int maxNumPlayers) {
        this.maxNumPlayers = maxNumPlayers;
    }
    public List<ActionCard> getActionCardsInGame() {
        return actionCardsInGame;
    }
    public void setActionCardsInGame(List<ActionCard> actionCardsInGame) {
        this.actionCardsInGame = actionCardsInGame;
    }

    public void serverShutDown() throws IOException {
        for(ServerSideConnection ssc: serverSideConnections) {
            ssc.individualSend("serverShutDown " + ssc.getPlayerInfoString());
            ssc.shutDown();
        }
        serverSocket.close();
    }
    public void startGame() {
        try {
            serverSocket.close();
            int firstPlayerTurn = (int) (Math.random()*serverSideConnections.size());

            while(serverSideConnections.get(firstPlayerTurn).getName()==null) {
                Thread.sleep(5);
            }
            serverSideConnections.get(firstPlayerTurn).broadcastAll("startTurn " + serverSideConnections.get(firstPlayerTurn).getPlayerInfoString());
        } catch (Exception ex) {
            System.out.println("Exception @DominionServer_startGame");
        }
        System.out.println("Game started");
    }
}
