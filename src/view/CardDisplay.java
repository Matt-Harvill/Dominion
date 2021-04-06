package view;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import model.card.Card;

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
        setImage();
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
        numberDisplay.setVisible(getNum()>0);
    }
    public void hide() {
        cardRect.setVisible(false);
        numberDisplay.setVisible(false);
    }

    public boolean contains(Object obj) {
        return obj.equals(cardRect) || numberDisplay.contains(obj);
    }

    protected void setImage() {
        cardRect.setFill(new ImagePattern(card.getCardImage()));
    }
}
