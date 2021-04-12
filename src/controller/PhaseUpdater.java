package controller;

import model.Player;

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
        Main.getGameController().getSwitchCardViewButton().setText("View Cards In Play");
    }
    public static void trashPhase() {
        player.setPhase("trashPhase");

        DisplayUpdater.updateHandDisplay();
        Main.getGameController().getSwitchCardViewButton().setText("View Cards In Play");
    }
    public static void gainPhase() {
        player.setPhase("gainPhase");

        DisplayUpdater.updateHandDisplay();
    }

    private static void checkCanDoAction() {
        if (player.getNumActions()==0 || player.getHand().getDistinctActionCards().size()==0) {
            buyPhase();
        }
    }
}
