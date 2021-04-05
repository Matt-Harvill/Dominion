package model.factory;

import javafx.scene.image.Image;
import model.card.*;

import java.io.File;

public class CardFactory {

    public static Card getCard(String cardType) {
        return switch (cardType) {
            case "BackViewCard" -> new BlankCard(cardType, 0, new Image(new File("src/resources/BackViewCard.jpg").toURI().toString()));
            case "Copper" -> new TreasureCard(cardType, 0, 1, new Image(new File("src/resources/Copper.jpg").toURI().toString()),
                    new Image(new File("src/resources/Copper_Small.jpg").toURI().toString()), "All 60");
            case "Silver" -> new TreasureCard(cardType, 3, 2, new Image(new File("src/resources/Silver.jpg").toURI().toString()),
                    new Image(new File("src/resources/Silver_Small.jpg").toURI().toString()), "All 40");
            case "Gold" -> new TreasureCard(cardType, 6, 3, new Image(new File("src/resources/Gold.jpg").toURI().toString()),
                    new Image(new File("src/resources/Gold_Small.jpg").toURI().toString()), "All 30");
            case "Platinum" -> new TreasureCard(cardType, 9, 5, new Image(new File("src/resources/Platinum.jpg").toURI().toString()),
                    new Image(new File("src/resources/Platinum_Small.jpg").toURI().toString()), "All 12");
            case "Village" -> new ActionCard(cardType, 3, "+1 Card +2 Actions", new Image(new File("src/resources/Village.jpg").toURI().toString()),
                    new Image(new File("src/resources/Village_Small.jpg").toURI().toString()), null,"All 1");
            case "Woodcutter" -> new ActionCard(cardType, 3, "+1 Buy +2 Coins", new Image(new File("src/resources/Woodcutter.jpg").toURI().toString()),
                    new Image(new File("src/resources/Woodcutter_Small.jpg").toURI().toString()), null, "All 1");
            case "Smithy" -> new ActionCard(cardType, 4, "+3 Cards", new Image(new File("src/resources/Smithy.jpg").toURI().toString()),
                    new Image(new File("src/resources/Smithy_Small.jpg").toURI().toString()), null, "All 1");
            case "Festival" -> new ActionCard(cardType, 5, "+2 Actions +1 Buy +2 Coins", new Image(new File("src/resources/Festival.jpg").toURI().toString()),
                    new Image(new File("src/resources/Festival_Small.jpg").toURI().toString()), null, "All 10");
            case "Laboratory" -> new ActionCard(cardType, 5, "+2 Cards +1 Action", new Image(new File("src/resources/Laboratory.jpg").toURI().toString()),
                    new Image(new File("src/resources/Laboratory_Small.jpg").toURI().toString()), null, "All 10");
            case "Market" -> new ActionCard(cardType, 5, "+1 Card +1 Action +1 Buy +1 Coin", new Image(new File("src/resources/Market.jpg").toURI().toString()),
                    new Image(new File("src/resources/Market_Small.jpg").toURI().toString()), null, "All 10");
            case "Estate" -> new VictoryCard(cardType, 2, 1, new Image(new File("src/resources/Estate.jpg").toURI().toString()),
                    new Image(new File("src/resources/Estate_Small.jpg").toURI().toString()), "2player 8 3+player 12");
            case "Duchy" -> new VictoryCard(cardType, 5, 3, new Image(new File("src/resources/Duchy.jpg").toURI().toString()),
                    new Image(new File("src/resources/Duchy_Small.jpg").toURI().toString()), "2player 8 3+player 12");
            case "Province" -> new VictoryCard(cardType, 8, 6, new Image(new File("src/resources/Province.jpg").toURI().toString()),
                    new Image(new File("src/resources/Province_Small.jpg").toURI().toString()), "2player 8 3+player 12");
            case "Colony" -> new VictoryCard(cardType, 11, 10, new Image(new File("src/resources/Colony.jpg").toURI().toString()),
                    new Image(new File("src/resources/Colony_Small.jpg").toURI().toString()), "2player 8 3+player 12");
            default -> null;
        };
    }
}
