package view;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class LeftSupplyCardDisplay {

    private final Rectangle[] cardsInSupply;
    private final Rectangle[] cardNumBacks;
    private final Text[] cardNums;
    private final Rectangle[] cardBuyButtons;
    private final String[] namesOfCardsInSupply;

    public LeftSupplyCardDisplay(Rectangle[] cardsInSupply, Rectangle[] cardNumBacks,
                                   Text[] cardNums, Rectangle[] cardBuyButtons, String[] namesOfCardsInSupply) {
            this.cardsInSupply = cardsInSupply;
            this.cardNumBacks = cardNumBacks;
            this.cardNums = cardNums;
            this.cardBuyButtons = cardBuyButtons;
            this.namesOfCardsInSupply = namesOfCardsInSupply;
    }

    public Rectangle[] getCardsInSupply() {
        return cardsInSupply;
    }
    public Rectangle[] getCardNumBacks() { return cardNumBacks;
    }
    public Text[] getCardNums() {
        return cardNums;
    }
    public Rectangle[] getCardBuyButtons() {
        return cardBuyButtons;
    }
    public String[] getNamesOfCardsInSupply() {
        return namesOfCardsInSupply;
    }
}
