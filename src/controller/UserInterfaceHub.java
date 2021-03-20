package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Client;
import model.ClientSideConnection;
import model.card.ActionCard;
import model.card.Card;
import model.factory.CardFactory;

import java.util.ArrayList;
import java.util.List;

public class UserInterfaceHub extends Application {

    private static Parent gameUIScene, serverConnectScene, setNameScene;
    private static Stage window;

    private static GameController gameController;
    private static Client client;
    private static ClientSideConnection clientSideConnection;

    @Override
    public void start(Stage primaryStage) throws Exception{

        window = primaryStage;

        client = new Client();

        FXMLLoader gameUILoader = new FXMLLoader(getClass().getResource("../view/gameScene.fxml"));
        gameUIScene = gameUILoader.load();

        gameController = gameUILoader.getController();

        serverConnectScene = FXMLLoader.load(getClass().getResource("../view/serverConnectScene.fxml"));
        setNameScene = FXMLLoader.load(getClass().getResource("../view/setPlayerNameScene.fxml"));

        //-------------Use this for listening to restore down--------------------//
//        window.centerOnScreen();

//        window.setTitle("Connect to Server");
//        window.setScene(new Scene(serverConnectScene));
//        window.show();
        switchToSetNameScene();
//        testCards();

    }

    public static void testCards() {
        Card village = CardFactory.getCard("Village");
        Card smithy = CardFactory.getCard("Smithy");
        Card copper = CardFactory.getCard("Copper");
        Card colony = CardFactory.getCard("Colony");
        List<Card> listOfCards = new ArrayList<>();
        List<ActionCard> actionCards = new ArrayList<>();

        listOfCards.add(village); listOfCards.add(smithy); listOfCards.add(copper); listOfCards.add(colony);
        actionCards.add((ActionCard) village); actionCards.add((ActionCard) smithy);
        for(Card c: listOfCards) {
            System.out.print(c.getName() + " ");
            if(c instanceof ActionCard) {
                for(ActionCard actionCard: actionCards) {
                    if(c.equals(actionCard)) System.out.print("is dependent " + actionCard.isDependent());
                }
            }
            System.out.println();
        }
    }

    public static Client getClient() {
        return client;
    }
    public static ClientSideConnection getClientSideConnection() {
        return clientSideConnection;
    }
    public static GameController getGameController() {
        return gameController;
    }
    public static void setClientSideConnection(ClientSideConnection clientSideConnection) {
        UserInterfaceHub.clientSideConnection = clientSideConnection;
    }
    public static void switchToGameScene() {
        window.setTitle("Dominion");
        window.setScene(new Scene(gameUIScene));
        window.setMaximized(true);
        window.setMinWidth(1320);
        window.setMinHeight(845);
        window.show();
        window.show();
    }
    public static void switchToSetNameScene() {
        window.setTitle("Select Name");
        window.setScene(new Scene(setNameScene));
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

