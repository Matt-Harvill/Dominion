package org.matthew.model.factory;

import org.matthew.Main;
import javafx.scene.image.Image;
import org.matthew.model.card.action.ActionCard;
import org.matthew.model.card.*;

public class CardFactory {

    public static Card getCard(String cardType) {
        switch (cardType) {
            case "BackViewCard": {
                return new BlankCard(cardType, 0, new Image(String.valueOf(Main.class.getResource("BackViewCard.jpg"))));
            }
            case "Copper": {
                return new TreasureCard(cardType, 0, 1, new Image(String.valueOf(Main.class.getResource("Copper.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Copper_Small.jpg"))), "All 60");
            }
            case "Silver": {
                return new TreasureCard(cardType, 3, 2, new Image(String.valueOf(Main.class.getResource("Silver.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Silver_Small.jpg"))), "All 40");
            }
            case "Gold": {
                return new TreasureCard(cardType, 6, 3, new Image(String.valueOf(Main.class.getResource("Gold.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Gold_Small.jpg"))), "All 30");
            }
            case "Platinum": {
                return new TreasureCard(cardType, 9, 5, new Image(String.valueOf(Main.class.getResource("Platinum.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Platinum_Small.jpg"))), "All 12");
            }
            case "Estate": {
                return new VictoryCard(cardType, 2, "1", new Image(String.valueOf(Main.class.getResource("Estate.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Estate_Small.jpg"))), "2player 8 3+player 12");
            }
            case "Duchy": {
                return new VictoryCard(cardType, 5, "3", new Image(String.valueOf(Main.class.getResource("Duchy.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Duchy_Small.jpg"))), "2player 8 3+player 12");
            }
            case "Province": {
                return new VictoryCard(cardType, 8, "6", new Image(String.valueOf(Main.class.getResource("Province.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Province_Small.jpg"))), "2player 8 3+player 12");
            }
            case "Colony": {
                return new VictoryCard(cardType, 11, "10", new Image(String.valueOf(Main.class.getResource("Colony.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Colony_Small.jpg"))), "2player 8 3+player 12");
            }
            case "Gardens": {
                return new VictoryCard(cardType, 4, "numCards10", new Image(String.valueOf(Main.class.getResource("Gardens.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Gardens_Small.jpg"))), "2player 8 3+player 12");
            }
            case "Village": {
                return new ActionCard(cardType, 3, "title_draw num_1  \n  title_action num_2", new Image(String.valueOf(Main.class.getResource("Village.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Village_Small.jpg"))),"All 10");
            }
            case "Woodcutter": {
                return new ActionCard(cardType, 3, "title_buy num_1  \n  title_coin num_2", new Image(String.valueOf(Main.class.getResource("Woodcutter.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Woodcutter_Small.jpg"))), "All 10");
            }
            case "Smithy": {
                return new ActionCard(cardType, 4, "title_draw num_3", new Image(String.valueOf(Main.class.getResource("Smithy.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Smithy_Small.jpg"))), "All 10");
            }
            case "Festival": {
                return new ActionCard(cardType, 5, "title_action num_2  \n  title_buy num_1  \n  title_coin num_2", new Image(String.valueOf(Main.class.getResource("Festival.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Festival_Small.jpg"))), "All 10");
            }
            case "Laboratory": {
                return new ActionCard(cardType, 5, "title_draw num_2  \n  title_action num_1", new Image(String.valueOf(Main.class.getResource("Laboratory.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Laboratory_Small.jpg"))), "All 10");
            }
            case "Market": {
                return new ActionCard(cardType, 5, "title_draw num_1  \n  title_action num_1  \n  title_buy num_1  \n  title_coin num_1", new Image(String.valueOf(Main.class.getResource("Market.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Market_Small.jpg"))), "All 10");
            }
            case "Cellar": {
                return new ActionCard(cardType,2,"title_action num_1 \n title_discard comparator_<= num_any memoryName_numDiscarded \n title_draw num_numDiscarded",new Image(String.valueOf(Main.class.getResource("Cellar.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Cellar_Small.jpg"))),"All 10");
            }
            case "Poacher": {
                return new ActionCard(cardType,4,"title_draw num_1 \n title_action num_1 \n title_coin num_1 \n title_discard num_numEmptyStacks",new Image(String.valueOf(Main.class.getResource("Poacher.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Poacher_Small.jpg"))),"All 10");
            }
            case "Remodel": {
                return new ActionCard(cardType,4,"title_trash num_1 memoryName_costOfCardTrashed \n title_gain cost_costOfCardTrashed+2",new Image(String.valueOf(Main.class.getResource("Remodel.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Remodel_Small.jpg"))),"All 10");
            }
            case "Chapel": {
                return new ActionCard(cardType,2,"title_trash num_4 comparator_<=",new Image(String.valueOf(Main.class.getResource("Chapel.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Chapel_Small.jpg"))),"All 10");
            }
            case "Workshop": {
                return new ActionCard(cardType,3,"title_gain cost_4",new Image(String.valueOf(Main.class.getResource("Workshop.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Workshop_Small.jpg"))),"All 10");
            }
            case "Mine": {
                return new ActionCard(cardType,5,"title_trash comparator_<= num_1 memoryName_costOfCardTrashed type_treasureCard \n title_gain cost_costOfCardTrashed+3 type_treasureCard location_hand",new Image(String.valueOf(Main.class.getResource("Mine.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Mine_Small.jpg"))),"All 10");
            }
            case "Moneylender": {
                return new ActionCard(cardType,4,"title_trash comparator_<= num_1 memoryName_cardTrashed type_Copper \n title_coin num_3 doAction_cardTrashed",new Image(String.valueOf(Main.class.getResource("Moneylender.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Moneylender_Small.jpg"))),"All 10");
            }
            case "Artisan": {
                return new ActionCard(cardType,6,"title_gain num_1 cost_5 \n title_toDeck num_1",new Image(String.valueOf(Main.class.getResource("Artisan.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Artisan_Small.jpg"))),"All 10");
            }
            case "Courtyard": {
                return new ActionCard(cardType,2,"title_draw num_3 \n title_toDeck num_1",new Image(String.valueOf(Main.class.getResource("Courtyard.jpg"))),
                        new Image(String.valueOf(Main.class.getResource("Courtyard_Small.jpg"))),"All 10");
            }
            default: return null;
        }
    }
}
