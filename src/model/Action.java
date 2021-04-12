package model;

import controller.ActionParser;

public class Action {
    private final String title;
    private final String playersAffected;

    private final String type,comparatorString,memoryName,numString,costString;
    private int memory, numSelected;

    public Action(String title, String playersAffected, String type, String comparatorString, String memoryName, String numString, String costString, int memory) {
        this.title = title;
        this.playersAffected = playersAffected;
        this.type = type;
        this.comparatorString = comparatorString;
        this.memoryName = memoryName;
        this.numString = numString;
        this.costString = costString;
        this.memory = memory;
        this.numSelected = 0;
    }

    public String getType() {
        return type;
    }
    public String getTitle() {
        return title;
    }
    public String getPlayersAffected() {
        return playersAffected;
    }
    public String getComparatorString() {
        return comparatorString;
    }
    public String getMemoryName() {
        return memoryName;
    }
    public String getNumString() {
        return numString;
    }
    public String getCostString() {return costString;}
    public int getMemory() {
        return memory;
    }
    public void setMemory(int memory) {
        this.memory = memory;
    }
    public void setNumSelected(int numSelected) {
        this.numSelected = numSelected;
    }

    public boolean isComplete() {
        switch (comparatorString) {
            case "<=": {
                return numSelected <= ActionParser.parseStringToInt(this,"num");
            }
            case ">=": {
                return numSelected >= ActionParser.parseStringToInt(this,"num");
            }
            case "=": {
                return numSelected == ActionParser.parseStringToInt(this,"num");
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Action{" +
                "title='" + title + '\'' +
                ", playersAffected='" + playersAffected + '\'' +
                ", comparator='" + comparatorString + '\'' +
                ", memoryName='" + memoryName + '\'' +
                ", num=" + numString +
                ", memory=" + memory +
                '}';
    }
}
