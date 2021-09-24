package org.matthew.view;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class LabelDisplay {

    private final Rectangle labelBack;
    private final Text labelText;

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

    public void setText(String s) {
        labelText.setText(s);
    }
    public String getText() {
        return labelText.getText();
    }
}
