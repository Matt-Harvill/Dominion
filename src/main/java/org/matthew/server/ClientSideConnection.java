package org.matthew.server;

import  org.matthew.Main;
import javafx.application.Platform;
import org.matthew.controller.DisplayUpdater;
import org.matthew.controller.PhaseUpdater;
import org.matthew.model.Player;
import org.matthew.model.ServerPlayer;
import org.matthew.model.card.Card;
import org.matthew.model.card.CardStack;
import org.matthew.model.factory.CardFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientSideConnection implements Runnable {
    private final Socket socket;
    private DataOutputStream dataOut;
    private DataInputStream dataIn;
    private static final Player player = Main.getPlayer();
    private final List<ServerPlayer> players;

    public ClientSideConnection(String ipAddress, String portNum) throws IOException, NumberFormatException {
        socket = new Socket(ipAddress, Integer.parseInt(portNum));
        players = new ArrayList<>();
        try {
            dataIn = new DataInputStream(socket.getInputStream());
            dataOut = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println("IOException from CSC Constructor");
        }
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        send("join " + player.getInfoString());

        while(!socket.isClosed()) {
            String receive = receive();

            if(receive==null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }

            System.out.println(receive + " <---- received message");
            Scanner scanner = new Scanner(receive);
            final String instruction = scanner.next();
            String playerName = scanner.next();
            int playerPoints = scanner.nextInt();
            int numCardsInDeck = scanner.nextInt();
            ServerPlayer tempPlayer = new ServerPlayer(playerName,playerPoints,numCardsInDeck);

            switch (instruction) {
                //----------Other Players-----------//
                case "inGame": inGame(tempPlayer); break;
                case "chat": chat(tempPlayer,scanner.nextLine()); break;
                case "connected": connected(tempPlayer); break;
                case "cardsInGame": cardsInGame(scanner.nextLine()); break;
                case "cardsInGameNums": cardsInGameNums(scanner.nextLine()); break;
                case "serverShutDown": serverShutDown(); break;
                case "leaveGame": leftGame(tempPlayer); break;
                case "playCard": cardPlayed(tempPlayer,scanner.next()); break;
                case "buyButtonClicked": cardPurchased(tempPlayer,scanner.next()); break;
                case "gainButtonClicked": cardGained(tempPlayer, scanner.next()); break;
                case "displayGameOver": gameOver(); break;
                case "updateInfo": updateInfo(tempPlayer); break;
                //-----------Self-----------//
                case "startTurn": startTurn(tempPlayer); break;
                case "nameChanged": nameChanged(playerName); break;
                default: break;
            }
        }
    }

    public List<ServerPlayer> getPlayers() {
        return players;
    }

    private void connected(ServerPlayer serverPlayer) {
        if(!players.contains(serverPlayer)) {
            players.add(serverPlayer);
        }

//        printPlayers();

        Platform.runLater(() -> {
            DisplayUpdater.updatePlayerLabel(serverPlayer.getName(), serverPlayer.getPoints());
            DisplayUpdater.addMsgToGameLog(serverPlayer.getName() + " has joined the game");
        });
    }
    private void inGame(ServerPlayer serverPlayer) {
        if(!players.contains(serverPlayer)) {
            players.add(serverPlayer);
        }

//        printPlayers();

        Platform.runLater(() -> {
            DisplayUpdater.updatePlayerLabel(serverPlayer.getName(), serverPlayer.getPoints());
            DisplayUpdater.addMsgToGameLog(serverPlayer.getName() + " was already in the game");
        });
    }
    private void chat(ServerPlayer serverPlayer, String chatMessage) {
        if(!players.contains(serverPlayer)) {
            players.add(serverPlayer);
        }

//        printPlayers();

        Platform.runLater(() -> DisplayUpdater.addMsgToChatLog(serverPlayer.getName() + ":" + chatMessage));
    }
    private void startTurn(ServerPlayer serverPlayer) {
        boolean myTurn = (player.getName().equals(serverPlayer.getName()));
        if(!myTurn) {
            boolean playerNotFound = true;
            for(ServerPlayer player: players) {
                if(player.equals(serverPlayer)) {
                    player.updateInfo(serverPlayer);
                    player.resetInPlay();
                    serverPlayer = player;
                    playerNotFound = false;
                    break;
                }
            }

            if(playerNotFound) {
                players.add(serverPlayer);
            }
        }

//        printPlayers();

        ServerPlayer finalServerPlayer = serverPlayer;
        Platform.runLater(() -> {
            if(myTurn) {
                PhaseUpdater.actionPhase();
            }
            else {
                DisplayUpdater.addMsgToGameLog(finalServerPlayer.getName() + " has started their turn");
                DisplayUpdater.updateInPlayDisplay(finalServerPlayer.getInPlay(), finalServerPlayer.getName(), finalServerPlayer.getNumCardsInDeck(), false);
            }
        });
    }
    private void cardsInGame(String cardsString) {
        Scanner scanner = new Scanner(cardsString);
        List<String> cardNames = new ArrayList<>();
        while (scanner.hasNext()) {
            cardNames.add(scanner.next());
        }
        Platform.runLater(() -> {
            Main.getGameController().setCardsInGame(cardNames);
            Main.getGameController().displayCardsInGame();
        });
    }
    private void cardsInGameNums(String cardsString) {
        Scanner scanner = new Scanner(cardsString);
        List<CardStack> cardStacks = new ArrayList<>();
        while (scanner.hasNext()) {
            String cardName = scanner.next();
            int cardNum = scanner.nextInt();
            cardStacks.add(new CardStack(CardFactory.getCard(cardName),cardNum));
        }
        Platform.runLater(() -> {
            Main.getGameController().setCardStacks(cardStacks);
            Main.getGameController().displayCardsInGameNums();
        });

    }
    private void serverShutDown() {
        Main.closeMainStage();
        try {
            socket.close();
        } catch (IOException ex) {
            System.out.println("IOException @CSC_serverShutDown");
        }
    }
    private void leftGame(ServerPlayer serverPlayer) {
        players.removeIf(player -> player.equals(serverPlayer));

//        printPlayers();

        Platform.runLater(() -> DisplayUpdater.addMsgToGameLog(serverPlayer.getName() + " left the game"));
    }
    private void cardPlayed(ServerPlayer serverPlayer,String cardName) {
        boolean playerNotFound = true;
        for(ServerPlayer player: players) {
            if(player.equals(serverPlayer)) {
                player.updateInfo(serverPlayer);
                player.addCardInPlay(CardFactory.getCard(cardName));
                playerNotFound = false;
                serverPlayer = player;
                break;
            }
        }

        if(playerNotFound) {
            players.add(serverPlayer);
        }

//        printPlayers();

        ServerPlayer finalServerPlayer = serverPlayer;
        Platform.runLater(() -> DisplayUpdater.updateInPlayDisplay(finalServerPlayer.getInPlay(), finalServerPlayer.getName(), finalServerPlayer.getNumCardsInDeck(), false));
    }
    private void cardPurchased(ServerPlayer serverPlayer, String cardName) {
        Card card = CardFactory.getCard(cardName);
        boolean playerNotFound = true;
        for(ServerPlayer player: players) {
            if(player.equals(serverPlayer)) {
                player.updateInfo(serverPlayer);
                serverPlayer = player;
                playerNotFound = false;
                break;
            }
        }

        if(playerNotFound) {
            players.add(serverPlayer);
        }

//        printPlayers();

        ServerPlayer finalServerPlayer = serverPlayer;
        Platform.runLater(() -> {
            DisplayUpdater.addMsgToGameLog(finalServerPlayer.getName() + " purchased a " + cardName);
            DisplayUpdater.updatePlayerLabel(finalServerPlayer.getName(), finalServerPlayer.getPoints());
            DisplayUpdater.updateInPlayDisplay(finalServerPlayer.getInPlay(), finalServerPlayer.getName(), finalServerPlayer.getNumCardsInDeck(), false);
            DisplayUpdater.updateCardSupply(card);
        });
    }
    private void cardGained(ServerPlayer serverPlayer, String cardName) {
        Card card = CardFactory.getCard(cardName);
        boolean playerNotFound = true;
        for(ServerPlayer player: players) {
            if(player.equals(serverPlayer)) {
                player.updateInfo(serverPlayer);
                serverPlayer = player;
                playerNotFound = false;
                break;
            }
        }

        if(playerNotFound) {
            players.add(serverPlayer);
        }

//        printPlayers();

        ServerPlayer finalServerPlayer = serverPlayer;
        Platform.runLater(() -> {
            DisplayUpdater.addMsgToGameLog(finalServerPlayer.getName() + " gained a " + cardName);
            DisplayUpdater.updatePlayerLabel(finalServerPlayer.getName(), finalServerPlayer.getPoints());
            DisplayUpdater.updateInPlayDisplay(finalServerPlayer.getInPlay(), finalServerPlayer.getName(), finalServerPlayer.getNumCardsInDeck(), false);
            DisplayUpdater.updateCardSupply(card);
        });
    }
    private void gameOver() {
        System.out.println("\n\n------------------Game Over--------------------\n\n");
        Platform.runLater(DisplayUpdater::gameOver);
    }
    private void updateInfo(ServerPlayer serverPlayer) {
        for(ServerPlayer player: players) {
            if(player.equals(serverPlayer)) {
                player.updateInfo(serverPlayer);
                serverPlayer = player;
            }
        }

//        printPlayers();

        ServerPlayer finalServerPlayer = serverPlayer;
        Platform.runLater(() -> DisplayUpdater.updateInPlayDisplay(finalServerPlayer.getInPlay(), finalServerPlayer.getName(), finalServerPlayer.getNumCardsInDeck(), false));
    }
    private void nameChanged(String playerName) {
        player.setName(playerName);
    }

    public String receive() {
        try {
            return dataIn.readUTF();
        } catch (IOException ex) {
            System.out.println("IOException @CSCS_receive");
            return null;
        }
    }
    public void send(String s) {
        try {
            dataOut.writeUTF(s);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void leaveGame() {
        send("leaveGame " + player.getInfoString());
        try {
            socket.close();
        } catch (Exception ex) {
            System.out.println("Exception @CSC_leaveGame");
        }
    }

//    private void printPlayers() {
//        System.out.println("ServerPlayers: ");
//        for(ServerPlayer player: players) {
//            System.out.println("name: " + player.getName() + " points: " + player.getPoints() + " numCardsInDeck: " + player.getNumCardsInDeck());
//        }
//    }
}
