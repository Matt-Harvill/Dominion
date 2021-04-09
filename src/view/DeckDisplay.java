package view;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class DeckDisplay {

    private CardDisplay cardDisplay;
    private LabelDisplay labelDisplay;
    public DeckDisplay(CardDisplay cardDisplay, LabelDisplay labelDisplay) {
        this.cardDisplay = cardDisplay;
        this.labelDisplay = labelDisplay;
    }

    public void show() {
        cardDisplay.show();
        labelDisplay.show();
    }
    public void hide() {
        cardDisplay.hide();
        labelDisplay.hide();
    }
    public CardDisplay getCardDisplay() {
        return cardDisplay;
    }
    public LabelDisplay getLabelDisplay() {
        return labelDisplay;
    }
}
