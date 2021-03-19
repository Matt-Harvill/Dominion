package view.groupings;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public abstract class CardsInGame{

    protected Label[] labels;
    protected ImageView[] imageSlots;

    public Label getLabel(int slotNum) {
        return labels[slotNum-1];
    }
    public ImageView getImageSlot(int slotNum) {
        return imageSlots[slotNum-1];
    }
    public void setLabel(Label label, int slotNum) {
        labels[slotNum-1] = label;
    }
    public void setImageSlot(ImageView imageView, int slotNum) {
        imageSlots[slotNum-1] = imageView;
    }
    public Label[] getLabels() {
        return labels;
    }
    public void setLabels(Label[] labels) {
        this.labels = labels;
    }
    public ImageView[] getImageSlots() {
        return imageSlots;
    }
    public void setImageSlots(ImageView[] imageSlots) {
        this.imageSlots = imageSlots;
    }

}


