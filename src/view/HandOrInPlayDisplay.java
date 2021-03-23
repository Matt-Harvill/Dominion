package view;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.card.Card;

import java.util.List;

public class HandOrInPlayDisplay {

    private final Rectangle[] cards;
    private final Rectangle[] cardNumBacks;
    private final Text[] cardNums;
    private Card[] namesOfCards;
    private String nameOfDisplay;

    public HandOrInPlayDisplay(Rectangle[] cards, Rectangle[] cardNumBacks, Text[] cardNums, Card[] namesOfCards, String nameOfDisplay) {
        this.cards = cards;
        this.cardNumBacks = cardNumBacks;
        this.cardNums = cardNums;
        this.namesOfCards = namesOfCards;
        this.nameOfDisplay = nameOfDisplay;
    }

    public Rectangle[] getCardsInHandOrInPlay() {
        return cards;
    }
    public Rectangle[] getCardInHandOrInPlayNumBacks() {
        return cardNumBacks;
    }
    public Text[] getCardInHandOrInPlayNums() {
        return cardNums;
    }
    public Card[] getCardObjectsInHandOrInPlay() { return namesOfCards; }
    public void setCardObjectsInHandOrInPlay(Card[] namesOfCards) {
        this.namesOfCards = namesOfCards;
    }
}
