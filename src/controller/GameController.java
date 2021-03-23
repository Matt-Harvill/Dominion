package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import view.ActionCardSupplyDisplay;
import view.LeftSupplyCardDisplay;
import view.PlayerHandDisplay;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameController {

    //-------------List of Action Cards in the Game------------//
    private final File actionCardsInGame = new File("src/resources/ActionCardsInGame.txt");
    private final File leftSupplyCardsInGame = new File("src/resources/leftSupplyCardsInGame.txt");

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
    @FXML private Rectangle cardInHandNumBack1,cardInHandNumBack2,cardInHandNumBack3,cardInHandNumBack4,cardInHandNumBack5,cardInHandNumBack6,
            cardInHandNumBack7,cardInHandNumBack8,cardInHandNumBack9,cardInHandNumBack10,cardInHandNumBack11;
    private Rectangle[] cardsInHandNumBacks;
    @FXML private Text cardInHandNum1,cardInHandNum2,cardInHandNum3,cardInHandNum4,cardInHandNum5,cardInHandNum6,cardInHandNum7,
            cardInHandNum8,cardInHandNum9,cardInHandNum10,cardInHandNum11;
    private Text[] cardsInHandNums;
    private PlayerHandDisplay playerHandDisplay;

    //---------------ActionCards In Supply---------------//
    @FXML private Rectangle actionCardInSupply1,actionCardInSupply2,actionCardInSupply3,actionCardInSupply4,actionCardInSupply5,
            actionCardInSupply6,actionCardInSupply7,actionCardInSupply8,actionCardInSupply9,actionCardInSupply10;
    private Rectangle[] actionCardsInSupply;
    @FXML private Rectangle actionCardNumBack1,actionCardNumBack2,actionCardNumBack3,actionCardNumBack4,actionCardNumBack5,actionCardNumBack6,
            actionCardNumBack7,actionCardNumBack8,actionCardNumBack9,actionCardNumBack10;
    private Rectangle[] actionCardNumBacks;
    @FXML private Text actionCardNum1,actionCardNum2,actionCardNum3,actionCardNum4,actionCardNum5,
            actionCardNum6,actionCardNum7,actionCardNum8,actionCardNum9,actionCardNum10;
    private Text[] actionCardNums;
    @FXML private Rectangle actionCardBuyButton1,actionCardBuyButton2,actionCardBuyButton3,actionCardBuyButton4,actionCardBuyButton5,
            actionCardBuyButton6,actionCardBuyButton7,actionCardBuyButton8,actionCardBuyButton9,actionCardBuyButton10;
    private Rectangle[] actionCardBuyButtons;
    private String[] namesOfActionCardsInSupply;
    private ActionCardSupplyDisplay actionCardSupplyDisplay;

    //---------------TreasureCards In Supply---------------//
    @ FXML private Rectangle treasureCardInSupply1,treasureCardInSupply2,treasureCardInSupply3,treasureCardInSupply4;
    @FXML private Rectangle treasureCardNumBack1,treasureCardNumBack2,treasureCardNumBack3,treasureCardNumBack4;
    @FXML private Text treasureCardNum1,treasureCardNum2,treasureCardNum3,treasureCardNum4;
    @FXML private Rectangle treasureCardBuyButton1,treasureCardBuyButton2,treasureCardBuyButton3,treasureCardBuyButton4;

    //---------------VictoryCards In Supply---------------//
    @ FXML private Rectangle victoryCardInSupply1,victoryCardInSupply2,victoryCardInSupply3,victoryCardInSupply4;
    @FXML private Rectangle victoryCardNumBack1,victoryCardNumBack2,victoryCardNumBack3,victoryCardNumBack4;
    @FXML private Text victoryCardNum1,victoryCardNum2,victoryCardNum3,victoryCardNum4;
    @FXML private Rectangle victoryCardBuyButton1,victoryCardBuyButton2,victoryCardBuyButton3,victoryCardBuyButton4;

    //---------------Extra Cards In Supply---------------//
    @ FXML private Rectangle extraCardInSupply1,extraCardInSupply2,extraCardInSupply3,extraCardInSupply4;
    @FXML private Rectangle extraCardNumBack1,extraCardNumBack2,extraCardNumBack3,extraCardNumBack4;
    @FXML private Text extraCardNum1,extraCardNum2,extraCardNum3,extraCardNum4;
    @FXML private Rectangle extraCardBuyButton1,extraCardBuyButton2,extraCardBuyButton3,extraCardBuyButton4;

    //-------------All Cards In LeftMost Supply-----------//
    private Rectangle[] cardsInLeftSupply;
    private Rectangle[] cardsInLeftSupplyNumBacks;
    private Text[] cardsInLeftSupplyNums;
    private Rectangle[] leftSupplyCardsBuyButtons;
    private String[] namesOfCardsInLeftSupply;
    private LeftSupplyCardDisplay leftSupplyCardDisplay;

    //---------------Cards in Play---------------//
    @FXML private Rectangle cardInPlay1,cardInPlay2,cardInPlay3,cardInPlay4,cardInPlay5,cardInPlay6,
            cardInPlay7,cardInPlay8,cardInPlay9,cardInPlay10,cardInPlay11;
    private Rectangle[] cardsInPlay;
    @FXML private Rectangle cardInPlayNumBack1,cardInPlayNumBack2,cardInPlayNumBack3,cardInPlayNumBack4,cardInPlayNumBack5,
            cardInPlayNumBack6,cardInPlayNumBack7,cardInPlayNumBack8,cardInPlayNumBack9,cardInPlayNumBack10,cardInPlayNumBack11;
    private Rectangle[] cardsInPlayNumBacks;
    @FXML private Text cardInPlayNum1,cardInPlayNum2,cardInPlayNum3,cardInPlayNum4,cardInPlayNum5,cardInPlayNum6,
            cardInPlayNum7,cardInPlayNum8,cardInPlayNum9,cardInPlayNum10,cardInPlayNum11;
    private Text[] cardsInPlayNums;

    //---------------Opponent and Player Deck------------------//
    @FXML private Rectangle playerDeck, opponentDeck;

    @FXML private Button actionButton;

    public void initialize() throws FileNotFoundException {
        chatDisplayStrings = new ArrayList<>();
        gameDisplayStrings = new ArrayList<>();
        chatType.setText(null);

        //--------------Initialize order of CardsInHand-----------------//
        cardsInHand = new Rectangle[]{cardInHand1, cardInHand2, cardInHand3, cardInHand4, cardInHand5, cardInHand6, cardInHand7, cardInHand8, cardInHand9, cardInHand10, cardInHand11};
        cardsInHandNumBacks = new Rectangle[]{cardInHandNumBack1,cardInHandNumBack2,cardInHandNumBack3,cardInHandNumBack4,cardInHandNumBack5,cardInHandNumBack6,
                cardInHandNumBack7,cardInHandNumBack9,cardInHandNumBack8,cardInHandNumBack10,cardInHandNumBack11};
        cardsInHandNums = new Text[]{cardInHandNum1,cardInHandNum2,cardInHandNum3,cardInHandNum4,cardInHandNum5,cardInHandNum6,cardInHandNum7,
                cardInHandNum8,cardInHandNum9,cardInHandNum10,cardInHandNum11};
        for(int i=0; i<cardsInHand.length; i++) {
            cardsInHand[i].setViewOrder(cardsInHand.length-i);
            cardsInHandNumBacks[i].setViewOrder(cardsInHand.length-i-0.1);
            cardsInHandNums[i].setViewOrder(cardsInHand.length-i-0.2);
        }
        playerHandDisplay = new PlayerHandDisplay(cardsInHand, cardsInHandNumBacks, cardsInHandNums);

        //--------------Initialize all ActionCardsInSupply to CardsInGame-----------------//
        actionCardsInSupply = new Rectangle[]{actionCardInSupply1,actionCardInSupply2,actionCardInSupply3,actionCardInSupply4,actionCardInSupply5,
                actionCardInSupply6,actionCardInSupply7,actionCardInSupply8,actionCardInSupply9,actionCardInSupply10};
        actionCardNumBacks = new Rectangle[]{actionCardNumBack1,actionCardNumBack2,actionCardNumBack3,actionCardNumBack4,actionCardNumBack5,actionCardNumBack6,
                actionCardNumBack7,actionCardNumBack8,actionCardNumBack9,actionCardNumBack10};
        actionCardNums = new Text[]{actionCardNum1,actionCardNum2,actionCardNum3,actionCardNum4,actionCardNum5,
                actionCardNum6,actionCardNum7,actionCardNum8,actionCardNum9,actionCardNum10};
        actionCardBuyButtons = new Rectangle[]{actionCardBuyButton1,actionCardBuyButton2,actionCardBuyButton3,actionCardBuyButton4,actionCardBuyButton5,
                actionCardBuyButton6,actionCardBuyButton7,actionCardBuyButton8,actionCardBuyButton9,actionCardBuyButton10};
        namesOfActionCardsInSupply = new String[actionCardsInSupply.length];
        Scanner scanner = new Scanner(actionCardsInGame);
        ImagePattern imagePattern;
        int index = 0;
        while(scanner.hasNext()) {
            String s = scanner.next();
            namesOfActionCardsInSupply[index] = s;
            int num = scanner.nextInt();
            imagePattern = new ImagePattern(new Image(new File("src/resources/" + s + ".jpg").toURI().toString()));
            actionCardsInSupply[index].setFill(imagePattern);
            imagePattern = new ImagePattern(new Image(new File("src/resources/Plus Sign.png").toURI().toString()));
            actionCardBuyButtons[index].setFill(imagePattern);
            actionCardNums[index].setText(String.valueOf(num));
            actionCardsInSupply[index].setVisible(true);
            actionCardNumBacks[index].setVisible(true);
            actionCardNums[index].setVisible(true);
//            buyActionCardButtons[index].setVisible(true);
            index++;
        }
        actionCardSupplyDisplay = new ActionCardSupplyDisplay(actionCardsInSupply,actionCardNumBacks,actionCardNums,
                actionCardBuyButtons,namesOfActionCardsInSupply);

        //--------------Initialize all Treasure/Victory/ExtraCardsInSupply to CardsInGame-----------------//
        cardsInLeftSupply = new Rectangle[]{treasureCardInSupply1,treasureCardInSupply2,treasureCardInSupply3,treasureCardInSupply4,
                victoryCardInSupply1,victoryCardInSupply2,victoryCardInSupply3,victoryCardInSupply4,extraCardInSupply1,extraCardInSupply2,extraCardInSupply3,extraCardInSupply4};
        cardsInLeftSupplyNumBacks = new Rectangle[]{treasureCardNumBack1,treasureCardNumBack2,treasureCardNumBack3,treasureCardNumBack4,
                victoryCardNumBack1,victoryCardNumBack2,victoryCardNumBack3,victoryCardNumBack4,extraCardNumBack1,extraCardNumBack2,extraCardNumBack3,extraCardNumBack4};
        cardsInLeftSupplyNums = new Text[]{treasureCardNum1,treasureCardNum2,treasureCardNum3,treasureCardNum4,
                victoryCardNum1,victoryCardNum2,victoryCardNum3,victoryCardNum4,extraCardNum1,extraCardNum2,extraCardNum3,extraCardNum4};
        leftSupplyCardsBuyButtons = new Rectangle[]{treasureCardBuyButton1,treasureCardBuyButton2,treasureCardBuyButton3,treasureCardBuyButton4,
                victoryCardBuyButton1,victoryCardBuyButton2,victoryCardBuyButton3,victoryCardBuyButton4,extraCardBuyButton1,extraCardBuyButton2,extraCardBuyButton3,extraCardBuyButton4};
        namesOfCardsInLeftSupply = new String[cardsInLeftSupply.length];
        leftSupplyCardDisplay = new LeftSupplyCardDisplay(cardsInLeftSupply,cardsInLeftSupplyNumBacks,cardsInLeftSupplyNums,
                leftSupplyCardsBuyButtons,namesOfCardsInLeftSupply);

        scanner = new Scanner(leftSupplyCardsInGame);
        String s = scanner.next();
        index = 0;
        while (scanner.hasNext()) {
            s = scanner.next();
            if (s.contains("Cards")) continue;
            namesOfCardsInLeftSupply[index] = s;
            int num = scanner.nextInt();
            imagePattern = new ImagePattern(new Image(new File("src/resources/" + s + "_Small.jpg").toURI().toString()));
            cardsInLeftSupply[index].setFill(imagePattern);
            cardsInLeftSupplyNums[index].setText(String.valueOf(num));
            cardsInLeftSupply[index].setVisible(true);
            cardsInLeftSupplyNumBacks[index].setVisible(true);
            cardsInLeftSupplyNums[index].setVisible(true);
            index++;
        }
        for(String string: namesOfCardsInLeftSupply) System.out.println(string);

        for(Rectangle rectangle: leftSupplyCardsBuyButtons) {
            imagePattern = new ImagePattern(new Image(new File("src/resources/Plus Sign.png").toURI().toString()));
            rectangle.setFill(imagePattern);
        }

        //--------------Initialize player Decks-----------------//
        imagePattern = new ImagePattern(new Image(new File("src/resources/BackViewCard.png").toURI().toString()));
        playerDeck.setFill(imagePattern);
        opponentDeck.setFill(imagePattern);
        playerDeck.setVisible(true);
        opponentDeck.setVisible(true);

        //------------------Initialize Cards In Play--------------//
        cardsInPlay = new Rectangle[]{cardInPlay1,cardInPlay2,cardInPlay3,cardInPlay4,cardInPlay5,cardInPlay6,
                cardInPlay7,cardInPlay8,cardInPlay9,cardInPlay10,cardInPlay11};
        cardsInPlayNumBacks = new Rectangle[]{cardInPlayNumBack1,cardInPlayNumBack2,cardInPlayNumBack3,cardInPlayNumBack4,cardInPlayNumBack5,
                cardInPlayNumBack6,cardInPlayNumBack7,cardInPlayNumBack8,cardInPlayNumBack9,cardInPlayNumBack10,cardInPlayNumBack11};
        cardsInPlayNums = new Text[]{cardInPlayNum1,cardInPlayNum2,cardInPlayNum3,cardInPlayNum4,cardInPlayNum5,cardInPlayNum6,
                cardInPlayNum7,cardInPlayNum8,cardInPlayNum9,cardInPlayNum10,cardInPlayNum11};

    }

    //-------------Getters----------------//

    public Button getActionButton() { return actionButton; }
    public List<String> getChatDisplayStrings() {return chatDisplayStrings;}
    public List<String> getGameDisplayStrings() {return gameDisplayStrings;}
    public TextArea getChatLog() {
        return chatLog;
    }
    public TextArea getGameLog() {
        return gameLog;
    }
    public TextField getChatType() {
        return chatType;
    }
    public PlayerHandDisplay getPlayerHandDisplay() {return playerHandDisplay;}
    public ActionCardSupplyDisplay getActionCardSupplyDisplay() {return actionCardSupplyDisplay;}
    public LeftSupplyCardDisplay getLeftSupplyCardDisplay() { return leftSupplyCardDisplay;}
    public String getGreenCardGlowStyle() {return greenCardGlowStyle;}

    //-------------------Internal Updates------------------------//

    public void chatSend(ActionEvent actionEvent) {
        if(chatType.getText()==null) return;
        String newChat = UserInterfaceHub.getPlayer().getName() + ": " + chatType.getText();
        UserInterfaceHub.getPlayerActionMediator().addMessageToChatLog(newChat);
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
                if(cardsInHand[i].equals(cardClicked) || cardsInHandNumBacks[i].equals(cardClicked)) {
                    cardsInHand[i].setViewOrder(0.2);
                    cardsInHandNumBacks[i].setViewOrder(0.1);
                    cardsInHandNums[i].setViewOrder(0);
                    cardsInHand[i].setStyle(redCardGlowStyle);
                    break;
                }
            }
        } else if(cardClicked instanceof Text) {
            for(int i=0; i<cardsInHand.length; i++) {
                if(cardsInHandNums[i].equals(cardClicked)) {
                    cardsInHand[i].setViewOrder(0.2);
                    cardsInHandNumBacks[i].setViewOrder(0.1);
                    cardsInHandNums[i].setViewOrder(0);
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
                if(cardsInHand[i].equals(cardClicked) || cardsInHandNumBacks[i].equals(cardClicked)) {
                    cardsInHand[i].setViewOrder(cardsInHand.length-i);
                    cardsInHandNumBacks[i].setViewOrder(cardsInHand.length-i-0.1);
                    cardsInHandNums[i].setViewOrder(cardsInHand.length-i-0.2);
                    break;
                }
            }
        } else if(cardClicked instanceof Text) {
            for(int i=0; i<cardsInHand.length; i++) {
                if(cardsInHandNums[i].equals(cardClicked)) {
                    cardsInHand[i].setViewOrder(cardsInHand.length-i);
                    cardsInHandNumBacks[i].setViewOrder(cardsInHand.length-i-0.1);
                    cardsInHandNums[i].setViewOrder(cardsInHand.length-i-0.2);
                    break;
                }
            }
        }

    }
    public void actionButtonClicked(ActionEvent actionEvent) {
        if(actionButton.getText().equals("Start Turn")) {
            UserInterfaceHub.getPlayerActionMediator().startPhase();
        }
        else if(actionButton.getText().equals("End Turn")) {
            UserInterfaceHub.getPlayerActionMediator().endPhase();
        }

    }
    public void actionCardInSupplyClicked(MouseEvent mouseEvent) {
        Object cardClicked = mouseEvent.getSource();
        if(cardClicked instanceof Rectangle) {
            for(int i=0; i<actionCardsInSupply.length; i++) {
                if(actionCardsInSupply[i].equals(cardClicked) || actionCardNumBacks[i].equals(cardClicked)) {
                    if(Integer.parseInt(actionCardNums[i].getText())<=1) {
                        actionCardNums[i].setText("0");
                    } else {
                        actionCardNums[i].setText(String.valueOf(Integer.parseInt(actionCardNums[i].getText())-1));
                    }
                }
            }
        } else if(cardClicked instanceof Text) {
            for(int i=0; i<actionCardsInSupply.length; i++) {
                if(actionCardNums[i].equals(cardClicked)) {
                    if(Integer.parseInt(actionCardNums[i].getText())<=1) {
                        actionCardNums[i].setText("0");
                    } else {
                        actionCardNums[i].setText(String.valueOf(Integer.parseInt(actionCardNums[i].getText())-1));
                    }
                }
            }
        }
    }
    public void buyButtonClicked(MouseEvent mouseEvent) {
        Rectangle buyButtonClicked = (Rectangle) mouseEvent.getSource();
        String cardClicked = "Error in buyButtonClicked";;
        for(int i=0; i< leftSupplyCardsBuyButtons.length; i++) {
            if(leftSupplyCardsBuyButtons[i].equals(buyButtonClicked)) {
                cardClicked = namesOfCardsInLeftSupply[i];
                UserInterfaceHub.getPlayerActionMediator().buyFromLeftCardSupply(cardClicked);
            }
        }
        for(int i=0; i< actionCardBuyButtons.length; i++) {
            if(actionCardBuyButtons[i].equals(buyButtonClicked)) {
                cardClicked = namesOfActionCardsInSupply[i];
                UserInterfaceHub.getPlayerActionMediator().buyFromActionCardSupply(cardClicked);
            }
        }
    }
}
