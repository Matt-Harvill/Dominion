package controller;

import model.Player;

public final class ServerSender {

    private static final ClientSideConnection clientSideConnection = Main.getClientSideConnection();
    private static final Player player = Main.getPlayer();

    public static void chatSend(String chatText) {
        clientSideConnection.send("chat " + player.getInfoString() + chatText);
    }
    public static void endTurn() {
        clientSideConnection.send("endTurn " + player.getInfoString());
    }
    public static void playCard(String cardName) {
        clientSideConnection.send("playCard " + player.getInfoString() + cardName);
    }
    public static void buyCard(String cardName) {
        clientSideConnection.send("buyCard " + player.getInfoString() + cardName);
    }
    public static void updateInfo() {
        clientSideConnection.send("updateInfo " + player.getInfoString());
    }

}
