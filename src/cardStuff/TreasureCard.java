package cardStuff;

import javafx.scene.image.Image;

import java.io.File;

public class TreasureCard extends Card {

    private int purchasePower;

    public TreasureCard(String name, int cost, int purchasePower, Image image) {
        this.name = name;
        this.cost = cost;
        this.purchasePower = purchasePower;
        this.cardImage = image;
    }

    public int getPurchasePower() {
        return purchasePower;
    }
    public void setPurchasePower(int purchasePower) {
        this.purchasePower = purchasePower;
    }
    public Image getCardImage() {
        return cardImage;
    }
    public void setCardImage(Image cardImage) {
        this.cardImage = cardImage;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getCost() {
        return cost;
    }
    public void setCost(int cost) {
        this.cost = cost;
    }
}
