package view;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class PlayerNamePointsDisplay {

    private Rectangle[] playerLabels, playerLabelVictories;
    private Text[] playerLabelNames, playerLabelVictoryNums;

    public PlayerNamePointsDisplay(Rectangle[] playerLabels, Rectangle[] playerLabelVictories, Text[] playerLabelNames, Text[] playerLabelVictoryNums) {
        this.playerLabels = playerLabels;
        this.playerLabelVictories = playerLabelVictories;
        this.playerLabelNames = playerLabelNames;
        this.playerLabelVictoryNums = playerLabelVictoryNums;
    }

    public Rectangle[] getPlayerLabels() {
        return playerLabels;
    }
    public Rectangle[] getPlayerLabelVictories() {
        return playerLabelVictories;
    }
    public Text[] getPlayerLabelNames() {
        return playerLabelNames;
    }
    public Text[] getPlayerLabelVictoryNums() {
        return playerLabelVictoryNums;
    }
}
