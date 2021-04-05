package server;

import controller.Main;
import model.CardCollection;
import model.card.ActionCard;
import model.card.Card;
import model.card.CardStack;

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
    private String name, sendMessage;
    private int points;
    private String playerInfoString;
    private boolean myTurn;

    public ServerSideConnection(Socket s) {
        socket = s;
        myTurn = false;
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
        label:
        while(!socket.isClosed()) {
            try {
                String receivedMessage = receive();
                sendMessage = receivedMessage;
                Scanner scanner = new Scanner(receivedMessage);
                String command = scanner.next();

                switch (command) {
                    case "join" -> {
                        name = scanner.next();
                        points = scanner.nextInt();
                        sendMessage = "inGame ";
                        DominionServer server = Main.getServer();
                        List<ServerSideConnection> serverSideConnections = server.getServerSideConnections();
                        for (ServerSideConnection ssc : serverSideConnections) {
                            if (ssc.equals(this)) continue;
                            sendMessage += ssc.getPlayerInfoString();
                            individualSend(sendMessage);
                            sendMessage = "inGame ";
                        }
                        CardCollection cardsInGame = Main.getServer().getCardsInGame();
                        sendMessage = "cardsInGame " + getPlayerInfoString();
                        parseCardsInGame(cardsInGame);
                        individualSend(sendMessage);
                        sendMessage = "connected " + getPlayerInfoString();
                    }
                    case "endTurn" -> assignNewTurn();
                    case "leaveGame" -> {
                        broadcast(sendMessage);
                        if(myTurn) {
                            assignNewTurn();
                        }
                        Main.getServer().getServerSideConnections().remove(this);
                        shutDown();
                    }
                }
                broadcast(sendMessage);

            } catch (IOException e) {
                System.out.println("receive error @" + name + "'s SSC");
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

    private void parseCardsInGame(CardCollection cardsInGame) {
        for (Card card : cardsInGame.getDistinctTreasureCards()) {
            sendMessage += card.getName() + " ";
        }
        for(Card card: cardsInGame.getDistinctVictoryCards()) {
            sendMessage += card.getName() + " ";
        }
        for(Card card: cardsInGame.getDistinctActionCards()) {
            sendMessage += card.getName() + " ";
        }
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

    public void assignNewTurn() throws IOException {
        List<ServerSideConnection> connections = Main.getServer().getServerSideConnections();

        //---------------------------------------------------------//
        System.out.println("List of SSCs @SSC_assignNewTurn");
        for(ServerSideConnection ssc: connections) {
            System.out.print(ssc.getName() + "   ");
        }
        //---------------------------------------------------------//

        int indexOfThis = connections.indexOf(this);
        sendMessage = "startTurn ";
        setTurn(false);
        if(indexOfThis==connections.size()-1) {
            sendMessage+=connections.get(0).getPlayerInfoString();
            connections.get(0).setTurn(true);
        }
        else {
            sendMessage+=connections.get(indexOfThis + 1).getPlayerInfoString();
            connections.get(indexOfThis + 1).setTurn(true);
        }
        individualSend(sendMessage);
    }
    public void shutDown() throws IOException {
        dataIn.close();
        dataOut.close();
        socket.close();
    }

    private void setTurn(boolean b) {
        myTurn = b;
    }
}
