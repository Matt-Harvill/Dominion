package org.matthew.model;

import org.matthew.model.card.action.ActionCard;
import org.matthew.model.card.Card;
import org.matthew.model.card.TreasureCard;
import org.matthew.model.card.VictoryCard;

import java.util.*;

public class CardCollection {

    private List<Card> collection;

    public CardCollection() {
        collection = new LinkedList<>();
    }

    public void addCardToCollection(Card c){
        collection.add(c);
    }
    public Card removeCardFromCollection(Card c) {
        collection.remove(c);
        return c;
    }

    public Card peekLastCard() {
        if(collection.size()>0) {
            return collection.get(collection.size()-1);
        } else {
            return null;
        }
    }
    public List<Card> getCollection() {return collection;}
    public List<ActionCard> getDistinctActionCards(){
        List<ActionCard> actionCards = new LinkedList<>();
        for(Card card: collection) {
            if(card instanceof ActionCard) {
                if(!actionCards.contains(card))
                actionCards.add((ActionCard) card);
            }
        }
        actionCards.sort(Comparator.comparing(Card::getCost));
        return actionCards;
    }
    public List<VictoryCard> getDistinctVictoryCards(){
        List<VictoryCard> victoryCards = new LinkedList<>();
        for(Card card: collection) {
            if(card instanceof VictoryCard) {
                if(!victoryCards.contains(card))
                victoryCards.add((VictoryCard) card);
            }
        }
        victoryCards.sort(Comparator.comparing(Card::getCost));
        return victoryCards;
    }
    public List<TreasureCard> getDistinctTreasureCards(){
        List<TreasureCard> treasureCards = new LinkedList<>();
        for(Card card: collection) {
            if(card instanceof TreasureCard) {
                if(!treasureCards.contains(card))
                treasureCards.add((TreasureCard) card);
            }
        }
        treasureCards.sort(Comparator.comparing(Card::getCost));
        return treasureCards;
    }
    public int getSize() {
        return collection.size();
    }
    public void addToTop(Card card) {collection.add(0,card);}
    public Card drawTopCard() {
        return collection.remove(0);
    }
    public void shuffle() {
        Collections.shuffle(collection);
    }
    public int numCardInCollection(Card card) {
        return Collections.frequency(collection,card);
    }
    public void printCardNamesInCollection() {
        for(Card c: collection) {
            System.out.print(c.getName() + " ");
        }
        System.out.println();
    }

}
