package org.matthew.model;


import org.matthew.model.card.action.ActionCard;
import org.matthew.model.card.Card;
import org.matthew.model.card.TreasureCard;
import org.matthew.model.card.VictoryCard;
import org.matthew.model.factory.CardCollectionFactory;
import org.matthew.model.factory.CardFactory;
import org.matthew.controller.ActionCardPerformer;

public class Player {

    private String name, phase;
    private CardCollection hand, deck, discardPile, inPlay, select, trash;
    private CardCollection boughtThisTurn, gainedThisTurn;
    private int handLimit, numActions, numBuys, purchasePower, amountSpentThisTurn, bonusPurchasePower;
    private ActionCard actionCardInPlay;

    public Player() {
        hand = CardCollectionFactory.getCardCollection();
        deck = CardCollectionFactory.getCardCollection();
        discardPile = CardCollectionFactory.getCardCollection();
        inPlay = CardCollectionFactory.getCardCollection();
        select = CardCollectionFactory.getCardCollection();
        trash = CardCollectionFactory.getCardCollection();
        boughtThisTurn = CardCollectionFactory.getCardCollection();
        gainedThisTurn = CardCollectionFactory.getCardCollection();

        for(int i=0; i<7; i++)
            deck.addCardToCollection(CardFactory.getCard("Copper"));
        for(int i=0; i<3; i++)
            deck.addCardToCollection(CardFactory.getCard("Estate"));
        shuffleCollection(deck);

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
    public CardCollection getSelect() {return select;}
    public CardCollection getTrash() {return trash;}
    public CardCollection getBoughtThisTurn() {
        return boughtThisTurn;
    }
    public CardCollection getGainedThisTurn() {
        return gainedThisTurn;
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
        boughtThisTurn = CardCollectionFactory.getCardCollection();
        gainedThisTurn= CardCollectionFactory.getCardCollection();
    }
    public void buyCard(Card card) {
        discardPile.addCardToCollection(card);
        boughtThisTurn.addCardToCollection(card);
        numBuys--;
        amountSpentThisTurn+=card.getCost();
    }
    public void gainCard(Card card, String location) {
        CardCollection collection = null;
        if(location==null) {
            collection = discardPile;
        } else {
            switch (location) {
                case "hand": {
                    collection = hand; break;
                }
                case "deck": {
                    collection = deck; break;
                }
            }
        }
        collection.addCardToCollection(card);
        gainedThisTurn.addCardToCollection(card);
    }
    public void playCard(Card card) {
        inPlay.addCardToCollection(hand.removeCardFromCollection(card));
        if(card instanceof ActionCard) {
            actionCardInPlay = (ActionCard) card;
            ActionCardPerformer.playActionCard();
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
    public int getTotalNumCards() {
        return deck.getSize() + hand.getSize() + discardPile.getSize() + select.getSize() + inPlay.getSize();
    }
    public int getPoints() {
        int points = 0;
        points+=getVictoryPoints(discardPile);
        points+=getVictoryPoints(deck);
        points+=getVictoryPoints(hand);
        points+=getVictoryPoints(inPlay);
        points+=getVictoryPoints(select);
        return points;
    }
    public ActionCard getActionCardInPlay() {return actionCardInPlay;}
    public void resetActionCardInPlay() {
        actionCardInPlay = null;
    }

    private int getVictoryPoints(CardCollection collection) {
        int points = 0;
        for (VictoryCard card : collection.getDistinctVictoryCards()) {
            int numCard = collection.numCardInCollection(card);
            points += card.getVictoryPoints()*numCard;
        }
        return points;
    }
    public boolean drawCardFromDeck(){
        if(deck.getSize()==0){
            shuffleCollection(discardPile);
            discardPileToDeck();
        }
        if(hand.getSize()>=handLimit){
            //Full hand
            return false;
        }
        else if(deck.getSize()==0) {
            //No more cards
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
    private void shuffleCollection(CardCollection collection){
        collection.shuffle();
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

    private void playTreasureCard(TreasureCard treasureCard) {
        purchasePower+=treasureCard.getPurchasePower();
    }

    public String toString() {
        String playerInfo = name + " info:\n";
        playerInfo+="handLimit: " + handLimit;
        playerInfo+=" numActions: " + numActions;
        playerInfo+=" numBuys: " + numBuys + "\n";
        playerInfo+="purchasePower: " + purchasePower;
        playerInfo+=" amountSpentThisTurn: " + amountSpentThisTurn;
        playerInfo+=" bonusPurchasePower: " + bonusPurchasePower;
        return playerInfo;
    }
    public String getInfoString() {
        return name + " " + getPoints() + " " + (deck.getSize()+hand.getSize()+ discardPile.getSize()) + " ";
    }

    //========================New Functions for ActionCards================//
    public void incrementNumBuys(int increment) {
        numBuys += increment;
    }
    public void incrementNumActions(int increment) {
        numActions += increment;
    }
    public void incrementPurchasePower(int increment) {
        purchasePower += increment;
    }
    public void incrementHandLimit() {
        handLimit++;
    }
    public void decrementNumActions() {
        numActions--;
    }

    public void handToSelect(Card cardClicked) {
        select.addCardToCollection(hand.removeCardFromCollection(cardClicked));
    }
    public void selectToHand(Card cardClicked) {
        hand.addCardToCollection(select.removeCardFromCollection(cardClicked));
    }
    public void selectToLocation(String location) {
        CardCollection collection = null;
        switch (location) {
            case "discard": {
                collection = discardPile; break;
            }
            case "trash": {
                collection = trash; break;
            }
            case "deck": {
                collection = deck; break;
            }
        }
        while(select.getSize()>0) {
            collection.addToTop(select.drawTopCard());
        }
    }
}
