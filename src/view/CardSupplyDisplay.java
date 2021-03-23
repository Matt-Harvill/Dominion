package view;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.card.Card;

import java.util.List;

public class CardSupplyDisplay {

    private final Rectangle[] cardsInSupply;
    private final Rectangle[] cardNumBacks;
    private final Text[] cardNums;
    private final Rectangle[] cardBuyButtons;
    private final Card[] namesOfCardsInSupply;

    public CardSupplyDisplay(Rectangle[] cardsInSupply, Rectangle[] cardNumBacks,
                             Text[] cardNums, Rectangle[] cardBuyButtons, Card[] namesOfCardsInSupply) {
            this.cardsInSupply = cardsInSupply;
            this.cardNumBacks = cardNumBacks;
            this.cardNums = cardNums;
            this.cardBuyButtons = cardBuyButtons;
            this.namesOfCardsInSupply = namesOfCardsInSupply;
    }

    public Rectangle[] getCardsInSupply() {
        return cardsInSupply;
    }
    public Rectangle[] getCardInSupplyNumBacks() { return cardNumBacks;
    }
    public Text[] getCardInSupplyNums() {
        return cardNums;
    }
    public Rectangle[] getCardBuyButtons() {
        return cardBuyButtons;
    }
    public Card[] getCardObjectsInSupply() {
        return namesOfCardsInSupply;
    }
}
