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
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.CardCollection;
import model.card.*;
import model.factory.CardFactory;
import view.*;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameController {

    //-------------List of Cards in the Game------------//
    private CardCollection cardsInGame;
    private List<CardStack> cardStacks;

    //----------------CSS Styles for Cards---------------//
    private final String greenCardGlowStyle = "-fx-stroke-width: 3; -fx-stroke: #54ff54;";

    //-----------------Info for ServerInfoDisplay------------//
    @FXML private Text hostIP, portNum;
    @FXML Rectangle serverInfoBackground;
    @FXML Pane serverInfoPane;
    @FXML Button startGameButton;

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
    private CardDisplay[] CIHDisplays;

    @FXML private Button actionButton;
    @FXML private Text gameInfoText;

    //---------------Cards In Supply---------------//
    @FXML private Rectangle actionCardInSupply1,actionCardInSupply2,actionCardInSupply3,actionCardInSupply4,actionCardInSupply5,
            actionCardInSupply6,actionCardInSupply7,actionCardInSupply8,actionCardInSupply9,actionCardInSupply10;
    @FXML private Rectangle actionCardNumBack1,actionCardNumBack2,actionCardNumBack3,actionCardNumBack4,actionCardNumBack5,actionCardNumBack6,
            actionCardNumBack7,actionCardNumBack8,actionCardNumBack9,actionCardNumBack10;
    @FXML private Text actionCardNum1,actionCardNum2,actionCardNum3,actionCardNum4,actionCardNum5,
            actionCardNum6,actionCardNum7,actionCardNum8,actionCardNum9,actionCardNum10;
    @FXML private Rectangle actionCardBuyButton1,actionCardBuyButton2,actionCardBuyButton3,actionCardBuyButton4,actionCardBuyButton5,
            actionCardBuyButton6,actionCardBuyButton7,actionCardBuyButton8,actionCardBuyButton9,actionCardBuyButton10;
    private BuyableCardDisplay[] ACISDisplays;

    @ FXML private Rectangle treasureCardInSupply1,treasureCardInSupply2,treasureCardInSupply3,treasureCardInSupply4;
    @FXML private Rectangle treasureCardNumBack1,treasureCardNumBack2,treasureCardNumBack3,treasureCardNumBack4;
    @FXML private Text treasureCardNum1,treasureCardNum2,treasureCardNum3,treasureCardNum4;
    @FXML private Rectangle treasureCardBuyButton1,treasureCardBuyButton2,treasureCardBuyButton3,treasureCardBuyButton4;
    private BuyableCardDisplay[] TCISDisplays;

    @ FXML private Rectangle victoryCardInSupply1,victoryCardInSupply2,victoryCardInSupply3,victoryCardInSupply4;
    @FXML private Rectangle victoryCardNumBack1,victoryCardNumBack2,victoryCardNumBack3,victoryCardNumBack4;
    @FXML private Text victoryCardNum1,victoryCardNum2,victoryCardNum3,victoryCardNum4;
    @FXML private Rectangle victoryCardBuyButton1,victoryCardBuyButton2,victoryCardBuyButton3,victoryCardBuyButton4;
    private BuyableCardDisplay[] VCISDisplays;

    @ FXML private Rectangle extraCardInSupply1,extraCardInSupply2,extraCardInSupply3,extraCardInSupply4;
    @FXML private Rectangle extraCardNumBack1,extraCardNumBack2,extraCardNumBack3,extraCardNumBack4;
    @FXML private Text extraCardNum1,extraCardNum2,extraCardNum3,extraCardNum4;
    @FXML private Rectangle extraCardBuyButton1,extraCardBuyButton2,extraCardBuyButton3,extraCardBuyButton4;
    private BuyableCardDisplay[] ECISDisplays;

    private List<BuyableCardDisplay> allCISDisplays;

    //---------------Cards in Play---------------//
    @FXML private Rectangle cardInPlay1,cardInPlay2,cardInPlay3,cardInPlay4,cardInPlay5,cardInPlay6,
            cardInPlay7,cardInPlay8,cardInPlay9,cardInPlay10,cardInPlay11;
    @FXML private Rectangle cardInPlayNumBack1,cardInPlayNumBack2,cardInPlayNumBack3,cardInPlayNumBack4,cardInPlayNumBack5,
            cardInPlayNumBack6,cardInPlayNumBack7,cardInPlayNumBack8,cardInPlayNumBack9,cardInPlayNumBack10,cardInPlayNumBack11;
    @FXML private Text cardInPlayNum1,cardInPlayNum2,cardInPlayNum3,cardInPlayNum4,cardInPlayNum5,cardInPlayNum6,
            cardInPlayNum7,cardInPlayNum8,cardInPlayNum9,cardInPlayNum10,cardInPlayNum11;
    private CardDisplay[] CIPDisplays;

    @FXML private Rectangle inPlayPlayerLabelBack;
    @FXML private Text inPlayPlayerLabelText;
    private LabelDisplay inPlayPlayerLabel;

    //---------------Decks------------//
    @FXML private Rectangle discardPileLabel, opponentDeckLabel, playerDeckLabel;
    @FXML private Text discardPileLabelText, opponentDeckLabelText, playerDeckLabelText;
    @FXML private Rectangle playerDiscard, playerDeck, opponentDeck, playerDiscardNumBack, playerDeckNumBack, opponentDeckNumBack;
    @FXML private Text playerDiscardNum, playerDeckNum, opponentDeckNum;
    private DeckDisplay playerDeckDisplay, opponentDeckDisplay, playerDiscardDisplay;

    //--------------PlayerName and Point Displays------------//
    @FXML private Rectangle playerLabel1, playerLabel2,playerLabel3,playerLabel4,playerLabel5,playerLabel6;
    @FXML private Text playerLabelName1,playerLabelName2,playerLabelName3,playerLabelName4,playerLabelName5,playerLabelName6;
    @FXML private Rectangle playerLabelVictory1,playerLabelVictory2,playerLabelVictory3,playerLabelVictory4,playerLabelVictory5,playerLabelVictory6;
    @FXML private Text playerLabelVictoryNum1,playerLabelVictoryNum2,playerLabelVictoryNum3,playerLabelVictoryNum4,playerLabelVictoryNum5,playerLabelVictoryNum6;
    private PlayerInfoDisplay[] playerInfoDisplays;

    //-------------ZoomActionCard------------//
    @FXML private Rectangle zoomActionCard;

    public void initialize() {
        chatDisplayStrings = new ArrayList<>();
        gameDisplayStrings = new ArrayList<>();
        chatType.setText(null);

        //---------------Set ServerInfoPane to notVisible----------//
        serverInfoPane.setVisible(false);

        //--------------Initialize order of CardsInHand-----------------//
        CardDisplay CIHDisplay1 = new CardDisplay(cardInHand1,new NumberDisplay(cardInHandNumBack1,cardInHandNum1));
        CardDisplay CIHDisplay2 = new CardDisplay(cardInHand2,new NumberDisplay(cardInHandNumBack2,cardInHandNum2));
        CardDisplay CIHDisplay3 = new CardDisplay(cardInHand3,new NumberDisplay(cardInHandNumBack3,cardInHandNum3));
        CardDisplay CIHDisplay4 = new CardDisplay(cardInHand4,new NumberDisplay(cardInHandNumBack4,cardInHandNum4));
        CardDisplay CIHDisplay5 = new CardDisplay(cardInHand5,new NumberDisplay(cardInHandNumBack5,cardInHandNum5));
        CardDisplay CIHDisplay6 = new CardDisplay(cardInHand6,new NumberDisplay(cardInHandNumBack6,cardInHandNum6));
        CardDisplay CIHDisplay7 = new CardDisplay(cardInHand7,new NumberDisplay(cardInHandNumBack7,cardInHandNum7));
        CardDisplay CIHDisplay8 = new CardDisplay(cardInHand8,new NumberDisplay(cardInHandNumBack8,cardInHandNum8));
        CardDisplay CIHDisplay9 = new CardDisplay(cardInHand9,new NumberDisplay(cardInHandNumBack9,cardInHandNum9));
        CardDisplay CIHDisplay10 = new CardDisplay(cardInHand10,new NumberDisplay(cardInHandNumBack10,cardInHandNum10));
        CardDisplay CIHDisplay11 = new CardDisplay(cardInHand11,new NumberDisplay(cardInHandNumBack11,cardInHandNum11));
        CIHDisplays = new CardDisplay[]{CIHDisplay1,CIHDisplay2,CIHDisplay3,CIHDisplay4,CIHDisplay5,CIHDisplay6,CIHDisplay7,CIHDisplay8,CIHDisplay9,CIHDisplay10,CIHDisplay11};

        setInitialViewOrder(CIHDisplays);

        //------------------Initialize Cards In Play--------------//
        CardDisplay CIPDisplay1 = new CardDisplay(cardInPlay1,new NumberDisplay(cardInPlayNumBack1,cardInPlayNum1));
        CardDisplay CIPDisplay2 = new CardDisplay(cardInPlay2,new NumberDisplay(cardInPlayNumBack2,cardInPlayNum2));
        CardDisplay CIPDisplay3 = new CardDisplay(cardInPlay3,new NumberDisplay(cardInPlayNumBack3,cardInPlayNum3));
        CardDisplay CIPDisplay4 = new CardDisplay(cardInPlay4,new NumberDisplay(cardInPlayNumBack4,cardInPlayNum4));
        CardDisplay CIPDisplay5 = new CardDisplay(cardInPlay5,new NumberDisplay(cardInPlayNumBack5,cardInPlayNum5));
        CardDisplay CIPDisplay6 = new CardDisplay(cardInPlay6,new NumberDisplay(cardInPlayNumBack6,cardInPlayNum6));
        CardDisplay CIPDisplay7 = new CardDisplay(cardInPlay7,new NumberDisplay(cardInPlayNumBack7,cardInPlayNum7));
        CardDisplay CIPDisplay8 = new CardDisplay(cardInPlay8,new NumberDisplay(cardInPlayNumBack8,cardInPlayNum8));
        CardDisplay CIPDisplay9 = new CardDisplay(cardInPlay9,new NumberDisplay(cardInPlayNumBack9,cardInPlayNum9));
        CardDisplay CIPDisplay10 = new CardDisplay(cardInPlay10,new NumberDisplay(cardInPlayNumBack10,cardInPlayNum10));
        CardDisplay CIPDisplay11 = new CardDisplay(cardInPlay11,new NumberDisplay(cardInPlayNumBack11,cardInPlayNum11));
        CIPDisplays = new CardDisplay[]{CIPDisplay1,CIPDisplay2,CIPDisplay3,CIPDisplay4,CIPDisplay5,CIPDisplay6,CIPDisplay7,CIPDisplay8,CIPDisplay9,CIPDisplay10,CIPDisplay11};

        setInitialViewOrder(CIPDisplays);

        inPlayPlayerLabel = new LabelDisplay(inPlayPlayerLabelBack,inPlayPlayerLabelText);

        //--------------Initialize Cards in Supply---------------//
        BuyableCardDisplay ACISDisplay1 = new BuyableCardDisplay(actionCardInSupply1,new NumberDisplay(actionCardNumBack1,actionCardNum1),actionCardBuyButton1);
        BuyableCardDisplay ACISDisplay2 = new BuyableCardDisplay(actionCardInSupply2,new NumberDisplay(actionCardNumBack2,actionCardNum2),actionCardBuyButton2);
        BuyableCardDisplay ACISDisplay3 = new BuyableCardDisplay(actionCardInSupply3,new NumberDisplay(actionCardNumBack3,actionCardNum3),actionCardBuyButton3);
        BuyableCardDisplay ACISDisplay4 = new BuyableCardDisplay(actionCardInSupply4,new NumberDisplay(actionCardNumBack4,actionCardNum4),actionCardBuyButton4);
        BuyableCardDisplay ACISDisplay5 = new BuyableCardDisplay(actionCardInSupply5,new NumberDisplay(actionCardNumBack5,actionCardNum5),actionCardBuyButton5);
        BuyableCardDisplay ACISDisplay6 = new BuyableCardDisplay(actionCardInSupply6,new NumberDisplay(actionCardNumBack6,actionCardNum6),actionCardBuyButton6);
        BuyableCardDisplay ACISDisplay7 = new BuyableCardDisplay(actionCardInSupply7,new NumberDisplay(actionCardNumBack7,actionCardNum7),actionCardBuyButton7);
        BuyableCardDisplay ACISDisplay8 = new BuyableCardDisplay(actionCardInSupply8,new NumberDisplay(actionCardNumBack8,actionCardNum8),actionCardBuyButton8);
        BuyableCardDisplay ACISDisplay9 = new BuyableCardDisplay(actionCardInSupply9,new NumberDisplay(actionCardNumBack9,actionCardNum9),actionCardBuyButton9);
        BuyableCardDisplay ACISDisplay10 = new BuyableCardDisplay(actionCardInSupply10,new NumberDisplay(actionCardNumBack10,actionCardNum10),actionCardBuyButton10);

        BuyableCardDisplay TCISDisplay1 = new BuyableCardDisplay(treasureCardInSupply1,new NumberDisplay(treasureCardNumBack1,treasureCardNum1),treasureCardBuyButton1);
        BuyableCardDisplay TCISDisplay2 = new BuyableCardDisplay(treasureCardInSupply2,new NumberDisplay(treasureCardNumBack2,treasureCardNum2),treasureCardBuyButton2);
        BuyableCardDisplay TCISDisplay3 = new BuyableCardDisplay(treasureCardInSupply3,new NumberDisplay(treasureCardNumBack3,treasureCardNum3),treasureCardBuyButton3);
        BuyableCardDisplay TCISDisplay4 = new BuyableCardDisplay(treasureCardInSupply4,new NumberDisplay(treasureCardNumBack4,treasureCardNum4),treasureCardBuyButton4);

        BuyableCardDisplay VCISDisplay1 = new BuyableCardDisplay(victoryCardInSupply1,new NumberDisplay(victoryCardNumBack1,victoryCardNum1),victoryCardBuyButton1);
        BuyableCardDisplay VCISDisplay2 = new BuyableCardDisplay(victoryCardInSupply2,new NumberDisplay(victoryCardNumBack2,victoryCardNum2),victoryCardBuyButton2);
        BuyableCardDisplay VCISDisplay3 = new BuyableCardDisplay(victoryCardInSupply3,new NumberDisplay(victoryCardNumBack3,victoryCardNum3),victoryCardBuyButton3);
        BuyableCardDisplay VCISDisplay4 = new BuyableCardDisplay(victoryCardInSupply4,new NumberDisplay(victoryCardNumBack4,victoryCardNum4),victoryCardBuyButton4);

        BuyableCardDisplay ECISDisplay1 = new BuyableCardDisplay(extraCardInSupply1,new NumberDisplay(extraCardNumBack1,extraCardNum1),extraCardBuyButton1);
        BuyableCardDisplay ECISDisplay2 = new BuyableCardDisplay(extraCardInSupply2,new NumberDisplay(extraCardNumBack2,extraCardNum2),extraCardBuyButton2);
        BuyableCardDisplay ECISDisplay3 = new BuyableCardDisplay(extraCardInSupply3,new NumberDisplay(extraCardNumBack3,extraCardNum3),extraCardBuyButton3);
        BuyableCardDisplay ECISDisplay4 = new BuyableCardDisplay(extraCardInSupply4,new NumberDisplay(extraCardNumBack4,extraCardNum4),extraCardBuyButton4);

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
        try {
            ImagePattern imagePattern = new ImagePattern(new Image(Main.class.getResource("/BackViewCard.png").toURI().toString()));
            playerDeck.setFill(imagePattern);
            opponentDeck.setFill(imagePattern);
        } catch(URISyntaxException ex) {
            ex.printStackTrace();
        }

        playerDeckDisplay = new DeckDisplay(new DeckCardDisplay(playerDeck,new NumberDisplay(playerDeckNumBack,playerDeckNum)),new LabelDisplay(playerDeckLabel,playerDeckLabelText));
        opponentDeckDisplay = new DeckDisplay(new DeckCardDisplay(opponentDeck,new NumberDisplay(opponentDeckNumBack,opponentDeckNum)),new LabelDisplay(opponentDeckLabel,opponentDeckLabelText));
        playerDiscardDisplay = new DeckDisplay(new DeckCardDisplay(playerDiscard,new NumberDisplay(playerDiscardNumBack,playerDiscardNum)),new LabelDisplay(discardPileLabel,discardPileLabelText));

        playerDeckDisplay.hide();
        opponentDeckDisplay.hide();
        playerDiscardDisplay.hide();

        //--------------Initialize Player labels----------------//
        Rectangle[] playerLabelVictories = new Rectangle[]{playerLabelVictory1, playerLabelVictory2, playerLabelVictory3, playerLabelVictory4, playerLabelVictory5, playerLabelVictory6};
        try {
            ImagePattern imagePattern = new ImagePattern(new Image(Main.class.getResource("/Victory_Symbol.png").toURI().toString()));
            for(Rectangle victoryImageSlot: playerLabelVictories) {
                victoryImageSlot.setFill(imagePattern);
            }
        } catch(URISyntaxException ex) {
            ex.printStackTrace();
        }


        PlayerInfoDisplay playerInfoDisplay1 = new PlayerInfoDisplay(new LabelDisplay(playerLabel1, playerLabelName1), new NumberDisplay(playerLabelVictory1, playerLabelVictoryNum1));
        PlayerInfoDisplay playerInfoDisplay2 = new PlayerInfoDisplay(new LabelDisplay(playerLabel2,playerLabelName2),new NumberDisplay(playerLabelVictory2,playerLabelVictoryNum2));
        PlayerInfoDisplay playerInfoDisplay3 = new PlayerInfoDisplay(new LabelDisplay(playerLabel3,playerLabelName3),new NumberDisplay(playerLabelVictory3,playerLabelVictoryNum3));
        PlayerInfoDisplay playerInfoDisplay4 = new PlayerInfoDisplay(new LabelDisplay(playerLabel4,playerLabelName4),new NumberDisplay(playerLabelVictory4,playerLabelVictoryNum4));
        PlayerInfoDisplay playerInfoDisplay5 = new PlayerInfoDisplay(new LabelDisplay(playerLabel5,playerLabelName5),new NumberDisplay(playerLabelVictory5,playerLabelVictoryNum5));
        PlayerInfoDisplay playerInfoDisplay6 = new PlayerInfoDisplay(new LabelDisplay(playerLabel6,playerLabelName6),new NumberDisplay(playerLabelVictory6,playerLabelVictoryNum6));

        playerInfoDisplays = new PlayerInfoDisplay[]{playerInfoDisplay1,playerInfoDisplay2,playerInfoDisplay3,playerInfoDisplay4,playerInfoDisplay5,playerInfoDisplay6};
        for(PlayerInfoDisplay display: playerInfoDisplays) {
            display.hide();
        }

        //-------------Set ActionButton to not visible------------//
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
    public PlayerInfoDisplay[] getPlayerInfoDisplays() {return playerInfoDisplays;}
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
            case "Start Turn": ActionHandler.startPhase(); break;
            case "Enter Buy Phase": ActionHandler.buyPhase(); break;
            case "End Turn": ActionHandler.endPhase(); break;
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
        String ipAddress = Main.getServer().getIpAddress();
        hostIP.setText(ipAddress);
        int port = Main.getServer().getPortNumber();
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
            try {
                cardDisplay.setBuyButtonImage(new Image(Main.class.getResource("/Plus Sign.png").toURI().toString()));
                cardDisplay.hide();
            } catch(URISyntaxException ex) {
                ex.printStackTrace();
            }
        }
    }
    private void setInitialViewOrder(CardDisplay[] cardDisplays) {
        for(int i=0; i < cardDisplays.length; i++) {
            cardDisplays[i].setViewOrder(cardDisplays.length-i);
        }
    }
}
