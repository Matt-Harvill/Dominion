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
import model.CardCollection;
import model.card.Card;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class GameController {

    //-------------List of Action Cards in the Game------------//
    private final File actionCardsInGame = new File("src/resources/ActionCardsInGame.txt");
    private final File treasureVictoryExtraCardsInGame = new File("src/resources/TreasureVictoryExtraCardsInGame.txt");
    private Scanner scanner;

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
    @FXML private Rectangle actionCardNumBackground1,actionCardNumBackground2,actionCardNumBackground3,actionCardNumBackground4,actionCardNumBackground5,
            actionCardNumBackground6,actionCardNumBackground7,actionCardNumBackground8,actionCardNumBackground9,actionCardNumBackground10;
    private Rectangle[] actionCardNumBackgrounds;
    @FXML private Text actionCardNumber1,actionCardNumber2,actionCardNumber3,actionCardNumber4,actionCardNumber5,
            actionCardNumber6,actionCardNumber7,actionCardNumber8,actionCardNumber9,actionCardNumber10;
    private Text[] actionCardNumbers;

    //---------------TreasureCards In Supply---------------//
    @ FXML private Rectangle treasureCardInSupply1,treasureCardInSupply2,treasureCardInSupply3,treasureCardInSupply4;
    private Rectangle[] treasureCardsInSupply;
    @FXML private Rectangle treasureCardNumBack1,treasureCardNumBack2,treasureCardNumBack3,treasureCardNumBack4;
    private Rectangle[] treasureCardNumBacks;
    @FXML private Text treasureCardNum1,treasureCardNum2,treasureCardNum3,treasureCardNum4;
    private Text[] treasureCardNums;

    //---------------VictoryCards In Supply---------------//
    @ FXML private Rectangle victoryCardInSupply1,victoryCardInSupply2,victoryCardInSupply3,victoryCardInSupply4;
    private Rectangle[] victoryCardsInSupply;
    @FXML private Rectangle victoryCardNumBack1,victoryCardNumBack2,victoryCardNumBack3,victoryCardNumBack4;
    private Rectangle[] victoryCardNumBacks;
    @FXML private Text victoryCardNum1,victoryCardNum2,victoryCardNum3,victoryCardNum4;
    private Text[] victoryCardNums;

    //---------------Extra Cards In Supply---------------//
    @ FXML private Rectangle extraCardInSupply1,extraCardInSupply2,extraCardInSupply3,extraCardInSupply4;
    private Rectangle[] extraCardsInSupply;
    @FXML private Rectangle extraCardNumBack1,extraCardNumBack2,extraCardNumBack3,extraCardNumBack4;
    private Rectangle[] extraCardNumBacks;
    @FXML private Text extraCardNum1,extraCardNum2,extraCardNum3,extraCardNum4;
    private Text[] extraCardNums;

    //---------------Opponent Played Cards---------------//
    @FXML private Rectangle oppCardPlayed1,oppCardPlayed2,oppCardPlayed3,oppCardPlayed4,oppCardPlayed5,oppCardPlayed6,
            oppCardPlayed7,oppCardPlayed8,oppCardPlayed9,oppCardPlayed10,oppCardPlayed11;
    private Rectangle[] oppCardsPlayed;

    //---------------Opponent and Player Deck------------------//
    @FXML private Rectangle playerDeck, opponentDeck;

    public void initialize() throws FileNotFoundException {
        chatDisplayStrings = new ArrayList<>();
        gameDisplayStrings = new ArrayList<>();

        //--------------Initialize order of CardsInHand-----------------//
        cardsInHand = new Rectangle[]{cardInHand1, cardInHand2, cardInHand3, cardInHand4, cardInHand5, cardInHand6, cardInHand7, cardInHand8, cardInHand9, cardInHand10, cardInHand11};
        numsCardInHand = new Rectangle[]{numCardInHand1,numCardInHand2,numCardInHand3,numCardInHand4,numCardInHand5,
                numCardInHand6,numCardInHand7,numCardInHand8,numCardInHand9,numCardInHand10,numCardInHand11};
        cardInHandNumbers = new Text[]{cardInHandNumber1,cardInHandNumber2,cardInHandNumber3,cardInHandNumber4,cardInHandNumber5,
                cardInHandNumber6,cardInHandNumber7,cardInHandNumber8,cardInHandNumber9,cardInHandNumber10,cardInHandNumber11};
        for(int i=0; i<cardsInHand.length; i++) {
            cardsInHand[i].setViewOrder(cardsInHand.length-i);
            numsCardInHand[i].setViewOrder(cardsInHand.length-i-0.1);
            cardInHandNumbers[i].setViewOrder(cardsInHand.length-i-0.2);
        }

        //--------------Initialize all ActionCardsInSupply to CardsInGame-----------------//
        actionCardsInSupply = new Rectangle[]{actionCardInSupply1,actionCardInSupply2,actionCardInSupply3,actionCardInSupply4,actionCardInSupply5,
                actionCardInSupply6,actionCardInSupply7,actionCardInSupply8,actionCardInSupply9,actionCardInSupply10};
        actionCardNumBackgrounds = new Rectangle[]{actionCardNumBackground1,actionCardNumBackground2,actionCardNumBackground3,actionCardNumBackground4,actionCardNumBackground5,
                actionCardNumBackground6,actionCardNumBackground7,actionCardNumBackground8,actionCardNumBackground9,actionCardNumBackground10};
        actionCardNumbers = new Text[]{actionCardNumber1,actionCardNumber2,actionCardNumber3,actionCardNumber4,actionCardNumber5,
                actionCardNumber6,actionCardNumber7,actionCardNumber8,actionCardNumber9,actionCardNumber10};
        scanner = new Scanner(actionCardsInGame);
        ImagePattern imagePattern;
        int index = 0;
        while(scanner.hasNext()) {
            String s = scanner.next();
            int num = scanner.nextInt();
            imagePattern = new ImagePattern(new Image(new File("src/resources/" + s + ".jpg").toURI().toString()));
            actionCardsInSupply[index].setFill(imagePattern);
            actionCardNumbers[index].setText(String.valueOf(num));
            actionCardsInSupply[index].setVisible(true);
            actionCardNumBackgrounds[index].setVisible(true);
            actionCardNumbers[index].setVisible(true);
            index++;
        }

        //--------------Initialize all Treasure/Victory/ExtraCardsInSupply to CardsInGame-----------------//
        treasureCardsInSupply = new Rectangle[]{treasureCardInSupply1,treasureCardInSupply2,treasureCardInSupply3,treasureCardInSupply4};
        treasureCardNumBacks = new Rectangle[]{treasureCardNumBack1,treasureCardNumBack2,treasureCardNumBack3,treasureCardNumBack4};
        treasureCardNums = new Text[]{treasureCardNum1,treasureCardNum2,treasureCardNum3,treasureCardNum4};
        victoryCardsInSupply = new Rectangle[]{victoryCardInSupply1,victoryCardInSupply2,victoryCardInSupply3,victoryCardInSupply4};
        victoryCardNumBacks = new Rectangle[]{victoryCardNumBack1,victoryCardNumBack2,victoryCardNumBack3,victoryCardNumBack4};
        victoryCardNums = new Text[]{victoryCardNum1,victoryCardNum2,victoryCardNum3,victoryCardNum4};
        extraCardsInSupply = new Rectangle[]{extraCardInSupply1,extraCardInSupply2,extraCardInSupply3,extraCardInSupply4};
        extraCardNumBacks = new Rectangle[]{extraCardNumBack1,extraCardNumBack2,extraCardNumBack3,extraCardNumBack4};
        extraCardNums = new Text[]{extraCardNum1,extraCardNum2,extraCardNum3,extraCardNum4};
        scanner = new Scanner(treasureVictoryExtraCardsInGame);
        String s = scanner.next();
        if(s.contains("TreasureCards")) {
            index = 0;
            while (scanner.hasNext()) {
                s = scanner.next();
                if (s.contains("VictoryCards")) break;
                int num = scanner.nextInt();
                imagePattern = new ImagePattern(new Image(new File("src/resources/" + s + "_Small.jpg").toURI().toString()));
                treasureCardsInSupply[index].setFill(imagePattern);
                treasureCardNums[index].setText(String.valueOf(num));
                treasureCardsInSupply[index].setVisible(true);
                treasureCardNumBacks[index].setVisible(true);
                treasureCardNums[index].setVisible(true);
                index++;
            }
        }
        if(s.contains("VictoryCards")) {
            index = 0;
            while (scanner.hasNext()) {
                s = scanner.next();
                if (s.contains("ExtraCards")) break;
                int num = scanner.nextInt();
                imagePattern = new ImagePattern(new Image(new File("src/resources/" + s + "_Small.jpg").toURI().toString()));
                victoryCardsInSupply[index].setFill(imagePattern);
                victoryCardNums[index].setText(String.valueOf(num));
                victoryCardsInSupply[index].setVisible(true);
                victoryCardNumBacks[index].setVisible(true);
                victoryCardNums[index].setVisible(true);
                index++;
            }
        }
        if(s.contains("ExtraCards")) {
            index = 0;
            while (scanner.hasNext()) {
                s = scanner.next();
                int num = scanner.nextInt();
                imagePattern = new ImagePattern(new Image(new File("src/resources/" + s + "_Small.jpg").toURI().toString()));
                extraCardsInSupply[index].setFill(imagePattern);
                extraCardNums[index].setText(String.valueOf(num));
                extraCardsInSupply[index].setVisible(true);
                extraCardNumBacks[index].setVisible(true);
                extraCardNums[index].setVisible(true);
                index++;
            }
        }

        //--------------Initialize player Decks-----------------//
        imagePattern = new ImagePattern(new Image(new File("src/resources/BackViewCard.png").toURI().toString()));
        playerDeck.setFill(imagePattern);
        opponentDeck.setFill(imagePattern);
        playerDeck.setVisible(true);
        opponentDeck.setVisible(true);

        //--------------Initialize all OppCardPlayed-----------------//
        oppCardsPlayed = new Rectangle[]{oppCardPlayed1,oppCardPlayed2,oppCardPlayed3,oppCardPlayed4,oppCardPlayed5,oppCardPlayed6,
                oppCardPlayed7,oppCardPlayed8,oppCardPlayed9,oppCardPlayed10,oppCardPlayed11};

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
        UserInterfaceHub.getPlayer().newTurn();
        CardCollection hand = UserInterfaceHub.getPlayer().getHand();
        displayHand(hand);
        UserInterfaceHub.getPlayer().discardHand();
    }
    public void actionCardInSupplyClicked(MouseEvent mouseEvent) {
        Object cardClicked = mouseEvent.getSource();
        if(cardClicked instanceof Rectangle) {
            for(int i=0; i<actionCardsInSupply.length; i++) {
                if(actionCardsInSupply[i].equals(cardClicked) || actionCardNumBackgrounds[i].equals(cardClicked)) {
                    if(Integer.parseInt(actionCardNumbers[i].getText())<=1) {
                        actionCardNumbers[i].setText("0");
                    } else {
                        actionCardNumbers[i].setText(String.valueOf(Integer.parseInt(actionCardNumbers[i].getText())-1));
                    }
                }
            }
        } else if(cardClicked instanceof Text) {
            for(int i=0; i<actionCardsInSupply.length; i++) {
                if(actionCardNumbers[i].equals(cardClicked)) {
                    if(Integer.parseInt(actionCardNumbers[i].getText())<=1) {
                        actionCardNumbers[i].setText("0");
                    } else {
                        actionCardNumbers[i].setText(String.valueOf(Integer.parseInt(actionCardNumbers[i].getText())-1));
                    }
                }
            }
        }
    }

    public void displayHand(CardCollection hand) {
        int index = 0;
        List<Card> cards = hand.getAllCards();
        Set<String> distinctCardsInHand = hand.getDistinctCards();

        for(int i=0; i<cardsInHand.length; i++) {
            cardsInHand[i].setVisible(false);
            numsCardInHand[i].setVisible(false);
            cardInHandNumbers[i].setVisible(false);
        }

        int countOfCard;

        for(String s: distinctCardsInHand) {
            Card cardToDisplay = null;
            countOfCard = 0;
            for(Card c: cards) {
                if(c.getName().equals(s)) {
                    countOfCard = hand.numCardInCollection(c);
                    cardToDisplay = c;
                    break;
                }
            }
            if(countOfCard==1) {
                cardsInHand[index].setFill(new ImagePattern(cardToDisplay.getCardImage()));
                cardsInHand[index].setVisible(true);
                index++;
            } else if(countOfCard>1) {
                cardsInHand[index].setFill(new ImagePattern(cardToDisplay.getCardImage()));
                cardInHandNumbers[index].setText(String.valueOf(countOfCard));
                cardsInHand[index].setVisible(true);
                numsCardInHand[index].setVisible(true);
                cardInHandNumbers[index].setVisible(true);
                index++;
            }
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
