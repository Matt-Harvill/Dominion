package controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Player;
import server.DominionServer;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    private static Parent gameUIScene, hostJoinScene, cardSelectScene;
    private static Stage mainStage, cardSelect;
    private static GameController gameController;
    private static HostJoinController hostJoinController;
    private static Player player;
    private static ClientSideConnection clientSideConnection;
    private static DominionServer server;

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage = primaryStage;
        player = new Player();
        FXMLLoader gameSceneLoader = new FXMLLoader(getClass().getResource("/gameScene.fxml"));
        gameUIScene = gameSceneLoader.load();

        gameUIScene.getStylesheets().add(getClass().getResource("/style1.css").toExternalForm());

        gameController = gameSceneLoader.getController();
        FXMLLoader hostJoinSceneLoader = new FXMLLoader(getClass().getResource("/hostJoinScene.fxml"));
        hostJoinScene = hostJoinSceneLoader.load();
        hostJoinController = hostJoinSceneLoader.getController();
        FXMLLoader cardSelectLoader = new FXMLLoader(getClass().getResource("/selectCards.fxml"));
        cardSelectScene = cardSelectLoader.load();

        mainStage.setResizable(false);
        goToHostJoinScene();

        //-------------Center Frame if resized--------------------//
        mainStage.widthProperty().addListener((obs, oldVal, newVal) -> mainStage.centerOnScreen());
        mainStage.heightProperty().addListener((obs, oldVal, newVal) -> mainStage.centerOnScreen());

        //-----(ShutDown Server if Host or Leave Game if Client) when Stage Closed-----//
        mainStage.setOnCloseRequest(windowEvent ->  {
            if(server!=null) {
                try {
                    server.serverShutDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if(clientSideConnection!=null) clientSideConnection.leaveGame();
            }
        });
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
    public static void setServerToNull() {
        server = null;
    }
    public static void setClientSideConnection(ClientSideConnection clientSideConnection) {
        Main.clientSideConnection = clientSideConnection;
    }

    public static void goToHostJoinScene() {
        mainStage.setTitle("Dominion");
        mainStage.setScene(new Scene(hostJoinScene));
        mainStage.show();
    }
    public static void goToGameScene() {
        mainStage.setTitle("Dominion");
        mainStage.setScene(new Scene(gameUIScene));

        ActionHandler.startPhase();
        DisplayUpdater.updatePlayerLabel(player.getName(), player.getPoints());

        mainStage.show();
    }
    public static void openCardSelectStage() {
        if(cardSelect==null) {
            cardSelect = new Stage();
            cardSelect.setScene(new Scene(cardSelectScene));
            cardSelect.setResizable(false);
            cardSelect.show();
            cardSelect.setOnCloseRequest(windowEvent -> cardSelect=null);
        }
    }
    public static void closeCardSelectStage() {
        if(cardSelect!=null) {
            cardSelect.close();
            cardSelect=null;
        }
    }
    public static void closeMainStage() {
        Platform.runLater(() -> mainStage.close());
    }

    public static void createServer() {
        if (server==null) server = new DominionServer();
    }
    public static void startServer(int maxNumPlayers) {
        server.setMaxNumPlayers(maxNumPlayers);
        gameController.initializeServerInfoDisplay();

        Thread thread = new Thread(server::acceptConnections);
        thread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

