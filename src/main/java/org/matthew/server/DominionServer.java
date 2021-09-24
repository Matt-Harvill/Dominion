package org.matthew.server;

import  org.matthew.Main;
import org.matthew.model.CardCollection;
import org.matthew.model.card.Card;
import org.matthew.model.card.CardStack;
import org.matthew.model.factory.CardFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DominionServer {

    private ServerSocket serverSocket;
    private int portNumber, maxNumPlayers;
    private String ipAddress;

    private final List<ServerSideConnection> serverSideConnections;
    private final CardCollection cardsInGame;
    private final List<CardStack> cardStacks;
    
    public DominionServer(){
        serverSideConnections = new ArrayList<>();
        cardsInGame = new CardCollection();
        cardStacks = new ArrayList<>();
//        setTreasureVictoryCardsInGame();
        try {
            serverSocket = new ServerSocket(0);
            ipAddress = InetAddress.getLocalHost().getHostAddress();
            portNumber = serverSocket.getLocalPort();
        } catch (Exception ex) {
            System.out.println("Exception @DS()");
        }
    }

    public void acceptConnections() {
        setTreasureVictoryCardsInGame();
        try {
            System.out.println("Accepting connections...");
            do {
                Socket s = serverSocket.accept();
                serverSideConnections.add(new ServerSideConnection(s));
                System.out.println("There are " + serverSideConnections.size() + " clients connected");
            }
            while (serverSideConnections.size()<maxNumPlayers);
            System.out.println("Max players reached");
            startGame();
        } catch (IOException ex) {
            System.out.println("IOException @DS_acceptConnections()");
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
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
    public CardCollection getCardsInGame() {
        return cardsInGame;
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
            ServerSideConnection firstPlayer = serverSideConnections.get(firstPlayerTurn);
            //Wait until player name has been setup in SSC
            while(firstPlayer.getName()==null) {
                Thread.sleep(1);
            }

            createCardStacks();
            setSSCCardStacks();
            broadcastCardNums();

            firstPlayer.broadcastAll("startTurn " + firstPlayer.getPlayerInfoString());
            Main.getGameController().hideServerInfoPane();

        } catch (Exception ex) {
            System.out.println("Exception @DS_startGame");
        }
    }

    private void broadcastCardNums() throws IOException {
        for(ServerSideConnection ssc: serverSideConnections) {
            String sendMessage = "cardsInGameNums " + ssc.getPlayerInfoString();
            for(CardStack cardStack: cardStacks) {
                sendMessage += cardStack.getCard().getName() + " " + cardStack.getNumCards() + " ";
            }
            ssc.individualSend(sendMessage);
        }
    }
    private void setSSCCardStacks() {
        for(ServerSideConnection ssc: serverSideConnections) {
            ssc.setCardStacks(cardStacks);
        }
    }
    private void createCardStacks() {
        for(Card card: cardsInGame.getCollection()) {
            cardStacks.add(new CardStack(card));
        }
        setCardStackNums();
    }
    private void setCardStackNums() {
        for(CardStack cardStack: cardStacks) {
            cardStack.setNumCards(parseNumCardString(cardStack.getCard().getNumCards()));
        }
    }
    private int parseNumCardString(String cardNum) {
        Scanner scanner = new Scanner(cardNum);
//        System.out.println(cardNum);
        while(scanner.hasNext()) {
            String s = scanner.next();
            if(s.equals("All")) {
                return scanner.nextInt();
            } else if(s.contains("player")) {
                int numPlayerSpec = Integer.parseInt(s.substring(0,1));
                boolean containsPlus = s.contains("+");
                if(numPlayerSpec==serverSideConnections.size() || (containsPlus && serverSideConnections.size() > numPlayerSpec)) {
                    return scanner.nextInt();
                } else {
                    scanner.next();
                }
            }
//            else if (s.contains("player")){
//                int numPlayers = Integer.parseInt(s.substring(0,1));
//                if(s.contains("+")) {
//
//                }
//            }
        }
        return -1;
    }

    private void setTreasureVictoryCardsInGame() {
        cardsInGame.addCardToCollection(CardFactory.getCard("Copper"));
        cardsInGame.addCardToCollection(CardFactory.getCard("Silver"));
        cardsInGame.addCardToCollection(CardFactory.getCard("Gold"));
        cardsInGame.addCardToCollection(CardFactory.getCard("Platinum"));
        cardsInGame.addCardToCollection(CardFactory.getCard("Estate"));
        cardsInGame.addCardToCollection(CardFactory.getCard("Duchy"));
        cardsInGame.addCardToCollection(CardFactory.getCard("Province"));
        cardsInGame.addCardToCollection(CardFactory.getCard("Colony"));
    }
}
