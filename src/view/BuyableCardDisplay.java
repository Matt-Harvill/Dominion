package view;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class BuyableCardDisplay extends CardDisplay {

    private Rectangle buyButton;

    public BuyableCardDisplay(Rectangle card, NumberDisplay numberDisplay, Rectangle buyButton) {
        super(card,numberDisplay);
        this.buyButton = buyButton;
    }

    public void showBuyButton() {
        buyButton.setVisible(true);
    }
    public void hideBuyButton() {
        buyButton.setVisible(false);
    }

    @Override
    protected void setImage() {
        cardRect.setFill(new ImagePattern(card.getSmallCardImage()));
    }
}
