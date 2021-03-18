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
    @FXML private ImageView image1, image2, image3, image4, image5;
    @FXML private ImageView cardInGame00,cardInGame10,cardInGame20,cardInGame30,cardInGame40,cardInGame01,cardInGame11,cardInGame21,cardInGame31,cardInGame41;
    @FXML private Label label00, label10, label20, label30, label40, label01, label11, label21, label31, label41;

    private List<ImageView> images = new ArrayList<>();

    public void initialize() {
        chatDisplay.setText("This is the chat display");

        cardInGame00.setImage(CardFactory.getCard("Silver").getCardImage());
        label00.setText("Cards Remaining: 10");
        cardInGame10.setImage(CardFactory.getCard("Copper").getCardImage());
        label10.setText("Cards Remaining: 20");
        cardInGame20.setImage(CardFactory.getCard("Gold").getCardImage());
        label20.setText("Cards Remaining: 5");
    }

    public void gameCommandEntered(ActionEvent actionEvent) {
        if(gameDisplayStrings.size()>7) gameDisplayStrings.remove(0);
        gameDisplayStrings.add(gameInterface.getText());
        gameInterface.setText(null);
        StringBuilder builder = new StringBuilder();
        for(String s: gameDisplayStrings) {
            builder.append(s); builder.append("\n");
        }
        gameDisplay.setText(builder.toString());
    }
    public void chatMessageEntered(ActionEvent actionEvent) {
        if(chatDisplayStrings.size()>7) chatDisplayStrings.remove(0);
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
