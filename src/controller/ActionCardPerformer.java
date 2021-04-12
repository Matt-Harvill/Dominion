package controller;

import model.Action;
import model.CardCollection;
import model.Player;
import model.card.ActionCard;
import model.card.Card;

import java.util.List;

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

        if(num==0) {
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
//                    System.out.println("action num: " + num + " @ACP_startAction");
                    player.incrementNumActions(num);
                    submitAction();
                    break;
                }
                case "discard": {
                    PhaseUpdater.discardPhase();
                    break;
                }
                case "trash": {
                    PhaseUpdater.trashPhase();
                    break;
                }
                case "gain": {
                    PhaseUpdater.gainPhase();
                    DisplayUpdater.showGainableCards(true, ActionParser.parseStringToInt(action,"cost"));
                    break;
                }
            }
        }
    }

    public static void setMemory() {
        ActionCard actionCard = player.getActionCardInPlay();
        Action action = actionCard.getAction();
        String memoryName = action.getMemoryName();

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
            CardCollection select = player.getSelect();
            Card lastCard = select.peekLastCard();
            action.setMemory(lastCard.getCost() + memory);
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
        return action.isComplete(numSelected);
    }
    public static boolean actionComplete() {
        ActionCard actionCard = player.getActionCardInPlay();
        Action action = actionCard.getAction();
        return action.isOptional();
    }

    public static void submitAction() {
        ActionCard actionCard = player.getActionCardInPlay();
        Action action = actionCard.getAction();

        if(action.getMemoryName()!=null) {
            setMemory();
        }
        //have a switch case for what info to use as object memory
        DisplayUpdater.updateHandDisplay();
        DisplayUpdater.updateInPlayDisplay(player.getInPlay(), player.getName(), -1,true);
        DisplayUpdater.updatePlayerLabel(player.getName(), player.getPoints());

        if(player.getPhase().equals("discardPhase")) {
            player.discardSelect();
        } else if(player.getPhase().equals("trashPhase")) {
            player.trashSelect();
        }

//        PhaseUpdater.actionPhase();
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
