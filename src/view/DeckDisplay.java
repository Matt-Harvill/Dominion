package view;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class DeckDisplay {

    private StackPane deckStackPane, deckLabelStackPane;

    private Rectangle deck, deckNumBack, deckLabel;
    private Text deckNum, deckLabelText;

    public DeckDisplay(StackPane deckStackPane, StackPane deckLabelStackPane, Rectangle deck, Rectangle deckNumBack,
                       Rectangle deckLabel, Text deckNum, Text deckLabelText) {
        this.deckStackPane = deckStackPane;
        this.deckLabelStackPane = deckLabelStackPane;
        this.deck = deck;
        this.deckNumBack = deckNumBack;
        this.deckLabel = deckLabel;
        this.deckNum = deckNum;
        this.deckLabelText = deckLabelText;
    }

    public StackPane getDeckStackPane() {
        return deckStackPane;
    }
    public StackPane getDeckLabelStackPane() {
        return deckLabelStackPane;
    }
    public Rectangle getDeck() {
        return deck;
    }
    public Rectangle getDeckNumBack() {
        return deckNumBack;
    }
    public Rectangle getDeckLabel() {
        return deckLabel;
    }
    public Text getDeckNum() {
        return deckNum;
    }
    public Text getDeckLabelText() {
        return deckLabelText;
    }

    public void setVisible(boolean b) {
        deckStackPane.setVisible(b);
        deckLabelStackPane.setVisible(b);
    }
}
