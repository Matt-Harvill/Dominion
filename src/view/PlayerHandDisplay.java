package view;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class PlayerHandDisplay {

    private final Rectangle[] cards;
    private final Rectangle[] cardNumBacks;
    private final Text[] cardNums;
    private String[] namesOfCards;

    public PlayerHandDisplay(Rectangle[] cards, Rectangle[] cardNumBacks, Text[] cardNums, String[] namesOfCards) {
        this.cards = cards;
        this.cardNumBacks = cardNumBacks;
        this.cardNums = cardNums;
        this.namesOfCards = namesOfCards;
    }

    public Rectangle[] getCards() {
        return cards;
    }
    public Rectangle[] getCardNumBacks() {
        return cardNumBacks;
    }
    public Text[] getCardNums() {
        return cardNums;
    }
    public String[] getNamesOfCards() { return namesOfCards; }
    public void setNamesOfCards(String[] namesOfCards) {
        this.namesOfCards = namesOfCards;
    }
}
