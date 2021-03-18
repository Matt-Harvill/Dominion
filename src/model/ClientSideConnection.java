package model;

import java.io.IOException;
import java.net.Socket;

public class ClientSideConnection {
    private Socket socket;
    public ClientSideConnection(String ipAddress, String portNum) throws IOException, NumberFormatException {
        socket = new Socket(ipAddress, Integer.parseInt(portNum));
    }
}
