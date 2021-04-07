package controller;

import javafx.application.Platform;
import model.Player;
import model.ServerPlayer;
import model.card.Card;
import model.card.CardStack;
import model.card.VictoryCard;
import model.factory.CardFactory;

import javax.naming.NameNotFoundException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientSideConnection implements Runnable {
    private Socket socket;
    private DataOutputStream dataOut;
    private DataInputStream dataIn;
    private static final Player player = Main.getPlayer();
    private List<ServerPlayer> players;

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
                case "buyCard": cardPurchased(tempPlayer,scanner.next()); break;
                case "gameOver": gameOver(); break;
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
            PlayerActionMediator.displayPlayerLabel(serverPlayer);
            PlayerActionMediator.addMessageToGameLog(serverPlayer.getName() + " has joined the game");
        });
    }
    private void inGame(ServerPlayer serverPlayer) {
        if(!players.contains(serverPlayer)) {
            players.add(serverPlayer);
        }

//        printPlayers();

        Platform.runLater(() -> {
            PlayerActionMediator.displayPlayerLabel(serverPlayer);
            PlayerActionMediator.addMessageToGameLog(serverPlayer.getName() + " was already in the game");
        });
    }
    private void chat(ServerPlayer serverPlayer, String chatMessage) {
        if(!players.contains(serverPlayer)) {
            players.add(serverPlayer);
        }

//        printPlayers();

        Platform.runLater(() -> PlayerActionMediator.addMessageToChatLog(serverPlayer.getName() + ":" + chatMessage));
    }
    private void startTurn(ServerPlayer serverPlayer) {
        boolean myTurn = (player.getName().equals(serverPlayer.getName()));
        if(!myTurn) {
            boolean playerNotFound = true;
            for(ServerPlayer player: players) {
                if(player.equals(serverPlayer)) {
                    player.updateInfo(serverPlayer);
                    player.resetInPlay();
                    playerNotFound = false;
                    break;
                }
            }

            if(playerNotFound) {
                players.add(serverPlayer);
            }
        }

//        printPlayers();

        Platform.runLater(() -> {
            if(myTurn) {
                PlayerActionMediator.actionPhase();
            }
            else {
                PlayerActionMediator.addMessageToGameLog(serverPlayer.getName() + " has started their turn");
                PlayerActionMediator.displayInPlay(serverPlayer);
                PlayerActionMediator.displayInPlayPlayerLabel(serverPlayer.getName());
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

        Platform.runLater(() -> PlayerActionMediator.addMessageToGameLog(serverPlayer.getName() + " left the game"));
    }
    private void cardPlayed(ServerPlayer serverPlayer,String cardName) {
        boolean playerNotFound = true;
        for(ServerPlayer player: players) {
            if(player.equals(serverPlayer)) {
                player.updateInfo(serverPlayer);
                player.addCardInPlay(CardFactory.getCard(cardName));
                playerNotFound = false;
                break;
            }
        }

        if(playerNotFound) {
            players.add(serverPlayer);
        }

//        printPlayers();

        Platform.runLater(() -> {
            PlayerActionMediator.addMessageToGameLog(serverPlayer.getName() + " played a " + cardName);
            PlayerActionMediator.displayInPlay(serverPlayer);
            PlayerActionMediator.displayOpponentDeckDisplay(serverPlayer.getName(),serverPlayer.getNumCardsInDeck());
        });
    }
    private void cardPurchased(ServerPlayer serverPlayer, String cardName) {
        Card card = CardFactory.getCard(cardName);
        boolean playerNotFound = true;
        for(ServerPlayer player: players) {
            if(player.equals(serverPlayer)) {
                player.updateInfo(serverPlayer);
//                if(card instanceof VictoryCard) {
//                    player.incrementPoints(((VictoryCard) card).getVictoryPoints());
//                }
                playerNotFound = false;
                break;
            }
        }

        if(playerNotFound) {
            players.add(serverPlayer);
        }

//        printPlayers();

        Platform.runLater(() -> {
            PlayerActionMediator.addMessageToGameLog(serverPlayer.getName() + " purchased a " + cardName);
            PlayerActionMediator.displayPlayerLabel(serverPlayer);
            PlayerActionMediator.displayOpponentDeckDisplay(serverPlayer.getName(),serverPlayer.getNumCardsInDeck());
            PlayerActionMediator.cardPurchased(card);
        });
    }
    private void gameOver() {
        System.out.println("\n\n------------------Game Over--------------------\n\n");
        Platform.runLater(PlayerActionMediator::gameOverDisplay);
    }
    private void updateInfo(ServerPlayer serverPlayer) {
        for(ServerPlayer player: players) {
            if(player.equals(serverPlayer)) {
                player.updateInfo(serverPlayer);
            }
        }

//        printPlayers();

        Platform.runLater(() -> PlayerActionMediator.displayOpponentDeckDisplay(serverPlayer.getName(), serverPlayer.getNumCardsInDeck()));
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
    public boolean send(String s) {
        try {
            dataOut.writeUTF(s);
            return true;
        } catch (IOException ex) {
            return false;
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

    private void printPlayers() {
        System.out.println("ServerPlayers: ");
        for(ServerPlayer player: players) {
            System.out.println("name: " + player.getName() + " points: " + player.getPoints() + " numCardsInDeck: " + player.getNumCardsInDeck());
        }
    }
}
