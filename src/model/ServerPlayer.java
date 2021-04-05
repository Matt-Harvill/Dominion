package model;

import model.card.Card;

public class ServerPlayer {

    private String name;
    private int points;
    private CardCollection inPlay;

    public ServerPlayer(String name, int points) {
        this.name = name;
        this.points = points;
        resetInPlay();
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
    public void addCardInPlay(Card card) {
        inPlay.addCardToCollection(card);
    }
    public void resetInPlay() {
        inPlay = new CardCollection();
    }
    public CardCollection getInPlay() {return inPlay;}
}
