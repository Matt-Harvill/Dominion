package org.matthew.model.card;

import javafx.scene.image.Image;

public class BlankCard extends Card {
    public BlankCard(String name, int cost, Image cardImage) {
        super(name, cost, cardImage, null,null);
    }
}
