package controller;

import model.Action;
import model.CardCollection;
import model.Player;
import model.card.ActionCard;

public class ActionCardPerformer {

    private static final Player player = Main.getPlayer();

    public static void playActionCard() {
        startNextAction();
    }

    public static void startNextAction() {
        Action action = player.getActionCardInPlay().getAction();

        if(action==null) {
            actionCardCompleted();
            player.getActionCardInPlay().resetActionIndex();
        } else {
            startAction(action);
        }
    }

    public static void startAction(Action action) {

        int num = ActionParser.parseNum(action);

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
                    System.out.println("action num: " + num + " @ACP_startAction");
                    player.incrementNumActions(num);
                    submitAction();
                    break;
                }
                case "discard": {
                    player.setPhase("discardPhase");
                    DisplayUpdater.updateHandDisplay();
                    Main.getGameController().getSwitchCardViewButton().setText("View Cards In Play");
                    break;
                }
            }
        }
    }

    public static void setMemory() {
        ActionCard actionCard = player.getActionCardInPlay();
        Action action = actionCard.getAction();
        CardCollection select = player.getSelect();
        System.out.println("printing cards in Select @ACP_setMemory");
        select.printCardNamesInCollection();
        action.setMemory(player.getSelect().getSize());
    }
    public static boolean checkActionComplete() {
        ActionCard actionCard = player.getActionCardInPlay();
        Action action = actionCard.getAction();
        return action.isComplete();
    }

    public static void submitAction() {
        ActionCard actionCard = player.getActionCardInPlay();
        Action action = actionCard.getAction();

        setMemory();
        //have a switch case for what info to use as object memory
        DisplayUpdater.updateHandDisplay();
        DisplayUpdater.updateInPlayDisplay(player.getInPlay(), player.getName(), -1,true);
        DisplayUpdater.updatePlayerLabel(player.getName(), player.getPoints());

//        if(action.getMemoryName()!=null) {
//            action.setMemory(action.getMemory());
//        }
        if(player.getPhase().equals("discardPhase")) {
            player.discardSelect();
        }

        player.setPhase("actionPhase");
        actionCard.incrementActionIndex();
        startNextAction();
    }

    public static void actionCardCompleted() {
        player.decrementNumActions();
        GUIInputHandler.checkCanDoAction();

        DisplayUpdater.updateHandDisplay();
        DisplayUpdater.updateInPlayDisplay(player.getInPlay(), player.getName(), -1,true);
        DisplayUpdater.updatePlayerLabel(player.getName(), player.getPoints());
    }
}
