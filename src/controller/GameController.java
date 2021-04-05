package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.CardCollection;
import model.card.*;
import model.factory.CardFactory;
import org.w3c.dom.css.Rect;
import view.CardSupplyDisplay;
import view.HandOrInPlayDisplay;
import view.PlayerNamePointsDisplay;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class GameController {

    //-------------List of Cards in the Game------------//
    private CardCollection cardsInGame;
    private List<CardStack> cardStacks;

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
    private Card[] cardObjectsInHand;
    private HandOrInPlayDisplay playerHandDisplay;

    //---------------ActionCards In Supply---------------//
    @FXML private Rectangle actionCardInSupply1,actionCardInSupply2,actionCardInSupply3,actionCardInSupply4,actionCardInSupply5,
            actionCardInSupply6,actionCardInSupply7,actionCardInSupply8,actionCardInSupply9,actionCardInSupply10;
    @FXML private Rectangle actionCardNumBack1,actionCardNumBack2,actionCardNumBack3,actionCardNumBack4,actionCardNumBack5,actionCardNumBack6,
            actionCardNumBack7,actionCardNumBack8,actionCardNumBack9,actionCardNumBack10;
    @FXML private Text actionCardNum1,actionCardNum2,actionCardNum3,actionCardNum4,actionCardNum5,
            actionCardNum6,actionCardNum7,actionCardNum8,actionCardNum9,actionCardNum10;
    @FXML private Rectangle actionCardBuyButton1,actionCardBuyButton2,actionCardBuyButton3,actionCardBuyButton4,actionCardBuyButton5,
            actionCardBuyButton6,actionCardBuyButton7,actionCardBuyButton8,actionCardBuyButton9,actionCardBuyButton10;
    private Rectangle[] actionCardsInSupply,actionCardNumBacks,actionCardBuyButtons;
    private Text[] actionCardNums;

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

    //-------------All Cards In Supply-----------//
    private Rectangle[] cardsInSupply;
    private Rectangle[] cardsInSupplyNumBacks;
    private Text[] cardsInSupplyNums;
    private Rectangle[] cardsInSupplyBuyButtons;
    private Card[] cardObjectsInSupply;
    private CardSupplyDisplay cardSupplyDisplay;

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
    private Card[] cardObjectsInPlay;
    private HandOrInPlayDisplay inPlayDisplay;

    //---------------Opponent and Player Deck------------------//
    @FXML private Rectangle playerDeck, opponentDeck;
    @FXML private Button actionButton;
    @FXML private Text gameInfoText;
    @FXML private StackPane actionBar;

    //--------------PlayerName and Point Displays------------//
    @FXML private Rectangle playerLabel1, playerLabel2,playerLabel3,playerLabel4,playerLabel5,playerLabel6;
    @FXML private Text playerLabelName1,playerLabelName2,playerLabelName3,playerLabelName4,playerLabelName5,playerLabelName6;
    @FXML private Rectangle playerLabelVictory1,playerLabelVictory2,playerLabelVictory3,playerLabelVictory4,playerLabelVictory5,playerLabelVictory6;
    @FXML private Text playerLabelVictoryNum1,playerLabelVictoryNum2,playerLabelVictoryNum3,playerLabelVictoryNum4,playerLabelVictoryNum5,playerLabelVictoryNum6;
    private Rectangle[] playerLabels;
    private Text[] playerLabelNames;
    private Rectangle[] playerLabelVictories;
    private Text[] playerLabelVictoryNums;
    PlayerNamePointsDisplay playerNamePointsDisplay;

    @FXML private Rectangle zoomActionCard;

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
        cardObjectsInHand = new Card[cardsInHand.length];
        for(int i=0; i<cardsInHand.length; i++) {
            cardsInHand[i].setViewOrder(cardsInHand.length-i);
            cardsInHandNumBacks[i].setViewOrder(cardsInHand.length-i-0.1);
            cardsInHandNums[i].setViewOrder(cardsInHand.length-i-0.2);
        }
        playerHandDisplay = new HandOrInPlayDisplay(cardsInHand, cardsInHandNumBacks, cardsInHandNums, cardObjectsInHand,"playerHandDisplay");

        //------------------Initialize Cards In Play--------------//
        cardsInPlay = new Rectangle[]{cardInPlay1,cardInPlay2,cardInPlay3,cardInPlay4,cardInPlay5,cardInPlay6,
                cardInPlay7,cardInPlay8,cardInPlay9,cardInPlay10,cardInPlay11};
        cardsInPlayNumBacks = new Rectangle[]{cardInPlayNumBack1,cardInPlayNumBack2,cardInPlayNumBack3,cardInPlayNumBack4,cardInPlayNumBack5,
                cardInPlayNumBack6,cardInPlayNumBack7,cardInPlayNumBack8,cardInPlayNumBack9,cardInPlayNumBack10,cardInPlayNumBack11};
        cardsInPlayNums = new Text[]{cardInPlayNum1,cardInPlayNum2,cardInPlayNum3,cardInPlayNum4,cardInPlayNum5,cardInPlayNum6,
                cardInPlayNum7,cardInPlayNum8,cardInPlayNum9,cardInPlayNum10,cardInPlayNum11};
        cardObjectsInPlay = new Card[cardsInPlay.length];
        for(int i=0; i<cardsInPlay.length; i++) {
            cardsInPlay[i].setViewOrder(cardsInPlay.length-i);
            cardsInPlayNumBacks[i].setViewOrder(cardsInPlay.length-i-0.1);
            cardsInPlayNums[i].setViewOrder(cardsInPlay.length-i-0.2);
        }
        inPlayDisplay = new HandOrInPlayDisplay(cardsInPlay,cardsInPlayNumBacks,cardsInPlayNums,cardObjectsInPlay,"inPlayDisplay");

        //--------------Initialize all CardsInSupply-----------------//
        cardsInSupply = new Rectangle[]{treasureCardInSupply1,treasureCardInSupply2,treasureCardInSupply3,treasureCardInSupply4,victoryCardInSupply1,
                victoryCardInSupply2, victoryCardInSupply3,victoryCardInSupply4,extraCardInSupply1,extraCardInSupply2,extraCardInSupply3,extraCardInSupply4,
                actionCardInSupply1,actionCardInSupply2,actionCardInSupply3,actionCardInSupply4,actionCardInSupply5,
                actionCardInSupply6,actionCardInSupply7,actionCardInSupply8,actionCardInSupply9,actionCardInSupply10};
        cardsInSupplyNumBacks = new Rectangle[]{treasureCardNumBack1,treasureCardNumBack2,treasureCardNumBack3,treasureCardNumBack4, victoryCardNumBack1,
                victoryCardNumBack2,victoryCardNumBack3,victoryCardNumBack4,extraCardNumBack1,extraCardNumBack2,extraCardNumBack3,extraCardNumBack4,
                actionCardNumBack1,actionCardNumBack2,actionCardNumBack3,actionCardNumBack4,actionCardNumBack5,
                actionCardNumBack6, actionCardNumBack7,actionCardNumBack8,actionCardNumBack9,actionCardNumBack10};
        cardsInSupplyNums = new Text[]{treasureCardNum1,treasureCardNum2,treasureCardNum3,treasureCardNum4, victoryCardNum1,victoryCardNum2,
                victoryCardNum3,victoryCardNum4,extraCardNum1,extraCardNum2,extraCardNum3,extraCardNum4,
                actionCardNum1,actionCardNum2,actionCardNum3,actionCardNum4,actionCardNum5,
                actionCardNum6,actionCardNum7,actionCardNum8,actionCardNum9,actionCardNum10};
        cardsInSupplyBuyButtons = new Rectangle[]{treasureCardBuyButton1,treasureCardBuyButton2,treasureCardBuyButton3,treasureCardBuyButton4, victoryCardBuyButton1,
                victoryCardBuyButton2,victoryCardBuyButton3,victoryCardBuyButton4,extraCardBuyButton1,extraCardBuyButton2,extraCardBuyButton3,extraCardBuyButton4,
                actionCardBuyButton1,actionCardBuyButton2,actionCardBuyButton3,actionCardBuyButton4,actionCardBuyButton5,
                actionCardBuyButton6,actionCardBuyButton7,actionCardBuyButton8,actionCardBuyButton9,actionCardBuyButton10};
        cardObjectsInSupply = new Card[cardsInSupply.length];
        cardSupplyDisplay = new CardSupplyDisplay(cardsInSupply,cardsInSupplyNumBacks,cardsInSupplyNums,
                cardsInSupplyBuyButtons,cardObjectsInSupply);

        //--------------Initialize action cards in supply---------------//
        actionCardsInSupply = new Rectangle[]{actionCardInSupply1,actionCardInSupply2,actionCardInSupply3,actionCardInSupply4,actionCardInSupply5,
                actionCardInSupply6,actionCardInSupply7,actionCardInSupply8,actionCardInSupply9,actionCardInSupply10};
        actionCardNumBacks = new Rectangle[]{actionCardNumBack1,actionCardNumBack2,actionCardNumBack3,actionCardNumBack4,actionCardNumBack5,
                actionCardNumBack6, actionCardNumBack7,actionCardNumBack8,actionCardNumBack9,actionCardNumBack10};
        actionCardBuyButtons = new Rectangle[]{actionCardBuyButton1,actionCardBuyButton2,actionCardBuyButton3,actionCardBuyButton4,actionCardBuyButton5,
                actionCardBuyButton6,actionCardBuyButton7,actionCardBuyButton8,actionCardBuyButton9,actionCardBuyButton10};
        actionCardNums = new Text[]{actionCardNum1,actionCardNum2,actionCardNum3,actionCardNum4,actionCardNum5,
                actionCardNum6,actionCardNum7,actionCardNum8,actionCardNum9,actionCardNum10};

        //--------------Initialize player Decks-----------------//
        ImagePattern imagePattern = new ImagePattern(new Image(new File("src/resources/BackViewCard.png").toURI().toString()));
        playerDeck.setFill(imagePattern);
        opponentDeck.setFill(imagePattern);
        playerDeck.setVisible(true);
        opponentDeck.setVisible(true);

        //--------------Initialize Player labels----------------//
        playerLabels = new Rectangle[]{playerLabel1, playerLabel2,playerLabel3,playerLabel4,playerLabel5,playerLabel6};
        playerLabelNames = new Text[]{playerLabelName1,playerLabelName2,playerLabelName3,playerLabelName4,playerLabelName5,playerLabelName6};
        playerLabelVictories = new Rectangle[]{playerLabelVictory1,playerLabelVictory2,playerLabelVictory3,playerLabelVictory4,playerLabelVictory5,playerLabelVictory6};
        playerLabelVictoryNums = new Text[]{playerLabelVictoryNum1,playerLabelVictoryNum2,playerLabelVictoryNum3,playerLabelVictoryNum4,playerLabelVictoryNum5,playerLabelVictoryNum6};
        imagePattern = new ImagePattern(new Image(new File("src/resources/Victory_Symbol.png").toURI().toString()));
        for(Rectangle victoryImageSlot: playerLabelVictories) {
            victoryImageSlot.setFill(imagePattern);
        }
        playerNamePointsDisplay = new PlayerNamePointsDisplay(playerLabels,playerLabelVictories,playerLabelNames,playerLabelVictoryNums);

        //-----------------ActionBar-------------------//
        actionBar.setVisible(false);

    }

    //-------------Getters----------------//

    public Button getActionButton() { return actionButton; }
    public Text getGameInfoText() {return gameInfoText;}
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
    public HandOrInPlayDisplay getPlayerHandDisplay() {return playerHandDisplay;}
    public HandOrInPlayDisplay getInPlayDisplay() {return inPlayDisplay;}
    public CardSupplyDisplay getCardSupplyDisplay() { return cardSupplyDisplay;}
    public PlayerNamePointsDisplay getPlayerNamePointsDisplay() {return playerNamePointsDisplay;}
    public String getGreenCardGlowStyle() {return greenCardGlowStyle;}
    public StackPane getActionBar() {return actionBar;}

    //-------------------Internal Updates------------------------//

    public void chatSend(ActionEvent actionEvent) {
        if(chatType.getText()==null) return;
        String chatText = chatType.getText();
        PlayerActionMediator.addMessageToChatLog(Main.getPlayer().getName() + ": " + chatText);
        ServerSender.chatSend(chatText);
    }

    public void cardInHandClicked(MouseEvent mouseEvent) {
        Rectangle cardClicked = (Rectangle) mouseEvent.getSource();
        for(int i=0; i<cardsInHand.length; i++) {
            if(cardsInHand[i].equals(cardClicked)) {

                //------------Temporary Fix---------------//
                if(cardClicked.getStyle().equals(greenCardGlowStyle)) {
                    PlayerActionMediator.playCard(playerHandDisplay.getCardObjectsInHandOrInPlay()[i]);
                }
                //----------------------------------------//
//                System.out.println(cardsInHand[i].toString() + " was clicked");
            }
        }

    }

    public void mouseOverCardInHand(MouseEvent mouseEvent) {
        mouseOverCard(mouseEvent, cardsInHand, cardsInHandNumBacks, cardsInHandNums,true);
    }
    public void mouseExitedCardInHand(MouseEvent mouseEvent) {
        mouseOverCard(mouseEvent, cardsInHand, cardsInHandNumBacks, cardsInHandNums,false);
    }
    public void mouseOverCardInPlay(MouseEvent mouseEvent) {
        mouseOverCard(mouseEvent, cardsInPlay, cardsInPlayNumBacks, cardsInPlayNums,true);
    }
    public void mouseExitedCardInPlay(MouseEvent mouseEvent) {
        mouseOverCard(mouseEvent, cardsInPlay, cardsInPlayNumBacks, cardsInPlayNums,false);
    }
    public void mouseOverActionCardInSupply(MouseEvent mouseEvent) {
        mouseOverCard(mouseEvent,actionCardsInSupply,actionCardNumBacks,actionCardNums,true);
    }
    public void mouseExitedActionCardInSupply(MouseEvent mouseEvent) {
        mouseOverCard(mouseEvent,actionCardsInSupply,actionCardNumBacks,actionCardNums,false);
    }

    private void mouseOverCard(MouseEvent mouseEvent, Rectangle[] cards, Rectangle[] cardNumBacks, Text[] cardNums, boolean entered) {
        Object cardClicked = mouseEvent.getSource();
        for(int i = 0; i< cards.length; i++) {
            if(cards[i].equals(cardClicked) || cardNumBacks[i].equals(cardClicked) || cardNums[i].equals(cardClicked)) {
                if(cards.equals(actionCardsInSupply)) {
                    showZoomActionCard(i+12,entered);
                } else
                setViewingOrder(cards[i],cardNumBacks[i],cardNums[i],cards.length,i,entered);
                break;
            }
        }
    }
    private void setViewingOrder(Rectangle card, Rectangle cardNumBack, Text cardNum, int cardsLength, int i, boolean entered) {
        if(entered) {
            card.setViewOrder(0.2);
            cardNumBack.setViewOrder(0.1);
            cardNum.setViewOrder(0);
        } else {
            card.setViewOrder(cardsLength-i);
            cardNumBack.setViewOrder(cardsLength-i-0.1);
            cardNum.setViewOrder(cardsLength-i-0.2);
        }
    }
    private void showZoomActionCard(int i, boolean entered) {
        if(entered) {
            zoomActionCard.setFill(new ImagePattern(cardObjectsInSupply[i].getCardImage()));
            zoomActionCard.setStyle(greenCardGlowStyle);
            zoomActionCard.setVisible(true);
        } else {
            zoomActionCard.setVisible(false);
        }
    }

    public void actionButtonClicked(ActionEvent actionEvent) {
        switch (actionButton.getText()) {
            case "Start Turn" -> PlayerActionMediator.startPhase();
            case "Enter Buy Phase" -> PlayerActionMediator.buyPhase();
            case "End Turn" -> PlayerActionMediator.endPhase();
        }

    }
    public void buyButtonClicked(MouseEvent mouseEvent) {
        Rectangle buyButtonClicked = (Rectangle) mouseEvent.getSource();
        for(int i=0; i< cardsInSupplyBuyButtons.length; i++) {
            if(cardsInSupplyBuyButtons[i].equals(buyButtonClicked)) {
                Card cardClicked = cardObjectsInSupply[i];
                PlayerActionMediator.buyFromCardSupply(cardClicked);
            }
        }
    }

    public void setCardsInGame(List<String> cardsInGame) {
        this.cardsInGame = new CardCollection();
        for(String cardName: cardsInGame) {
            this.cardsInGame.addCardToCollection(CardFactory.getCard(cardName));
        }
    }
    public void setCardStacks(List<CardStack> cardStacks) {
        this.cardStacks = cardStacks;
    }

    public void displayCardsInGame() {
        int index = 3;

        List<TreasureCard> treasureCardsInGame = cardsInGame.getDistinctTreasureCards();
        List<VictoryCard> victoryCardsInGame = cardsInGame.getDistinctVictoryCards();
        List<ActionCard> actionCardsInGame = cardsInGame.getDistinctActionCards();

        for(TreasureCard card: treasureCardsInGame) {
            cardObjectsInSupply[index] = card;
            cardsInSupply[index].setFill(new ImagePattern(card.getSmallCardImage()));
            cardsInSupplyBuyButtons[index].setFill(new ImagePattern(new Image(new File("src/resources/Plus Sign.png").toURI().toString())));
            cardsInSupply[index].setVisible(true);
            index--;
        }
        index = 7;
        for(VictoryCard card: victoryCardsInGame) {
            cardObjectsInSupply[index] = card;
            cardsInSupply[index].setFill(new ImagePattern(card.getSmallCardImage()));
            cardsInSupplyBuyButtons[index].setFill(new ImagePattern(new Image(new File("src/resources/Plus Sign.png").toURI().toString())));
            cardsInSupply[index].setVisible(true);
            index--;
        }
        index = 12;
        for(ActionCard card: actionCardsInGame) {
            cardObjectsInSupply[index] = card;
            cardsInSupply[index].setFill(new ImagePattern(card.getSmallCardImage()));
            cardsInSupplyBuyButtons[index].setFill(new ImagePattern(new Image(new File("src/resources/Plus Sign.png").toURI().toString())));
            cardsInSupply[index].setVisible(true);
            index++;
        }
    }
    public void displayCardsInGameNums() {
        for(CardStack cardStack: cardStacks) {
            Card card = cardStack.getCard();
            for(int i=0; i<cardObjectsInSupply.length; i++) {
                if(cardObjectsInSupply[i]==null) continue;
                if(cardObjectsInSupply[i].equals(card)) {
                    cardsInSupplyNumBacks[i].setVisible(true);
                    cardsInSupplyNums[i].setText(String.valueOf(cardStack.getNumCards()));
                    cardsInSupplyNums[i].setVisible(true);
                    break;
                }
            }
        }
    }
}
