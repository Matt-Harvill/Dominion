package controller;

import model.Player;

public final class ServerSender {

    private static final ClientSideConnection clientSideConnection = Main.getClientSideConnection();
    private static final Player player = Main.getPlayer();

    public static void chatSend(String chatText) {
        clientSideConnection.send("chat " + player.getName() + " " + chatText);
    }

    public static void endTurn() {
        clientSideConnection.send("endTurn");
    }

}
