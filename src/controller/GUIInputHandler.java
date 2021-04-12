package controller;

import model.CardCollection;
import model.Player;
import model.ServerPlayer;
import model.card.Card;
import model.card.TreasureCard;

import java.util.ArrayList;
import java.util.List;

public final class GUIInputHandler {

    private static final Player player = Main.getPlayer();

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
    private static boolean checkNumBuys() {
        if(player.getNumBuys()==0) {
            PhaseUpdater.endPhase();
            return false;
        }
        return true;
    }

    public static void gainCard(Card cardClicked) {
        player.gainCard(cardClicked);

        ServerSender.gainCard(cardClicked.getName());

        DisplayUpdater.updateCardSupply(cardClicked);
        DisplayUpdater.addMsgToGameLog("You gained a " + cardClicked.getName());
        DisplayUpdater.updateHandDisplay();
        DisplayUpdater.updatePlayerLabel(player.getName(), player.getPoints());
        DisplayUpdater.showGainableCards(false,-1);

        ActionCardPerformer.submitAction();
    }
    private static void playCard(Card cardClicked) {
        for(Card card: player.getHand().getCollection()) {
            if(card.equals(cardClicked)) {
                player.playCard(card);
                break;
            }
        }

        ServerSender.playCard(cardClicked.getName());

        if(cardClicked instanceof TreasureCard) {
            DisplayUpdater.showBuyableCards(true);
        }
    }

    public static void greenCardInHandClicked(Card card) {
        switch (player.getPhase()) {
            case "actionPhase":
            case "buyPhase": {
                playCard(card);
                break;
            }
            case "trashPhase":
            case "discardPhase": {
                player.handToSelect(card);
                break;
            }
        }

        updateAfterClick();
    }
    public static void cardInPlayClicked(Card card) {
        CardCollection select = player.getSelect();
        boolean inSelect = select.getCollection().contains(card);

        String phase = player.getPhase();

        if((phase.equals("discardPhase") || phase.equals("trashPhase")) && inSelect) {
            player.selectToHand(card);
        }
        updateAfterClick();
    }
    private static void updateAfterClick() {
        DisplayUpdater.updateHandDisplay();
        CardCollection inPlayDisplayCollection;

        String phase = player.getPhase();

        if(phase.equals("discardPhase") || phase.equals("trashPhase")) {
            inPlayDisplayCollection = player.getSelect();
        } else {
            inPlayDisplayCollection = player.getInPlay();
        }
        DisplayUpdater.updateInPlayDisplay(inPlayDisplayCollection, player.getName(), -1,true);
        DisplayUpdater.updatePlayerLabel(player.getName(), player.getPoints());
    }

    public static void cardViewButtonClicked(String buttonText) {
        if(buttonText.equals("View Selected Cards")) {
            DisplayUpdater.updateInPlayDisplay(player.getSelect(), player.getName(), -1,true);
        } else if(buttonText.equals("View Cards In Play")) {
            DisplayUpdater.updateInPlayDisplay(player.getInPlay(), player.getName(), -1,true);
        }
    }

    public static void actionButtonClicked(String actionButtonText) {
        switch (actionButtonText) {
            case "Start Turn": PhaseUpdater.startPhase(); break;
            case "Enter Buy Phase": PhaseUpdater.buyPhase(); break;
            case "End Turn": PhaseUpdater.endPhase(); break;
            case "Don't Gain":
                DisplayUpdater.showGainableCards(false,-1);
            case "Trash Cards":
            case "Discard Cards":
                ActionCardPerformer.submitAction(); break;
        }
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
