package controller;

import javafx.application.Platform;
import model.Player;
import model.ServerPlayer;
import model.card.CardStack;
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
            System.out.println(receive + " <---- received message");

            if(receive==null) {
                try {
                    socket.close();
                    System.out.println("receive==null entered in CSC run()");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }

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
                default: break;
            }
        }
    }

    private void connected(String playerName, int playerPoints) {
        Platform.runLater(() -> {
            players.add(new ServerPlayer(playerName,playerPoints));
            PlayerActionMediator.displayPlayerLabel(playerName,playerPoints);
            PlayerActionMediator.addMessageToGameLog(playerName + " has joined the game");
        });
    }
    private void inGame(String playerName, int playerPoints) {
        ServerPlayer newPlayer = new ServerPlayer(playerName,playerPoints);
        players.add(newPlayer);

        Platform.runLater(() -> {
            PlayerActionMediator.displayPlayerLabel(playerName,playerPoints);
            PlayerActionMediator.addMessageToGameLog(playerName + " was already in the game");
        });
    }
    private void chat(String playerName, String chatMessage) {
        Platform.runLater(() -> PlayerActionMediator.addMessageToChatLog(playerName + ":" + chatMessage));
    }
    private void startTurn(String playerName) {
        Platform.runLater(() -> {
            if(player.getName().equals(playerName)) PlayerActionMediator.actionPhase();
            else PlayerActionMediator.addMessageToGameLog(playerName + " has started their turn");
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
