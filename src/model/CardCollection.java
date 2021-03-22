package model;

import model.card.ActionCard;
import model.card.Card;
import model.card.TreasureCard;
import model.card.VictoryCard;

import java.util.*;

public class CardCollection {

    private List<Card> collection;

    public CardCollection() {
        collection = new LinkedList<>();
    }

    public void addCardToCollection(Card c){
        collection.add(c);
    }
    public void removeCardFromCollection(Card c) {
        collection.remove(c);
    }

    public List<ActionCard> getDistinctActionCards(){
        List<ActionCard> actionCards = new LinkedList<>();
        for(Card card: collection) {
            if(card instanceof ActionCard) {
                if(!actionCards.contains(card))
                actionCards.add((ActionCard) card);
            }
        }
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
        return treasureCards;
    }
    public int getSize() {
        return collection.size();
    }
    public Card drawTopCard() {
        return collection.remove(0);
    }
    public void shuffle() {
        Collections.shuffle(collection);
    }
    public int numCardInCollection(Card card) {
        return Collections.frequency(collection,card);
    }

}
