package controller;

import model.Player;
import model.ServerPlayer;
import model.card.ActionCard;
import model.card.Card;
import model.card.TreasureCard;

import java.util.ArrayList;
import java.util.List;

public final class ActionHandler {

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
//        DisplayUpdater.updateHandDisplay();
//        DisplayUpdater.updateInPlayDisplay(player.getInPlay(), player.getName(), -1,true);
        startPhase();

        ServerSender.endTurn();
    }

    public static void buyCard(Card cardClicked) {
        player.buyCard(cardClicked);

        ServerSender.buyCard(cardClicked.getName());

        DisplayUpdater.updateCardSupply(cardClicked);
        DisplayUpdater.addMsgToGameLog("You purchased a " + cardClicked.getName());
        DisplayUpdater.updateHandDisplay();
        DisplayUpdater.updatePlayerLabel(player.getName(), player.getPoints());
        if(checkNumBuys()) {
            DisplayUpdater.showBuyableCards(true);
        }
    }
    public static void playCard(Card cardClicked) {
        for(Card card: player.getHand().getCollection()) {
            if(card.equals(cardClicked)) {
                player.playCard(card);
                break;
            }
        }

        ServerSender.playCard(cardClicked.getName());

        DisplayUpdater.updateHandDisplay();
        DisplayUpdater.updateInPlayDisplay(player.getInPlay(), player.getName(), -1,true);
        DisplayUpdater.updatePlayerLabel(player.getName(), player.getPoints());
//        if(cardClicked instanceof ActionCard) {
//            checkCanDoAction();
//        } else
        if(cardClicked instanceof TreasureCard) {
            DisplayUpdater.showBuyableCards(true);
        }
    }

    public static void actionCardCompleted(ActionCard card) {
        checkCanDoAction();
    }

    public static void gameOver() {
        String gameOverText = "Game Over -> ";
        List<String> winners = calcWinners(Main.getClientSideConnection().getPlayers());
        if(winners.size()==1) {
            if(winners.get(0).equals(player.getName())) {
                gameOverText += "You won";
            } else {
                gameOverText += winners.get(0) + " won";
            }
        } else {
            for(int i=0; i<winners.size()-1; i++) {
                gameOverText += winners.get(i) + " and ";
            }
            gameOverText += winners.get(winners.size()-1) + " tied";
        }

        DisplayUpdater.gameOver(gameOverText);
    }

    public static void checkCanDoAction() {
        if (player.getNumActions()==0 || player.getHand().getDistinctActionCards().size()==0) {
            buyPhase();
        }
    }
    private static boolean checkNumBuys() {
        if(player.getNumBuys()==0) {
            endPhase();
            return false;
        }
        return true;
    }
    private static List<String> calcWinners(List<ServerPlayer> players) {
        List<String> winners = new ArrayList<>();

        int maxPoints = player.getPoints();
        winners.add(player.getName());

        for(ServerPlayer serverPlayer: players) {
            if(serverPlayer.getPoints() > maxPoints) {
                winners = new ArrayList<>();
                winners.add(serverPlayer.getName());
            } else if (serverPlayer.getPoints()==maxPoints) {
                winners.add(serverPlayer.getName());
            }
        }
        return winners;
    }
}
