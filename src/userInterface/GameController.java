package userInterface;

import cardStuff.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameController {

    @FXML private TextField gameInterface, chatInterface;
    @FXML private TextArea gameDisplay, chatDisplay;
    private List<String> gameDisplayStrings = new ArrayList<>();
    private List<String> chatDisplayStrings = new ArrayList<>();

    @FXML private ImageView card1, card2, card3, card4, card5, otherCard1, otherCard2, otherCard3, otherCard4, otherCard5;

    @FXML private ImageView actionCardInGame1, actionCardInGame2, actionCardInGame3, actionCardInGame4, actionCardInGame5,
            actionCardInGame6, actionCardInGame7, actionCardInGame8, actionCardInGame9, actionCardInGame10,
            treasureCardInGame1, treasureCardInGame2, treasureCardInGame3, treasureCardInGame4,
            victoryCardInGame1, victoryCardInGame2, victoryCardInGame3, victoryCardInGame4;
    @FXML private Label labelACIG1, labelACIG2, labelACIG3, labelACIG4, labelACIG5, labelACIG6, labelACIG7, labelACIG8, labelACIG9, labelACIG10,
            labelTCIG1, labelTCIG2, labelTCIG3, labelTCIG4,
            labelVCIG1, labelVCIG2, labelVCIG3, labelVCIG4;

    private List<ImageView> images = new ArrayList<>();

    public void initialize() {
        chatDisplay.setText("This is the chat display");

        otherCard1.setImage(CardFactory.getCard("BackViewCard").getCardImage());
        otherCard2.setImage(CardFactory.getCard("BackViewCard").getCardImage());
        otherCard3.setImage(CardFactory.getCard("BackViewCard").getCardImage());
        otherCard4.setImage(CardFactory.getCard("BackViewCard").getCardImage());
        otherCard5.setImage(CardFactory.getCard("BackViewCard").getCardImage());

        actionCardInGame1.setImage(CardFactory.getCard("Village").getCardImage());
        labelACIG1.setText("Cards Remaining: 1");
        actionCardInGame2.setImage(CardFactory.getCard("Woodcutter").getCardImage());
        labelACIG2.setText("Cards Remaining: 2");
        actionCardInGame3.setImage(CardFactory.getCard("Smithy").getCardImage());
        labelACIG3.setText("Cards Remaining: 3");
        actionCardInGame4.setImage(CardFactory.getCard("Festival").getCardImage());
        labelACIG4.setText("Cards Remaining: 4");
        actionCardInGame5.setImage(CardFactory.getCard("Laboratory").getCardImage());
        labelACIG5.setText("Cards Remaining: 5");
        actionCardInGame6.setImage(CardFactory.getCard("Market").getCardImage());
        labelACIG6.setText("Cards Remaining: 6");

        treasureCardInGame1.setImage(CardFactory.getCard("Copper").getCardImage());
        labelTCIG1.setText("Cards Left: 1");
        treasureCardInGame2.setImage(CardFactory.getCard("Silver").getCardImage());
        labelTCIG2.setText("Cards Left: 2");
        treasureCardInGame3.setImage(CardFactory.getCard("Gold").getCardImage());
        labelTCIG3.setText("Cards Left: 3");
        treasureCardInGame4.setImage(CardFactory.getCard("Platinum").getCardImage());
        labelTCIG4.setText("Cards Left: 4");

        victoryCardInGame1.setImage(CardFactory.getCard("Estate").getCardImage());
        labelVCIG1.setText("Cards Left: 1");
        victoryCardInGame2.setImage(CardFactory.getCard("Duchy").getCardImage());
        labelVCIG2.setText("Cards Left: 2");
        victoryCardInGame3.setImage(CardFactory.getCard("Province").getCardImage());
        labelVCIG3.setText("Cards Left: 3");
        victoryCardInGame4.setImage(CardFactory.getCard("Colony").getCardImage());
        labelVCIG4.setText("Cards Left: 4");

    }

    public void gameCommandEntered(ActionEvent actionEvent) {
        if(gameDisplayStrings.size()>=7) gameDisplayStrings.remove(0);
        gameDisplayStrings.add(gameInterface.getText());
        gameInterface.setText(null);
        StringBuilder builder = new StringBuilder();
        for(String s: gameDisplayStrings) {
            builder.append(s); builder.append("\n");
        }
        gameDisplay.setText(builder.toString());
    }
    public void chatMessageEntered(ActionEvent actionEvent) {
        if(chatDisplayStrings.size()>=7) chatDisplayStrings.remove(0);
        chatDisplayStrings.add(chatInterface.getText());
        chatInterface.setText(null);
        StringBuilder builder = new StringBuilder();
        for(String s: chatDisplayStrings) {
            builder.append(s); builder.append("\n");
        }
        chatDisplay.setText(builder.toString());
    }

    public void onImageClicked(MouseEvent mouseEvent) {
        ImageView temp = (ImageView) mouseEvent.getSource();
        temp.setImage(CardFactory.getCard("Copper").getCardImage());
    }
}
