package org.matthew.view;

import javafx.scene.shape.Rectangle;

public class DeckCardDisplay extends CardDisplay {

    public DeckCardDisplay(Rectangle cardRect, NumberDisplay numberDisplay) {
        super(cardRect, numberDisplay);
    }

    @Override
    public void show() {
        cardRect.setVisible(getNum()>0);
        numberDisplay.setVisible(getNum()>0);
    }

}
