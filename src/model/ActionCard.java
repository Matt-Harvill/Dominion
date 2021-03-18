package model;

import javafx.scene.image.Image;

public abstract class ActionCard extends Card {
    protected String action;
    protected boolean dependent = true;

    public ActionCard(String name, int cost, String action, Image image) {
        super(name, cost, image);
        this.action = action;
    }

    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
}

class ActionCardIndependent extends ActionCard {
    public ActionCardIndependent(String name, int cost, String action, Image image) {
        super(name, cost, action, image);
        this.dependent = false;
    }
}

class ActionCardDependent extends ActionCard {
    public ActionCardDependent(String name, int cost, String action, Image image) {
        super(name, cost, action, image);
        this.dependent = true;
    }
}