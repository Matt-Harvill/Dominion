package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.CardCollection;
import model.card.*;
import model.factory.CardFactory;
import view.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameController {

    //-----------------Info for ServerInfoDisplay------------//
    @FXML private Text hostIP, portNum;
    @FXML Rectangle serverInfoBackground;
    @FXML Pane serverInfoPane;
    @FXML Button startGameButton;
    private String ipAddress; private int port;

    //--------------Player inPlay Label-----------------//
    @FXML private Rectangle inPlayPlayerLabelBack;
    @FXML private Text inPlayPlayerLabelText;
    private LabelDisplay inPlayPlayerLabel;

    //-------------List of Cards in the Game------------//
    private CardCollection cardsInGame;
    private List<CardStack> cardStacks;

    //----------------CSS Styles for Cards---------------//
    private final String greenCardGlowStyle = "-fx-stroke-width: 3; -fx-stroke: #54ff54;";

    //---------------Chat Log and Game Log--------------//
    @FXML private TextArea chatLog, gameLog;
    @FXML private TextField chatType;
    private List<String> chatDisplayStrings, gameDisplayStrings;

    //---------------Cards In Hand---------------//
    @FXML private Rectangle cardInHand1,cardInHand2,cardInHand3,cardInHand4,cardInHand5,cardInHand6,cardInHand7,cardInHand8,cardInHand9,cardInHand10,cardInHand11;
    @FXML private Rectangle cardInHandNumBack1,cardInHandNumBack2,cardInHandNumBack3,cardInHandNumBack4,cardInHandNumBack5,cardInHandNumBack6,
            cardInHandNumBack7,cardInHandNumBack8,cardInHandNumBack9,cardInHandNumBack10,cardInHandNumBack11;
    @FXML private Text cardInHandNum1,cardInHandNum2,cardInHandNum3,cardInHandNum4,cardInHandNum5,cardInHandNum6,cardInHandNum7,
            cardInHandNum8,cardInHandNum9,cardInHandNum10,cardInHandNum11;
    private CardDisplay CIHDisplay1,CIHDisplay2,CIHDisplay3,CIHDisplay4,CIHDisplay5,CIHDisplay6,CIHDisplay7,CIHDisplay8,CIHDisplay9,CIHDisplay10,CIHDisplay11;
    private CardDisplay[] CIHDisplays;

    //---------------ActionCards In Supply---------------//
    @FXML private Rectangle actionCardInSupply1,actionCardInSupply2,actionCardInSupply3,actionCardInSupply4,actionCardInSupply5,
            actionCardInSupply6,actionCardInSupply7,actionCardInSupply8,actionCardInSupply9,actionCardInSupply10;
    @FXML private Rectangle actionCardNumBack1,actionCardNumBack2,actionCardNumBack3,actionCardNumBack4,actionCardNumBack5,actionCardNumBack6,
            actionCardNumBack7,actionCardNumBack8,actionCardNumBack9,actionCardNumBack10;
    @FXML private Text actionCardNum1,actionCardNum2,actionCardNum3,actionCardNum4,actionCardNum5,
            actionCardNum6,actionCardNum7,actionCardNum8,actionCardNum9,actionCardNum10;
    @FXML private Rectangle actionCardBuyButton1,actionCardBuyButton2,actionCardBuyButton3,actionCardBuyButton4,actionCardBuyButton5,
            actionCardBuyButton6,actionCardBuyButton7,actionCardBuyButton8,actionCardBuyButton9,actionCardBuyButton10;
    private BuyableCardDisplay ACISDisplay1,ACISDisplay2,ACISDisplay3,ACISDisplay4,ACISDisplay5,ACISDisplay6, ACISDisplay7,ACISDisplay8,ACISDisplay9,ACISDisplay10;
    private BuyableCardDisplay[] ACISDisplays;

    //---------------TreasureCards In Supply---------------//
    @ FXML private Rectangle treasureCardInSupply1,treasureCardInSupply2,treasureCardInSupply3,treasureCardInSupply4;
    @FXML private Rectangle treasureCardNumBack1,treasureCardNumBack2,treasureCardNumBack3,treasureCardNumBack4;
    @FXML private Text treasureCardNum1,treasureCardNum2,treasureCardNum3,treasureCardNum4;
    @FXML private Rectangle treasureCardBuyButton1,treasureCardBuyButton2,treasureCardBuyButton3,treasureCardBuyButton4;
    private BuyableCardDisplay TCISDisplay1,TCISDisplay2,TCISDisplay3,TCISDisplay4;
    private BuyableCardDisplay[] TCISDisplays;

    //---------------VictoryCards In Supply---------------//
    @ FXML private Rectangle victoryCardInSupply1,victoryCardInSupply2,victoryCardInSupply3,victoryCardInSupply4;
    @FXML private Rectangle victoryCardNumBack1,victoryCardNumBack2,victoryCardNumBack3,victoryCardNumBack4;
    @FXML private Text victoryCardNum1,victoryCardNum2,victoryCardNum3,victoryCardNum4;
    @FXML private Rectangle victoryCardBuyButton1,victoryCardBuyButton2,victoryCardBuyButton3,victoryCardBuyButton4;
    private BuyableCardDisplay VCISDisplay1,VCISDisplay2,VCISDisplay3,VCISDisplay4;
    private BuyableCardDisplay[] VCISDisplays;

    //---------------Extra Cards In Supply---------------//
    @ FXML private Rectangle extraCardInSupply1,extraCardInSupply2,extraCardInSupply3,extraCardInSupply4;
    @FXML private Rectangle extraCardNumBack1,extraCardNumBack2,extraCardNumBack3,extraCardNumBack4;
    @FXML private Text extraCardNum1,extraCardNum2,extraCardNum3,extraCardNum4;
    @FXML private Rectangle extraCardBuyButton1,extraCardBuyButton2,extraCardBuyButton3,extraCardBuyButton4;
    private BuyableCardDisplay ECISDisplay1,ECISDisplay2,ECISDisplay3,ECISDisplay4;
    private BuyableCardDisplay[] ECISDisplays;

    //-----------------All Cards In Supply-----------------//
    private List<BuyableCardDisplay> allCISDisplays;

    //---------------Cards in Play---------------//
    @FXML private Rectangle cardInPlay1,cardInPlay2,cardInPlay3,cardInPlay4,cardInPlay5,cardInPlay6,
            cardInPlay7,cardInPlay8,cardInPlay9,cardInPlay10,cardInPlay11;
    @FXML private Rectangle cardInPlayNumBack1,cardInPlayNumBack2,cardInPlayNumBack3,cardInPlayNumBack4,cardInPlayNumBack5,
            cardInPlayNumBack6,cardInPlayNumBack7,cardInPlayNumBack8,cardInPlayNumBack9,cardInPlayNumBack10,cardInPlayNumBack11;
    @FXML private Text cardInPlayNum1,cardInPlayNum2,cardInPlayNum3,cardInPlayNum4,cardInPlayNum5,cardInPlayNum6,
            cardInPlayNum7,cardInPlayNum8,cardInPlayNum9,cardInPlayNum10,cardInPlayNum11;
    private CardDisplay CIPDisplay1,CIPDisplay2,CIPDisplay3,CIPDisplay4,CIPDisplay5,CIPDisplay6,CIPDisplay7,CIPDisplay8,CIPDisplay9,CIPDisplay10,CIPDisplay11;
    private CardDisplay[] CIPDisplays;

    //---------------ActionBar and gameInfoText------------------//
    @FXML private Button actionButton;
    @FXML private Text gameInfoText;
    @FXML private StackPane actionBar;

    //---------------discardPile, playerDeck, opponentDeck Labels------------//
    @FXML private Rectangle discardPileLabel, opponentDeckLabel, playerDeckLabel;
    @FXML private Text discardPileLabelText, opponentDeckLabelText, playerDeckLabelText;

    //---------------discardPile, playerDeck, opponentDeck-------------------//
    @FXML private Rectangle playerDiscard, playerDeck, opponentDeck, playerDiscardNumBack, playerDeckNumBack, opponentDeckNumBack;
    @FXML private Text playerDiscardNum, playerDeckNum, opponentDeckNum;
    private DeckDisplay playerDeckDisplay, opponentDeckDisplay, playerDiscardDisplay;

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

    public void initialize() {
        chatDisplayStrings = new ArrayList<>();
        gameDisplayStrings = new ArrayList<>();
        chatType.setText(null);

        //---------------Set ServerInfoPane to notVisible----------//
        serverInfoPane.setVisible(false);

        //--------------Initialize order of CardsInHand-----------------//
        CIHDisplay1 = new CardDisplay(cardInHand1,new NumberDisplay(cardInHandNumBack1,cardInHandNum1));
        CIHDisplay2 = new CardDisplay(cardInHand2,new NumberDisplay(cardInHandNumBack2,cardInHandNum2));
        CIHDisplay3 = new CardDisplay(cardInHand3,new NumberDisplay(cardInHandNumBack3,cardInHandNum3));
        CIHDisplay4 = new CardDisplay(cardInHand4,new NumberDisplay(cardInHandNumBack4,cardInHandNum4));
        CIHDisplay5 = new CardDisplay(cardInHand5,new NumberDisplay(cardInHandNumBack5,cardInHandNum5));
        CIHDisplay6 = new CardDisplay(cardInHand6,new NumberDisplay(cardInHandNumBack6,cardInHandNum6));
        CIHDisplay7 = new CardDisplay(cardInHand7,new NumberDisplay(cardInHandNumBack7,cardInHandNum7));
        CIHDisplay8 = new CardDisplay(cardInHand8,new NumberDisplay(cardInHandNumBack8,cardInHandNum8));
        CIHDisplay9 = new CardDisplay(cardInHand9,new NumberDisplay(cardInHandNumBack9,cardInHandNum9));
        CIHDisplay10 = new CardDisplay(cardInHand10,new NumberDisplay(cardInHandNumBack10,cardInHandNum10));
        CIHDisplay11 = new CardDisplay(cardInHand11,new NumberDisplay(cardInHandNumBack11,cardInHandNum11));
        CIHDisplays = new CardDisplay[]{CIHDisplay1,CIHDisplay2,CIHDisplay3,CIHDisplay4,CIHDisplay5,CIHDisplay6,CIHDisplay7,CIHDisplay8,CIHDisplay9,CIHDisplay10,CIHDisplay11};

        setInitialViewOrder(CIHDisplays);


        //------------------Initialize Cards In Play--------------//
        CIPDisplay1 = new CardDisplay(cardInPlay1,new NumberDisplay(cardInPlayNumBack1,cardInPlayNum1));
        CIPDisplay2 = new CardDisplay(cardInPlay2,new NumberDisplay(cardInPlayNumBack2,cardInPlayNum2));
        CIPDisplay3 = new CardDisplay(cardInPlay3,new NumberDisplay(cardInPlayNumBack3,cardInPlayNum3));
        CIPDisplay4 = new CardDisplay(cardInPlay4,new NumberDisplay(cardInPlayNumBack4,cardInPlayNum4));
        CIPDisplay5 = new CardDisplay(cardInPlay5,new NumberDisplay(cardInPlayNumBack5,cardInPlayNum5));
        CIPDisplay6 = new CardDisplay(cardInPlay6,new NumberDisplay(cardInPlayNumBack6,cardInPlayNum6));
        CIPDisplay7 = new CardDisplay(cardInPlay7,new NumberDisplay(cardInPlayNumBack7,cardInPlayNum7));
        CIPDisplay8 = new CardDisplay(cardInPlay8,new NumberDisplay(cardInPlayNumBack8,cardInPlayNum8));
        CIPDisplay9 = new CardDisplay(cardInPlay9,new NumberDisplay(cardInPlayNumBack9,cardInPlayNum9));
        CIPDisplay10 = new CardDisplay(cardInPlay10,new NumberDisplay(cardInPlayNumBack10,cardInPlayNum10));
        CIPDisplay11 = new CardDisplay(cardInPlay11,new NumberDisplay(cardInPlayNumBack11,cardInPlayNum11));
        CIPDisplays = new CardDisplay[]{CIPDisplay1,CIPDisplay2,CIPDisplay3,CIPDisplay4,CIPDisplay5,CIPDisplay6,CIPDisplay7,CIPDisplay8,CIPDisplay9,CIPDisplay10,CIPDisplay11};

        setInitialViewOrder(CIPDisplays);

        inPlayPlayerLabel = new LabelDisplay(inPlayPlayerLabelBack,inPlayPlayerLabelText);

        //--------------Initialize Cards in Supply---------------//
        ACISDisplay1 = new BuyableCardDisplay(actionCardInSupply1,new NumberDisplay(actionCardNumBack1,actionCardNum1),actionCardBuyButton1);
        ACISDisplay2 = new BuyableCardDisplay(actionCardInSupply2,new NumberDisplay(actionCardNumBack2,actionCardNum2),actionCardBuyButton2);
        ACISDisplay3 = new BuyableCardDisplay(actionCardInSupply3,new NumberDisplay(actionCardNumBack3,actionCardNum3),actionCardBuyButton3);
        ACISDisplay4 = new BuyableCardDisplay(actionCardInSupply4,new NumberDisplay(actionCardNumBack4,actionCardNum4),actionCardBuyButton4);
        ACISDisplay5 = new BuyableCardDisplay(actionCardInSupply5,new NumberDisplay(actionCardNumBack5,actionCardNum5),actionCardBuyButton5);
        ACISDisplay6 = new BuyableCardDisplay(actionCardInSupply6,new NumberDisplay(actionCardNumBack6,actionCardNum6),actionCardBuyButton6);
        ACISDisplay7 = new BuyableCardDisplay(actionCardInSupply7,new NumberDisplay(actionCardNumBack7,actionCardNum7),actionCardBuyButton7);
        ACISDisplay8 = new BuyableCardDisplay(actionCardInSupply8,new NumberDisplay(actionCardNumBack8,actionCardNum8),actionCardBuyButton8);
        ACISDisplay9 = new BuyableCardDisplay(actionCardInSupply9,new NumberDisplay(actionCardNumBack9,actionCardNum9),actionCardBuyButton9);
        ACISDisplay10 = new BuyableCardDisplay(actionCardInSupply10,new NumberDisplay(actionCardNumBack10,actionCardNum10),actionCardBuyButton10);

        TCISDisplay1 = new BuyableCardDisplay(treasureCardInSupply1,new NumberDisplay(treasureCardNumBack1,treasureCardNum1),treasureCardBuyButton1);
        TCISDisplay2 = new BuyableCardDisplay(treasureCardInSupply2,new NumberDisplay(treasureCardNumBack2,treasureCardNum2),treasureCardBuyButton2);
        TCISDisplay3 = new BuyableCardDisplay(treasureCardInSupply3,new NumberDisplay(treasureCardNumBack3,treasureCardNum3),treasureCardBuyButton3);
        TCISDisplay4 = new BuyableCardDisplay(treasureCardInSupply4,new NumberDisplay(treasureCardNumBack4,treasureCardNum4),treasureCardBuyButton4);

        VCISDisplay1 = new BuyableCardDisplay(victoryCardInSupply1,new NumberDisplay(victoryCardNumBack1,victoryCardNum1),victoryCardBuyButton1);
        VCISDisplay2 = new BuyableCardDisplay(victoryCardInSupply2,new NumberDisplay(victoryCardNumBack2,victoryCardNum2),victoryCardBuyButton2);
        VCISDisplay3 = new BuyableCardDisplay(victoryCardInSupply3,new NumberDisplay(victoryCardNumBack3,victoryCardNum3),victoryCardBuyButton3);
        VCISDisplay4 = new BuyableCardDisplay(victoryCardInSupply4,new NumberDisplay(victoryCardNumBack4,victoryCardNum4),victoryCardBuyButton4);

        ECISDisplay1 = new BuyableCardDisplay(extraCardInSupply1,new NumberDisplay(extraCardNumBack1,extraCardNum1),extraCardBuyButton1);
        ECISDisplay2 = new BuyableCardDisplay(extraCardInSupply2,new NumberDisplay(extraCardNumBack2,extraCardNum2),extraCardBuyButton2);
        ECISDisplay3 = new BuyableCardDisplay(extraCardInSupply3,new NumberDisplay(extraCardNumBack3,extraCardNum3),extraCardBuyButton3);
        ECISDisplay4 = new BuyableCardDisplay(extraCardInSupply4,new NumberDisplay(extraCardNumBack4,extraCardNum4),extraCardBuyButton4);

        ACISDisplays = new BuyableCardDisplay[]{ACISDisplay1,ACISDisplay2,ACISDisplay3,ACISDisplay4,ACISDisplay5,ACISDisplay6,ACISDisplay7,ACISDisplay8,ACISDisplay9,ACISDisplay10};
        TCISDisplays = new BuyableCardDisplay[]{TCISDisplay1,TCISDisplay2,TCISDisplay3,TCISDisplay4};
        VCISDisplays = new BuyableCardDisplay[]{VCISDisplay1,VCISDisplay2,VCISDisplay3,VCISDisplay4};
        ECISDisplays = new BuyableCardDisplay[]{ECISDisplay1,ECISDisplay2,ECISDisplay3,ECISDisplay4};

        initializeCardsInSupply(ACISDisplays);
        initializeCardsInSupply(TCISDisplays);
        initializeCardsInSupply(VCISDisplays);
        initializeCardsInSupply(ECISDisplays);

        allCISDisplays = new ArrayList<>();
        Collections.addAll(allCISDisplays, ACISDisplays);
        Collections.addAll(allCISDisplays, TCISDisplays);
        Collections.addAll(allCISDisplays, VCISDisplays);
        Collections.addAll(allCISDisplays, ECISDisplays);

        //--------------Initialize player Decks and Discard-----------------//
        ImagePattern imagePattern = new ImagePattern(new Image(new File("src/resources/BackViewCard.png").toURI().toString()));
        playerDeck.setFill(imagePattern);
        opponentDeck.setFill(imagePattern);
        playerDeckDisplay = new DeckDisplay(new CardDisplay(playerDeck,new NumberDisplay(playerDeckNumBack,playerDeckNum)),new LabelDisplay(playerDeckLabel,playerDeckLabelText));
        opponentDeckDisplay = new DeckDisplay(new CardDisplay(opponentDeck,new NumberDisplay(opponentDeckNumBack,opponentDeckNum)),new LabelDisplay(opponentDeckLabel,opponentDeckLabelText));
        playerDiscardDisplay = new DeckDisplay(new CardDisplay(playerDiscard,new NumberDisplay(playerDiscardNumBack,playerDiscardNum)),new LabelDisplay(discardPileLabel,discardPileLabelText));

        playerDeckDisplay.hide();
        opponentDeckDisplay.hide();
        playerDiscardDisplay.hide();

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

        actionButton.setVisible(false);

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
    public PlayerNamePointsDisplay getPlayerNamePointsDisplay() {return playerNamePointsDisplay;}
    public String getGreenCardGlowStyle() {return greenCardGlowStyle;}
    public LabelDisplay getInPlayPlayerLabel() {return inPlayPlayerLabel;}
    public DeckDisplay getPlayerDeckDisplay() {return playerDeckDisplay;}
    public DeckDisplay getOpponentDeckDisplay() {return opponentDeckDisplay;}
    public DeckDisplay getPlayerDiscardDisplay() {return playerDiscardDisplay;}

    public List<BuyableCardDisplay> getAllCISDisplays() {return allCISDisplays;}
    public CardDisplay[] getCIHDisplays() {return CIHDisplays;}
    public CardDisplay[] getCIPDisplays() {return CIPDisplays;}

    //-------------------Internal Updates------------------------//

    public void chatSend(ActionEvent actionEvent) {
        if(chatType.getText()==null) return;
        String chatText = chatType.getText();
        DisplayUpdater.addMsgToChatLog(Main.getPlayer().getName() + ": " + chatText);
        ServerSender.chatSend(chatText);
    }

    public void cardInHandClicked(MouseEvent mouseEvent) {
        Rectangle cardClicked = (Rectangle) mouseEvent.getSource();
        for(CardDisplay cardDisplay: CIHDisplays) {
            if(cardDisplay.contains(cardClicked)) {
                if(cardClicked.getStyle().equals(greenCardGlowStyle)) {
                    ActionHandler.playCard(cardDisplay.getCard());
                }
            }
        }
    }

    public void mouseOverCardInHand(MouseEvent mouseEvent) {
        bringToFront(mouseEvent, CIHDisplays, true);
    }
    public void mouseExitedCardInHand(MouseEvent mouseEvent) {
        bringToFront(mouseEvent, CIHDisplays, false);
    }
    public void mouseOverCardInPlay(MouseEvent mouseEvent) {
        bringToFront(mouseEvent,CIPDisplays,true);
    }
    public void mouseExitedCardInPlay(MouseEvent mouseEvent) {
        bringToFront(mouseEvent,CIPDisplays,false);
    }
    public void mouseOverActionCardInSupply(MouseEvent mouseEvent) {
        Object objectClicked = mouseEvent.getSource();
        for(BuyableCardDisplay cardDisplay: ACISDisplays) {
            if(cardDisplay.contains(objectClicked)) {
                showZoomActionCard(cardDisplay.getCard(),true);
            }
        }
    }
    public void mouseExitedActionCardInSupply(MouseEvent mouseEvent) {
        Object objectClicked = mouseEvent.getSource();
        for(BuyableCardDisplay cardDisplay: ACISDisplays) {
            if(cardDisplay.contains(objectClicked)) {
                showZoomActionCard(cardDisplay.getCard(),false);
            }
        }
    }

    private void bringToFront(MouseEvent mouseEvent, CardDisplay[] cardDisplays, boolean entered) {
        Object cardClicked = mouseEvent.getSource();
        for(int i = 0; i< cardDisplays.length;i++) {
            if(cardDisplays[i].contains(cardClicked)) {
                if(entered) {
                    cardDisplays[i].setViewOrder(0.2);
                } else {
                    cardDisplays[i].setViewOrder(cardDisplays.length-i);
                }
                break;
            }
        }
    }
    private void showZoomActionCard(Card card, boolean entered) {
        if(entered) {
            zoomActionCard.setFill(new ImagePattern(card.getCardImage()));
            zoomActionCard.setVisible(true);
        } else {
            zoomActionCard.setVisible(false);
        }
    }

    public void actionButtonClicked(ActionEvent actionEvent) {
        switch (actionButton.getText()) {
            case "Start Turn" -> ActionHandler.startPhase();
            case "Enter Buy Phase" -> ActionHandler.buyPhase();
            case "End Turn" -> ActionHandler.endPhase();
        }

    }
    public void buyButtonClicked(MouseEvent mouseEvent) {
        Object buyButtonClicked = mouseEvent.getSource();
        for(BuyableCardDisplay cardDisplay: allCISDisplays) {
            if(cardDisplay.contains(buyButtonClicked)) {
                ActionHandler.buyCard(cardDisplay.getCard());
            }
        }
        System.out.println("buyButtonClicked");
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
        List<TreasureCard> treasureCardsInGame = cardsInGame.getDistinctTreasureCards();
        List<VictoryCard> victoryCardsInGame = cardsInGame.getDistinctVictoryCards();
        List<ActionCard> actionCardsInGame = cardsInGame.getDistinctActionCards();

        for(int i=0; i<treasureCardsInGame.size(); i++) {
            TreasureCard card = treasureCardsInGame.get(i);
            BuyableCardDisplay cardDisplay = TCISDisplays[i];
            cardDisplay.setCard(card);
            cardDisplay.show();
        }
        for(int i=0; i<victoryCardsInGame.size(); i++) {
            VictoryCard card = victoryCardsInGame.get(i);
            BuyableCardDisplay cardDisplay = VCISDisplays[i];
            cardDisplay.setCard(card);
            cardDisplay.show();
        }
        for(int i=0; i<actionCardsInGame.size(); i++) {
            ActionCard card = actionCardsInGame.get(i);
            BuyableCardDisplay cardDisplay = ACISDisplays[i];
            cardDisplay.setCard(card);
            cardDisplay.show();
        }
    }
    public void displayCardsInGameNums() {
        for(CardStack cardStack: cardStacks) {
            Card card = cardStack.getCard();
            for(BuyableCardDisplay cardDisplay: allCISDisplays) {
                Card cardInSupply = cardDisplay.getCard();
                if(cardInSupply!=null && cardInSupply.equals(card)) {
                    cardDisplay.setNum(cardStack.getNumCards());
                    cardDisplay.show();
                    break;
                }
            }
        }
    }

    public void initializeServerInfoDisplay() {
        ipAddress = Main.getServer().getIpAddress();
        hostIP.setText(ipAddress);
        port = Main.getServer().getPortNumber();
        portNum.setText(String.valueOf(port));
        serverInfoBackground.setViewOrder(Integer.MAX_VALUE);
        serverInfoPane.setVisible(true);
    }
    public void hostIPCopyToClipboard(ActionEvent actionEvent) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(hostIP.getText());
        clipboard.setContent(content);
    }
    public void portNumCopyToClipboard(ActionEvent actionEvent) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(portNum.getText());
        clipboard.setContent(content);
    }
    public void startGame(ActionEvent actionEvent) {
        Main.getServer().startGame();
    }

    public void hideServerInfoPane() {
        serverInfoPane.setVisible(false);
    }

    private void initializeCardsInSupply(BuyableCardDisplay[] cardDisplays) {
        for(BuyableCardDisplay cardDisplay: cardDisplays) {
            cardDisplay.setBuyButtonImage(new Image(new File("src/resources/Plus Sign.png").toURI().toString()));
            cardDisplay.hide();
        }
    }
    private void setInitialViewOrder(CardDisplay[] cardDisplays) {
        for(int i=0; i < cardDisplays.length; i++) {
            cardDisplays[i].setViewOrder(cardDisplays.length-i);
        }
    }
}
