package cardStuff;

import javafx.scene.image.Image;

import java.io.File;

public class CardFactory {

    public static Card getCard(String cardType) {
        switch (cardType) {
            case "Copper": return new TreasureCard(cardType,0, 1, new Image(new File("Images/Copper.jpg").toURI().toString()));
            case "Silver": return new TreasureCard(cardType,3, 2, new Image(new File("Images/Silver.jpg").toURI().toString()));
            case "Gold": return new TreasureCard(cardType,6, 3, new Image(new File("Images/Gold.jpg").toURI().toString()));

        }
        return null;
    }
}
