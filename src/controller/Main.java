package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Player;
import server.DominionServer;

import java.io.IOException;

public class Main extends Application {

    private static Parent gameUIScene, hostJoinScene;
    private static Stage window;

    private static GameController gameController;
    private static HostJoinController hostJoinController;
    private static Player player;
    private static ClientSideConnection clientSideConnection;

    private static DominionServer server;
    private static Stage cardSelect;

    @Override
    public void start(Stage primaryStage) throws Exception{

        window = primaryStage;
        player = new Player();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/gameScene.fxml"));
        gameUIScene = loader.load();
        gameController = loader.getController();

        loader = new FXMLLoader(getClass().getResource("../view/hostJoinScene.fxml"));
        hostJoinScene = loader.load();
        hostJoinController = loader.getController();

        window.setResizable(false);
        goToHostJoinScene();
//        switchToSetNameScene();

        //-------------Center Frame if resized--------------------//
        window.widthProperty().addListener((obs, oldVal, newVal) -> window.centerOnScreen());
        window.heightProperty().addListener((obs, oldVal, newVal) -> window.centerOnScreen());
    }

    public static Player getPlayer() {
        return player;
    }
    public static ClientSideConnection getClientSideConnection() {
        return clientSideConnection;
    }
    public static GameController getGameController() {
        return gameController;
    }
    public static HostJoinController getHostJoinController() {return hostJoinController;}
    public static DominionServer getServer() {return server;}
    public static void setClientSideConnection(ClientSideConnection clientSideConnection) {
        Main.clientSideConnection = clientSideConnection;
    }
    public static void closeCardSelect() {
        cardSelect.close();
    }

    public static void goToHostJoinScene() {
        window.setTitle("Dominion");
        window.setScene(new Scene(hostJoinScene));
        window.show();

    }
    public static void switchToGameScene() {
        window.setTitle("Dominion");
        window.setScene(new Scene(gameUIScene));

        PlayerActionMediator.startPhase();
        PlayerActionMediator.displayPlayerLabel(player.getName(), player.getPoints());

//        window.setMaximized(true);
        window.setMinWidth(1296);
        window.setMinHeight(839);
        window.show();
    }

    public static void selectCardsPopup() {
        try {
            cardSelect = new Stage();
            cardSelect.setScene(new Scene(FXMLLoader.load(Main.class.getResource("../view/selectCardsInGame.fxml"))));
            cardSelect.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void createServer() {
        if (server==null)
        server = new DominionServer();
    }

    public static void startServer(int maxNumPlayers) throws IOException {
        server.setMaxNumPlayers(maxNumPlayers);

        Thread serverAccepting = new Thread(() -> server.acceptConnections());
        serverAccepting.start();

        Stage newStage = new Stage();
        newStage.setScene(new Scene(FXMLLoader.load(Main.class.getResource("../view/ServerInfoScene.fxml"))));
        newStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

