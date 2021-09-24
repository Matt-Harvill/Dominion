package org.matthew.view;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class BuyableCardDisplay extends CardDisplay {

    private final Rectangle buyButton, gainButton;

    public BuyableCardDisplay(Rectangle card, NumberDisplay numberDisplay, Rectangle buyButton, Rectangle gainButton) {
        super(card,numberDisplay);
        this.buyButton = buyButton;
        this.gainButton = gainButton;
    }

    public void showBuyButton() {
        buyButton.setVisible(true);
    }
    public void hideBuyButton() {
        buyButton.setVisible(false);
    }
    public void showGainButton() {
        gainButton.setVisible(true);
    }
    public void hideGainButton() {
        gainButton.setVisible(false);
    }
    public void setBuyButtonImage(Image image) {
        buyButton.setFill(new ImagePattern(image));
    }
    public void setGainButtonImage(Image image) {
        gainButton.setFill(new ImagePattern(image));
    }


    @Override
    public void show() {
        cardRect.setVisible(true);
        numberDisplay.setVisible(getNum()>=0);
    }

    @Override
    public void hide() {
        super.hide();
        hideBuyButton();
        hideGainButton();
    }

    @Override
    public boolean contains(Object obj) {
        return super.contains(obj) || obj.equals(buyButton) || obj.equals(gainButton);
    }

    @Override
    protected void setImage() {
        cardRect.setFill(new ImagePattern(card.getSmallCardImage()));
    }
}
