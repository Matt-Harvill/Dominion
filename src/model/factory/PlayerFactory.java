package model.factory;

import model.*;

public class PlayerFactory {
    public static Player getPlayer(String playerName) {
        return new Player(playerName);
    }
}
