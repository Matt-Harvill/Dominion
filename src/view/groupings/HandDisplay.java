package view.groupings;

import javafx.scene.image.ImageView;

public class HandDisplay {

    private ImageView[] imageSlots;

    public HandDisplay(int maxNumCards) {
        imageSlots = new ImageView[maxNumCards];
    }

    public ImageView getImageSlot(int slotNum) {
        return imageSlots[slotNum-1];
    }
    public void setImageSlot(ImageView imageView, int slotNum) {
        imageSlots[slotNum-1] = imageView;
    }
    public ImageView[] getImageSlots() {
        return imageSlots;
    }
    public void setImageSlots(ImageView[] imageSlots) {
        this.imageSlots = imageSlots;
    }
}
