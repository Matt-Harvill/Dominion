package controller;

import javafx.application.Platform;
import model.Player;
import model.ServerPlayer;

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

        while(true) {
            String receive = receive();
            System.out.println(receive);

            if(receive==null) {
                try {
                    socket.close();
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
                case "actionCardsInGame": actionCardsInGame(scanner.nextLine());
                default:
                    break;
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
    private void actionCardsInGame(String cardsString) {
        Scanner scanner = new Scanner(cardsString);
        String[] cardNames = new String[10];
        int[] cardNums = new int[10];
        int index = 0;
        while (scanner.hasNext() && index<cardNames.length) {
            cardNames[index] = scanner.next();
            cardNums[index] = scanner.nextInt();
            index++;
        }
        Platform.runLater(() -> Main.getGameController().displayActionCardsInGame(cardNames,cardNums));
    }

    public String receive() {
        try {
            return dataIn.readUTF();
        } catch (IOException ex) {
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
}
