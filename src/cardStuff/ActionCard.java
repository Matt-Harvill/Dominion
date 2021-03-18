package cardStuff;

import javafx.scene.image.Image;

public class ActionCard extends Card {

    private String action;

    public ActionCard(String name, int cost, String action, Image image) {
        this.name = name;
        this.cost = cost;
        this.action = action;
        this.cardImage = image;
    }

    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
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
