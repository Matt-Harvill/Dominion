package model;

import javafx.scene.image.Image;

public class VictoryCard extends Card {

    private int victoryPoints;

    public VictoryCard(String name, int cost, int points, Image image) {
        super(name,cost,image);
        this.victoryPoints = points;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }
    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }
}
