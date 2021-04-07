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
    private String name;
    private int points, numCardsInDeck, initialCardStacksNum;
    private String playerInfoString;
    private boolean myTurn;
    private List<CardStack> cardStacks;

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
        while(!socket.isClosed()) {
            try {
                String receivedMessage = receive();
                String sendMessage = receivedMessage;
                Scanner scanner = new Scanner(receivedMessage);
                String command = scanner.next();

                switch (command) {
                    case "join" -> {
                        name = scanner.next();
                        points = scanner.nextInt();
                        numCardsInDeck = scanner.nextInt();

                        int numTimesNameTaken = nameAlreadyTaken(name);
                        if(numTimesNameTaken > 0) {
                            name = name + "(" + numTimesNameTaken + ")";
                            individualSend("nameChanged " + getPlayerInfoString());
                        }

                        sendMessage = "inGame ";
                        DominionServer server = Main.getServer();
                        List<ServerSideConnection> serverSideConnections = server.getServerSideConnections();

                        //Test
                        for(ServerSideConnection ssc: serverSideConnections) {
                            System.out.print("ssc name: " + ssc.getName());
                        }
                        System.out.println("@" + name + "'s ssc join");
                        //

                        for (ServerSideConnection ssc : serverSideConnections) {
                            if (ssc.equals(this)) continue;
                            sendMessage += ssc.getPlayerInfoString();
                            individualSend(sendMessage);
                            sendMessage = "inGame ";
                        }
                        CardCollection cardsInGame = Main.getServer().getCardsInGame();
                        sendMessage = "cardsInGame " + getPlayerInfoString();
                        sendMessage+= parseCardsInGame(cardsInGame);
                        individualSend(sendMessage);
                        sendMessage = "connected " + getPlayerInfoString();
                    }
                    case "endTurn" -> {
                        if(gameOver()) {
                            myTurn = false;
                            sendMessage = "gameOver " + getPlayerInfoString();
                            individualSend(sendMessage);
                        } else {
                            sendMessage = assignNewTurn();
                        }
                    }
                    case "leaveGame" -> {
                        if(myTurn) {
                            broadcast(sendMessage);
                            sendMessage = assignNewTurn();
                        }
                        Main.getServer().getServerSideConnections().remove(this);
                        shutDown();
                    }
                    case "buyCard" -> {
                        //Get the name, points, numCardsInDeck sorted out first
                        scanner.next(); scanner.nextInt(); scanner.nextInt();

                        String cardName = scanner.next();
                        for (CardStack cardStack : cardStacks) {
                            if (cardStack.getCard().getName().equals(cardName)) {
                                cardStack.decrement();
                                if(cardStack.getNumCards()==0) cardStacks.remove(cardStack);
                                break;
                            }
                        }
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

    private int nameAlreadyTaken(String name) {
        int numTimesTaken = 0;

        for(ServerSideConnection ssc: Main.getServer().getServerSideConnections()) {
            if(ssc.equals(this)) continue;
            if((ssc.getName().contains(name) && ssc.getName().contains("(")) || ssc.getName().equals(name)) {
                numTimesTaken++;
            }
        }
        return numTimesTaken;
    }

    private String parseCardsInGame(CardCollection cardsInGame) {
        String sendMessage = "";
        for (Card card : cardsInGame.getDistinctTreasureCards()) {
            sendMessage += card.getName() + " ";
        }
        for(Card card: cardsInGame.getDistinctVictoryCards()) {
            sendMessage += card.getName() + " ";
        }
        for(Card card: cardsInGame.getDistinctActionCards()) {
            sendMessage += card.getName() + " ";
        }
        return sendMessage;
    }

    public DataOutputStream getDataOut() {
        return dataOut;
    }
    public String getPlayerInfoString() {
        playerInfoString = name + " " + points + " " + numCardsInDeck + " ";
        return playerInfoString;
    }
    public String getName() {return name;}
    public int getPoints() {
        return points;
    }

    public String assignNewTurn() throws IOException {
        List<ServerSideConnection> connections = Main.getServer().getServerSideConnections();

        //---------------------------------------------------------//
//        System.out.println("List of SSCs @SSC_assignNewTurn");
//        for(ServerSideConnection ssc: connections) {
//            System.out.print(ssc.getName() + "   ");
//        }
        //---------------------------------------------------------//

        int indexOfThis = connections.indexOf(this);
        String sendMessage = "startTurn ";
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
        return sendMessage;
    }
    public void shutDown() throws IOException {
        dataIn.close();
        dataOut.close();
        socket.close();
    }

    private void setTurn(boolean b) {
        myTurn = b;
    }

    public void setCardStacks(List<CardStack> cardStacks) {
        this.cardStacks = cardStacks;
        initialCardStacksNum = cardStacks.size();
    }

    private boolean gameOver() {
        boolean colonyFound = false;
        boolean provinceFound = false;
        boolean threeStacksEmpty = true;
        for(CardStack cardStack: cardStacks) {
//            System.out.println(cardStack.getCard().getName() + " " + cardStack.getNumCards() + " ");
            if(cardStack.getCard().getName().equals("Colony")) colonyFound = true;
            if(cardStack.getCard().getName().equals("Province")) provinceFound = true;
        }
        if(initialCardStacksNum-3 < cardStacks.size()) {
            threeStacksEmpty = false;
        }
        return (!colonyFound || !provinceFound || threeStacksEmpty);
    }
}
