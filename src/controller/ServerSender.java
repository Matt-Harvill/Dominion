package controller;

import model.Player;

public final class ServerSender {

    private static final ClientSideConnection clientSideConnection = Main.getClientSideConnection();
    private static final Player player = Main.getPlayer();

    public static void chatSend(String chatText) {
        clientSideConnection.send("chat " + getPlayerInfoString() + chatText);
    }
    public static void endTurn() {
        clientSideConnection.send("endTurn " + getPlayerInfoString());
    }

    private static String getPlayerInfoString() {
        return player.getName() + " " + player.getPoints() + " ";
    }

}
