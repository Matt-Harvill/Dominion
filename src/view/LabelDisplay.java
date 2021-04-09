package view;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class LabelDisplay {

    private Rectangle labelBack;
    private Text labelText;

    public LabelDisplay(Rectangle labelBack, Text labelText) {
        this.labelBack = labelBack;
        this.labelText = labelText;
    }

    public void show() {
        labelBack.setVisible(true);
        labelText.setVisible(true);
    }
    public void hide() {
        labelBack.setVisible(false);
        labelText.setVisible(false);
    }

    public void setLabel(String s) {
        labelText.setText(s);
    }
}
