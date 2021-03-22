package model.card;

import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.Objects;

public abstract class Card implements Serializable {
    protected String name;
    protected int cost;
    protected Image cardImage;

    public Card(String name, int cost, Image cardImage) {
        this.name = name;
        this.cost = cost;
        this.cardImage = cardImage;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        final Card other = (Card) obj;
        if (!name.equals(other.getName())) {
            return false;
        }
        return true;
    }

}


