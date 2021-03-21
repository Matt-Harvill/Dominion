package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameController {

    //----------------CSS Styles for Cards---------------//
    private final String greenCardGlowStyle = "-fx-stroke-width: 3; -fx-stroke: #54ff54;";
    private final String redCardGlowStyle = "-fx-stroke-width: 3; -fx-stroke: #ff0101;";

    //---------------Chat Log and Game Log--------------//
    @FXML private TextArea chatLog, gameLog;
    @FXML private TextField chatType;
    private List<String> chatDisplayStrings, gameDisplayStrings;

    //---------------Cards In Hand---------------//
    @FXML private Rectangle cardInHand1,cardInHand2,cardInHand3,cardInHand4,cardInHand5,cardInHand6,cardInHand7,cardInHand8,cardInHand9,cardInHand10,cardInHand11;
    private Rectangle[] cardsInHand;
    @FXML private Rectangle numCardInHand1,numCardInHand2,numCardInHand3,numCardInHand4,numCardInHand5,
            numCardInHand6,numCardInHand7,numCardInHand8,numCardInHand9,numCardInHand10,numCardInHand11;
    private Rectangle[] numsCardInHand;
    @FXML private Text cardInHandNumber1,cardInHandNumber2,cardInHandNumber3,cardInHandNumber4,cardInHandNumber5,
            cardInHandNumber6,cardInHandNumber7,cardInHandNumber8,cardInHandNumber9,cardInHandNumber10,cardInHandNumber11;
    private Text[] cardInHandNumbers;

    //---------------ActionCards In Supply---------------//
    @FXML private Rectangle actionCardInSupply1,actionCardInSupply2,actionCardInSupply3,actionCardInSupply4,actionCardInSupply5,
            actionCardInSupply6,actionCardInSupply7,actionCardInSupply8,actionCardInSupply9,actionCardInSupply10;
    private Rectangle[] actionCardsInSupply;

    //---------------Opponent Played Cards---------------//
    @FXML private Rectangle oppCardPlayed1,oppCardPlayed2,oppCardPlayed3,oppCardPlayed4,oppCardPlayed5,oppCardPlayed6,
            oppCardPlayed7,oppCardPlayed8,oppCardPlayed9,oppCardPlayed10,oppCardPlayed11;
    private Rectangle[] oppCardsPlayed;

    public void initialize() {
        chatDisplayStrings = new ArrayList<>();
        gameDisplayStrings = new ArrayList<>();

        //--------------Initialize order of CardsInHand and setVisible to false-----------------//
        cardsInHand = new Rectangle[]{cardInHand1, cardInHand2, cardInHand3, cardInHand4, cardInHand5, cardInHand6, cardInHand7, cardInHand8, cardInHand9, cardInHand10, cardInHand11};
        numsCardInHand = new Rectangle[]{numCardInHand1,numCardInHand2,numCardInHand3,numCardInHand4,numCardInHand5,
                numCardInHand6,numCardInHand7,numCardInHand8,numCardInHand9,numCardInHand10,numCardInHand11};
        cardInHandNumbers = new Text[]{cardInHandNumber1,cardInHandNumber2,cardInHandNumber3,cardInHandNumber4,cardInHandNumber5,
                cardInHandNumber6,cardInHandNumber7,cardInHandNumber8,cardInHandNumber9,cardInHandNumber10,cardInHandNumber11};
        for(int i=0; i<cardsInHand.length; i++) {
            cardsInHand[i].setViewOrder(cardsInHand.length-i);
            numsCardInHand[i].setViewOrder(cardsInHand.length-i-0.1);
            cardInHandNumbers[i].setViewOrder(cardsInHand.length-i-0.2);
            cardsInHand[i].setVisible(false);
            numsCardInHand[i].setVisible(false);
            cardInHandNumbers[i].setVisible(false);
        }

        //--------------Initialize all ActionCardsInSupply to Villages-----------------//
        actionCardsInSupply = new Rectangle[]{actionCardInSupply1,actionCardInSupply2,actionCardInSupply3,actionCardInSupply4,actionCardInSupply5,
                actionCardInSupply6,actionCardInSupply7,actionCardInSupply8,actionCardInSupply9,actionCardInSupply10};
        ImagePattern imagePattern2 = new ImagePattern(new Image(new File("src/resources/Village.jpg").toURI().toString()));
        for (Rectangle rectangle : actionCardsInSupply) {
            rectangle.setFill(imagePattern2);
        }

        //--------------Initialize all OppCardPlayed and setVisible to false-----------------//
        oppCardsPlayed = new Rectangle[]{oppCardPlayed1,oppCardPlayed2,oppCardPlayed3,oppCardPlayed4,oppCardPlayed5,oppCardPlayed6,
                oppCardPlayed7,oppCardPlayed8,oppCardPlayed9,oppCardPlayed10,oppCardPlayed11};
        for (Rectangle rectangle : oppCardsPlayed) {
            rectangle.setVisible(false);
        }

    }

    public Rectangle[] getCardsInHandDisplay() {
        return cardsInHand;
    }
    public Rectangle[] getCardsInHandNumBackground() {return numsCardInHand; }
    public Text[] getCardInHandNumbers() { return cardInHandNumbers; }
    public Rectangle[] getOppCardsPlayed() {
        return oppCardsPlayed;
    }

    //-------------------Internal Updates------------------------//

    public void chatSend(ActionEvent actionEvent) {
        String newChat = UserInterfaceHub.getPlayer().getName() + ": " + chatType.getText();
        addMessageToChatLog(newChat);
//        UserInterfaceHub.getClientSideConnection().send("chat " + newChat);
    }

    public void cardInHandClicked(MouseEvent mouseEvent) {
        Rectangle cardClicked = (Rectangle) mouseEvent.getSource();
        for(Rectangle card: cardsInHand) {
            if(card.equals(cardClicked)) {
                System.out.println(card.toString() + " was clicked");
                card.setStyle(greenCardGlowStyle);
            }
        }

    }
    public void mouseOverCardInHand(MouseEvent mouseEvent) {
        Object cardClicked = mouseEvent.getSource();
        if(cardClicked instanceof Rectangle) {
            for(int i=0; i<cardsInHand.length; i++) {
                if(cardsInHand[i].equals(cardClicked) || numsCardInHand[i].equals(cardClicked)) {
                    cardsInHand[i].setViewOrder(0.2);
                    numsCardInHand[i].setViewOrder(0.1);
                    cardInHandNumbers[i].setViewOrder(0);
                    cardsInHand[i].setStyle(redCardGlowStyle);
                    break;
                }
            }
        } else if(cardClicked instanceof Text) {
            for(int i=0; i<cardsInHand.length; i++) {
                if(cardInHandNumbers[i].equals(cardClicked)) {
                    cardsInHand[i].setViewOrder(0.2);
                    numsCardInHand[i].setViewOrder(0.1);
                    cardInHandNumbers[i].setViewOrder(0);
                    cardsInHand[i].setStyle(redCardGlowStyle);
                    break;
                }
            }
        }
    }
    public void mouseExitedCardInHand(MouseEvent mouseEvent) {
        Object cardClicked = mouseEvent.getSource();
        if(cardClicked instanceof Rectangle) {
            for(int i=0; i<cardsInHand.length; i++) {
                if(cardsInHand[i].equals(cardClicked) || numsCardInHand[i].equals(cardClicked)) {
                    cardsInHand[i].setViewOrder(cardsInHand.length-i);
                    numsCardInHand[i].setViewOrder(cardsInHand.length-i-0.1);
                    cardInHandNumbers[i].setViewOrder(cardsInHand.length-i-0.2);
                    break;
                }
            }
        } else if(cardClicked instanceof Text) {
            for(int i=0; i<cardsInHand.length; i++) {
                if(cardInHandNumbers[i].equals(cardClicked)) {
                    cardsInHand[i].setViewOrder(cardsInHand.length-i);
                    numsCardInHand[i].setViewOrder(cardsInHand.length-i-0.1);
                    cardInHandNumbers[i].setViewOrder(cardsInHand.length-i-0.2);
                    break;
                }
            }
        }

    }
    public void drawCards(ActionEvent actionEvent) {
        UserInterfaceHub.getPlayer().executeTurn();
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
