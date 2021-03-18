package model;

import javafx.scene.image.Image;

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