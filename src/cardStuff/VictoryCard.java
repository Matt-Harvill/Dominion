package cardStuff;

import javafx.scene.image.Image;

public class VictoryCard extends Card {

    private int victoryPoints;

    public VictoryCard(String name, int cost, int points, Image image) {
        this.name = name;
        this.cost = cost;
        this.victoryPoints = points;
        this.cardImage = image;
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
