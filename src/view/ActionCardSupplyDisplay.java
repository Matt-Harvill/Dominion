package view;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ActionCardSupplyDisplay {

    private final Rectangle[] actionCardsInSupply;
    private final Rectangle[] actionCardNumBackgrounds;
    private final Text[] actionCardNumbers;
    private final Rectangle[] buyActionCardButtons;
    private final String[] namesOfActionCardsInSupply;

    public ActionCardSupplyDisplay(Rectangle[] actionCardsInSupply, Rectangle[] actionCardNumBackgrounds,
                                   Text[] actionCardNumbers, Rectangle[] buyActionCardButtons, String[] namesOfActionCardsInSupply) {
        this.actionCardsInSupply = actionCardsInSupply;
        this.actionCardNumBackgrounds = actionCardNumBackgrounds;
        this.actionCardNumbers = actionCardNumbers;
        this.buyActionCardButtons = buyActionCardButtons;
        this.namesOfActionCardsInSupply = namesOfActionCardsInSupply;
    }

    public Rectangle[] getActionCardsInSupply() {
        return actionCardsInSupply;
    }
    public Rectangle[] getActionCardNumBackgrounds() {
        return actionCardNumBackgrounds;
    }
    public Text[] getActionCardNumbers() {
        return actionCardNumbers;
    }
    public Rectangle[] getBuyActionCardButtons() {
        return buyActionCardButtons;
    }
    public String[] getNamesOfActionCardsInSupply() {
        return namesOfActionCardsInSupply;
    }
}
