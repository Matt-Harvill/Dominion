package model.card;

import javafx.scene.image.Image;

public class ActionCard extends Card {
    protected String action;

    protected boolean dependent = true;
    protected String dependencies;

    public ActionCard(String name, int cost, String action, Image image, String dependencies) {
        super(name, cost, image);
        this.action = action;
        this.dependencies = dependencies;
        if(dependencies==null) dependent = false;
    }

    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public boolean isDependent() {
        return dependent;
    }
    public void setDependent(boolean dependent) {
        this.dependent = dependent;
    }
    public String getDependencies() {
        return dependencies;
    }
    public void setDependencies(String dependencies) {
        this.dependencies = dependencies;
    }
}