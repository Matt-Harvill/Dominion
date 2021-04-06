package view;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class NumberDisplay {

    private Rectangle numBack;
    private Text num;

    public NumberDisplay(Rectangle numBack, Text num) {
        this.numBack = numBack;
        this.num = num;
    }

    public int getNum() {
        return Integer.parseInt(num.getText());
    }
    public void setNum(int num) {
        this.num.setText(String.valueOf(num));
    }
    public void setVisible(boolean show) {
        numBack.setVisible(show);
        num.setVisible(show);
    }
}
