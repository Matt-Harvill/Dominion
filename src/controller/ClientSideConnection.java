package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientSideConnection implements Runnable {
    private Socket socket;
    private DataOutputStream dataOut;
    private DataInputStream dataIn;
    private String playerName;

    public ClientSideConnection(String ipAddress, String portNum, String playerName) throws IOException, NumberFormatException {
        socket = new Socket(ipAddress, Integer.parseInt(portNum));
        this.playerName = playerName;
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
        Scanner scanner;
        send("setName " + playerName);

        while(true) {
            String receive = receive();

            if(receive==null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            scanner = new Scanner(receive);
            String instruction = scanner.next();
            String playerName = scanner.next();
            if(scanner.hasNext()) {
                receive = scanner.nextLine();
            }

            if(instruction.equals("chat")) {
                Main.getPlayerActionMediator().addMessageToChatLog(playerName + ":" + receive);
            } else if(instruction.equals("connected")) {
                Main.getPlayerActionMediator().addMessageToGameLog(playerName + " joined the game");
            } else if(instruction.equals("startTurn") && playerName.equals(playerName)) {
                Main.getPlayerActionMediator().actionPhase();
            }
        }
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
