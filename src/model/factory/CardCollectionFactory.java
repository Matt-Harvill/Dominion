package model.factory;

import model.CardCollection;

public class CardCollectionFactory {
    public static CardCollection getCardCollection() {
        return new CardCollection();
    }
}
