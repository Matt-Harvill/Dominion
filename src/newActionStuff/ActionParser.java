package newActionStuff;

import controller.Main;
import model.Player;

import java.util.Scanner;

public class ActionParser {

    private static final Player player = Main.getPlayer();
    public static Action parse(String cardAction) {
        String title = null;
        String playersAffected = "self";
        String type = "all";
        String num = "-1";
        boolean optional = false;
        String comparator = null;
        String memoryName = null;
        int memory = -1;

        Scanner scanner = new Scanner(cardAction);
        while(scanner.hasNext()) {
            String msg = scanner.next();
            if(msg.contains("title_")) {
                title = msg.substring(6);
            } else if(msg.contains("comparator_")) {
                comparator = msg.substring(11);
            } else if(msg.contains("num_")) {
                num = msg.substring(4);
            } else if(msg.contains("memoryName_")) {
                memoryName = msg.substring(11);
            } else if(msg.contains("playersAffected_")) {
                playersAffected = msg.substring(16);
            } else if(msg.contains("optional_")) {
                String option = msg.substring(9);
                optional = Boolean.parseBoolean(option);
            } else if(msg.contains("type_")) {
                type = msg.substring(5);
            }
        }

        return new Action(title,playersAffected,type,comparator,memoryName,num,memory,optional);
    }

    public static int parseNum(Action action) {
        int num = -1;
        String numString = action.getNum();
        System.out.println("numString: " + numString + " @ActionParser_parseNum");
        boolean isNumeric = numString.chars().allMatch(Character::isDigit);
        if(isNumeric) {
            num = Integer.parseInt(numString);
        } else {
            switch (numString) {
                case "any": {
                    num = Integer.MAX_VALUE; break;
                }
                case "numDiscarded": {
                    num = player.getActionCardInPlay().getMemory(numString); break;
                }
            }
        }
        System.out.println("num: " + num + " @ActionParser_parseNum");
        return num;
    }

}
