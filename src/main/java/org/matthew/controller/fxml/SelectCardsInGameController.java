package org.matthew.controller.fxml;

import  org.matthew.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import org.matthew.model.card.Card;
import org.matthew.model.factory.CardFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class SelectCardsInGameController {

    @FXML
    Button pageButton1,pageButton2,pageButton3,pageButton4,pageButton5,pageButton6,pageButton7,pageButton8,pageButton9;

    @FXML
    Rectangle card1,card2,card3,card4,card5,card6,card7,card8,card9,card10,
            card11,card12,card13,card14,card15,card16,card17,card18,card19,card20,
            card21,card22,card23,card24,card25;
    Rectangle[] cards;
    List<Boolean> cardsSelected;

    private List<Card> allActionCards;
    private List<Card> cardsChosen;

    private int currentPageNum = 1;

    public void initialize() throws IOException {
        cards = new Rectangle[]{card1,card2,card3,card4,card5,card6,card7,card8,card9,card10,
                card11,card12,card13,card14,card15,card16,card17,card18,card19,card20,
                card21,card22,card23,card24,card25};

        InputStream actionCardsInGame = (Main.class.getResource("AllCards.txt")).openStream();
        Scanner scanner = new Scanner(actionCardsInGame);

        allActionCards = new ArrayList<>();
        int index = 0;
        while(scanner.hasNext()) {
            while(scanner.hasNext()) {
                String cardName = scanner.next();
                if(cardName.equals("Next_Expansion")) {
                    while((index% cards.length)!=0) {
                        allActionCards.add(null);
                        index++;
                    }
                    break;
                }
                allActionCards.add(CardFactory.getCard(cardName));
                index++;
            }
        }

        cardsSelected = new ArrayList<>();
        for(int i=0; i<allActionCards.size(); i++) {
            cardsSelected.add(false);
        }

        cardsChosen = new ArrayList<>();
        if(Main.getServer()!=null && Main.getServer().getCardsInGame().getCollection().size()!=0) {
            cardsChosen.addAll(Main.getServer().getCardsInGame().getCollection());
        }

        for (Card card : cardsChosen) {
            index = allActionCards.indexOf(card);
            cardsSelected.set(index, true);
            System.out.println(card.toString());
        }

        displayPage();
    }

    public void goToPage(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();

        switch (button.getText()) {
            case "Base": {
                currentPageNum = 1;
                break;
            }
            case "Intrigue": {
                currentPageNum = 2;
                break;
            }
//            case 3:
//            case 4:
//            case 5:
//            case 6:
//            case 7:
//            case 8:
//            case 9:
        }
        displayPage();
    }

    private void displayPage() {
        for(int i=0; i< cards.length; i++) {
            if((i+((currentPageNum-1)*cards.length))<allActionCards.size() &&
                    allActionCards.get(i+((currentPageNum-1)*cards.length))!=null) {
                cards[i].setFill(new ImagePattern(allActionCards.get(i+((currentPageNum-1)*cards.length)).getCardImage()));
                if(cardsSelected.get(i+(currentPageNum-1)*cards.length)) {
                    cards[i].setStyle("-fx-stroke-width: 3; -fx-stroke: #54ff54;");
                } else {
                    cards[i].setStyle(null);
                }
                cards[i].setVisible(true);
            } else {
                cards[i].setVisible(false);
            }
        }
    }

    public void cardClicked(MouseEvent mouseEvent) {
        Rectangle card  = (Rectangle) mouseEvent.getSource();

        int index = -1;
        for(int i=0; i<cards.length; i++) {
            if (card.equals(cards[i])) {
                index = i;
            }
        }
        Card cardObject = allActionCards.get(index + cards.length*(currentPageNum-1));

        if(cardsSelected.get(index + cards.length*(currentPageNum-1))) {
            cardsSelected.set(index + cards.length*(currentPageNum-1),false);
            card.setStyle(null);
            cardsChosen.remove(cardObject);
            Main.getServer().getCardsInGame().removeCardFromCollection(cardObject);
        } else {
            cardsSelected.set(index + cards.length*(currentPageNum-1),true);
            card.setStyle("-fx-stroke-width: 3; -fx-stroke: #54ff54;");
            System.out.println("\ncardsChosen @SCIGController_cardClicked before:");
            for(Card card2: cardsChosen) System.out.println(card2.getName() + " ");
            cardsChosen.add(cardObject);
            System.out.println("cardsChosen @SCIGController_cardClicked after:");
            for(Card card2: cardsChosen) System.out.println(card2.getName() + " ");
            System.out.println("CardsInGame @SCIGController_cardClicked before:");
            Main.getServer().getCardsInGame().printCardNamesInCollection();
            Main.getServer().getCardsInGame().addCardToCollection(cardObject);
            System.out.println("CardsInGame @SCIGController_cardClicked after:");
            Main.getServer().getCardsInGame().printCardNamesInCollection();
            System.out.println();
        }



        Main.getHostJoinController().updateActionCardInGameSlots(cardsChosen);
    }
}
