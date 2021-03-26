package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Player;

public class Main extends Application {

    private static Parent gameUIScene, serverConnectScene, setNameScene;
    private static Stage window;

    private static GameController gameController;
    private static Player player;
    private static ClientSideConnection clientSideConnection;

    @Override
    public void start(Stage primaryStage) throws Exception{

        window = primaryStage;
        player = new Player();

        FXMLLoader gameUILoader = new FXMLLoader(getClass().getResource("../view/gameScene.fxml"));
        gameUIScene = gameUILoader.load();
        gameController = gameUILoader.getController();

        serverConnectScene = FXMLLoader.load(getClass().getResource("../view/serverConnectScene.fxml"));
        setNameScene = FXMLLoader.load(getClass().getResource("../view/setPlayerNameScene.fxml"));


        switchToSetNameScene();

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
    public static void setClientSideConnection(ClientSideConnection clientSideConnection) {
        Main.clientSideConnection = clientSideConnection;
    }

    public static void switchToGameScene() {
        window.setTitle("Dominion");
        window.setScene(new Scene(gameUIScene));

        PlayerActionMediator.startPhase();
        PlayerActionMediator.displayPlayerLabel();

        window.setMaximized(true);
        window.setMinWidth(1296);
        window.setMinHeight(839);
        window.show();
    }
    public static void switchToSetNameScene() {
        window.setTitle("Select Name");
        window.setScene(new Scene(setNameScene));
        window.show();
    }
    public static void switchToServerConnectScene() {
        window.setTitle("Connect to Server");
        window.setScene(new Scene(serverConnectScene));
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

