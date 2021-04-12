package model;

import controller.ActionParser;

public class Action {
    private final String title;
    private final String playersAffected;

    private final String type;
    private final String comparatorString;
    private final String memoryName;
    private final String numString;
    private int memory;

    public Action(String title, String playersAffected, String type, String comparatorString, String memoryName, String numString, int memory) {
        this.title = title;
        this.playersAffected = playersAffected;
        this.type = type;
        this.comparatorString = comparatorString;
        this.memoryName = memoryName;
        this.numString = numString;
        this.memory = memory;
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
    public int getMemory() {
        return memory;
    }
    public void setMemory(int memory) {this.memory = memory;}

    public boolean isComplete() {
        switch (comparatorString) {
            case "<=": {
                return memory <= ActionParser.parseNum(this);
            }
            case ">=": {
                return memory >= ActionParser.parseNum(this);
            }
            case "=": {
                return memory == ActionParser.parseNum(this);
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
