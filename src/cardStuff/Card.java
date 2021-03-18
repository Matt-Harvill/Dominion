package cardStuff;

import java.io.File;
import java.io.Serializable;

public abstract class Card implements Serializable {
    protected File imageFile;
    protected String name;
    protected int cost;

    public File getImageFile() {
        return imageFile;
    }
    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
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
}


