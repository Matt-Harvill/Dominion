package model;

import controller.UserInterfaceHub;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientSideConnection implements Runnable {
    private Socket socket;
    private DataOutputStream dataOut;
    private DataInputStream dataIn;

    public ClientSideConnection(String ipAddress, String portNum) throws IOException, NumberFormatException {
        socket = new Socket(ipAddress, Integer.parseInt(portNum));
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
        while(true) {
            String receive = receive();
            scanner = new Scanner(receive);
            String instruction = scanner.next();
            receive = scanner.nextLine();
            if(instruction.equals("chat")) {
                UserInterfaceHub.getGameController().addMessageToChatLog(receive);
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
