package cardStuff;

import javafx.scene.image.Image;

import java.io.File;

public class CardFactory {

    public static Card getCard(String cardType) {
        switch (cardType) {
            case "Copper": return new TreasureCard(cardType,0, 1, new Image(new File("Images/Copper.jpg").toURI().toString()));
            case "Silver": return new TreasureCard(cardType,3, 2, new Image(new File("Images/Silver.jpg").toURI().toString()));
            case "Gold": return new TreasureCard(cardType,6, 3, new Image(new File("Images/Gold.jpg").toURI().toString()));
            case "Platinum": return new TreasureCard(cardType,9, 5, new Image(new File("Images/Platinum.jpg").toURI().toString()));
            case "Village": return new ActionCard(cardType, 3, "+1 Card +2 Actions", new Image(new File("Images/Village.jpg").toURI().toString()));
            case "Woodcutter": return new ActionCard(cardType, 3, "+1 Buy +2 Coins", new Image(new File("Images/Woodcutter.jpg").toURI().toString()));
            case "Smithy": return new ActionCard(cardType, 4, "+3 Cards", new Image(new File("Images/Smithy.jpg").toURI().toString()));
            case "Festival": return new ActionCard(cardType, 5, "+2 Actions +1 Buy +2 Coins", new Image(new File("Images/Festival.jpg").toURI().toString()));
            case "Laboratory": return new ActionCard(cardType, 5, "+2 Cards +1 Action", new Image(new File("Images/Laboratory.jpg").toURI().toString()));
            case "Market": return new ActionCard(cardType, 5, "+1 Card +1 Action +1 Buy +1 Coin", new Image(new File("Images/Market.jpg").toURI().toString()));
            case "BackViewCard": return new BaseCard(cardType, new Image(new File("Images/BackViewCard.jpg").toURI().toString()));
            case "Estate": return new VictoryCard(cardType,2, 1, new Image(new File("Images/Estate.jpg").toURI().toString()));
            case "Duchy": return new VictoryCard(cardType,5, 3, new Image(new File("Images/Duchy.jpg").toURI().toString()));
            case "Province": return new VictoryCard(cardType,8, 6, new Image(new File("Images/Province.jpg").toURI().toString()));
            case "Colony": return new VictoryCard(cardType,11, 10, new Image(new File("Images/Colony.jpg").toURI().toString()));
        }
        return null;
    }
}
