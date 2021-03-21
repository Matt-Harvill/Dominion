package model;

import model.card.ActionCard;
import model.card.Card;
import model.card.TreasureCard;
import model.card.VictoryCard;

import java.util.*;

public class CardCollection {

    private List<Card> collection;
    Set<String> distinctCards;

    public CardCollection() {
        collection = new LinkedList<>();
        distinctCards = new HashSet<>();
    }

    public void addCardToCollection(Card c){
        collection.add(c);
        distinctCards.add(c.getName());
    }
    public void removeCardFromCollection(Card c) {
        collection.remove(c);
        if(!collection.contains(c)) distinctCards.remove(c.getName());
    }

    public List<ActionCard> getActionCards(){
        List<ActionCard> actionCards = new LinkedList<>();
        for(Card card: collection) {
            if(card instanceof ActionCard) actionCards.add((ActionCard) card);
        }
        return actionCards;
    }
    public List<VictoryCard> getVictoryCards(){
        List<VictoryCard> victoryCards = new LinkedList<>();
        for(Card card: collection) {
            if(card instanceof VictoryCard) victoryCards.add((VictoryCard) card);
        }
        return victoryCards;
    }
    public List<TreasureCard> getTreasureCards(){
        List<TreasureCard> treasureCards = new LinkedList<>();
        for(Card card: collection) {
            if(card instanceof TreasureCard) treasureCards.add((TreasureCard) card);
        }
        return treasureCards;
    }
    public List<Card> getAllCards() {
        return collection;
    }

    public int getSize() {
        return collection.size();
    }
    public Card drawTopCard() {
        Card removedCard = collection.remove(0);
        if(!collection.contains(removedCard)) distinctCards.remove(removedCard.getName());
        return removedCard;
    }
    public void shuffle() {
        Collections.shuffle(collection);
    }
    public int numCardInCollection(Card card) {
        int numCards = 0;
        for(Card c: collection) {
            if(c.getName().equals(card.getName())) numCards++;
        }
        return numCards;
    }
    public Card getCardFromName(String cardName) {
        for(Card c: collection) if(c.getName().equals(cardName)) return c;
        return null;
    }
    public Set<String> getDistinctCards() {
        return distinctCards;
    }

}
