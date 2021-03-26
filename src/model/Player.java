package model;


import model.card.ActionCard;
import model.card.Card;
import model.card.TreasureCard;
import model.card.VictoryCard;
import model.factory.CardCollectionFactory;
import model.factory.CardFactory;

import java.util.Scanner;

public class Player {

    private String name, phase;
    private CardCollection hand, deck, discardPile, inPlay;
    private int handLimit, numActions, numBuys, purchasePower, amountSpentThisTurn, bonusPurchasePower;

    public Player() {
        hand = CardCollectionFactory.getCardCollection();
        deck = CardCollectionFactory.getCardCollection();
        discardPile = CardCollectionFactory.getCardCollection();
        inPlay = CardCollectionFactory.getCardCollection();
        for(int i=0; i<7; i++)
            deck.addCardToCollection(CardFactory.getCard("Copper"));
        for(int i=0; i<3; i++)
            deck.addCardToCollection(CardFactory.getCard("Estate"));
        shuffleDeck();

        handLimit = 5;
        numActions = 1;
        numBuys = 1;
        purchasePower = 0;
        amountSpentThisTurn = 0;
        bonusPurchasePower = 0;
    }

    public CardCollection getHand() {
        return hand;
    }
    public CardCollection getDeck() {
        return deck;
    }
    public CardCollection getDiscardPile() {
        return discardPile;
    }

    public String getName() {
        return name;
    }
    public CardCollection getInPlay() {
        return inPlay;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhase() {
        return phase;
    }
    public void setPhase(String phase) {
        this.phase = phase;
    }

    public void newTurn(){
        handLimit = 5;
        numActions = 1;
        numBuys = 1;
        purchasePower = 0;
        amountSpentThisTurn = 0;
        bonusPurchasePower = 0;
        drawHand();
    }
    public void endTurn() {
        discardHand();
        discardInPlay();
    }
    public void buyCard(Card card) {
        discardPile.addCardToCollection(card);
        numBuys--;
        amountSpentThisTurn+=card.getCost();
    }
    public void playCard(Card card) {
        inPlay.addCardToCollection(hand.removeCardFromCollection(card));
        if(card instanceof ActionCard) {
            playActionCard((ActionCard) card);
        } else if(card instanceof TreasureCard) {
            playTreasureCard((TreasureCard) card);
        }
    }

    public int getNumBuys(){
        return numBuys;
    }
    public int getNumActions() {
        return numActions;
    }
    public int getPurchasePower(){
        return purchasePower - amountSpentThisTurn + bonusPurchasePower;
    }
    public int getTotalPoints() {
        int points = 0;
        points+=getVictoryPoints(discardPile);
        points+=getVictoryPoints(deck);
        points+=getVictoryPoints(hand);
        points+=getVictoryPoints(inPlay);
        return points;
    }

    private int getVictoryPoints(CardCollection collection) {
        int points = 0;
        for (VictoryCard card : collection.getDistinctVictoryCards()) {
            int numCard = collection.numCardInCollection(card);
            points += card.getVictoryPoints()*numCard;
        }
        return points;
    }
    private boolean drawCardFromDeck(){
        if(deck.getSize()==0){
            discardPileToDeck();
            shuffleDeck();
            System.out.println("drawCardFromDeck\ndeck: ");
            deck.printCardNamesInCollection();
            System.out.println("discard: ");
            discardPile.printCardNamesInCollection();
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
    private void discardPileToDeck(){
        while(discardPile.getSize()>0) {
            deck.addCardToCollection(discardPile.drawTopCard());
        }
    }
    private void drawHand(){
        for(int i=0;i<handLimit;i++){
            if(!drawCardFromDeck()) break;
        }
    }
    private void shuffleDeck(){
        deck.shuffle();
    }
    private void discardHand(){
        while(hand.getSize()>0) {
            discardPile.addCardToCollection(hand.drawTopCard());
        }
    }
    private void discardInPlay(){
        while(inPlay.getSize()>0) {
            discardPile.addCardToCollection(inPlay.drawTopCard());
        }
    }
    private void playActionCard(ActionCard actionCard){
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
        numActions--;
    }
    private void playTreasureCard(TreasureCard treasureCard) {
        purchasePower+=treasureCard.getPurchasePower();
    }
}
