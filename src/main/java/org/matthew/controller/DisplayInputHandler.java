package org.matthew.controller;

import  org.matthew.Main;
import org.matthew.model.CardCollection;
import org.matthew.model.Player;
import org.matthew.model.card.Card;
import org.matthew.model.card.TreasureCard;
import org.matthew.server.ServerSender;

public final class DisplayInputHandler {

    private static final Player player = Main.getPlayer();

    public static void buyButtonClicked(Card cardClicked) {
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
    public static void gainButtonClicked(Card cardClicked) {
        player.gainCard(cardClicked, player.getActionCardInPlay().getAction().getLocation());

        ServerSender.gainCard(cardClicked.getName());

        DisplayUpdater.updateCardSupply(cardClicked);
        DisplayUpdater.addMsgToGameLog("You gained a " + cardClicked.getName());
        DisplayUpdater.updateHandDisplay();
        DisplayUpdater.updatePlayerLabel(player.getName(), player.getPoints());
        DisplayUpdater.showGainableCards(false,-1);

        ActionCardPerformer.submitAction();
    }
    public static void greenCardInHandClicked(Card card) {
        switch (player.getPhase()) {
            case "actionPhase":
            case "buyPhase": {
                playCard(card);
                break;
            }
            case "toDeckPhase":
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

        if((phase.equals("discardPhase") || phase.equals("trashPhase") || phase.equals("toDeckPhase")) && inSelect) {
            player.selectToHand(card);
        }
        updateAfterClick();
    }
    public static void cardViewButtonClicked(String buttonText) {
        if(buttonText.equals("View Selected Cards")) {
            DisplayUpdater.updateInPlayDisplay(player.getSelect(), player.getName(), -1,true);
        } else if(buttonText.equals("View Cards In Play")) {
            DisplayUpdater.updateInPlayDisplay(player.getInPlay(), player.getName(), -1,true);
        }
    }
    public static void actionButtonClicked(String actionButtonText) {
//        String phase = player.getPhase();
        switch (actionButtonText) {
            case "Start Turn": PhaseUpdater.startPhase(); break;
            case "Enter Buy Phase": PhaseUpdater.buyPhase(); break;
            case "End Turn": PhaseUpdater.endPhase(); break;
            case "Don't Gain":
                DisplayUpdater.showGainableCards(false,-1);
            case "Skip Discarding":
            case "Skip Trashing":
            case "Skip Moving":
            case "Move Card(s)":
                case "Trash Card(s)":
            case "Discard Card(s)":
                ActionCardPerformer.submitAction(); break;
        }
    }

    private static boolean checkNumBuys() {
        if(player.getNumBuys()==0) {
            PhaseUpdater.endPhase();
            return false;
        }
        return true;
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
    private static void updateAfterClick() {
        DisplayUpdater.updateHandDisplay();
        CardCollection inPlayDisplayCollection;

        String phase = player.getPhase();

        if(phase.equals("discardPhase") || phase.equals("trashPhase") || phase.equals("toDeckPhase")) {
            inPlayDisplayCollection = player.getSelect();
        } else {
            inPlayDisplayCollection = player.getInPlay();
        }
        DisplayUpdater.updateInPlayDisplay(inPlayDisplayCollection, player.getName(), -1,true);
        DisplayUpdater.updatePlayerLabel(player.getName(), player.getPoints());
    }
}
