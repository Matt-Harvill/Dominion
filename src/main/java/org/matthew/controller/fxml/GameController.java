package org.matthew.controller.fxml;

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
import org.matthew.Main;
import org.matthew.controller.DisplayInputHandler;
import org.matthew.controller.DisplayUpdater;
import org.matthew.model.CardCollection;
import org.matthew.model.card.Card;
import org.matthew.model.card.CardStack;
import org.matthew.model.card.TreasureCard;
import org.matthew.model.card.VictoryCard;
import org.matthew.model.card.action.ActionCard;
import org.matthew.model.factory.CardFactory;
import org.matthew.server.ServerSender;
import org.matthew.view.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameController {

    //-------------List of Cards in the Game------------//
    private CardCollection cardsInGame;
    private List<CardStack> cardStacks;

    //----------------CSS Styles for Cards---------------//
    private final String greenCardGlowStyle = "-fx-stroke-width: 3; -fx-stroke: #54ff54;";
    private final String yellowCardGlowStyle = "-fx-stroke-width: 3; -fx-stroke: yellow";
    private final String redCardGlowStyle = "-fx-stroke-width: 3; -fx-stroke: red";
    private final String cyanCardGlowStyle = "-fx-stroke-width: 3; -fx-stroke: cyan";

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

    @FXML private Button actionButton, switchCardViewButton;
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
    @FXML private Rectangle actionCardGainButton1,actionCardGainButton2,actionCardGainButton3,actionCardGainButton4,actionCardGainButton5,
            actionCardGainButton6,actionCardGainButton7,actionCardGainButton8,actionCardGainButton9,actionCardGainButton10;
    private BuyableCardDisplay[] ACISDisplays;

    @ FXML private Rectangle treasureCardInSupply1,treasureCardInSupply2,treasureCardInSupply3,treasureCardInSupply4;
    @FXML private Rectangle treasureCardNumBack1,treasureCardNumBack2,treasureCardNumBack3,treasureCardNumBack4;
    @FXML private Text treasureCardNum1,treasureCardNum2,treasureCardNum3,treasureCardNum4;
    @FXML private Rectangle treasureCardBuyButton1,treasureCardBuyButton2,treasureCardBuyButton3,treasureCardBuyButton4;
    @FXML private Rectangle treasureCardGainButton1,treasureCardGainButton2,treasureCardGainButton3,treasureCardGainButton4;
    private BuyableCardDisplay[] TCISDisplays;

    @ FXML private Rectangle victoryCardInSupply1,victoryCardInSupply2,victoryCardInSupply3,victoryCardInSupply4;
    @FXML private Rectangle victoryCardNumBack1,victoryCardNumBack2,victoryCardNumBack3,victoryCardNumBack4;
    @FXML private Text victoryCardNum1,victoryCardNum2,victoryCardNum3,victoryCardNum4;
    @FXML private Rectangle victoryCardBuyButton1,victoryCardBuyButton2,victoryCardBuyButton3,victoryCardBuyButton4;
    @FXML private Rectangle victoryCardGainButton1,victoryCardGainButton2,victoryCardGainButton3,victoryCardGainButton4;
    private BuyableCardDisplay[] VCISDisplays;

    @ FXML private Rectangle extraCardInSupply1,extraCardInSupply2,extraCardInSupply3,extraCardInSupply4;
    @FXML private Rectangle extraCardNumBack1,extraCardNumBack2,extraCardNumBack3,extraCardNumBack4;
    @FXML private Text extraCardNum1,extraCardNum2,extraCardNum3,extraCardNum4;
    @FXML private Rectangle extraCardBuyButton1,extraCardBuyButton2,extraCardBuyButton3,extraCardBuyButton4;
    @FXML private Rectangle extraCardGainButton1,extraCardGainButton2,extraCardGainButton3,extraCardGainButton4;
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

        //-----------Set switchCardViewButton to notVisible-----------//
        switchCardViewButton.setVisible(false);

        //--------------Initialize Cards In Hand-----------------//
        Rectangle[] cardsInHand = new Rectangle[]{cardInHand1,cardInHand2,cardInHand3,cardInHand4,cardInHand5,cardInHand6,cardInHand7,cardInHand8,cardInHand9,cardInHand10,cardInHand11};
        Rectangle[] cardsInHandNumBacks = new Rectangle[]{cardInHandNumBack1,cardInHandNumBack2,cardInHandNumBack3,cardInHandNumBack4,cardInHandNumBack5,cardInHandNumBack6,
                cardInHandNumBack7,cardInHandNumBack8,cardInHandNumBack9,cardInHandNumBack10,cardInHandNumBack11};
        Text[] cardsInHandNums = new Text[]{cardInHandNum1,cardInHandNum2,cardInHandNum3,cardInHandNum4,cardInHandNum5,cardInHandNum6,cardInHandNum7,
                cardInHandNum8,cardInHandNum9,cardInHandNum10,cardInHandNum11};

        CIHDisplays = new CardDisplay[cardsInHand.length];
        for(int i=0; i<cardsInHand.length; i++) {
            CIHDisplays[i] = new CardDisplay(cardsInHand[i],new NumberDisplay(cardsInHandNumBacks[i],cardsInHandNums[i]));
        }
        setInitialViewOrder(CIHDisplays);

        //------------------Initialize Cards In Play--------------//
        Rectangle[] cardsInPlay = new Rectangle[]{cardInPlay1,cardInPlay2,cardInPlay3,cardInPlay4,cardInPlay5,cardInPlay6,
                cardInPlay7,cardInPlay8,cardInPlay9,cardInPlay10,cardInPlay11};
        Rectangle[] cardsInPlayNumBacks = new Rectangle[]{cardInPlayNumBack1,cardInPlayNumBack2,cardInPlayNumBack3,cardInPlayNumBack4,cardInPlayNumBack5,
                cardInPlayNumBack6,cardInPlayNumBack7,cardInPlayNumBack8,cardInPlayNumBack9,cardInPlayNumBack10,cardInPlayNumBack11};
        Text[] cardsInPlayNums = new Text[]{cardInPlayNum1,cardInPlayNum2,cardInPlayNum3,cardInPlayNum4,cardInPlayNum5,cardInPlayNum6,
                cardInPlayNum7,cardInPlayNum8,cardInPlayNum9,cardInPlayNum10,cardInPlayNum11};

        CIPDisplays = new CardDisplay[cardsInPlay.length];
        for(int i=0; i<cardsInPlay.length; i++) {
            CIPDisplays[i] = new CardDisplay(cardsInPlay[i],new NumberDisplay(cardsInPlayNumBacks[i],cardsInPlayNums[i]));
        }
        setInitialViewOrder(CIPDisplays);
        inPlayPlayerLabel = new LabelDisplay(inPlayPlayerLabelBack,inPlayPlayerLabelText);

        //--------------Initialize Cards in Supply---------------//
        Rectangle[] ACIS = new Rectangle[]{actionCardInSupply1,actionCardInSupply2,actionCardInSupply3,actionCardInSupply4,actionCardInSupply5,
                actionCardInSupply6,actionCardInSupply7,actionCardInSupply8,actionCardInSupply9,actionCardInSupply10};
        Rectangle[] ACISNumBacks = new Rectangle[]{actionCardNumBack1,actionCardNumBack2,actionCardNumBack3,actionCardNumBack4,actionCardNumBack5,actionCardNumBack6,
                actionCardNumBack7,actionCardNumBack8,actionCardNumBack9,actionCardNumBack10};
        Text[] ACISNums = new Text[]{actionCardNum1,actionCardNum2,actionCardNum3,actionCardNum4,actionCardNum5,
                actionCardNum6,actionCardNum7,actionCardNum8,actionCardNum9,actionCardNum10};
        Rectangle[] ACISBuyButtons = new Rectangle[]{actionCardBuyButton1,actionCardBuyButton2,actionCardBuyButton3,actionCardBuyButton4,actionCardBuyButton5,
                actionCardBuyButton6,actionCardBuyButton7,actionCardBuyButton8,actionCardBuyButton9,actionCardBuyButton10};
        Rectangle[] ACISGainButtons = new Rectangle[]{actionCardGainButton1,actionCardGainButton2,actionCardGainButton3,actionCardGainButton4,actionCardGainButton5,
                actionCardGainButton6,actionCardGainButton7,actionCardGainButton8,actionCardGainButton9,actionCardGainButton10};

        Rectangle[] TCIS = new Rectangle[]{treasureCardInSupply1,treasureCardInSupply2,treasureCardInSupply3,treasureCardInSupply4};
        Rectangle[] TCISNumBacks = new Rectangle[]{treasureCardNumBack1,treasureCardNumBack2,treasureCardNumBack3,treasureCardNumBack4};
        Text[] TCISNums = new Text[]{treasureCardNum1,treasureCardNum2,treasureCardNum3,treasureCardNum4};
        Rectangle[] TCISBuyButtons = new Rectangle[]{treasureCardBuyButton1,treasureCardBuyButton2,treasureCardBuyButton3,treasureCardBuyButton4};
        Rectangle[] TCISGainButtons = new Rectangle[]{treasureCardGainButton1,treasureCardGainButton2,treasureCardGainButton3,treasureCardGainButton4};

        Rectangle[] VCIS = new Rectangle[]{victoryCardInSupply1,victoryCardInSupply2,victoryCardInSupply3,victoryCardInSupply4};
        Rectangle[] VCISNumBacks = new Rectangle[]{victoryCardNumBack1,victoryCardNumBack2,victoryCardNumBack3,victoryCardNumBack4};
        Text[] VCISNums = new Text[]{victoryCardNum1,victoryCardNum2,victoryCardNum3,victoryCardNum4};
        Rectangle[] VCISBuyButtons = new Rectangle[]{victoryCardBuyButton1,victoryCardBuyButton2,victoryCardBuyButton3,victoryCardBuyButton4};
        Rectangle[] VCISGainButtons = new Rectangle[]{victoryCardGainButton1,victoryCardGainButton2,victoryCardGainButton3,victoryCardGainButton4};

        Rectangle[] ECIS = new Rectangle[]{extraCardInSupply1,extraCardInSupply2,extraCardInSupply3,extraCardInSupply4};
        Rectangle[] ECISNumBacks = new Rectangle[]{extraCardNumBack1,extraCardNumBack2,extraCardNumBack3,extraCardNumBack4};
        Text[] ECISNums = new Text[]{extraCardNum1,extraCardNum2,extraCardNum3,extraCardNum4};
        Rectangle[] ECISBuyButtons = new Rectangle[]{extraCardBuyButton1,extraCardBuyButton2,extraCardBuyButton3,extraCardBuyButton4};
        Rectangle[] ECISGainButtons = new Rectangle[]{extraCardGainButton1,extraCardGainButton2,extraCardGainButton3,extraCardGainButton4};

        ACISDisplays = new BuyableCardDisplay[ACIS.length];
        for(int i=0; i<ACIS.length; i++) {
            ACISDisplays[i] = new BuyableCardDisplay(ACIS[i],new NumberDisplay(ACISNumBacks[i],ACISNums[i]),ACISBuyButtons[i],ACISGainButtons[i]);
        }
        TCISDisplays = new BuyableCardDisplay[TCIS.length];
        for(int i=0; i<TCIS.length; i++) {
            TCISDisplays[i] = new BuyableCardDisplay(TCIS[i],new NumberDisplay(TCISNumBacks[i],TCISNums[i]),TCISBuyButtons[i],TCISGainButtons[i]);
        }
        VCISDisplays = new BuyableCardDisplay[VCIS.length];
        for(int i=0; i<VCIS.length; i++) {
            VCISDisplays[i] = new BuyableCardDisplay(VCIS[i],new NumberDisplay(VCISNumBacks[i],VCISNums[i]),VCISBuyButtons[i],VCISGainButtons[i]);
        }
        ECISDisplays = new BuyableCardDisplay[ECIS.length];
        for(int i=0; i<ECIS.length; i++) {
            ECISDisplays[i] = new BuyableCardDisplay(ECIS[i],new NumberDisplay(ECISNumBacks[i],ECISNums[i]),ECISBuyButtons[i],ECISGainButtons[i]);
        }

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
        ImagePattern imagePattern = new ImagePattern(new Image(String.valueOf(Main.class.getResource("BackViewCard.png"))));
        playerDeck.setFill(imagePattern);
        opponentDeck.setFill(imagePattern);

        playerDeckDisplay = new DeckDisplay(new DeckCardDisplay(playerDeck,new NumberDisplay(playerDeckNumBack,playerDeckNum)),new LabelDisplay(playerDeckLabel,playerDeckLabelText));
        opponentDeckDisplay = new DeckDisplay(new DeckCardDisplay(opponentDeck,new NumberDisplay(opponentDeckNumBack,opponentDeckNum)),new LabelDisplay(opponentDeckLabel,opponentDeckLabelText));
        playerDiscardDisplay = new DeckDisplay(new DeckCardDisplay(playerDiscard,new NumberDisplay(playerDiscardNumBack,playerDiscardNum)),new LabelDisplay(discardPileLabel,discardPileLabelText));

        playerDeckDisplay.hide();
        opponentDeckDisplay.hide();
        playerDiscardDisplay.hide();

        //--------------Initialize Player labels----------------//
        Rectangle[] playerLabels = new Rectangle[]{playerLabel1, playerLabel2,playerLabel3,playerLabel4,playerLabel5,playerLabel6};
        Text[] playerLabelNames = new Text[]{playerLabelName1,playerLabelName2,playerLabelName3,playerLabelName4,playerLabelName5,playerLabelName6};
        Rectangle[] playerLabelVictories = new Rectangle[]{playerLabelVictory1,playerLabelVictory2,playerLabelVictory3,playerLabelVictory4,playerLabelVictory5,playerLabelVictory6};
        Text[] playerLabelVictoryNums = new Text[]{playerLabelVictoryNum1,playerLabelVictoryNum2,playerLabelVictoryNum3,playerLabelVictoryNum4,playerLabelVictoryNum5,playerLabelVictoryNum6};

        imagePattern = new ImagePattern(new Image(String.valueOf(Main.class.getResource("Victory_Symbol.png"))));
        for(Rectangle victoryImageSlot: playerLabelVictories) {
            victoryImageSlot.setFill(imagePattern);
        }

        playerInfoDisplays = new PlayerInfoDisplay[playerLabels.length];
        for(int i=0; i<playerLabels.length; i++) {
            playerInfoDisplays[i] = new PlayerInfoDisplay(new LabelDisplay(playerLabels[i], playerLabelNames[i]), new NumberDisplay(playerLabelVictories[i], playerLabelVictoryNums[i]));
            playerInfoDisplays[i].hide();
        }

        //-------------Set ActionButton to not visible------------//
        actionButton.setVisible(false);

    }

    //-------------Getters----------------//

    public Button getActionButton() { return actionButton; }
    public Button getSwitchCardViewButton() {return switchCardViewButton;}
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
    public String getYellowCardGlowStyle() {return yellowCardGlowStyle;}
    public String getRedCardGlowStyle() {return redCardGlowStyle;}
    public String getCyanCardGlowStyle() {return cyanCardGlowStyle;}
    public LabelDisplay getInPlayPlayerLabel() {return inPlayPlayerLabel;}
    public DeckDisplay getPlayerDeckDisplay() {return playerDeckDisplay;}
    public DeckDisplay getOpponentDeckDisplay() {return opponentDeckDisplay;}
    public DeckDisplay getPlayerDiscardDisplay() {return playerDiscardDisplay;}

    public List<BuyableCardDisplay> getAllCISDisplays() {return allCISDisplays;}
    public CardDisplay[] getCIHDisplays() {return CIHDisplays;}
    public CardDisplay[] getCIPDisplays() {return CIPDisplays;}

    //-------------------Update methods------------------------//
    public void chatSend(ActionEvent actionEvent) {
        if(chatType.getText()==null) return;
        String chatText = chatType.getText();
        DisplayUpdater.addMsgToChatLog(Main.getPlayer().getName() + ": " + chatText);
        ServerSender.chatSend(chatText);
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

    public void cardInHandClicked(MouseEvent mouseEvent) {
        Rectangle cardClicked = (Rectangle) mouseEvent.getSource();
        for(CardDisplay cardDisplay: CIHDisplays) {
            if(cardDisplay.contains(cardClicked)) {
                if(cardClicked.getStyle().equals(greenCardGlowStyle)) {
                    DisplayInputHandler.greenCardInHandClicked(cardDisplay.getCard());
                }
            }
        }
    }
    public void actionButtonClicked(ActionEvent actionEvent) {
        DisplayInputHandler.actionButtonClicked(actionButton.getText());
    }
    public void buyButtonClicked(MouseEvent mouseEvent) {
        Object buyButtonClicked = mouseEvent.getSource();
        for(BuyableCardDisplay cardDisplay: allCISDisplays) {
            if(cardDisplay.contains(buyButtonClicked)) {
                DisplayInputHandler.buyButtonClicked(cardDisplay.getCard());
            }
        }
    }
    public void gainButtonClicked(MouseEvent mouseEvent) {
        Object gainButtonClicked = mouseEvent.getSource();
        for(BuyableCardDisplay cardDisplay: allCISDisplays) {
            if(cardDisplay.contains(gainButtonClicked)) {
                DisplayInputHandler.gainButtonClicked(cardDisplay.getCard());
            }
        }
    }
    public void switchCardViewButtonClicked(ActionEvent actionEvent) {
        DisplayInputHandler.cardViewButtonClicked(switchCardViewButton.getText());
        if(switchCardViewButton.getText().equals("View Selected Cards")) {
            switchCardViewButton.setText("View Cards In Play");
        } else if(switchCardViewButton.getText().equals("View Cards In Play")) {
            switchCardViewButton.setText("View Selected Cards");
        }
    }
    public void cardInPlayClicked(MouseEvent mouseEvent) {
        Object objectClicked = mouseEvent.getSource();
        for(CardDisplay cardDisplay: CIPDisplays) {
            if(cardDisplay.contains(objectClicked)) {
                DisplayInputHandler.cardInPlayClicked(cardDisplay.getCard()); break;
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
        List<TreasureCard> treasureCardsInGame = cardsInGame.getDistinctTreasureCards();
        List<VictoryCard> victoryCardsInGame = cardsInGame.getDistinctVictoryCards();
        List<ActionCard> actionCardsInGame = cardsInGame.getDistinctActionCards();

        List<Card> mainSupply = new ArrayList<>();
        mainSupply.addAll(actionCardsInGame);
        for(Card card: victoryCardsInGame) {
            if(card.getName().equals("Gardens")) {
                mainSupply.add(CardFactory.getCard("Gardens"));
            }
        }
        mainSupply.sort(Comparator.comparing(Card::getCost));

        for(int i=0; i<treasureCardsInGame.size(); i++) {
            TreasureCard card = treasureCardsInGame.get(i);
            BuyableCardDisplay cardDisplay = TCISDisplays[i];
            cardDisplay.setCard(card);
            cardDisplay.show();
        }
        int victoryIndex = 0;
        for (VictoryCard card : victoryCardsInGame) {
            if (card.getName().equals("Gardens")) {
                continue;
            }
            BuyableCardDisplay cardDisplay = VCISDisplays[victoryIndex];
            cardDisplay.setCard(card);
            cardDisplay.show();
            victoryIndex++;
        }
        for(int i=0; i< mainSupply.size(); i++) {
            Card card = mainSupply.get(i);
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
            cardDisplay.setBuyButtonImage(new Image(String.valueOf(Main.class.getResource("Plus_Sign_Blue.png"))));
            cardDisplay.setGainButtonImage(new Image(String.valueOf(Main.class.getResource("Plus_Sign_Purple.png"))));
            cardDisplay.hide();
        }

    }
    private void setInitialViewOrder(CardDisplay[] cardDisplays) {
        for(int i=0; i < cardDisplays.length; i++) {
            cardDisplays[i].setViewOrder(cardDisplays.length-i);
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
}
