package controller;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.CardCollection;
import model.Player;
import model.card.ActionCard;
import model.card.Card;
import model.card.TreasureCard;
import model.card.VictoryCard;
import model.factory.CardFactory;
import view.CardSupplyDisplay;
import view.PlayerHandDisplay;

import java.util.ArrayList;
import java.util.List;

public class PlayerActionMediator {

    private Player player;
    private GameController controller;

    private List<Card> cardsInDisplayOrder;

    public PlayerActionMediator(Player player, GameController controller) {
        this.player = player;
        this.controller = controller;
        cardsInDisplayOrder = new ArrayList<>();
    }

    public void startTurn() {
        player.newTurn();
        displayHand();
        actionPhase();
    }
    public void actionPhase() {
        controller.getActionButton().setText("Enter Buy Phase");
        List<ActionCard> actionCards = player.getHand().getDistinctActionCards();
        Rectangle[] cardsInHand = controller.getPlayerHandDisplay().getCards();
        for(ActionCard actionCard: actionCards) {
            int index = cardsInDisplayOrder.indexOf(actionCard);
            cardsInHand[index].setStyle(controller.getGreenCardGlowStyle());
        }
        checkNumActions();
    }
    public void buyPhase() {
        enableBuying(true);

        List<TreasureCard> treasureCards = player.getHand().getDistinctTreasureCards();
        Rectangle[] cardsInHand = controller.getPlayerHandDisplay().getCards();
        for(TreasureCard treasureCard: treasureCards) {
            int index = cardsInDisplayOrder.indexOf(treasureCard);
            cardsInHand[index].setStyle(controller.getGreenCardGlowStyle());
        }
        controller.getActionButton().setText("End Turn");
    }
    public void endPhase() {
        enableBuying(false);

        player.endTurn();
        displayHand();

        controller.getActionButton().setText("Start Turn");
    }

    public void buyFromCardSupply(String cardClicked) {

        CardSupplyDisplay display = controller.getCardSupplyDisplay();
        String[] cardNames = display.getNamesOfCardsInSupply();
        Text[] cardNumbers = display.getCardNums();

        int index = -1;
        for(int i=0;i<cardNames.length;i++) {
            if(cardNames[i] != null && cardNames[i].equals(cardClicked)) index = i;
        }

        Text cardNumber = cardNumbers[index];
        int numCardRemaining = Integer.parseInt(cardNumber.getText());
        int costOfCard = CardFactory.getCard(cardClicked).getCost();


        if(numCardRemaining==0) {
            addMessageToGameLog("There aren't any " + cardClicked + "s left");
        }
        else if(player.getHandPurchasePower() < costOfCard) {
            addMessageToGameLog("You don't have enough coins to purchase a " + cardClicked);
        }
        if(player.getHandPurchasePower() >= costOfCard && numCardRemaining>0 ) {
            addMessageToGameLog("You purchased a " + cardClicked);
            player.buyCard(CardFactory.getCard(cardClicked));

            if(numCardRemaining==1) {
                cardNumber.setText("0");
            } else {
                cardNumber.setText(String.valueOf(numCardRemaining-1));
            }
        }

        displayHand();
        checkNumBuys();
    }

    public void playCard(String cardClicked) {
        for(Card card: player.getHand().getCollection()) {
            if(card.getName().equals(cardClicked)) {
                player.playCard(card);
                break;
            }
        }
        displayHand();
        checkNumActions();
    }

    private void enableBuying(boolean enable) {
        CardSupplyDisplay cardSupplyDisplay = controller.getCardSupplyDisplay();

        Rectangle[] cardBuyButtons = cardSupplyDisplay.getCardBuyButtons();
        Rectangle[] cards = cardSupplyDisplay.getCardsInSupply();
        for(int i=0;i<cards.length;i++) {
            if(cards[i].isVisible()) {
                cardBuyButtons[i].setVisible(enable);
            }
        }
    }
    private void displayHand() {
        resetHandDisplay();

        int index = 0;
        CardCollection hand = player.getHand();
        String [] namesOfCardsForDisplay = new String[hand.getSize()];

        List<ActionCard> actionCards = hand.getDistinctActionCards();
        List<TreasureCard> treasureCards = hand.getDistinctTreasureCards();
        List<VictoryCard> victoryCards = hand.getDistinctVictoryCards();

        for(ActionCard actionCard: actionCards) {
            int numCard = hand.numCardInCollection(actionCard);
            displayCardInHand(actionCard,index,numCard);
            cardsInDisplayOrder.add(actionCard);
            namesOfCardsForDisplay[index] = actionCard.getName();
            index++;
        }
        for(TreasureCard treasureCard: treasureCards) {
            int numCard = hand.numCardInCollection(treasureCard);
            displayCardInHand(treasureCard,index,numCard);
            cardsInDisplayOrder.add(treasureCard);
            namesOfCardsForDisplay[index] = treasureCard.getName();
            index++;
        }
        for(VictoryCard victoryCard: victoryCards) {
            int numCard = hand.numCardInCollection(victoryCard);
            displayCardInHand(victoryCard,index,numCard);
            cardsInDisplayOrder.add(victoryCard);
            namesOfCardsForDisplay[index] = victoryCard.getName();
            index++;
        }
        controller.getPlayerHandDisplay().setNamesOfCards(namesOfCardsForDisplay);

        Text gameInfoText = controller.getGameInfoText();
        StringBuilder gameInfoString = new StringBuilder();
        gameInfoString.append("Number of Actions: " + player.getNumActions() + " ");
        gameInfoString.append("Number of Buys: " + player.getNumBuys() + " ");
        gameInfoString.append("Purchase Power: " + player.getHandPurchasePower());
        gameInfoText.setText(String.valueOf(gameInfoString));

    }
    private void displayCardInHand(Card card, int index, int numCard) {
        PlayerHandDisplay playerHandDisplay = controller.getPlayerHandDisplay();
        if(numCard==1) {
            playerHandDisplay.getCards()[index].setFill(new ImagePattern(card.getCardImage()));
            playerHandDisplay.getCards()[index].setVisible(true);
        } else if(numCard>1) {
            playerHandDisplay.getCards()[index].setFill(new ImagePattern(card.getCardImage()));
            playerHandDisplay.getCardNums()[index].setText(String.valueOf(numCard));
            playerHandDisplay.getCards()[index].setVisible(true);
            playerHandDisplay.getCardNumBacks()[index].setVisible(true);
            playerHandDisplay.getCardNums()[index].setVisible(true);
        }
    }
    private void resetHandDisplay() {
        PlayerHandDisplay playerHandDisplay = controller.getPlayerHandDisplay();

        for(int i=0; i<playerHandDisplay.getCards().length; i++) {
            playerHandDisplay.getCards()[i].setVisible(false);
            playerHandDisplay.getCards()[i].setStyle(null);
            playerHandDisplay.getCardNumBacks()[i].setVisible(false);
            playerHandDisplay.getCardNums()[i].setVisible(false);
        }
        cardsInDisplayOrder = new ArrayList<>();
    }
    private void checkNumActions() {
        if (player.getNumActions()==0) {
            buyPhase();
        }
    }
    private void checkNumBuys() {
        if(player.getNumBuys()==0) {
            endPhase();
        }
    }

    public void addMessageToChatLog(String msg) {
        if(controller.getChatDisplayStrings().size()>=7) controller.getChatDisplayStrings().remove(0);
        controller.getChatDisplayStrings().add(msg);
        controller.getChatType().setText(null);
        StringBuilder builder = new StringBuilder();
        for(String s: controller.getChatDisplayStrings()) {
            builder.append(s); builder.append("\n");
        }
        controller.getChatLog().setText(builder.toString());
    }
    public void addMessageToGameLog(String msg) {
        if(controller.getGameDisplayStrings().size()>=7) controller.getGameDisplayStrings().remove(0);
        controller.getGameDisplayStrings().add(msg);
        controller.getGameLog().setText(null);
        StringBuilder builder = new StringBuilder();
        for(String s: controller.getGameDisplayStrings()) {
            builder.append(s); builder.append("\n");
        }
        controller.getGameLog().setText(builder.toString());
    }
}
