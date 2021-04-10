package newActionStuff;

import controller.ActionHandler;
import controller.DisplayUpdater;
import controller.Main;
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
            ActionCardPerformer.actionCardCompleted();
            player.getActionCardInPlay().resetActionIndex();
        } else {
            startAction(action);
        }
    }

    public static void startAction(Action action) {

        int num = ActionParser.parseNum(action);

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
                    player.setPhase("discardPhase");
                    DisplayUpdater.updateHandDisplay();
                    break;
                }
            }
        }
    }

    public static void checkActionComplete() {
//        if(action.complete()) {
//            //display submit button and possibly hide other stuff
//        } else {
//            //hide submit button
//        }
    }

    public static void submitAction() {
        ActionCard actionCard = player.getActionCardInPlay();
        Action action = actionCard.getAction();

        //have a switch case for what info to use as object memory
        DisplayUpdater.updateHandDisplay();
        DisplayUpdater.updateInPlayDisplay(player.getInPlay(), player.getName(), -1,true);
        DisplayUpdater.updatePlayerLabel(player.getName(), player.getPoints());

        if(action.getMemoryName()!=null) {
            action.setMemory(action.getMemory());
        }

        player.setPhase("actionPhase");
        actionCard.incrementActionIndex();
        startNextAction();
    }

    public static void actionCardCompleted() {
        player.decrementNumActions();
        ActionHandler.checkCanDoAction();
    }
}
