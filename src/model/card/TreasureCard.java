package model.card;

import javafx.scene.image.Image;
import model.card.Card;

public class TreasureCard extends Card {

    protected int purchasePower;

    public TreasureCard(String name, int cost, int purchasePower, Image image) {
        super(name,cost,image);
        this.purchasePower = purchasePower;
    }

    public int getPurchasePower() {
        return purchasePower;
    }
    public void setPurchasePower(int purchasePower) {
        this.purchasePower = purchasePower;
    }
}