package cardStuff;

public class CardFactory {

    public static Card getCard(String cardType) {
        switch (cardType) {
            case "Copper": return new ActionCard(cardType);
        }
        return null;
    }
}
