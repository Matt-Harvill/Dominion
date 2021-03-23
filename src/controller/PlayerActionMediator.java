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

    public void startPhase() {
        player.newTurn();
        controller.getPlayerHandDisplay().setNamesOfCards(displayHand());
        controller.getActionButton().setText("End Turn");
        controller.getActionButton().setVisible(true);
        actionPhase();
    }
    public void actionPhase() {
        List<ActionCard> actionCards = player.getHand().getDistinctActionCards();
        Rectangle[] cardsInHand = controller.getPlayerHandDisplay().getCards();
        for(ActionCard actionCard: actionCards) {
            int index = cardsInDisplayOrder.indexOf(actionCard);
            cardsInHand[index].setStyle(controller.getGreenCardGlowStyle());
        }
//        while(player.getNumActions()>0 && player.getHand().getDistinctActionCards().size()>0) {
//            //wait
//        }
        controller.getBuyPhaseButton().setVisible(true);
        if(actionCards.size()==0) buyPhase();
    }
    public void buyPhase() {
        controller.getBuyPhaseButton().setVisible(false);
        System.out.println("buy phase entered");
        enableBuying(true);

        //do buy phase stuff

        List<TreasureCard> treasureCards = player.getHand().getDistinctTreasureCards();
        Rectangle[] cardsInHand = controller.getPlayerHandDisplay().getCards();
        for(TreasureCard treasureCard: treasureCards) {
            int index = cardsInDisplayOrder.indexOf(treasureCard);
            cardsInHand[index].setStyle(controller.getGreenCardGlowStyle());
        }

    }
    public void endPhase() {
        enableBuying(false);
        player.discardHand();
        controller.getPlayerHandDisplay().setNamesOfCards(displayHand());
        controller.getActionButton().setText("Start Turn");
        controller.getActionButton().setVisible(true);
    }

    public String[] displayHand() {
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
        return namesOfCardsForDisplay;
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
            playerHandDisplay.getCardNumBacks()[i].setVisible(false);
            playerHandDisplay.getCardNums()[i].setVisible(false);
        }
        cardsInDisplayOrder = new ArrayList<>();
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

        if(player.getNumBuys()==0) {
            endPhase();
        }
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

    public void playActionCard(String cardClicked) {
        ActionCard card = (ActionCard) CardFactory.getCard(cardClicked);
        player.performAction(card);
        controller.getPlayerHandDisplay().setNamesOfCards(displayHand());
        if(player.getNumActions()==0) {
            buyPhase();
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
