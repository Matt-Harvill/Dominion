package org.matthew.model.card;

public class CardStack {

    private Card card;
    private int numCards;

    public CardStack(Card card) {
     this.card = card;
     this.numCards = -1;
    }

    public CardStack(Card card, int numCards) {
        this.card = card;
        this.numCards = numCards;
    }

    public Card getCard() {
        return card;
    }
    public int getNumCards() {
        return numCards;
    }
    public void setNumCards(int numCards) {
        this.numCards = numCards;
    }
    public void decrement() {
        numCards--;
    }
}
