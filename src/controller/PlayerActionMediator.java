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
import view.ActionCardSupplyDisplay;
import view.LeftSupplyCardDisplay;
import view.PlayerHandDisplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        displayHand();
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

        buyPhase();
    }
    public void buyPhase() {
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
        player.discardHand();
        displayHand();
        controller.getActionButton().setText("Start Turn");
        controller.getActionButton().setVisible(true);
    }

    public void displayHand() {
        resetHandDisplay();

        int index = 0;
        CardCollection hand = player.getHand();

        List<ActionCard> actionCards = hand.getDistinctActionCards();
        List<TreasureCard> treasureCards = hand.getDistinctTreasureCards();
        List<VictoryCard> victoryCards = hand.getDistinctVictoryCards();

        for(ActionCard actionCard: actionCards) {
            int numCard = hand.numCardInCollection(actionCard);
            displayCardInHand(actionCard,index,numCard);
            cardsInDisplayOrder.add(actionCard);
            index++;
        }
        for(TreasureCard treasureCard: treasureCards) {
            int numCard = hand.numCardInCollection(treasureCard);
            displayCardInHand(treasureCard,index,numCard);
            cardsInDisplayOrder.add(treasureCard);
            index++;
        }
        for(VictoryCard victoryCard: victoryCards) {
            int numCard = hand.numCardInCollection(victoryCard);
            displayCardInHand(victoryCard,index,numCard);
            cardsInDisplayOrder.add(victoryCard);
            index++;
        }

        /*
        CardCollection hand = player.getHand();
        List<Card> cards = hand.getAllCards();
        Set<String> distinctCardsInHand = hand.getDistinctCards();



        int countOfCard;

        for(String s: distinctCardsInHand) {
            Card cardToDisplay = null;
            countOfCard = 0;
            for(Card c: cards) {
                if(c.getName().equals(s)) {
                    countOfCard = hand.numCardInCollection(c);
                    cardToDisplay = c;
                    break;
                }
            }
            if(countOfCard==1) {
                controller.getCardsInHandDisplay()[index].setFill(new ImagePattern(cardToDisplay.getCardImage()));
                controller.getCardsInHandDisplay()[index].setVisible(true);
                index++;
            } else if(countOfCard>1) {
                controller.getCardsInHandDisplay()[index].setFill(new ImagePattern(cardToDisplay.getCardImage()));
                controller.getCardInHandNumbers()[index].setText(String.valueOf(countOfCard));
                controller.getCardsInHandDisplay()[index].setVisible(true);
                controller.getCardsInHandNumBackground()[index].setVisible(true);
                controller.getCardInHandNumbers()[index].setVisible(true);
                index++;
            }
        }*/
    }
    public void displayCardInHand(Card card, int index, int numCard) {
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
    public void resetHandDisplay() {
        PlayerHandDisplay playerHandDisplay = controller.getPlayerHandDisplay();

        for(int i=0; i<playerHandDisplay.getCards().length; i++) {
            playerHandDisplay.getCards()[i].setVisible(false);
            playerHandDisplay.getCardNumBacks()[i].setVisible(false);
            playerHandDisplay.getCardNums()[i].setVisible(false);
        }
        cardsInDisplayOrder = new ArrayList<>();
    }

    public void buyFromLeftCardSupply(String cardClicked) {
        LeftSupplyCardDisplay display = controller.getLeftSupplyCardDisplay();
        String[] names = display.getNamesOfCardsInSupply();
        Text[] actionCardNumbers = display.getCardNums();

        int index = -1;
        for(int i=0;i<names.length;i++) {
            if(names[i] != null && names[i].equals(cardClicked)) index = i;
        }

        Text actionCardNumber = actionCardNumbers[index];
        int numCardRemaining = Integer.parseInt(actionCardNumber.getText());
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
                actionCardNumber.setText("0");
            } else {
                actionCardNumber.setText(String.valueOf(numCardRemaining-1));
            }
        }

        if(player.getNumBuys()==0) {
            enableBuying(false);
            endPhase();
        }
    }
    public void buyFromActionCardSupply(String actionCardClicked) {

        ActionCardSupplyDisplay display = controller.getActionCardSupplyDisplay();
        String[] names = display.getNamesOfActionCardsInSupply();
        Text[] actionCardNumbers = display.getActionCardNumbers();

        int index = -1;
        for(int i=0;i<names.length;i++) {
            if(names[i] != null && names[i].equals(actionCardClicked)) index = i;
        }

        Text actionCardNumber = actionCardNumbers[index];
        int numCardRemaining = Integer.parseInt(actionCardNumber.getText());
        int costOfCard = CardFactory.getCard(actionCardClicked).getCost();


        if(numCardRemaining==0) {
            addMessageToGameLog("There aren't any " + actionCardClicked + "s left");
        }
        else if(player.getHandPurchasePower() < costOfCard) {
            addMessageToGameLog("You don't have enough coins to purchase a " + actionCardClicked);
        }
        if(player.getHandPurchasePower() >= costOfCard && numCardRemaining>0 ) {
            addMessageToGameLog("You purchased a " + actionCardClicked);
            player.buyCard(CardFactory.getCard(actionCardClicked));

            if(numCardRemaining==1) {
                actionCardNumber.setText("0");
            } else {
                actionCardNumber.setText(String.valueOf(numCardRemaining-1));
            }
        }

        if(player.getNumBuys()==0) {
            enableBuying(false);
            endPhase();
        }
    }
    public void enableBuying(boolean enable) {
        LeftSupplyCardDisplay leftSupplyCardDisplay = controller.getLeftSupplyCardDisplay();
        ActionCardSupplyDisplay actionCardSupplyDisplay = controller.getActionCardSupplyDisplay();

        Rectangle[] leftCardButtons = leftSupplyCardDisplay.getCardBuyButtons();
        Rectangle[] leftCards = leftSupplyCardDisplay.getCardsInSupply();
        Rectangle[] actionCardButtons = actionCardSupplyDisplay.getBuyActionCardButtons();
        Rectangle[] actionCards = actionCardSupplyDisplay.getActionCardsInSupply();
        for(int i=0;i<leftCardButtons.length;i++) {
            if(leftCards[i].isVisible()) {
                leftCardButtons[i].setVisible(enable);
            }
        }
        for(int i=0; i< actionCards.length; i++) {
            if(actionCards[i].isVisible()) {
                actionCardButtons[i].setVisible(enable);
            }
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
