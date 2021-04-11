package model;

import controller.GameController;
import controller.Main;
import view.CardDisplay;

public class StackCalculator {

    private static final GameController controller = Main.getGameController();

    public static int numEmptyStacks() {
        int numEmptyStacks = 0;
        for(CardDisplay cardDisplay: controller.getAllCISDisplays()) {
            if(cardDisplay.getNum()==0) {
                numEmptyStacks++;
            }
        }
        return numEmptyStacks;
    }
}
