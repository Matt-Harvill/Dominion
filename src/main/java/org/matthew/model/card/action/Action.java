package org.matthew.model.card.action;

public class Action {
    private final String title,playersAffected,type,comparatorString,memoryName,numString,costString,location,doActionString;
    private Object memory;
    private boolean optional;

    public Action(String title, String playersAffected, String type, String comparatorString, String memoryName, String numString,
                  String costString, String location, Object memory, boolean optional, String doActionString) {
        this.title = title;
        this.playersAffected = playersAffected;
        this.type = type;
        this.comparatorString = comparatorString;
        this.memoryName = memoryName;
        this.numString = numString;
        this.costString = costString;
        this.location = location;
        this.memory = memory;
        this.optional = optional;
        this.doActionString = doActionString;
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
    public String getMemoryName() {
        return memoryName;
    }
    public String getNumString() {
        return numString;
    }
    public String getCostString() {return costString;}
    public String getLocation() {return location;}
    public String getDoActionString() {return doActionString;}
    public Object getMemory() {
        return memory;
    }
    public void setMemory(Object memory) {
        this.memory = memory;
    }

    public boolean isComplete(int numSelected) {
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
    public boolean tooManyCardsSelected(int numCardsSelected) {
        if(!comparatorString.equals(">=") && numCardsSelected > ActionParser.parseStringToInt(this,"num")) {
            return true;
        }
        return false;
    }

    public boolean isOptional() {
        return optional;
    }
    public boolean doAction() {return ActionParser.parseDoActionString(this);}

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
