package userInterface;

import cardStuff.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameController {

    @FXML private TextField gameInterface;

    @FXML private TextField chatInterface;

    @FXML private TextArea gameDisplay;
    private List<String> gameDisplayStrings = new ArrayList<>();

    @FXML private TextArea chatDisplay;
    private List<String> chatDisplayStrings = new ArrayList<>();

    @FXML private ImageView img2;
    @FXML private ImageView img3;

    private int count = 0;
    private Player p;
    private Card card;

    public void initialize() {
        chatDisplay.setText("This is the chat display");
        p = new Player();

        img3.setImage(new Image(new File("Images/Me and John.jpg").toURI().toString()));
        card = CardFactory.getCard("Copper");
    }


    public void onMouseEntered(MouseEvent mouseEvent) {
        gameDisplay.setText("Mouse entered chat display");
    }

    public void onMouseExited(MouseEvent mouseEvent) {
        gameDisplay.setText("Mouse left chat display");
    }

    public void gameCommandEntered(ActionEvent actionEvent) {
        gameDisplayStrings.add(gameInterface.getText());
        gameInterface.setText(null);
        StringBuilder builder = new StringBuilder();
        for(String s: gameDisplayStrings) {
            builder.append(s); builder.append("\n");
        }
        gameDisplay.setText(builder.toString());
    }

    public void chatMessageEntered(ActionEvent actionEvent) {
        chatDisplayStrings.add(chatInterface.getText());
        chatInterface.setText(null);
        StringBuilder builder = new StringBuilder();
        for(String s: chatDisplayStrings) {
            builder.append(s); builder.append("\n");
        }
        chatDisplay.setText(builder.toString());
    }

    public void onImageClicked(MouseEvent mouseEvent) {
        if(count%2==0) {
            img2.setImage(new Image(new File("C:/Users/mharv/Documents/UTD Spring 2021/Prof Dev/LinkedIn Post.PNG").toURI().toString()));
            p.print();
        } else {
            img2.setImage(new Image(card.getImageFile().toURI().toString()));
        }
        count++;
    }
}
