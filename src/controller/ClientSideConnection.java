package controller;

import javafx.application.Platform;
import model.Player;
import model.ServerPlayer;
import model.card.Card;
import model.card.CardStack;
import model.card.VictoryCard;
import model.factory.CardFactory;

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
        send("join " + player.getName() + " " + player.getPoints());

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

            switch (instruction) {
                case "inGame": inGame(playerName,playerPoints); break;
                case "chat": chat(playerName,scanner.nextLine()); break;
                case "connected": connected(playerName,playerPoints); break;
                case "startTurn": startTurn(playerName); break;
                case "cardsInGame": cardsInGame(scanner.nextLine()); break;
                case "cardsInGameNums": cardsInGameNums(scanner.nextLine()); break;
                case "serverShutDown": serverShutDown(); break;
                case "leaveGame": leftGame(playerName); break;
                case "playCard": cardPlayed(playerName,scanner.next()); break;
                case "buyCard": cardPurchased(playerName,scanner.next()); break;
                default: break;
            }
        }
    }

    private void connected(String playerName, int playerPoints) {
        ServerPlayer player = new ServerPlayer(playerName,playerPoints);
        players.add(player);
        Platform.runLater(() -> {
            PlayerActionMediator.displayPlayerLabel(player);
            PlayerActionMediator.addMessageToGameLog(playerName + " has joined the game");
        });
    }
    private void inGame(String playerName, int playerPoints) {
        ServerPlayer newPlayer = new ServerPlayer(playerName,playerPoints);
        players.add(newPlayer);

        Platform.runLater(() -> {
            PlayerActionMediator.displayPlayerLabel(newPlayer);
            PlayerActionMediator.addMessageToGameLog(playerName + " was already in the game");
        });
    }
    private void chat(String playerName, String chatMessage) {
        Platform.runLater(() -> PlayerActionMediator.addMessageToChatLog(playerName + ":" + chatMessage));
    }
    private void startTurn(String playerName) {
        boolean myTurn = (player.getName().equals(playerName));
        if(!myTurn) {
            ServerPlayer otherPlayer = null;
            for(ServerPlayer serverPlayer: players) {
                if(serverPlayer.getName().equals(playerName)) {
                    otherPlayer = serverPlayer;
                    break;
                }
            }
            if(otherPlayer==null) {
                System.out.println("Error @CSC_startTurn");
            }
            else {
                otherPlayer.resetInPlay();
            }
        }

        Platform.runLater(() -> {
            if(myTurn) PlayerActionMediator.actionPhase();
            else {
                PlayerActionMediator.addMessageToGameLog(playerName + " has started their turn");
                PlayerActionMediator.displayInPlayPlayerLabel(playerName);
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
        Main.closeOpenStages();
        try {
            socket.close();
        } catch (IOException ex) {
            System.out.println("IOException @CSC_serverShutDown");
        }
    }
    private void leftGame(String playerName) {
        Platform.runLater(() -> PlayerActionMediator.addMessageToGameLog(playerName + " left the game"));
    }
    private void cardPlayed(String playerName,String cardName) {
        ServerPlayer otherPlayer = null;
        for(ServerPlayer serverPlayer: players) {
            if(serverPlayer.getName().equals(playerName)) {
                otherPlayer = serverPlayer;
                break;
            }
        }
        if(otherPlayer==null) {
            System.out.println("Error @CSC_cardPlayed");
        }
        else {
            otherPlayer.addCardInPlay(CardFactory.getCard(cardName));
        }

        ServerPlayer finalOtherPlayer = otherPlayer;

        Platform.runLater(() -> {
            PlayerActionMediator.addMessageToGameLog(playerName + " played a " + cardName);
            PlayerActionMediator.displayInPlay(finalOtherPlayer);
        });
    }
    private void cardPurchased(String playerName, String cardName) {
        Card card = CardFactory.getCard(cardName);
        ServerPlayer otherPlayer = null;
        if(card instanceof VictoryCard) {
            for(ServerPlayer serverPlayer: players) {
                if(serverPlayer.getName().equals(playerName)) {
                    otherPlayer = serverPlayer;
                    break;
                }
            }
            if(otherPlayer==null) {
                System.out.println("Error @CSC_cardPurchased");
            }
            else {
                otherPlayer.setPoints(otherPlayer.getPoints()+((VictoryCard) card).getVictoryPoints());
            }
        }
        ServerPlayer finalOtherPlayer = otherPlayer;
        Platform.runLater(() -> {
            PlayerActionMediator.addMessageToGameLog(playerName + " purchased a " + cardName);
            if(finalOtherPlayer !=null) {
                PlayerActionMediator.displayPlayerLabel(finalOtherPlayer);
            }
        });
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
        send("leaveGame " + player.getName() + " " + player.getPoints() + " ");
        try {
            socket.close();
        } catch (Exception ex) {
            System.out.println("Exception @CSC_leaveGame");
        }
    }
}
