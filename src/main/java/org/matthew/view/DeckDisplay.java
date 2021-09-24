package org.matthew.view;

public class DeckDisplay {

    private final CardDisplay cardDisplay;
    private final LabelDisplay labelDisplay;

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
}
