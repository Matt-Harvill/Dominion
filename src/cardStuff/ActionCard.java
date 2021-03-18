package cardStuff;

import java.io.File;

public class ActionCard extends Card {

    public ActionCard(String name) {
        this.name = name;
        imageFile = new File("Images/Copper.jpg");
    }

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
