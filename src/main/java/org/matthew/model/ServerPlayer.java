package org.matthew.model;

import org.matthew.model.card.Card;

public class ServerPlayer {

    private String name;
    private int points, numCardsInDeck;
    private CardCollection inPlay;

    public ServerPlayer(String name, int points, int numCardsInDeck) {
        this.name = name;
        this.points = points;
        this.numCardsInDeck = numCardsInDeck;
        resetInPlay();
    }

    public void updateInfo(ServerPlayer serverPlayer) {
        this.name = serverPlayer.getName();
        this.points = serverPlayer.getPoints();
        this.numCardsInDeck = serverPlayer.getNumCardsInDeck();
    }
    public int getPoints() {
        return points;
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
    public int getNumCardsInDeck() {
        return numCardsInDeck;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        final ServerPlayer other = (ServerPlayer) obj;
        return name.equals(other.getName());
    }
}
