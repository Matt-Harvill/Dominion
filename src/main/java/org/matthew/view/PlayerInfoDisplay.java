package org.matthew.view;

public class PlayerInfoDisplay {

    private LabelDisplay labelDisplay;
    private NumberDisplay num;
    public PlayerInfoDisplay(LabelDisplay labelDisplay, NumberDisplay num) {
        this.labelDisplay = labelDisplay;
        this.num = num;
    }

    public void show() {
        labelDisplay.show();
        num.setVisible(true);
    }
    public void hide() {
        labelDisplay.hide();
        num.setVisible(false);
    }

    public void setNum(int num) {
        this.num.setNum(num);
    }
    public int getNum() {
        return num.getNum();
    }
    public void setName(String name) {
        labelDisplay.setText(name);
    }
    public String getName() {
        return labelDisplay.getText();
    }
}
