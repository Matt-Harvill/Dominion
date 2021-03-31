package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import model.card.ActionCard;
import model.card.Card;
import model.factory.CardFactory;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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

    private List<ActionCard> allActionCards;
    private List<ActionCard> cardsChosen;

    private int currentPageNum = 1;

    public void initialize() throws FileNotFoundException {
        File actionCardsInGame = new File("src/resources/AllActionCards.txt");
        Scanner scanner = new Scanner(actionCardsInGame);

        allActionCards = new ArrayList<>();
        while(scanner.hasNext()) {
            String cardName = scanner.next();
            int numCard = scanner.nextInt();
            allActionCards.add((ActionCard) CardFactory.getCard(cardName));
        }

        cards = new Rectangle[]{card1,card2,card3,card4,card5,card6,card7,card8,card9,card10,
                card11,card12,card13,card14,card15,card16,card17,card18,card19,card20,
                card21,card22,card23,card24,card25};

        cardsSelected = new ArrayList<>();
        for(int i=0; i<allActionCards.size(); i++) {
            cardsSelected.add(false);
        }

        if(Main.getServer().getActionCardsInGame()==null) {
            cardsChosen = new ArrayList<>();
        } else {
            cardsChosen = Main.getServer().getActionCardsInGame();
        }

        System.out.println(cardsChosen.size());

        for(int i=0; i<cardsChosen.size(); i++) {
            int index = allActionCards.indexOf(cardsChosen.get(i));
            cardsSelected.set(index,true);
        }

        displayPage1();
    }

    public void goToPage(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();

        switch (Integer.parseInt(button.getText())) {
            case 1: displayPage1(); currentPageNum = 1; break;
            case 2: displayPage2(); currentPageNum = 2; break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
        }

    }

    private void displayPage1() {
        for(int i=0; i<25; i++) {
            if(i<allActionCards.size()) {
                cards[i].setFill(new ImagePattern(allActionCards.get(i).getCardImage()));
                if(cardsSelected.get(i)) {
                    cards[i].setStyle("-fx-stroke-width: 3; -fx-stroke: #54ff54;");
                } else {
                    cards[i].setStyle(null);
                }
            } else {
                cards[i].setVisible(false);
            }
        }
    }
    private void displayPage2() {

    }

    public void cardClicked(MouseEvent mouseEvent) {
        Rectangle card  = (Rectangle) mouseEvent.getSource();

        int index = -1;
        for(int i=0; i<cards.length; i++) {
            if (card.equals(cards[i])) {
                index = i;
            }
        }
        ActionCard cardObject = allActionCards.get(index + 25*(currentPageNum-1));

        System.out.println(cardsSelected.get(index));

        if(cardsSelected.get(index)) {
            cardsSelected.set(index,false);
            card.setStyle(null);
            cardsChosen.remove(cardObject);
        } else {
            cardsSelected.set(index,true);
            card.setStyle("-fx-stroke-width: 3; -fx-stroke: #54ff54;");
            cardsChosen.add(cardObject);
        }

        System.out.println(cardsChosen.toString());

        Main.getServer().setActionCardsInGame(cardsChosen);
        Main.getHostJoinController().updateActionCardInGameSlots(cardsChosen);
    }
}
