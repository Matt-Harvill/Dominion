package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class GameController {

    @FXML private Label actionCardInSupplyNum1;
    @FXML private BorderPane mainBorderPane;
    @FXML private StackPane mainStackPane;
    @FXML private TextArea chatLog, gameLog;
    @FXML private TextField chatType;

    @FXML private ImageView cardInHand1,cardInHand2,cardInHand3,cardInHand4,cardInHand5,cardInHand6,cardInHand7,cardInHand8,cardInHand9,cardInHand10,cardInHand11;

    private List<String> chatDisplayStrings, gameDisplayStrings;
    private ImageView[] cardsInHand;

    public void initialize() {
        chatDisplayStrings = new ArrayList<>();
        gameDisplayStrings = new ArrayList<>();
        cardsInHand = new ImageView[]{cardInHand1, cardInHand2, cardInHand3, cardInHand4, cardInHand5, cardInHand6, cardInHand7, cardInHand8, cardInHand9, cardInHand10, cardInHand11};
        for(int i=0; i<cardsInHand.length; i++) {
            cardsInHand[i].setViewOrder(cardsInHand.length-i);
        }
    }

    //-------------------Internal Updates------------------------//

    public void chatSend(ActionEvent actionEvent) {
        String newChat = UserInterfaceHub.getClient().getName() + ": " + chatType.getText();
        addMessageToChatLog(newChat);
//        UserInterfaceHub.getClientSideConnection().send("chat " + newChat);
    }

    public void cardInHandClicked(MouseEvent mouseEvent) {
        ImageView cardClicked = (ImageView) mouseEvent.getSource();
        for(ImageView card: cardsInHand) {
            if(card.equals(cardClicked)) System.out.println(card.toString() + " was clicked");
        }

    }
    public void mouseOverCardInHand(MouseEvent mouseEvent) throws FileNotFoundException {
        ImageView cardClicked = (ImageView) mouseEvent.getSource();
        for(ImageView card: cardsInHand) {
            if(card.equals(cardClicked)) card.setViewOrder(0);
        }
    }
    public void mouseExitedCardInHand(MouseEvent mouseEvent) {
        ImageView cardClicked = (ImageView) mouseEvent.getSource();
        for(int i=0; i<cardsInHand.length; i++) {
            if(cardsInHand[i].equals(cardClicked)) cardsInHand[i].setViewOrder(cardsInHand.length-i);
        }
    }


    //-------------------External Updates------------------------//

    public void addMessageToChatLog(String msg) {
        if(chatDisplayStrings.size()>=7) chatDisplayStrings.remove(0);
        chatDisplayStrings.add(msg);
        chatType.setText(null);
        StringBuilder builder = new StringBuilder();
        for(String s: chatDisplayStrings) {
            builder.append(s); builder.append("\n");
        }
        chatLog.setText(builder.toString());
    }
    public void addMessageToGameLog(String msg) {
        if(gameDisplayStrings.size()>=7) gameDisplayStrings.remove(0);
        gameDisplayStrings.add(msg);
        gameLog.setText(null);
        StringBuilder builder = new StringBuilder();
        for(String s: gameDisplayStrings) {
            builder.append(s); builder.append("\n");
        }
        gameLog.setText(builder.toString());
    }

}
