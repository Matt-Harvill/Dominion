package org.matthew.model.factory;

import org.matthew.model.CardCollection;

public class CardCollectionFactory {
    public static CardCollection getCardCollection() {
        return new CardCollection();
    }
}
