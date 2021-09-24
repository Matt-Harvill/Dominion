package org.matthew.model.card.action;

import javafx.scene.image.Image;
import org.matthew.model.card.Card;

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
    public Object getMemory(String memoryName) {

        for(Action action: actions) {
            if(action.getMemoryName()!=null && action.getMemoryName().equals(memoryName)) {
                return action.getMemory();
            }
        }

        return null;
    }

    private void loadInActions(String actionString) {
        Scanner scanner = new Scanner(actionString);
        while (scanner.hasNext()) {
            actions.add(ActionParser.parse(scanner.nextLine()));
        }
    }
}