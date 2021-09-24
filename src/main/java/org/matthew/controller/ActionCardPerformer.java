package org.matthew.controller;

import org.matthew.Main;
import org.matthew.model.CardCollection;
import org.matthew.model.Player;
import org.matthew.model.card.Card;
import org.matthew.model.card.action.Action;
import org.matthew.model.card.action.ActionCard;
import org.matthew.model.card.action.ActionParser;
import org.matthew.model.factory.CardFactory;

public class ActionCardPerformer {

    private static final Player player = Main.getPlayer();

    public static void playActionCard() {
        startNextAction();
    }

    public static void startNextAction() {
        Action action = player.getActionCardInPlay().getAction();

        if(action==null) {
            player.getActionCardInPlay().resetActionIndex();
            actionCardCompleted();
        } else {
            startAction(action);
        }
    }

    public static void startAction(Action action) {

        int num = ActionParser.parseStringToInt(action,"num");
        boolean doAction = action.doAction();

        if(num==0 || !doAction) {
            submitAction();
            return;
        }

        if(action.getPlayersAffected().equals("self")) {
            switch (action.getTitle()) {
                case "buy": {
                    player.incrementNumBuys(num);
                    submitAction();
                    break;
                }
                case "draw": {
                    for(int i=0; i< num; i++) {
                        player.incrementHandLimit();
                        player.drawCardFromDeck();
                    }
                    submitAction();
                    break;
                }
                case "coin": {
                    player.incrementPurchasePower(num);
                    submitAction();
                    break;
                }
                case "action": {
                    player.incrementNumActions(num);
                    submitAction();
                    break;
                }
                case "discard": {
                    if(hasNoCardsOfType(action.getType())) {
                        submitAction();
                        break;
                    }
                    PhaseUpdater.discardPhase();
                    break;
                }
                case "trash": {
                    if(hasNoCardsOfType(action.getType())) {
                        submitAction();
                        break;
                    }
                    PhaseUpdater.trashPhase();
                    break;
                }
                case "gain": {
                    PhaseUpdater.gainPhase();
                    int cost = ActionParser.parseStringToInt(action,"cost");
                    if(cost<0) {
                        submitAction();
                    } else {
                        DisplayUpdater.showGainableCards(true, cost);
                    }
                    break;
                }
                case "toDeck": {
                    if(hasNoCardsOfType(action.getType())) {
                        submitAction();
                        break;
                    }
                    PhaseUpdater.toDeckPhase();
                    break;
                }
            }
        }
    }

    public static void setMemory() {
        ActionCard actionCard = player.getActionCardInPlay();
        Action action = actionCard.getAction();
        String memoryName = action.getMemoryName();
        CardCollection select = player.getSelect();

        if(memoryName.contains("numDiscarded")) {
            int memory = 0;
            if(memoryName.length() > 12) {
                String operator = memoryName.substring(12,13);
                memory += parseOperator(action, memoryName, operator);
            }
            action.setMemory(player.getSelect().getSize() + memory);
        }
        else if(memoryName.contains("costOfCardTrashed")) {
            int memory = 0;
            if(memoryName.length() > 17) {
                String operator = memoryName.substring(17, 18);
                memory += parseOperator(action, memoryName, operator);
            }
            Card lastCard = select.peekLastCard();
            if(lastCard==null) {
                //set cost of card trashed to MIN_VALUE
                action.setMemory(Integer.MIN_VALUE);
            } else {
                action.setMemory(lastCard.getCost() + memory);
            }
        }
        else if(memoryName.contains("cardTrashed")) {
            boolean cardTrashed = select.getSize() == 1;
            action.setMemory(cardTrashed);
        }
    }
    private static int parseOperator(Action action, String memoryName, String operator) {
        int returnVal = 0;
        switch (operator) {
            case "+": {
                returnVal += Integer.parseInt(memoryName.substring(13));
                break;
            }
            case "-": {
                returnVal -= Integer.parseInt(memoryName.substring(13));
                break;
            }
        }
        return returnVal;
    }

    public static boolean actionComplete(int numSelected) {
        ActionCard actionCard = player.getActionCardInPlay();
        Action action = actionCard.getAction();

        if(action.tooManyCardsSelected(numSelected)) {
            return false;
        } else if(hasNoCardsOfType(action.getType())) {
            return true;
        } else {
            return action.isComplete(numSelected);
        }
    }
    public static boolean actionComplete() {
        ActionCard actionCard = player.getActionCardInPlay();
        Action action = actionCard.getAction();
        if(action.isOptional()) {
            return true;
        }
        else {
            return hasNoCardsOfType(action.getType());
        }
    }
    public static boolean hasNoCardsOfType(String type) {
        CardCollection hand = player.getHand();
        switch (type) {
            case "all": {
                return hand.getSize()==0;
            }
            case "treasureCard": {
                return hand.getDistinctTreasureCards().size()==0;
            }
            case "victoryCard": {
                return hand.getDistinctVictoryCards().size()==0;
            }
            case "actionCard": {
                return hand.getDistinctActionCards().size()==0;
            }
            case "Copper": {
                return hand.numCardInCollection(CardFactory.getCard(type))==0;
            }
        }
        System.out.println("Error @ACP_hasNoCardsOfType");
        return true;
    }

    public static void submitAction() {
        ActionCard actionCard = player.getActionCardInPlay();
        Action action = actionCard.getAction();
        String phase = player.getPhase();

        if(action.getMemoryName()!=null) {
            setMemory();
        }

        switch (phase) {
            case "discardPhase":
                player.selectToLocation("discard");
                break;
            case "trashPhase":
                player.selectToLocation("trash");
                break;
            case "toDeckPhase":
                player.selectToLocation("deck");
                break;
//            case "gainPhase":
//                System.out.println("@ACP_submitAction gainPhase switch entered, location: " + action.getLocation());
//                player.selectToLocation(action.getLocation());
//                break;
        }

        DisplayUpdater.updateHandDisplay();
        DisplayUpdater.updateInPlayDisplay(player.getInPlay(), player.getName(), -1,true);
        DisplayUpdater.updatePlayerLabel(player.getName(), player.getPoints());

        actionCard.incrementActionIndex();
        startNextAction();
    }

    public static void actionCardCompleted() {
        player.decrementNumActions();
        player.resetActionCardInPlay();
        //changed checkCanDoAction to actionPhase
        PhaseUpdater.actionPhase();

        DisplayUpdater.updateHandDisplay();
        DisplayUpdater.updateInPlayDisplay(player.getInPlay(), player.getName(), -1,true);
        DisplayUpdater.updatePlayerLabel(player.getName(), player.getPoints());
    }
}
