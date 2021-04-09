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
        displayCards(controller.getCIHDisplays());
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
        displayCards(controller.getCIPDisplays());
        displayCards(controller.getCIHDisplays());
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
        displayCards(controller.getCIPDisplays());
        displayCards(controller.getCIHDisplays());
        displayPlayerDeckDisplay(controller.getPlayerDeckDisplay(), true);
        controller.getActionButton().setText("End Turn");
    }
    public static void endPhase() {
        player.setPhase("endPhase");
        showBuyableCards(false);
        player.endTurn();
        displayPlayerDiscard();
        displayCards(controller.getCIPDisplays());
        displayCards(controller.getCIHDisplays());
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
//        displayCards(controller.getCIPDisplays());
        displayCards(controller.getCIHDisplays());
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
        displayCards(controller.getCIPDisplays());
        displayCards(controller.getCIHDisplays());
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
        CardDisplay[] display = controller.getCIHDisplays();
        resetCardDisplays(display);

        int index = 0;
        int freqOfCardInARow = 1;
        CardCollection inPlay = otherPlayer.getInPlay();
        List<Card> cardList = inPlay.getCollection();

        for(int i=0; i<cardList.size();i++) {
            if (i != cardList.size() - 1 && cardList.get(i).equals(cardList.get(i + 1))) {
                freqOfCardInARow++;
                continue;
            }
            setCardDisplay(display[index], cardList.get(i),freqOfCardInARow);
            index++;
            freqOfCardInARow = 1;
        }
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
    private static void displayCards(CardDisplay[] cardDisplays) {
        resetCardDisplays(cardDisplays);

        CardCollection hand = player.getHand();
        List<ActionCard> actionCards = hand.getDistinctActionCards();
        List<TreasureCard> treasureCards = hand.getDistinctTreasureCards();
        List<VictoryCard> victoryCards = hand.getDistinctVictoryCards();

        if(cardDisplays.equals(controller.getCIHDisplays())) {
            int index = 0;
            for(ActionCard actionCard: actionCards) {
                setCardDisplay(cardDisplays[index],actionCard,hand.numCardInCollection(actionCard));
                if(player.getPhase().equals("actionPhase")) {
                    cardDisplays[index].setStyle(controller.getGreenCardGlowStyle());
                }
                index++;
            }
            for(TreasureCard treasureCard: treasureCards) {
                setCardDisplay(cardDisplays[index],treasureCard,hand.numCardInCollection(treasureCard));
                if(player.getPhase().equals("buyPhase")) {
                    cardDisplays[index].setStyle(controller.getGreenCardGlowStyle());
                }
                index++;
            }
            for(VictoryCard victoryCard: victoryCards) {
                setCardDisplay(cardDisplays[index],victoryCard,hand.numCardInCollection(victoryCard));
                index++;
            }
        } else if(cardDisplays.equals(controller.getCIPDisplays())) {
            int index = 0;
            int freqOfCardInARow = 1;
            CardCollection inPlay = player.getInPlay();
            List<Card> cardList = inPlay.getCollection();

            for(int i=0; i<cardList.size();i++) {
                if(i!=cardList.size()-1 && cardList.get(i).equals(cardList.get(i+1))) {
                    freqOfCardInARow++; continue;
                }
                setCardDisplay(cardDisplays[index],cardList.get(i),freqOfCardInARow);
                index++;
                freqOfCardInARow=1;
            }
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
    private static void setCardDisplay(CardDisplay cardDisplay,Card card, int numCard) {
        cardDisplay.setCard(card);
        cardDisplay.setNum(numCard);
        cardDisplay.show();
    }
    private static void resetCardDisplays(CardDisplay[] cardDisplays) {
        for(CardDisplay cardDisplay: cardDisplays) {
            cardDisplay.setCard(null);
            cardDisplay.setStyle(null);
            cardDisplay.hide();
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
