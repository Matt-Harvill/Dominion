package model;

public class Client {

    private String name;
    private CardCollection hand, deck, discardPile;
    private int handLimit, numActions, numBuys, handPurchasePower, amountSpentThisTurn, bonusPurchasePower;

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
}
