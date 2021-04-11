package model.card;

import javafx.scene.image.Image;
import newActionStuff.Action;
import newActionStuff.ActionParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ActionCard extends Card {
    protected List<Action> actions;
    private int actionIndex;

    public ActionCard(String name, int cost, String actionString, Image image, Image smallCardImage, String numCards) {
        super(name, cost, image, smallCardImage, numCards);
        actions = new ArrayList<>();
        loadInActions(actionString);
//        printActions();
        actionIndex = 0;
    }

    public void resetActionIndex() {
        actionIndex = 0;
    }
    public void incrementActionIndex() {
        actionIndex++;
    }
    public Action getAction() {
        if(actionIndex==actions.size()) {
            return null;
        } else {
            return actions.get(actionIndex);
        }
    }

    private void loadInActions(String actionString) {
        Scanner scanner = new Scanner(actionString);
        while (scanner.hasNext()) {
            actions.add(ActionParser.parse(scanner.nextLine()));
        }
    }

    public int getMemory(String memoryName) {

        for(Action action: actions) {
            if(action.getMemoryName()!=null && action.getMemoryName().equals(memoryName)) {
                return action.getMemory();
            }
        }

        return -1;
    }

//    private void printActions() {
//        for(Action action: actions) {
//            System.out.println(action);
//        }
//    }
}