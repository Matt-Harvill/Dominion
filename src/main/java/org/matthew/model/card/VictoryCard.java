package org.matthew.model.card;

import org.matthew.Main;
import javafx.scene.image.Image;
import org.matthew.model.Player;

public class VictoryCard extends Card {

    private final Player player = Main.getPlayer();
    private String victoryPoints;

    public VictoryCard(String name, int cost, String points, Image image, Image smallCardImage, String numCards) {
        super(name,cost,image, smallCardImage, numCards);
        this.victoryPoints = points;
    }

    public int getVictoryPoints() {
        return parsePoints();
    }

    private int parsePoints() {
        int num = Integer.MIN_VALUE;

        boolean isNumeric = victoryPoints.chars().allMatch(Character::isDigit);

        if(isNumeric) {
            num = Integer.parseInt(victoryPoints);
        } else {
            if (victoryPoints.contains("numCards")) {
                num = player.getTotalNumCards();
                int numDivideBy = 0;
                if(victoryPoints.contains("/")) {
                    numDivideBy = Integer.parseInt(victoryPoints.substring(victoryPoints.indexOf("/")+1));
                    num /= numDivideBy;
                }
            }
        }
        return num;
    }
}
