package controller;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import model.CardCollection;
import model.Player;
import model.card.Card;
import model.factory.CardFactory;

import java.util.List;
import java.util.Set;

public class PlayerActionMediator {

    public static void StartTurn(Player player, GameController controller) {
        controller.getActionButton().setText(null);
        controller.getActionButton().setVisible(false);
        player.newTurn();
        CardCollection hand = player.getHand();
        displayHand(player,controller);
        if(hand.getActionCards().size()>0) {
            //do action stuff
        }
        if(hand.getTreasureCards().size()>0) {
            enableBuying(player,controller);
        }
        controller.getActionButton().setText("End Turn");
        controller.getActionButton().setVisible(true);
    }

    public static void EndTurn(Player player, GameController controller) {
        controller.getActionButton().setText(null);
        controller.getActionButton().setVisible(false);
        player.discardHand();
        CardCollection hand = player.getHand();
        displayHand(player,controller);
        controller.getActionButton().setText("Start Turn");
        controller.getActionButton().setVisible(true);
    }

    public static void displayHand(Player player, GameController controller) {
        int index = 0;
        CardCollection hand = player.getHand();
        List<Card> cards = hand.getAllCards();
        Set<String> distinctCardsInHand = hand.getDistinctCards();

        for(int i=0; i<controller.getCardsInHandDisplay().length; i++) {
            controller.getCardsInHandDisplay()[i].setVisible(false);
            controller.getCardsInHandNumBackground()[i].setVisible(false);
            controller.getCardInHandNumbers()[i].setVisible(false);
        }

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
        }
    }

    public static void enableBuying(Player player, GameController controller) {
        Rectangle[] buttons = controller.getBuyActionCardButtons();
        Rectangle[] actionCards = controller.getActionCardsInSupply();
        for(int i=0; i< actionCards.length; i++) {
            if(actionCards[i].isVisible()) {
                buttons[i].setVisible(true);
            }
        }
    }

    public static void buyCard(Player player, GameController controller, String actionCardClicked) {
        if(player.getHandPurchasePower() >= CardFactory.getCard(actionCardClicked).getCost()) {
            System.out.println("Card was purchased");
        }
    }
}
