package controller;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.CardCollection;
import model.Player;
import model.ServerPlayer;
import model.card.ActionCard;
import model.card.Card;
import model.card.TreasureCard;
import model.card.VictoryCard;
import view.*;

import java.util.ArrayList;
import java.util.List;

public final class PlayerActionMediator {

    private static final Player player = Main.getPlayer();
    private static final GameController controller = Main.getGameController();
    private static final int maxNumCardsInHandOrPlayDisplay = 11;

    public static void startPhase() {
        player.setPhase("startPhase");
        player.newTurn();

        displayPlayerDiscard();
        displayHandOrInPlay(controller.getPlayerHandDisplay());
        displayPlayerDeckDisplay(controller.getPlayerDeckDisplay(), true);
    }
    public static void actionPhase() {
        ServerSender.updateInfo();
        System.out.println("actionPhase was entered");
        controller.getActionBar().setVisible(true);

        controller.getInPlayPlayerLabel().setVisible(false);
        controller.getOpponentDeckDisplay().setVisible(false);

        player.setPhase("actionPhase");
        displayPlayerDiscard();
        displayHandOrInPlay(controller.getPlayerHandDisplay());
        displayHandOrInPlay(controller.getInPlayDisplay());
        displayPlayerDeckDisplay(controller.getPlayerDeckDisplay(), true);
        controller.getActionButton().setText("Enter Buy Phase");
        checkCanDoAction();
    }
    public static void buyPhase() {
        player.setPhase("buyPhase");

//        System.out.println(player);
//        List<TreasureCard> treasureCardsInHand = player.getHand().getDistinctTreasureCards();
//        for(TreasureCard treasureCard: treasureCardsInHand) {
//            int numCard = player.getHand().numCardInCollection(treasureCard);
//            for(int i=0; i<numCard; i++) {
//                playCard(treasureCard);
//            }
//        }
//        System.out.println(player);

        showBuyableCards(true);
        displayPlayerDiscard();
        displayHandOrInPlay(controller.getPlayerHandDisplay());
        displayHandOrInPlay(controller.getInPlayDisplay());
        displayPlayerDeckDisplay(controller.getPlayerDeckDisplay(), true);
        controller.getActionButton().setText("End Turn");
    }
    public static void endPhase() {
        player.setPhase("endPhase");
        showBuyableCards(false);
        player.endTurn();
        displayPlayerDiscard();
        displayHandOrInPlay(controller.getPlayerHandDisplay());
        displayHandOrInPlay(controller.getInPlayDisplay());
        displayPlayerDeckDisplay(controller.getPlayerDeckDisplay(), true);
        controller.getActionButton().setText("Start Turn");

        controller.getActionBar().setVisible(false);
        startPhase();
        ServerSender.endTurn();
    }

    public static void buyFromCardSupply(Card cardClicked) {
        player.buyCard(cardClicked);
        cardPurchased(cardClicked);

        addMessageToGameLog("You purchased a " + cardClicked.getName());
        ServerSender.buyCard(cardClicked.getName());

        displayPlayerDiscard();
        displayHandOrInPlay(controller.getPlayerHandDisplay());
        displayPlayerLabel(player.getName(), player.getPoints());
        displayPlayerDeckDisplay(controller.getPlayerDeckDisplay(), true);
        if(checkNumBuys()) {
            showBuyableCards(true);
        }
    }
    public static void playCard(Card cardClicked) {
        for(Card card: player.getHand().getCollection()) {
            if(card.equals(cardClicked)) {
                player.playCard(card);
                break;
            }
        }

//        addMessageToGameLog("You played a " + cardClicked.getName());
        ServerSender.playCard(cardClicked.getName());

        displayPlayerDiscard();
        displayHandOrInPlay(controller.getPlayerHandDisplay());
        displayHandOrInPlay(controller.getInPlayDisplay());
        displayPlayerLabel(player.getName(), player.getPoints());
        displayPlayerDeckDisplay(controller.getPlayerDeckDisplay(), true);
        if(cardClicked instanceof ActionCard) {
            checkCanDoAction();
        } else if(cardClicked instanceof TreasureCard) {
            showBuyableCards(true);
        }
    }

    public static void displayPlayerLabel(String playerName, int points) {
        PlayerNamePointsDisplay display = controller.getPlayerNamePointsDisplay();

        Text[] playerLabelNames = display.getPlayerLabelNames();
        Text[] playerPoints = display.getPlayerLabelVictoryNums();

        for(int i=0; i<playerLabelNames.length; i++) {
            if(playerName.equals(playerLabelNames[i].getText()) || playerLabelNames[i].getText().equals("")) {
                playerLabelNames[i].setText(playerName);
                playerPoints[i].setText(String.valueOf(points));
                display.getPlayerLabels()[i].setVisible(true);
                display.getPlayerLabelVictories()[i].setVisible(true);
                playerLabelNames[i].setVisible(true);
                playerPoints[i].setVisible(true);
                break;
            }
        }
    }
    public static void displayPlayerLabel(ServerPlayer player) {
        PlayerNamePointsDisplay display = controller.getPlayerNamePointsDisplay();

        Text[] playerLabelNames = display.getPlayerLabelNames();
        Text[] playerPoints = display.getPlayerLabelVictoryNums();

        for(int i=0; i<playerLabelNames.length; i++) {
            if(player.getName().equals(playerLabelNames[i].getText()) || playerLabelNames[i].getText().equals("")) {
                playerLabelNames[i].setText(player.getName());
                playerPoints[i].setText(String.valueOf(player.getPoints()));
                display.getPlayerLabels()[i].setVisible(true);
                display.getPlayerLabelVictories()[i].setVisible(true);
                playerLabelNames[i].setVisible(true);
                playerPoints[i].setVisible(true);
                break;
            }
        }
    }
    public static void displayInPlay(ServerPlayer otherPlayer) {
        HandOrInPlayDisplay display = controller.getInPlayDisplay();
        resetHandOrInPlayDisplay(display);

        int index = 0;
        int freqOfCardInARow = 1;
        CardCollection inPlay = otherPlayer.getInPlay();
        List<Card> cardList = inPlay.getCollection();
        Card[] cardsInPlayInDisplayOrder = new Card[maxNumCardsInHandOrPlayDisplay];
        for(int i=0; i<cardList.size();i++) {
            if (i != cardList.size() - 1 && cardList.get(i).equals(cardList.get(i + 1))) {
                freqOfCardInARow++;
                continue;
            }
            displayCardInHandOrInPlay(display, cardList.get(i), index, freqOfCardInARow);
            cardsInPlayInDisplayOrder[index] = cardList.get(i);
            index++;
            freqOfCardInARow = 1;
        }
        controller.getInPlayDisplay().setCardObjectsInHandOrInPlay(cardsInPlayInDisplayOrder);
    }
    public static void displayInPlayPlayerLabel(String otherPlayerName) {
        controller.getPlayerInPlayNameText().setText(otherPlayerName);
        controller.getInPlayPlayerLabel().setVisible(true);
    }
    public static void cardPurchased(Card card) {
        List<BuyableCardDisplay> cardDisplays = controller.getAllCISDisplays();

        for(BuyableCardDisplay cardDisplay: cardDisplays) {
            if(cardDisplay.getCard()!=null && cardDisplay.getCard().equals(card)) {
                int prevNum = cardDisplay.getNum();
                cardDisplay.setNum(prevNum-1);
            }
        }
    }
    public static void gameOverDisplay() {
        controller.getPlayerHandStackPane().setVisible(false);
        controller.getInPlayStackPane().setVisible(false);

        String gameOverText = "Game Over -> ";
        List<String> winners = calcWinners(Main.getClientSideConnection().getPlayers());
        if(winners.size()==1) {
            if(winners.get(0).equals(player.getName())) {
                gameOverText += "You won";
            } else {
                gameOverText += winners.get(0) + " won";
            }
        } else {
            for(int i=0; i<winners.size()-1; i++) {
                gameOverText += winners.get(i) + " and ";
            }
            gameOverText += winners.get(winners.size()-1) + " tied";
        }
        controller.getGameInfoText().setText(gameOverText);

        controller.getActionButton().setVisible(false);
        controller.getActionBar().setVisible(true);
        controller.getInPlayPlayerLabel().setVisible(false);

        controller.getPlayerDiscardDisplay().setVisible(false);
        controller.getPlayerDeckDisplay().setVisible(false);
        controller.getOpponentDeckDisplay().setVisible(false);
    }
    public static void displayOpponentDeckDisplay(String playerName, int numCards) {
        DeckDisplay display = controller.getOpponentDeckDisplay();
        display.getDeckNum().setText(String.valueOf(numCards));
//        if(playerName.length() > 9) {
//            display.getDeckLabelText().setText(playerName.substring(0,10) + "...'s Cards");
//        } else {
//            display.getDeckLabelText().setText(playerName + "'s Cards");
//        }
        display.getDeckLabelText().setText("Cards");

        controller.getOpponentDeckDisplay().setVisible(true);
    }

    private static void displayPlayerDeckDisplay(DeckDisplay display, boolean show) {
        int numCards = player.getDeck().getSize();
        display.getDeckNum().setText(String.valueOf(numCards));
        controller.getPlayerDeckDisplay().setVisible(show);
    }
    private static void showBuyableCards(boolean show) {
        List<BuyableCardDisplay> cardDisplays = controller.getAllCISDisplays();

        for(BuyableCardDisplay cardDisplay: cardDisplays) {
            Card card = cardDisplay.getCard();
            int cardNum = cardDisplay.getNum();
            if(card!=null && card.getCost()<=player.getPurchasePower() && cardNum>0 && show) {
                cardDisplay.showBuyButton();
            } else {
                cardDisplay.hideBuyButton();
            }
        }
    }
    private static void displayHandOrInPlay(HandOrInPlayDisplay display) {
        resetHandOrInPlayDisplay(display);

        if(display.getNameOfDisplay().equals("playerHandDisplay")) {
            int index = 0;
            CardCollection hand = player.getHand();
            List<ActionCard> actionCards = hand.getDistinctActionCards();
            List<TreasureCard> treasureCards = hand.getDistinctTreasureCards();
            List<VictoryCard> victoryCards = hand.getDistinctVictoryCards();
            Rectangle[] cardsInHand = controller.getPlayerHandDisplay().getCardsInHandOrInPlay();
            Card[] cardsInHandInDisplayOrder = new Card[maxNumCardsInHandOrPlayDisplay];
            for(ActionCard actionCard: actionCards) {
                int numCard = hand.numCardInCollection(actionCard);
                displayCardInHandOrInPlay(display,actionCard,index,numCard);
                if(player.getPhase().equals("actionPhase")) {
                    cardsInHand[index].setStyle(controller.getGreenCardGlowStyle());
                }
                cardsInHandInDisplayOrder[index] = actionCard;
                index++;
            }
            for(TreasureCard treasureCard: treasureCards) {
                int numCard = hand.numCardInCollection(treasureCard);
                displayCardInHandOrInPlay(display,treasureCard,index,numCard);
                if(player.getPhase().equals("buyPhase")) {
                    cardsInHand[index].setStyle(controller.getGreenCardGlowStyle());
                }
                cardsInHandInDisplayOrder[index] = treasureCard;
                index++;
            }
            for(VictoryCard victoryCard: victoryCards) {
                int numCard = hand.numCardInCollection(victoryCard);
                displayCardInHandOrInPlay(display,victoryCard,index,numCard);
                cardsInHandInDisplayOrder[index] = victoryCard;
                index++;
            }
            controller.getPlayerHandDisplay().setCardObjectsInHandOrInPlay(cardsInHandInDisplayOrder);
        } else if(display.getNameOfDisplay().equals("inPlayDisplay")) {
            int index = 0;
            int freqOfCardInARow = 1;
            CardCollection inPlay = player.getInPlay();
            List<Card> cardList = inPlay.getCollection();
            Card[] cardsInPlayInDisplayOrder = new Card[maxNumCardsInHandOrPlayDisplay];

            for(int i=0; i<cardList.size();i++) {
                if(i!=cardList.size()-1 && cardList.get(i).equals(cardList.get(i+1))) {
                    freqOfCardInARow++; continue;
                }
                displayCardInHandOrInPlay(display,cardList.get(i),index,freqOfCardInARow);
                cardsInPlayInDisplayOrder[index] = cardList.get(i);
                index++;
                freqOfCardInARow=1;
            }
            controller.getInPlayDisplay().setCardObjectsInHandOrInPlay(cardsInPlayInDisplayOrder);
        }

        Text gameInfoText = controller.getGameInfoText();
        String gameInfoString = "";
        if(player.getPhase().equals("actionPhase")) {
            gameInfoString+="Number of Actions: " + player.getNumActions() + "   ";
        }
        else if(player.getPhase().equals("buyPhase")) {
            gameInfoString+="Number of Buys Remaining : " + player.getNumBuys() + "   ";
            gameInfoString+="Purchase Power: " + player.getPurchasePower();
        }
        gameInfoText.setText(gameInfoString);

    }
    private static void displayCardInHandOrInPlay(HandOrInPlayDisplay display,Card card, int index, int numCard) {
        if(numCard==1) {
            display.getCardsInHandOrInPlay()[index].setFill(new ImagePattern(card.getCardImage()));
            display.getCardsInHandOrInPlay()[index].setVisible(true);
        } else if(numCard>1) {
            display.getCardsInHandOrInPlay()[index].setFill(new ImagePattern(card.getCardImage()));
            display.getCardInHandOrInPlayNums()[index].setText(String.valueOf(numCard));
            display.getCardsInHandOrInPlay()[index].setVisible(true);
            display.getCardInHandOrInPlayNumBacks()[index].setVisible(true);
            display.getCardInHandOrInPlayNums()[index].setVisible(true);
        }
    }
    private static void resetHandOrInPlayDisplay(HandOrInPlayDisplay display) {
        for(int i=0; i<display.getCardsInHandOrInPlay().length; i++) {
            display.getCardsInHandOrInPlay()[i].setVisible(false);
            display.getCardsInHandOrInPlay()[i].setStyle(null);
            display.getCardInHandOrInPlayNumBacks()[i].setVisible(false);
            display.getCardInHandOrInPlayNums()[i].setVisible(false);
        }
    }
    private static void displayPlayerDiscard() {
        Card topDiscardCard = player.getDiscardPile().peekLastCard();
        int numCardsInDiscardPile = player.getDiscardPile().getSize();

        DeckDisplay discardDisplay = controller.getPlayerDiscardDisplay();
        discardDisplay.getDeckNum().setText(String.valueOf(numCardsInDiscardPile));

        if(topDiscardCard==null) {
            discardDisplay.setVisible(false);
        } else {
            discardDisplay.getDeck().setFill(new ImagePattern(topDiscardCard.getCardImage()));
            discardDisplay.setVisible(true);
        }
    }
    private static void checkCanDoAction() {
        if (player.getNumActions()==0 || player.getHand().getDistinctActionCards().size()==0) {
            buyPhase();
        }
    }
    private static boolean checkNumBuys() {
        if(player.getNumBuys()==0) {
            endPhase();
            return false;
        }
        return true;
    }
    private static List<String> calcWinners(List<ServerPlayer> players) {
        List<String> winners = new ArrayList<>();

        int maxPoints = player.getPoints();
        winners.add(player.getName());

        for(ServerPlayer serverPlayer: players) {
            if(serverPlayer.getPoints() > maxPoints) {
                winners = new ArrayList<>();
                winners.add(serverPlayer.getName());
            } else if (serverPlayer.getPoints()==maxPoints) {
                winners.add(serverPlayer.getName());
            }
        }
        return winners;
    }

    public static void addMessageToChatLog(String msg) {
        if(controller.getChatDisplayStrings().size()>=15) controller.getChatDisplayStrings().remove(0);
        controller.getChatDisplayStrings().add(msg);
        controller.getChatType().setText(null);
        StringBuilder builder = new StringBuilder();
        for(String s: controller.getChatDisplayStrings()) {
            builder.append(s); builder.append("\n");
        }
        controller.getChatLog().setText(builder.toString());
    }
    public static void addMessageToGameLog(String msg) {
        if(controller.getGameDisplayStrings().size()>=15) controller.getGameDisplayStrings().remove(0);
        controller.getGameDisplayStrings().add(msg);
        controller.getGameLog().setText(null);
        StringBuilder builder = new StringBuilder();
        for(String s: controller.getGameDisplayStrings()) {
            builder.append(s); builder.append("\n");
        }
        controller.getGameLog().setText(builder.toString());
    }
}
