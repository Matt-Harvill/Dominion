package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.factory.CardFactory;
import view.groupings.*;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    @FXML private TextField gameInterface, chatInterface;
    @FXML private TextArea gameLogDisplay, chatLogDisplay;
    @FXML private ImageView card1, card2, card3, card4, card5, card6, card7, card8, card9, card10,
            opponentCard1, opponentCard2, opponentCard3, opponentCard4, opponentCard5, opponentCard6, opponentCard7, opponentCard8, opponentCard9, opponentCard10;
    @FXML private ImageView actionCardInGame1, actionCardInGame2, actionCardInGame3, actionCardInGame4, actionCardInGame5,
            actionCardInGame6, actionCardInGame7, actionCardInGame8, actionCardInGame9, actionCardInGame10,
            treasureCardInGame1, treasureCardInGame2, treasureCardInGame3, treasureCardInGame4,
            victoryCardInGame1, victoryCardInGame2, victoryCardInGame3, victoryCardInGame4;
    @FXML private Label labelACIG1, labelACIG2, labelACIG3, labelACIG4, labelACIG5, labelACIG6, labelACIG7, labelACIG8, labelACIG9, labelACIG10,
            labelTCIG1, labelTCIG2, labelTCIG3, labelTCIG4,
            labelVCIG1, labelVCIG2, labelVCIG3, labelVCIG4;

    @FXML private TextField changeNameField;

    private List<String> gameDisplayStrings, chatDisplayStrings;
    private CardsInGame actionCardsInGame, treasureCardsInGame, victoryCardsInGame;
    private HandDisplay opponentHand, playerHand;

    public void initialize() {
        gameDisplayStrings = new ArrayList<>();
        chatDisplayStrings = new ArrayList<>();

        actionCardsInGame = new ActionCardsInGame();
        treasureCardsInGame = new TreasureCardsInGame();
        victoryCardsInGame = new VictoryCardsInGame();
        opponentHand = new HandDisplay(10);
        playerHand = new HandDisplay(10);

        chatLogDisplay.setText("This is the chat display");
        gameLogDisplay.setText("This is the game event log");

        //ActionCard Initialization
        ImageView[] acigImageViews = {actionCardInGame1,actionCardInGame2,actionCardInGame3,actionCardInGame4,actionCardInGame5
                ,actionCardInGame6,actionCardInGame7,actionCardInGame8,actionCardInGame9,actionCardInGame10};
        actionCardsInGame.setImageSlots(acigImageViews);
        Label[] acigLabels = {labelACIG1, labelACIG2, labelACIG3, labelACIG4, labelACIG5, labelACIG6, labelACIG7, labelACIG8, labelACIG9, labelACIG10};
        actionCardsInGame.setLabels(acigLabels);

        actionCardsInGame.getImageSlot(1).setImage(CardFactory.getCard("Village").getCardImage());
        actionCardsInGame.getLabel(1).setText("Cards Remaining: 1");
        actionCardsInGame.getImageSlot(2).setImage(CardFactory.getCard("Woodcutter").getCardImage());
        actionCardsInGame.getLabel(2).setText("Cards Remaining: 2");
        actionCardsInGame.getImageSlot(3).setImage(CardFactory.getCard("Smithy").getCardImage());
        actionCardsInGame.getLabel(3).setText("Cards Remaining: 3");
        actionCardsInGame.getImageSlot(4).setImage(CardFactory.getCard("Festival").getCardImage());
        actionCardsInGame.getLabel(4).setText("Cards Remaining: 4");
        actionCardsInGame.getImageSlot(5).setImage(CardFactory.getCard("Laboratory").getCardImage());
        actionCardsInGame.getLabel(5).setText("Cards Remaining: 5");
        actionCardsInGame.getImageSlot(6).setImage(CardFactory.getCard("Market").getCardImage());
        actionCardsInGame.getLabel(6).setText("Cards Remaining: 6");
        //End of ActionCard Initialization

        //TreasureCard Initialization
        ImageView[] tcigImageViews = {treasureCardInGame1,treasureCardInGame2,treasureCardInGame3,treasureCardInGame4};
        treasureCardsInGame.setImageSlots(tcigImageViews);
        Label[] tcigLabels = {labelTCIG1,labelTCIG2,labelTCIG3,labelTCIG4};
        treasureCardsInGame.setLabels(tcigLabels);

        treasureCardsInGame.getImageSlot(1).setImage(CardFactory.getCard("Copper").getCardImage());
        treasureCardsInGame.getLabel(1).setText("Cards Left: 1");
        treasureCardsInGame.getImageSlot(2).setImage(CardFactory.getCard("Silver").getCardImage());
        treasureCardsInGame.getLabel(2).setText("Cards Left: 2");
        treasureCardsInGame.getImageSlot(3).setImage(CardFactory.getCard("Gold").getCardImage());
        treasureCardsInGame.getLabel(3).setText("Cards Left: 3");
        treasureCardsInGame.getImageSlot(4).setImage(CardFactory.getCard("Platinum").getCardImage());
        treasureCardsInGame.getLabel(4).setText("Cards Left: 4");
        //End of TreasureCard Initialization

        //VictoryCard Initialization
        ImageView[] vcigImageViews = {victoryCardInGame1,victoryCardInGame2,victoryCardInGame3,victoryCardInGame4};
        victoryCardsInGame.setImageSlots(vcigImageViews);
        Label[] vcigLabels = {labelVCIG1,labelVCIG2,labelVCIG3,labelVCIG4};
        victoryCardsInGame.setLabels(vcigLabels);

        victoryCardsInGame.getImageSlot(1).setImage(CardFactory.getCard("Estate").getCardImage());
        victoryCardsInGame.getLabel(1).setText("Cards Left: 1");
        victoryCardsInGame.getImageSlot(2).setImage(CardFactory.getCard("Duchy").getCardImage());
        victoryCardsInGame.getLabel(2).setText("Cards Left: 2");
        victoryCardsInGame.getImageSlot(3).setImage(CardFactory.getCard("Province").getCardImage());
        victoryCardsInGame.getLabel(3).setText("Cards Left: 3");
        victoryCardsInGame.getImageSlot(4).setImage(CardFactory.getCard("Colony").getCardImage());
        victoryCardsInGame.getLabel(4).setText("Cards Left: 4");
        //End of VictoryCard Initialization

        //OpponentHand Initialization
        ImageView[] opponentHandImageViews = {opponentCard1,opponentCard2,opponentCard3,opponentCard4,opponentCard5,
                opponentCard6,opponentCard7,opponentCard8,opponentCard9,opponentCard10};
        opponentHand.setImageSlots(opponentHandImageViews);
        opponentHand.getImageSlot(1).setImage(CardFactory.getCard("BackViewCard").getCardImage());
        opponentHand.getImageSlot(2).setImage(CardFactory.getCard("BackViewCard").getCardImage());
        opponentHand.getImageSlot(3).setImage(CardFactory.getCard("BackViewCard").getCardImage());
        opponentHand.getImageSlot(4).setImage(CardFactory.getCard("BackViewCard").getCardImage());
        opponentHand.getImageSlot(5).setImage(CardFactory.getCard("BackViewCard").getCardImage());
        //End of OpponentHand Initialization

        //PlayerHand Initialization
        ImageView[] playerHandImageViews = {card1,card2,card3,card4,card5,
                card6,card7,card8,card9,card10};
        opponentHand.setImageSlots(playerHandImageViews);
        //End of PlayerHand Initialization

    }

    public void gameCommandEntered(ActionEvent actionEvent) {
        if(gameDisplayStrings.size()>=7) gameDisplayStrings.remove(0);
        gameDisplayStrings.add(UserInterfaceHub.getPlayer().getName() + ": " + gameInterface.getText());
        gameInterface.setText(null);
        StringBuilder builder = new StringBuilder();
        for(String s: gameDisplayStrings) {
            builder.append(s); builder.append("\n");
        }
        gameLogDisplay.setText(builder.toString());
    }
    public void chatMessageEntered(ActionEvent actionEvent) {
        if(chatDisplayStrings.size()>=7) chatDisplayStrings.remove(0);
        chatDisplayStrings.add(UserInterfaceHub.getPlayer().getName() + ": " + chatInterface.getText());
        chatInterface.setText(null);
        StringBuilder builder = new StringBuilder();
        for(String s: chatDisplayStrings) {
            builder.append(s); builder.append("\n");
        }
        chatLogDisplay.setText(builder.toString());
    }

    public void changeName(ActionEvent actionEvent) {
        UserInterfaceHub.getPlayer().setName(changeNameField.getText());
        changeNameField.setText(null);
        actionCardsInGame.getLabel(5).setText("Changed");
    }

    public Object getDisplayObject(String displayObjectName) {
        switch (displayObjectName) {
            case "actionCardsInGame": return actionCardsInGame;
            case "treasureCardsInGame": return treasureCardsInGame;
            case "victoryCardsInGame": return victoryCardsInGame;
            case "opponentHand": return opponentHand;
            case "playerHand": return playerHand;
            case "chatLogDisplay": return chatLogDisplay;
            case "gameLogDisplay": return gameLogDisplay;
        }
        return null;
    }
}
