package controller;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.CardCollection;
import model.Player;
import model.card.ActionCard;
import model.card.Card;
import model.card.TreasureCard;
import model.card.VictoryCard;
import model.factory.CardFactory;
import view.CardSupplyDisplay;
import view.HandOrInPlayDisplay;
import view.PlayerNamePointsDisplay;

import java.util.ArrayList;
import java.util.List;

public class PlayerActionMediator {

    private Player player;
    private GameController controller;
    private String phase;
    private final int maxNumCardsInHandOrPlayDisplay = 11;

    private Card[] cardsInHandInDisplayOrder;
    private Card[] cardsInPlayInDisplayOrder;

    public PlayerActionMediator(Player player, GameController controller) {
        this.player = player;
        this.controller = controller;
        cardsInHandInDisplayOrder = new Card[maxNumCardsInHandOrPlayDisplay];
        cardsInPlayInDisplayOrder = new Card[maxNumCardsInHandOrPlayDisplay];
        startPhase();
    }


    public void startPhase() {
        phase = "startPhase";
        player.newTurn();
//        displayHand();
//        actionPhase();
        displayHandOrInPlay(controller.getPlayerHandDisplay());
    }
    public void actionPhase() {
        controller.getActionBar().setVisible(true);
        phase = "actionPhase";
        displayHandOrInPlay(controller.getPlayerHandDisplay());
        displayHandOrInPlay(controller.getInPlayDisplay());
        controller.getActionButton().setText("Enter Buy Phase");
        checkCanDoAction();
    }
    public void buyPhase() {
        phase = "buyPhase";
        enableBuying(true);
        displayHandOrInPlay(controller.getPlayerHandDisplay());
        displayHandOrInPlay(controller.getInPlayDisplay());
        controller.getActionButton().setText("End Turn");
    }
    public void endPhase() {
        phase = "endPhase";
        enableBuying(false);
        player.endTurn();
        displayHandOrInPlay(controller.getPlayerHandDisplay());
        displayHandOrInPlay(controller.getInPlayDisplay());
        controller.getActionButton().setText("Start Turn");

        controller.getActionBar().setVisible(false);
        startPhase();
        Main.getServerSender().endTurn();
    }

    public void buyFromCardSupply(Card cardClicked) {

        CardSupplyDisplay display = controller.getCardSupplyDisplay();
        Card[] cards = display.getCardObjectsInSupply();
        Text[] cardNumbers = display.getCardInSupplyNums();

        int index = -1;
        for(int i=0;i<cards.length;i++) {
            if(cards[i] != null && cards[i].equals(cardClicked)) index = i;
        }

        Text cardNumber = cardNumbers[index];
        int numCardRemaining = Integer.parseInt(cardNumber.getText());
        int costOfCard = cardClicked.getCost();

        player.buyCard(cardClicked);
        addMessageToGameLog("You purchased a " + cardClicked.getName());

        cardNumber.setText(String.valueOf(numCardRemaining-1));

        displayHandOrInPlay(controller.getPlayerHandDisplay());
        displayPlayerLabel();
        checkNumBuys();
    }

    public void playCard(Card cardClicked) {
        for(Card card: player.getHand().getCollection()) {
            if(card.equals(cardClicked)) {
                player.playCard(card);
                break;
            }
        }
        addMessageToGameLog("You played a " + cardClicked.getName());
        displayHandOrInPlay(controller.getPlayerHandDisplay());
        displayHandOrInPlay(controller.getInPlayDisplay());
        displayPlayerLabel();
        checkCanDoAction();
    }

    private void enableBuying(boolean enable) {
        CardSupplyDisplay cardSupplyDisplay = controller.getCardSupplyDisplay();

        Rectangle[] cardBuyButtons = cardSupplyDisplay.getCardBuyButtons();
        Rectangle[] cards = cardSupplyDisplay.getCardsInSupply();
        Text[] cardNums = cardSupplyDisplay.getCardInSupplyNums();
        for(int i=0;i<cards.length;i++) {
            if(cards[i].isVisible() && cardSupplyDisplay.getCardObjectsInSupply()[i].getCost()<=player.getPurchasePower()
            && Integer.parseInt(cardNums[i].getText())>0) {
                cardBuyButtons[i].setVisible(enable);
            } else {
                cardBuyButtons[i].setVisible(false);
            }
        }
    }

    private void displayHandOrInPlay(HandOrInPlayDisplay display) {
        resetHandOrInPlayDisplay(display);

        if(display.getNameOfDisplay().equals("playerHandDisplay")) {
            int index = 0;
            CardCollection hand = player.getHand();
            List<ActionCard> actionCards = hand.getDistinctActionCards();
            List<TreasureCard> treasureCards = hand.getDistinctTreasureCards();
            List<VictoryCard> victoryCards = hand.getDistinctVictoryCards();
            Rectangle[] cardsInHand = controller.getPlayerHandDisplay().getCardsInHandOrInPlay();
            for(ActionCard actionCard: actionCards) {
                int numCard = hand.numCardInCollection(actionCard);
                displayCardInHandOrInPlay(display,actionCard,index,numCard);
                if(phase.equals("actionPhase")) {
                    cardsInHand[index].setStyle(controller.getGreenCardGlowStyle());
                }
                cardsInHandInDisplayOrder[index] = actionCard;
                index++;
            }
            for(TreasureCard treasureCard: treasureCards) {
                int numCard = hand.numCardInCollection(treasureCard);
                displayCardInHandOrInPlay(display,treasureCard,index,numCard);
                if(phase.equals("buyPhase")) {
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
        StringBuilder gameInfoString = new StringBuilder();
        if(phase.equals("actionPhase")) {
            gameInfoString.append("Number of Actions: " + player.getNumActions() + "   ");
        }
        else if(phase.equals("buyPhase")) {
            gameInfoString.append("Number of Buys Remaining : " + player.getNumBuys() + "   ");
            gameInfoString.append("Purchase Power: " + player.getPurchasePower());
        }
        gameInfoText.setText(String.valueOf(gameInfoString));

    }
    private void displayCardInHandOrInPlay(HandOrInPlayDisplay display,Card card, int index, int numCard) {
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
    private void resetHandOrInPlayDisplay(HandOrInPlayDisplay display) {
        for(int i=0; i<display.getCardsInHandOrInPlay().length; i++) {
            display.getCardsInHandOrInPlay()[i].setVisible(false);
            display.getCardsInHandOrInPlay()[i].setStyle(null);
            display.getCardInHandOrInPlayNumBacks()[i].setVisible(false);
            display.getCardInHandOrInPlayNums()[i].setVisible(false);
        }
        if(display.getNameOfDisplay().equals("playerHandDisplay")) {
            cardsInHandInDisplayOrder = new Card[maxNumCardsInHandOrPlayDisplay];
        } else if(display.getNameOfDisplay().equals("inPlayDisplay")) {
            cardsInPlayInDisplayOrder = new Card[maxNumCardsInHandOrPlayDisplay];
        }
    }

    private void checkCanDoAction() {
        if (player.getNumActions()==0 || player.getHand().getDistinctActionCards().size()==0) {
            buyPhase();
        }
    }
    private void checkNumBuys() {
        if(player.getNumBuys()==0) {
            endPhase();
        }
    }

    public void addMessageToChatLog(String msg) {
        if(controller.getChatDisplayStrings().size()>=9) controller.getChatDisplayStrings().remove(0);
        controller.getChatDisplayStrings().add(msg);
        controller.getChatType().setText(null);
        StringBuilder builder = new StringBuilder();
        for(String s: controller.getChatDisplayStrings()) {
            builder.append(s); builder.append("\n");
        }
        controller.getChatLog().setText(builder.toString());
    }
    public void addMessageToGameLog(String msg) {
        if(controller.getGameDisplayStrings().size()>=9) controller.getGameDisplayStrings().remove(0);
        controller.getGameDisplayStrings().add(msg);
        controller.getGameLog().setText(null);
        StringBuilder builder = new StringBuilder();
        for(String s: controller.getGameDisplayStrings()) {
            builder.append(s); builder.append("\n");
        }
        controller.getGameLog().setText(builder.toString());
    }

    public void displayPlayerLabel() {
        PlayerNamePointsDisplay display = controller.getPlayerNamePointsDisplay();

        Text[] playerLabelNames = display.getPlayerLabelNames();
        Text[] playerPoints = display.getPlayerLabelVictoryNums();

        for(int i=0; i<playerLabelNames.length; i++) {
            if(player.getName().equals(playerLabelNames[i].getText()) || playerLabelNames[i].getText().equals("")) {
                playerLabelNames[i].setText(player.getName());
                playerPoints[i].setText(String.valueOf(player.getTotalPoints()));
                display.getPlayerLabels()[i].setVisible(true);
                display.getPlayerLabelVictories()[i].setVisible(true);
                playerLabelNames[i].setVisible(true);
                playerPoints[i].setVisible(true);
                break;
            }
        }
    }
}
