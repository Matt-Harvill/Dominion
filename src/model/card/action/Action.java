package model.card.action;

public class Action {
    private final String title;
    private final String playersAffected;

    private final String type,comparatorString,memoryName,numString,costString;
    private int memory;
    private boolean optional;

    public Action(String title, String playersAffected, String type, String comparatorString, String memoryName, String numString, String costString, int memory, boolean optional) {
        this.title = title;
        this.playersAffected = playersAffected;
        this.type = type;
        this.comparatorString = comparatorString;
        this.memoryName = memoryName;
        this.numString = numString;
        this.costString = costString;
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

    public boolean isOptional() {
        return optional;
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
