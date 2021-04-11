package model.factory;

import controller.Main;
import javafx.scene.image.Image;
import model.card.*;

import java.io.File;
import java.net.URISyntaxException;

public class CardFactory {

    public static Card getCard(String cardType) {
        try {
            switch (cardType) {
                case "BackViewCard": {
                    return new BlankCard(cardType, 0, new Image(Main.class.getResource("/BackViewCard.jpg").toURI().toString()));
                }
                case "Copper": {
                    return new TreasureCard(cardType, 0, 1, new Image(Main.class.getResource("/Copper.jpg").toURI().toString()),
                            new Image(Main.class.getResource("/Copper_Small.jpg").toURI().toString()), "All 60");
                }
                case "Silver": {
                    return new TreasureCard(cardType, 3, 2, new Image(Main.class.getResource("/Silver.jpg").toURI().toString()),
                            new Image(Main.class.getResource("/Silver_Small.jpg").toURI().toString()), "All 40");
                }
                case "Gold": {
                    return new TreasureCard(cardType, 6, 3, new Image(Main.class.getResource("/Gold.jpg").toURI().toString()),
                            new Image(Main.class.getResource("/Gold_Small.jpg").toURI().toString()), "All 30");
                }
                case "Platinum": {
                    return new TreasureCard(cardType, 9, 5, new Image(Main.class.getResource("/Platinum.jpg").toURI().toString()),
                            new Image(Main.class.getResource("/Platinum_Small.jpg").toURI().toString()), "All 12");
                }
                case "Estate": {
                    return new VictoryCard(cardType, 2, 1, new Image(Main.class.getResource("/Estate.jpg").toURI().toString()),
                            new Image(Main.class.getResource("/Estate_Small.jpg").toURI().toString()), "2player 8 3+player 12");
                }
                case "Duchy": {
                    return new VictoryCard(cardType, 5, 3, new Image(Main.class.getResource("/Duchy.jpg").toURI().toString()),
                            new Image(Main.class.getResource("/Duchy_Small.jpg").toURI().toString()), "2player 8 3+player 12");
                }
                case "Province": {
                    return new VictoryCard(cardType, 8, 6, new Image(Main.class.getResource("/Province.jpg").toURI().toString()),
                            new Image(Main.class.getResource("/Province_Small.jpg").toURI().toString()), "2player 8 3+player 12");
                }
                case "Colony": {
                    return new VictoryCard(cardType, 11, 10, new Image(Main.class.getResource("/Colony.jpg").toURI().toString()),
                            new Image(Main.class.getResource("/Colony_Small.jpg").toURI().toString()), "2player 8 3+player 12");
                }
                case "Village": {
                    return new ActionCard(cardType, 3, "title_draw num_1  \n  title_action num_2", new Image(Main.class.getResource("/Village.jpg").toURI().toString()),
                            new Image(Main.class.getResource("/Village_Small.jpg").toURI().toString()),"All 10");
                }
                case "Woodcutter": {
                    return new ActionCard(cardType, 3, "title_buy num_1  \n  title_coin num_2", new Image(Main.class.getResource("/Woodcutter.jpg").toURI().toString()),
                            new Image(Main.class.getResource("/Woodcutter_Small.jpg").toURI().toString()), "All 10");
                }
                case "Smithy": {
                    return new ActionCard(cardType, 4, "title_draw num_3", new Image(Main.class.getResource("/Smithy.jpg").toURI().toString()),
                            new Image(Main.class.getResource("/Smithy_Small.jpg").toURI().toString()), "All 10");
                }
                case "Festival": {
                    return new ActionCard(cardType, 5, "title_action num_2  \n  title_buy num_1  \n  title_coin num_2", new Image(Main.class.getResource("/Festival.jpg").toURI().toString()),
                            new Image(Main.class.getResource("/Festival_Small.jpg").toURI().toString()), "All 10");
                }
                case "Laboratory": {
                    return new ActionCard(cardType, 5, "title_draw num_2  \n  title_action num_1", new Image(Main.class.getResource("/Laboratory.jpg").toURI().toString()),
                            new Image(Main.class.getResource("/Laboratory_Small.jpg").toURI().toString()), "All 10");
                }
                case "Market": {
                    return new ActionCard(cardType, 5, "title_draw num_1  \n  title_action num_1  \n  title_buy num_1  \n  title_coin num_1", new Image(Main.class.getResource("/Market.jpg").toURI().toString()),
                            new Image(Main.class.getResource("/Market_Small.jpg").toURI().toString()), "All 10");
                }
                case "Cellar": {
                    return new ActionCard(cardType,2,"title_action num_1 \n title_discard optional_true num_any memoryName_numDiscarded \n title_draw num_numDiscarded",new Image(Main.class.getResource("/Cellar.jpg").toURI().toString()),
                            new Image(Main.class.getResource("/Cellar_Small.jpg").toURI().toString()),"All 10");
                }
                case "Poacher": {
                    return new ActionCard(cardType,4,"title_draw num_1 \n title_action num_1 \n title_coin num_1 \n title_discard optional_false num_numEmptyStacks",new Image(Main.class.getResource("/Poacher.jpg").toURI().toString()),
                            new Image(Main.class.getResource("/Poacher_Small.jpg").toURI().toString()),"All 10");
                }
                default: return null;
            }
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
