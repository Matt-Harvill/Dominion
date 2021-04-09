package controller;

import javafx.scene.control.Button;
import javafx.scene.text.Text;
import model.CardCollection;
import model.Player;
import model.card.ActionCard;
import model.card.Card;
import model.card.TreasureCard;
import model.card.VictoryCard;
import view.*;

import java.util.List;

public class DisplayUpdater {

    private static final GameController controller = Main.getGameController();
    private static final Player player = Main.getPlayer();

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

    public static void updateInPlayDisplay(CardCollection inPlay, String playerName, int numCards, boolean myTurn) {
        System.out.println("updateInPlayDisplay called on " + playerName);
        updateCardsInPlay(inPlay);
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
    private static void updateInPlayPlayerLabel(String otherPlayerName, boolean myTurn) {
        LabelDisplay labelDisplay = controller.getInPlayPlayerLabel();
        labelDisplay.setText(otherPlayerName);
        if(myTurn) {
            labelDisplay.hide();
        } else {
            labelDisplay.show();
        }

    }

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
            System.out.println("playerLabel name: " + display.getName());
            if (playerName.equals(display.getName()) || display.getName().equals("")) {
                display.setName(playerName);
                display.setNum(points);
                display.show();
                break;
            }
        }
    }
    public static void gameOver(String gameOverText) {
        updateGameInfoText(gameOverText);

        resetCardDisplays(controller.getCIHDisplays());
        resetCardDisplays(controller.getCIPDisplays());
        controller.getActionButton().setVisible(false);
        controller.getInPlayPlayerLabel().hide();
        controller.getPlayerDiscardDisplay().hide();
        controller.getPlayerDeckDisplay().hide();
        controller.getOpponentDeckDisplay().hide();
    }

    //---------------------Only this player----------------------//
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

    public static void updateHandDisplay() {
        updateCardsInHand();
        updateDeck();
        updateDiscard();
        updateActionBar();
    }
    private static void updateActionBar() {
        Text gameInfoText = controller.getGameInfoText();
        String gameInfoString = "";
        switch (player.getPhase()) {
            case "actionPhase":
                gameInfoString += "Number of Actions: " + player.getNumActions() + "   ";
                updateActionButtonText("Enter Buy Phase");
            case "buyPhase":
                gameInfoString += "Number of Buys Remaining : " + player.getNumBuys() + "   ";
                gameInfoString += "Purchase Power: " + player.getPurchasePower();
                updateActionButtonText("End Turn");
            case "endPhase": controller.getActionButton().setVisible(false);
            case "startPhase":
                updateGameInfoText("Wait for you turn");
                gameInfoString += "Here is your hand";
        }
        gameInfoText.setText(gameInfoString);
    }
    private static void updateActionButtonText(String text) {
        Button actionButton = controller.getActionButton();
        actionButton.setText(text);
        actionButton.setVisible(true);
    }
    private static void updateCardsInHand() {
        CardDisplay[] cardDisplays = controller.getCIHDisplays();
        resetCardDisplays(cardDisplays);

        CardCollection hand = player.getHand();
        List<ActionCard> actionCards = hand.getDistinctActionCards();
        List<TreasureCard> treasureCards = hand.getDistinctTreasureCards();
        List<VictoryCard> victoryCards = hand.getDistinctVictoryCards();

        int index = 0;
        for(ActionCard actionCard: actionCards) {
            setCardDisplay(cardDisplays[index],actionCard,hand.numCardInCollection(actionCard));
            if(player.getPhase().equals("actionPhase")) {
                cardDisplays[index].setStyle(controller.getGreenCardGlowStyle());
            }
            index++;
        }
        for(TreasureCard treasureCard: treasureCards) {
            setCardDisplay(cardDisplays[index],treasureCard,hand.numCardInCollection(treasureCard));
            if(player.getPhase().equals("buyPhase")) {
                cardDisplays[index].setStyle(controller.getGreenCardGlowStyle());
            }
            index++;
        }
        for(VictoryCard victoryCard: victoryCards) {
            setCardDisplay(cardDisplays[index],victoryCard,hand.numCardInCollection(victoryCard));
            index++;
        }
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
}
