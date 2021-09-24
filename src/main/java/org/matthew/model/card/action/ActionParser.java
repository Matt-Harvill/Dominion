package org.matthew.model.card.action;

import org.matthew.Main;
import org.matthew.model.Player;
import org.matthew.model.StackCalculator;

import java.util.Scanner;

public class ActionParser {

    private static final Player player = Main.getPlayer();
    public static Action parse(String cardAction) {
        String title = null;
        String playersAffected = "self";
        String type = "all";
        String num = "-1";
        String comparator = "=";
        String memoryName = null;
        Object memory = null;
        String costString = null;
        boolean optional = false;
        String doActionString = "true";
        String location = null;

        Scanner scanner = new Scanner(cardAction);

        while(scanner.hasNext()) {
            String msg = scanner.next();
            if(msg.contains("title_")) {
                title = msg.substring(msg.indexOf("_") + 1);
            } else if(msg.contains("comparator_")) {
                comparator = msg.substring(msg.indexOf("_") + 1);
            } else if(msg.contains("num_")) {
                num = msg.substring(msg.indexOf("_") + 1);
            } else if(msg.contains("memoryName_")) {
                memoryName = msg.substring(msg.indexOf("_") + 1);
            } else if(msg.contains("playersAffected_")) {
                playersAffected = msg.substring(msg.indexOf("_") + 1);
            } else if(msg.contains("type_")) {
                type = msg.substring(msg.indexOf("_") + 1);
            } else if(msg.contains("cost_")) {
                costString = msg.substring(msg.indexOf("_") + 1);
            } else if(msg.contains("optional_")) {
                optional = Boolean.parseBoolean(msg.substring(msg.indexOf("_") + 1));
            } else if(msg.contains("location_")) {
                location = msg.substring(msg.indexOf("_") + 1);
            } else if(msg.contains("doAction_")) {
                doActionString = msg.substring(msg.indexOf("_") + 1);
            }
        }

        return new Action(title,playersAffected,type,comparator,memoryName,num,costString,location,memory,optional,doActionString);
    }

    public static int parseStringToInt(Action action, String string) {
        int num = -1;
        String s = null;
        if(string.equals("num")) {
            s = action.getNumString();
        } else if(string.equals("cost")) {
            s = action.getCostString();
        }
//        System.out.println("numString: " + numString + " @ActionParser_parseNum");
        boolean isNumeric = s.chars().allMatch(Character::isDigit);
        if(isNumeric) {
            num = Integer.parseInt(s);
        } else {
            String operator = null;
            int opValue = 0;
            if(s.contains("-")) {
                operator = "-";
                opValue -= Integer.parseInt(s.substring(s.indexOf("-")+1));
                s = s.substring(0,s.indexOf("-"));
//                System.out.println("s: " + s + " @ActionParser_parseStringToInt");
            } else if(s.contains("+")) {
                operator = "+";
                opValue += Integer.parseInt(s.substring(s.indexOf("+")+1));
                s = s.substring(0,s.indexOf("+"));
//                System.out.println("s: " + s + " @ActionParser_parseStringToInt");
            }

            switch (s) {
                case "any": {
                    num = Integer.MAX_VALUE; break;
                }
                case "numDiscarded":
                case "costOfCardTrashed": {
                    num = (int) player.getActionCardInPlay().getMemory(s) + opValue; break;
                }
                case "numEmptyStacks": {
                    num = StackCalculator.numEmptyStacks() + opValue; break;
                }
            }
        }
//        System.out.println("num: " + num + " @ActionParser_parseNum");
        return num;
    }

    public static boolean parseDoActionString(Action action) {
        String doActionString = action.getDoActionString();
        switch (doActionString) {
            case "true": {
                return true;
            }
            case "false": {
                return false;
            }
            case "cardTrashed": {
                return (boolean) player.getActionCardInPlay().getMemory("cardTrashed");
            }
        }
        return false;
    }

}
