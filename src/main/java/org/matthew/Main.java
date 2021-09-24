package org.matthew;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.matthew.controller.DisplayUpdater;
import org.matthew.controller.PhaseUpdater;
import org.matthew.controller.fxml.GameController;
import org.matthew.controller.fxml.HostJoinController;
import org.matthew.model.Player;
import org.matthew.server.ClientSideConnection;
import org.matthew.server.DominionServer;

import java.io.IOException;

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
        FXMLLoader gameSceneLoader = new FXMLLoader(Main.class.getResource("gameScene.fxml"));
        gameUIScene = gameSceneLoader.load();

        gameUIScene.getStylesheets().add(Main.class.getResource("style1.css").toExternalForm());

        gameController = gameSceneLoader.getController();
        FXMLLoader hostJoinSceneLoader = new FXMLLoader(Main.class.getResource("hostJoinScene.fxml"));
        hostJoinScene = hostJoinSceneLoader.load();
        hostJoinController = hostJoinSceneLoader.getController();
        FXMLLoader cardSelectLoader = new FXMLLoader(Main.class.getResource("selectCards.fxml"));
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
            closeCardSelectStage();
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

        PhaseUpdater.startPhase();
        DisplayUpdater.updatePlayerLabel(player.getName(), player.getPoints());

        mainStage.show();
    }
    public static void openCardSelectStage() {
        if(cardSelect==null) {
            try {
                cardSelect = new Stage();
                cardSelect.setScene(new Scene(FXMLLoader.load(Main.class.getResource("selectCards.fxml"))));
                cardSelect.setResizable(false);
                cardSelect.show();
                cardSelect.setOnCloseRequest(windowEvent -> cardSelect=null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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

