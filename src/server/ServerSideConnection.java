package server;

import controller.Main;
import model.card.ActionCard;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;
import java.util.Scanner;

public class ServerSideConnection implements Runnable {

    private Socket socket;
    private DataOutputStream dataOut;
    private DataInputStream dataIn;
    private SocketAddress clientIP;
    private String name;
    private int points;
    private String playerInfoString;

    public ServerSideConnection(Socket s) {
        socket = s;
        clientIP = s.getRemoteSocketAddress();
        try {
            dataIn = new DataInputStream(socket.getInputStream());
            dataOut = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println("IOException from SSC Constructor");
        }
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        while(true) {
            try {
                String receivedMessage = receive();
                String sendMessage = receivedMessage;
                Scanner scanner = new Scanner(receivedMessage);
                String command = scanner.next();

                if(command.equals("join")) {
                    name = scanner.next();
                    points = scanner.nextInt();
                    sendMessage = "inGame ";

                    DominionServer server = Main.getServer();
                    List<ServerSideConnection> serverSideConnections = server.getServerSideConnections();

                    for(ServerSideConnection ssc: serverSideConnections) {
                        if(ssc.equals(this)) continue;
                        sendMessage+=ssc.getPlayerInfoString();
                        individualSend(sendMessage);
                        sendMessage = "inGame ";
                    }

                    List<ActionCard> actionCardsInGame = Main.getServer().getActionCardsInGame();
                    sendMessage = "actionCardsInGame " + getPlayerInfoString();
                    for(ActionCard card: actionCardsInGame) {
                        sendMessage+= card.getName() + " ";
                        sendMessage+= "10 ";
                    }
                    individualSend(sendMessage);

                    sendMessage = "connected " + getPlayerInfoString();

                    if(server.getNumClients()==server.getMaxNumPlayers()) {
                        int firstPlayerTurn = (int) (Math.random()*server.getMaxNumPlayers());
                        if(serverSideConnections.get(firstPlayerTurn).getName()!=null) {
                            serverSideConnections.get(firstPlayerTurn).broadcastAll(
                                    "startTurn " + serverSideConnections.get(firstPlayerTurn).getPlayerInfoString());
                        } else {
                            serverSideConnections.get(0).broadcastAll(
                                    "startTurn " + serverSideConnections.get(0).getPlayerInfoString());
                            System.out.println("player0 selected");
                        }
                    }
                }

                else if(command.equals("endTurn")) {
                    List<ServerSideConnection> connections = Main.getServer().getServerSideConnections();
                    int indexOfThis = connections.indexOf(this);
                    sendMessage = "startTurn ";
                    if(indexOfThis==connections.size()-1) {
                        sendMessage+=connections.get(0).getPlayerInfoString();
                    }
                    else sendMessage+=connections.get(indexOfThis + 1).getPlayerInfoString();
                    individualSend(sendMessage);
                }

                broadcast(sendMessage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String receive() throws IOException {
        return dataIn.readUTF();
    }
    public void broadcast(String s) throws IOException {
        for(ServerSideConnection ssc: Main.getServer().getServerSideConnections()) {
            if(ssc.equals(this)) continue;
            ssc.getDataOut().writeUTF(s);
            ssc.getDataOut().flush();
        }
    }
    public void broadcastAll(String s) throws IOException {
        for(ServerSideConnection ssc: Main.getServer().getServerSideConnections()) {
            ssc.getDataOut().writeUTF(s);
            ssc.getDataOut().flush();
        }
    }
    public void individualSend(String s) throws IOException {
        dataOut.writeUTF(s);
        dataOut.flush();
    }

    public DataOutputStream getDataOut() {
        return dataOut;
    }

    public String getPlayerInfoString() {
        playerInfoString = name + " " + points + " ";
        return playerInfoString;
    }

    public String getName() {return name;}
    public int getPoints() {
        return points;
    }
}
