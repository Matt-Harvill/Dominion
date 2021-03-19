package model;

import model.card.ActionCard;
import model.card.Card;
import model.card.TreasureCard;
import model.card.VictoryCard;

import java.util.LinkedList;
import java.util.List;

public class CardCollection {

    private List<Card> collection;

    public CardCollection() {
        collection = new LinkedList<>();
    }

    public void addCardToCollection(Card c){
        collection.add(c);
    }
    public void removeCardFromCollection(Card c) { collection.remove(c); }

    public List<Card> getActionCards(){
        List<Card> actionCards = collection;
        actionCards.removeIf(c -> !(c instanceof ActionCard));
        return actionCards;
    }
    public List<Card> getVictoryCards(){

        List<Card> victoryCards = collection;
        victoryCards.removeIf(c -> !(c instanceof VictoryCard));
        return victoryCards;
    }
    public List<Card> getTreasureCards(){
        List<Card> treasureCards = collection;
        treasureCards.removeIf(c -> !(c instanceof TreasureCard));
        return treasureCards;
    }

}
