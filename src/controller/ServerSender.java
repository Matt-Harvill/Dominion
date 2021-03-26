package controller;

import model.Player;

public class ServerSender {

    private ClientSideConnection clientSideConnection;
    private Player player;

    public ServerSender(ClientSideConnection clientSideConnection, Player player) {
        this.clientSideConnection = clientSideConnection;
        this.player = player;
    }
    public void chatSend(String chatText) {
        clientSideConnection.send("chat " + player.getName() + " " + chatText);
    }

    public void endTurn() {
        clientSideConnection.send("endTurn");
    }

}
