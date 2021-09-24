package org.matthew.controller;

import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.matthew.Main;
import org.matthew.controller.fxml.GameController;
import org.matthew.model.CardCollection;
import org.matthew.model.Player;
import org.matthew.model.ServerPlayer;
import org.matthew.model.card.Card;
import org.matthew.model.card.TreasureCard;
import org.matthew.model.card.VictoryCard;
import org.matthew.model.card.action.ActionCard;
import org.matthew.view.*;

import java.util.ArrayList;
import java.util.List;

public class DisplayUpdater {

    private static final GameController controller = Main.getGameController();
    private static final Player player = Main.getPlayer();

    //--------------------For all players-----------------------//
    public static void updateCardSupply(Card cardPurchased) {
        List<BuyableCardDisplay> cardDisplays = controller.getAllCISDisplays();

        for(BuyableCardDisplay cardDisplay: cardDisplays) {
            if(cardDisplay.getCard()!=null && cardDisplay.getCard().equals(cardPurchased)) {
                int prevNum = cardDisplay.getNum();
                cardDisplay.setNum(prevNum-1);
            }
        }
    }
    public static void updatePlayerLabel(String playerName, int points) {
        PlayerInfoDisplay[] displays = controller.getPlayerInfoDisplays();

        for (PlayerInfoDisplay display : displays) {
//            System.out.println("playerLabel name: " + display.getName());
            if (playerName.equals(display.getName()) || display.getName().equals("")) {
                display.setName(playerName);
                display.setNum(points);
                display.show();
                break;
            }
        }
    }
    public static void updateInPlayDisplay(CardCollection collection, String playerName, int numCards, boolean myTurn) {
        System.out.println("updateInPlayDisplay called @DisplayUpdater");
        if(collection.equals(player.getSelect())) {
            updateCardsInSelect();
            System.out.println("select");
            collection.printCardNamesInCollection();
        } else {
            updateCardsInPlay(collection);
            if (player.getInPlay().equals(collection)) {
                System.out.println("inPlay");
                collection.printCardNamesInCollection();
            }
        }
        updateOpponentDeck(numCards, myTurn);
        updateInPlayPlayerLabel(playerName,myTurn);
    }

    private static void updateOpponentDeck(int numCards, boolean myTurn) {
        DeckDisplay deckDisplay = controller.getOpponentDeckDisplay();
        deckDisplay.getCardDisplay().setNum(numCards);
        if(myTurn) {
            deckDisplay.hide();
        } else {
            deckDisplay.show();
        }
    }
    private static void updateCardsInPlay(CardCollection inPlay) {
        CardDisplay[] cardDisplays = controller.getCIPDisplays();
        resetCardDisplays(cardDisplays);

        int index = 0;
        int freqOfCardInARow = 1;
        List<Card> cardList = inPlay.getCollection();

        for(int i=0; i<cardList.size();i++) {
            if(i!=cardList.size()-1 && cardList.get(i).equals(cardList.get(i+1))) {
                freqOfCardInARow++; continue;
            }
            setCardDisplay(cardDisplays[index],cardList.get(i),freqOfCardInARow);
            index++;
            freqOfCardInARow=1;
        }
    }
    private static void updateCardsInSelect() {
        CardDisplay[] cardDisplays = controller.getCIPDisplays();
        resetCardDisplays(cardDisplays);

        CardCollection select = player.getSelect();
        List<ActionCard> actionCards = select.getDistinctActionCards();
        List<TreasureCard> treasureCards = select.getDistinctTreasureCards();
        List<VictoryCard> victoryCards = select.getDistinctVictoryCards();

        String phase = player.getPhase();
        String style = null;
        switch (phase) {
            case "discardPhase":
                style = controller.getYellowCardGlowStyle();
                break;
            case "trashPhase":
                style = controller.getRedCardGlowStyle();
                break;
            case "toDeckPhase":
                style = controller.getCyanCardGlowStyle();
                break;
        }

        int index = 0;
        for(ActionCard actionCard: actionCards) {
            setCardDisplay(cardDisplays[index],actionCard,select.numCardInCollection(actionCard));
            cardDisplays[index].setStyle(style);
            index++;
        }
        for(TreasureCard treasureCard: treasureCards) {
            setCardDisplay(cardDisplays[index],treasureCard,select.numCardInCollection(treasureCard));
            cardDisplays[index].setStyle(style);
            index++;
        }
        for(VictoryCard victoryCard: victoryCards) {
            setCardDisplay(cardDisplays[index],victoryCard,select.numCardInCollection(victoryCard));
            cardDisplays[index].setStyle(style);
            index++;
        }
    }
    private static void updateInPlayPlayerLabel(String otherPlayerName, boolean myTurn) {
        LabelDisplay labelDisplay = controller.getInPlayPlayerLabel();
        labelDisplay.setText(otherPlayerName);
        if(myTurn) {
            labelDisplay.hide();
        } else {
            labelDisplay.show();
        }

    }
    private static void setCardDisplay(CardDisplay cardDisplay,Card card, int numCard) {
        cardDisplay.setCard(card);
        cardDisplay.setNum(numCard);
        cardDisplay.show();
    }
    private static void resetCardDisplays(CardDisplay[] cardDisplays) {
        for(CardDisplay cardDisplay: cardDisplays) {
            cardDisplay.setCard(null);
            cardDisplay.setStyle(null);
            cardDisplay.hide();
        }
    }

    //---------------------Only this player----------------------//
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

        displayGameOver(gameOverText);
    }
    public static void showBuyableCards(boolean show) {
        List<BuyableCardDisplay> cardDisplays = controller.getAllCISDisplays();

        for(BuyableCardDisplay cardDisplay: cardDisplays) {
            Card card = cardDisplay.getCard();
            int cardNum = cardDisplay.getNum();
            if(card!=null && card.getCost()<=player.getPurchasePower() && cardNum>0 && show) {
                cardDisplay.showBuyButton();
            } else {
                cardDisplay.hideBuyButton();
            }
        }
    }
    public static void showGainableCards(boolean show, int maxCost) {
        List<BuyableCardDisplay> cardDisplays = controller.getAllCISDisplays();

        for(BuyableCardDisplay cardDisplay: cardDisplays) {
            Card card = cardDisplay.getCard();
            int cardNum = cardDisplay.getNum();
            if(card!=null && card.getCost()<=maxCost && cardNum>0 && show && isType(card)) {
                cardDisplay.showGainButton();
            } else {
                cardDisplay.hideGainButton();
            }
        }
    }
    private static boolean isType(Card card) {
        if(player.getActionCardInPlay()!=null) {
            String type = player.getActionCardInPlay().getAction().getType();
            switch (type) {
                case "all":
                    return true;
                case "treasureCard":
                    return card instanceof TreasureCard;
                case "victoryCard":
                    return card instanceof VictoryCard;
                case "actionCard":
                    return card instanceof ActionCard;
                case "Copper":
                    return card.getName().equals(type);
            }
        }
        return false;
    }
    public static void updateHandDisplay() {
        updateCardsInHand();
        updateDeck();
        updateDiscard();
        updateActionBar();
    }
    public static void addMsgToChatLog(String msg) {
        if(controller.getChatDisplayStrings().size()>=15) controller.getChatDisplayStrings().remove(0);
        controller.getChatDisplayStrings().add(msg);
        controller.getChatType().setText(null);
        StringBuilder builder = new StringBuilder();
        for(String s: controller.getChatDisplayStrings()) {
            builder.append(s); builder.append("\n");
        }
        controller.getChatLog().setText(builder.toString());
    }
    public static void addMsgToGameLog(String msg) {
        if(controller.getGameDisplayStrings().size()>=15) controller.getGameDisplayStrings().remove(0);
        controller.getGameDisplayStrings().add(msg);
        controller.getGameLog().setText(null);
        StringBuilder builder = new StringBuilder();
        for(String s: controller.getGameDisplayStrings()) {
            builder.append(s); builder.append("\n");
        }
        controller.getGameLog().setText(builder.toString());
    }

    private static void updateActionBar() {
        Text gameInfoText = controller.getGameInfoText();
        String gameInfoString = "";

        controller.getSwitchCardViewButton().setVisible(false);

        switch (player.getPhase()) {
            case "actionPhase": {
//                System.out.println("actionPhase entered @updateActionBar");
                gameInfoString += "Number of Actions: " + player.getNumActions() + "   ";
                updateActionButtonText("Enter Buy Phase",true);
                break;
            }
            case "buyPhase": {
//                System.out.println("buyPhase entered @updateActionBar");
                gameInfoString += "Number of Buys Remaining : " + player.getNumBuys() + "   ";
                gameInfoString += "Purchase Power: " + player.getPurchasePower();
                updateActionButtonText("End Turn",true);
                break;
            }
            case "endPhase": {
                controller.getActionButton().setVisible(false);
                break;
            }
            case "startPhase": {
                controller.getActionButton().setVisible(false);
                gameInfoString += "Here is your hand";
                break;
            }
            case "discardPhase": {
                int selectSize = player.getSelect().getSize();
                boolean actionComplete = ActionCardPerformer.actionComplete(selectSize);
                String actionButtonText = "";
                if(actionComplete) {
                    if(selectSize==0) {
                        actionButtonText = "Skip Discarding";
                    } else {
                        actionButtonText = "Discard Card(s)";
                    }
                }
                updateActionButtonText(actionButtonText,actionComplete);

                gameInfoString += "Select card(s) to discard";

                controller.getSwitchCardViewButton().setVisible(true);
                break;
            }
            case "trashPhase": {
                int selectSize = player.getSelect().getSize();
                boolean actionComplete = ActionCardPerformer.actionComplete(selectSize);
                String actionButtonText = "";
                if(actionComplete) {
                    if(selectSize==0) {
                        actionButtonText = "Skip Trashing";
                    } else {
                        actionButtonText = "Trash Card(s)";
                    }
                }
                updateActionButtonText(actionButtonText,actionComplete);

                gameInfoString += "Select card(s) to trash";

                controller.getSwitchCardViewButton().setVisible(true);
                break;
            }
            case "toDeckPhase": {
                int selectSize = player.getSelect().getSize();
                boolean actionComplete = ActionCardPerformer.actionComplete(selectSize);
                String actionButtonText = "";
                if(actionComplete) {
                    if(selectSize==0) {
                        actionButtonText = "Skip Moving";
                    } else {
                        actionButtonText = "Move Card(s)";
                    }
                }
                updateActionButtonText(actionButtonText,actionComplete);

                gameInfoString += "Select card(s) to move to deck";

                controller.getSwitchCardViewButton().setVisible(true);
                break;
            }
            case "gainPhase": {
                updateActionButtonText("Don't Gain",ActionCardPerformer.actionComplete());
                gameInfoString += "Gain a card";
                break;
            }
        }
        gameInfoText.setText(gameInfoString);
    }
    private static void updateActionButtonText(String text, boolean show) {
        Button actionButton = controller.getActionButton();
        actionButton.setText(text);
        actionButton.setVisible(show);
    }
    private static void updateCardsInHand() {
        CardDisplay[] cardDisplays = controller.getCIHDisplays();
        resetCardDisplays(cardDisplays);

        CardCollection hand = player.getHand();
        List<ActionCard> actionCards = hand.getDistinctActionCards();
        List<TreasureCard> treasureCards = hand.getDistinctTreasureCards();
        List<VictoryCard> victoryCards = hand.getDistinctVictoryCards();

        Boolean[] cardsTypesToHighlight = calcCardTypesToHighlight();

        int index = 0;
        for(ActionCard actionCard: actionCards) {
            setCardDisplay(cardDisplays[index],actionCard,hand.numCardInCollection(actionCard));
            if(cardsTypesToHighlight[0] || showThisType(actionCard)) {
                cardDisplays[index].setStyle(controller.getGreenCardGlowStyle());
            }
            index++;
        }
        for(TreasureCard treasureCard: treasureCards) {
            setCardDisplay(cardDisplays[index],treasureCard,hand.numCardInCollection(treasureCard));
            if(cardsTypesToHighlight[1] || showThisType(treasureCard)) {
                cardDisplays[index].setStyle(controller.getGreenCardGlowStyle());
            }
            index++;
        }
        for(VictoryCard victoryCard: victoryCards) {
            setCardDisplay(cardDisplays[index],victoryCard,hand.numCardInCollection(victoryCard));
            if(cardsTypesToHighlight[2] || showThisType(victoryCard)) {
                cardDisplays[index].setStyle(controller.getGreenCardGlowStyle());
            }
            index++;
        }
    }
    private static boolean showThisType(Card card) {
        String phase = player.getPhase();

        return (phase.equals("discardPhase") || phase.equals("trashPhase") || phase.equals("toDeckPhase")) && isType(card);
    }
    private static Boolean[] calcCardTypesToHighlight() {
        boolean highlightActionCards = false;
        boolean highlightTreasureCards = false;
        boolean highlightVictoryCards = false;

        String phase = player.getPhase();
        switch (phase) {
            case "actionPhase": {
                highlightActionCards = true;
                break;
            }
            case "buyPhase": {
                highlightTreasureCards = true;
                break;
            }
            case "toDeckPhase":
            case "trashPhase":
            case "discardPhase": {
                String type = player.getActionCardInPlay().getAction().getType();
//                System.out.println(type);
                if(type.equals("all")) {
                    type = "actionCard treasureCard victoryCard";
                }

                if(type.contains("actionCard")) {
                    highlightActionCards = true;
                }
                if(type.contains("treasureCard")) {
                    highlightTreasureCards = true;
                }
                if(type.contains("victoryCard")) {
                    highlightVictoryCards = true;
                }
                break;
            }
        }
        return new Boolean[]{highlightActionCards,highlightTreasureCards,highlightVictoryCards};
    }
    private static void updateDeck() {
        DeckDisplay deckDisplay = controller.getPlayerDeckDisplay();
        int numCards = player.getDeck().getSize();
        deckDisplay.getCardDisplay().setNum(numCards);
        deckDisplay.show();
    }
    private static void updateDiscard() {
        Card topDiscardCard = player.getDiscardPile().peekLastCard();
        int numCardsInDiscardPile = player.getDiscardPile().getSize();
        DeckDisplay discardDisplay = controller.getPlayerDiscardDisplay();

        discardDisplay.getCardDisplay().setNum(numCardsInDiscardPile);
        if(topDiscardCard==null) {
            discardDisplay.hide();
        } else {
            discardDisplay.getCardDisplay().setCard(topDiscardCard);
            discardDisplay.show();
        }

    }
    private static void updateGameInfoText(String text) {
        controller.getGameInfoText().setText(text);
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
    private static void displayGameOver(String gameOverText) {
        updateGameInfoText(gameOverText);

        resetCardDisplays(controller.getCIHDisplays());
        resetCardDisplays(controller.getCIPDisplays());
        controller.getActionButton().setVisible(false);
        controller.getInPlayPlayerLabel().hide();
        controller.getPlayerDiscardDisplay().hide();
        controller.getPlayerDeckDisplay().hide();
        controller.getOpponentDeckDisplay().hide();
    }
}
