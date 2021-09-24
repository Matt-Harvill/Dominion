package org.matthew.controller;

import org.matthew.Main;
import org.matthew.model.Player;
import org.matthew.server.ServerSender;

public class PhaseUpdater {

    private static final Player player = Main.getPlayer();

    public static void startPhase() {
        player.setPhase("startPhase");
        player.newTurn();

        DisplayUpdater.updateHandDisplay();
        DisplayUpdater.updateInPlayDisplay(player.getInPlay(), player.getName(), -1,true);
    }
    public static void actionPhase() {
        ServerSender.updateInfo();
        player.setPhase("actionPhase");

        DisplayUpdater.updateHandDisplay();
        DisplayUpdater.updateInPlayDisplay(player.getInPlay(), player.getName(), -1,true);

        checkCanDoAction();
    }
    private static void checkCanDoAction() {
        if (player.getNumActions()==0 || player.getHand().getDistinctActionCards().size()==0) {
            buyPhase();
        }
    }
    public static void buyPhase() {
        player.setPhase("buyPhase");

        DisplayUpdater.showBuyableCards(true);
        DisplayUpdater.updateHandDisplay();
        DisplayUpdater.updateInPlayDisplay(player.getInPlay(), player.getName(), -1,true);
    }
    public static void endPhase() {
        player.setPhase("endPhase");
        player.endTurn();

        DisplayUpdater.showBuyableCards(false);
        DisplayUpdater.updateHandDisplay();
        DisplayUpdater.updateInPlayDisplay(player.getInPlay(), player.getName(), -1,true);
        startPhase();

        ServerSender.endTurn();
    }
    public static void discardPhase() {
        player.setPhase("discardPhase");

        DisplayUpdater.updateHandDisplay();
        DisplayUpdater.updateInPlayDisplay(player.getSelect(), player.getName(), -1,true);
        Main.getGameController().getSwitchCardViewButton().setText("View Cards In Play");
    }
    public static void trashPhase() {
        player.setPhase("trashPhase");

        DisplayUpdater.updateHandDisplay();
        DisplayUpdater.updateInPlayDisplay(player.getSelect(), player.getName(), -1,true);
        Main.getGameController().getSwitchCardViewButton().setText("View Cards In Play");
    }
    public static void gainPhase() {
        player.setPhase("gainPhase");

        DisplayUpdater.updateHandDisplay();
    }
    public static void toDeckPhase() {
        player.setPhase("toDeckPhase");

        DisplayUpdater.updateHandDisplay();
        DisplayUpdater.updateInPlayDisplay(player.getSelect(), player.getName(), -1,true);
        Main.getGameController().getSwitchCardViewButton().setText("View Cards In Play");
    }
}
