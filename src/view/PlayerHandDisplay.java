package view;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class PlayerHandDisplay {

    private final Rectangle[] cards;
    private final Rectangle[] cardNumBacks;
    private final Text[] cardNums;

    public PlayerHandDisplay(Rectangle[] cards, Rectangle[] cardNumBacks, Text[] cardNums) {
        this.cards = cards;
        this.cardNumBacks = cardNumBacks;
        this.cardNums = cardNums;
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
}
