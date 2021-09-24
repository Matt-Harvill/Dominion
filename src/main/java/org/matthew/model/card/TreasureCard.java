package org.matthew.model.card;

import javafx.scene.image.Image;

public class TreasureCard extends Card {

    protected int purchasePower;

    public TreasureCard(String name, int cost, int purchasePower, Image image, Image smallCardImage, String numCards) {
        super(name,cost,image, smallCardImage, numCards);
        this.purchasePower = purchasePower;
    }

    public int getPurchasePower() {
        return purchasePower;
    }
    public void setPurchasePower(int purchasePower) {
        this.purchasePower = purchasePower;
    }
}