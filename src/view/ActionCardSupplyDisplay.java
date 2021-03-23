package view;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ActionCardSupplyDisplay {

    private final Rectangle[] actionCardsInSupply;
    private final Rectangle[] actionCardNumBacks;
    private final Text[] actionCardNums;
    private final Rectangle[] actionCardBuyButtons;
    private final String[] namesOfActionCardsInSupply;

    public ActionCardSupplyDisplay(Rectangle[] actionCardsInSupply, Rectangle[] actionCardNumBacks,
                                   Text[] actionCardNums, Rectangle[] actionCardBuyButtons, String[] namesOfActionCardsInSupply) {
        this.actionCardsInSupply = actionCardsInSupply;
        this.actionCardNumBacks = actionCardNumBacks;
        this.actionCardNums = actionCardNums;
        this.actionCardBuyButtons = actionCardBuyButtons;
        this.namesOfActionCardsInSupply = namesOfActionCardsInSupply;
    }

    public Rectangle[] getActionCardsInSupply() {
        return actionCardsInSupply;
    }
    public Rectangle[] getActionCardNumBackgrounds() {
        return actionCardNumBacks;
    }
    public Text[] getActionCardNumbers() {
        return actionCardNums;
    }
    public Rectangle[] getBuyActionCardButtons() {
        return actionCardBuyButtons;
    }
    public String[] getNamesOfActionCardsInSupply() {
        return namesOfActionCardsInSupply;
    }
}
