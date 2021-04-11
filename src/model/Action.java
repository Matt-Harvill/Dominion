package model;

import controller.ActionParser;

public class Action {
    private final String title;
    private final String playersAffected;

    private final String type;
    private final String comparator;
    private final String memoryName;
    private final String num;
    private int memory;
    private final boolean optional;

    public Action(String title, String playersAffected, String type, String comparator, String memoryName, String num, int memory, boolean optional) {
        this.title = title;
        this.playersAffected = playersAffected;
        this.type = type;
        this.comparator = comparator;
        this.memoryName = memoryName;
        this.num = num;
        this.memory = memory;
        this.optional = optional;
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
    public String getComparator() {
        return comparator;
    }
    public String getMemoryName() {
        return memoryName;
    }
    public String getNum() {
        return num;
    }
    public int getMemory() {
        return memory;
    }
    public void setMemory(int memory) {this.memory = memory;}
    public boolean isOptional() {
        return optional;
    }

    public boolean isComplete() {
        if(optional) {
            return true;
        } else {
//            parseComparator();
            return memory== ActionParser.parseNum(this);
        }
    }

    private void parseComparator() {
//        switch (comparator) {
//            case ""
//        }
    }

    @Override
    public String toString() {
        return "Action{" +
                "title='" + title + '\'' +
                ", playersAffected='" + playersAffected + '\'' +
                ", comparator='" + comparator + '\'' +
                ", memoryName='" + memoryName + '\'' +
                ", num=" + num +
                ", memory=" + memory +
                ", optional=" + optional +
                '}';
    }
}
