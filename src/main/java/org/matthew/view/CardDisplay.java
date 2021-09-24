package org.matthew.view;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import org.matthew.model.card.Card;

public class CardDisplay {

    protected Rectangle cardRect;
    protected NumberDisplay numberDisplay;
    protected Card card;

    public CardDisplay(Rectangle cardRect, NumberDisplay numberDisplay) {
        this.cardRect = cardRect;
        this.numberDisplay = numberDisplay;
    }

    public void setCard(Card card) {
        this.card = card;
        if(card!=null) {
            setImage();
        }
    }
    public Card getCard() {
        return card;
    }
    public int getNum() {
        return numberDisplay.getNum();
    }
    public void setNum(int num) {
        numberDisplay.setNum(num);
    }
    public void show() {
        cardRect.setVisible(true);
        numberDisplay.setVisible(getNum()>1);
    }
    public void hide() {
        cardRect.setVisible(false);
        numberDisplay.setVisible(false);
    }

    protected void setImage() {
        cardRect.setFill(new ImagePattern(card.getCardImage()));
    }

    public boolean contains(Object obj) {
        return obj.equals(cardRect) || numberDisplay.contains(obj);
    }
    public void setStyle(String style) {
        cardRect.setStyle(style);
    }
    public void setViewOrder(double order) {
        cardRect.setViewOrder(order);
        numberDisplay.setViewOrder(order-0.1);
    }
}
