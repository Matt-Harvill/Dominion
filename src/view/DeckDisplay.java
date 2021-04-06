package view;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class DeckDisplay {

    private Rectangle deck, deckNumBack;
    private Text deckNum;

    public DeckDisplay(Rectangle deck, Rectangle deckNumBack, Text deckNum) {
        this.deck = deck;
        this.deckNumBack = deckNumBack;
        this.deckNum = deckNum;
    }

    public Rectangle getDeck() {
        return deck;
    }
    public Rectangle getDeckNumBack() {
        return deckNumBack;
    }
    public Text getDeckNum() {
        return deckNum;
    }
}
