package model;

import controller.fxmlControllers.GameController;
import controller.mains.Main;
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
