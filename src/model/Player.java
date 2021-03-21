package model;


import controller.UserInterfaceHub;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.card.ActionCard;
import model.card.Card;
import model.card.TreasureCard;
import model.card.VictoryCard;
import model.factory.CardCollectionFactory;
import model.factory.CardFactory;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Player {

    private String name;
    private CardCollection hand, deck, discardPile;
    private int handLimit, numActions, numBuys, handPurchasePower, amountSpentThisTurn, bonusPurchasePower;

    public Player() {
        hand = CardCollectionFactory.getCardCollection();
        deck = CardCollectionFactory.getCardCollection();
        discardPile = CardCollectionFactory.getCardCollection();
        for(int i=0; i<7; i++)
            deck.addCardToCollection(CardFactory.getCard("Copper"));
        for(int i=0; i<3; i++)
            deck.addCardToCollection(CardFactory.getCard("Estate"));
        shuffleDeck();

        handLimit = 5;
        numActions = 1;
        numBuys = 1;
        handPurchasePower = 0;
        amountSpentThisTurn = 0;
        bonusPurchasePower = 0;
    }

    public void executeTurn() {
        newTurn();
        displayHandOn(UserInterfaceHub.getGameController().getCardsInHandDisplay(),
                UserInterfaceHub.getGameController().getCardsInHandNumBackground(),UserInterfaceHub.getGameController().getCardInHandNumbers());
        discardHand();
    }

    public CardCollection getHand() {
        return hand;
    }
    public void setHand(CardCollection hand) {
        this.hand = hand;
    }
    public CardCollection getDeck() {
        return deck;
    }
    public void setDeck(CardCollection deck) {
        this.deck = deck;
    }
    public CardCollection getDiscardPile() {
        return discardPile;
    }
    public void setDiscardPile(CardCollection discardPile) {
        this.discardPile = discardPile;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void newTurn(){
        handLimit = 5;
        numActions = 1;
        numBuys = 1;
        handPurchasePower = 0;
        amountSpentThisTurn = 0;
        bonusPurchasePower = 0;
        drawHand();
    }
    public int getNumBuys(){
        return numBuys;
    }
    public int getNumActions() {
        return numActions;
    }
    public void performAction(ActionCard actionCard){
        Scanner input = new Scanner(actionCard.getAction());
        String extractedString;
        int numAdds = 0;

        while(input.hasNext()){
            extractedString = input.next();
            if(extractedString.contains("+")){
                numAdds = Integer.parseInt(extractedString.substring(extractedString.indexOf("+")+1,extractedString.indexOf("+")+2));
            }
            else if(extractedString.contains("Card")){
                handLimit+=numAdds;
                for(int i=0;i<numAdds;i++){
                    drawCardFromDeck();
                }
            }
            else if(extractedString.contains("Action")){
                numActions+=numAdds;
            }
            else if(extractedString.contains("Buy")){
                numBuys+=numAdds;
            }
            else if(extractedString.contains("Coin")){
                bonusPurchasePower+=numAdds;
            }
        }
        discardPile.addCardToCollection(actionCard);
        hand.removeCardFromCollection(actionCard);
        numActions--;
    }
    public int getHandPurchasePower(){
        handPurchasePower = 0;
        for(TreasureCard card: hand.getTreasureCards()){
            handPurchasePower+=card.getPurchasePower();
        }
        handPurchasePower-=amountSpentThisTurn;
        handPurchasePower+=bonusPurchasePower;
        return handPurchasePower;
    }
    public boolean drawCardFromDeck(){
        if(deck.getSize()==0){
            discardPileToDeck();
            shuffleDeck();
        }
        if(hand.getSize()>=handLimit){
            System.out.println("You already have a full hand");
            return false;
        }
        else if(deck.getSize()==0) {
            System.out.println("You have no more cards");
            return false;
        }
        else{
            hand.addCardToCollection(deck.drawTopCard());
            return true;
        }
    }
    public void discardPileToDeck(){
        while(discardPile.getSize()>0) {
            deck.addCardToCollection(discardPile.drawTopCard());
        }
    }
    public void drawHand(){
        for(int i=0;i<handLimit;i++){
            if(!drawCardFromDeck()) break;
        }
    }
    public void shuffleDeck(){
        deck.shuffle();
    }
    public void discardHand(){
        while(hand.getSize()>0) {
            discardPile.addCardToCollection(hand.drawTopCard());
        }
    }
    public int getTotalPoints() {
        int points = 0;
        for (VictoryCard card : discardPile.getVictoryCards()) {
            points += card.getVictoryPoints();
        }
        for (VictoryCard card : deck.getVictoryCards()) {
            points += card.getVictoryPoints();
        }
        for (VictoryCard card : hand.getVictoryCards()) {
            points += card.getVictoryPoints();
        }
        return points;
    }
    public void completeTransaction(Card card) {
        discardPile.addCardToCollection(card);
        numBuys--;
        amountSpentThisTurn+=card.getCost();
    }

    public void displayHandOn(Rectangle[] displaySections, Rectangle[] displayNumberBackgrounds, Text[] displayNumbers) {
        int index = 0;
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
                displaySections[index].setFill(new ImagePattern(cardToDisplay.getCardImage()));
                displaySections[index].setVisible(true);
                displayNumberBackgrounds[index].setVisible(false);
                displayNumbers[index].setVisible(false);
                index++;
            } else if(countOfCard>1) {
                displaySections[index].setFill(new ImagePattern(cardToDisplay.getCardImage()));
                displayNumbers[index].setText(String.valueOf(countOfCard));
                displaySections[index].setVisible(true);
                displayNumberBackgrounds[index].setVisible(true);
                displayNumbers[index].setVisible(true);
                index++;
            }
        }

    }
}
