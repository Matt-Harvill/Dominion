package model;

public class ServerPlayer {

    private String name;
    private int points;

    public ServerPlayer(String name, int points) {
        this.name = name;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    public String getName() {
        return name;
    }
}
