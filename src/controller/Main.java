package controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Player;
import server.DominionServer;
import server.ServerSideConnection;

import java.io.IOException;

public class Main extends Application {

    private static Parent gameUIScene, hostJoinScene;
    private static Stage gameStage, cardSelect, serverInfo;

    private static GameController gameController;
    private static HostJoinController hostJoinController;
    private static Player player;
    private static ClientSideConnection clientSideConnection;

    private static DominionServer server;

    @Override
    public void start(Stage primaryStage) throws Exception{

        gameStage = primaryStage;
        player = new Player();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/gameScene.fxml"));
        gameUIScene = loader.load();
        gameController = loader.getController();

        loader = new FXMLLoader(getClass().getResource("../view/hostJoinScene.fxml"));
        hostJoinScene = loader.load();
        hostJoinController = loader.getController();

        gameStage.setResizable(false);
        goToHostJoinScene();
//        switchToSetNameScene();

        //-------------Center Frame if resized--------------------//
        gameStage.widthProperty().addListener((obs, oldVal, newVal) -> gameStage.centerOnScreen());
        gameStage.heightProperty().addListener((obs, oldVal, newVal) -> gameStage.centerOnScreen());
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
    public static Stage getGameStage() {return gameStage;}
    public static void serverToNull() {
        server = null;
    }
    public static void setClientSideConnection(ClientSideConnection clientSideConnection) {
        Main.clientSideConnection = clientSideConnection;
    }
    public static void closeCardSelect() {
        if(cardSelect!=null)
        cardSelect.close();
    }

    public static void goToHostJoinScene() {
        gameStage.setTitle("Dominion");
        gameStage.setScene(new Scene(hostJoinScene));
        gameStage.show();

    }
    public static void switchToGameScene() {
        gameStage.setTitle("Dominion");
        gameStage.setScene(new Scene(gameUIScene));

        PlayerActionMediator.startPhase();
        PlayerActionMediator.displayPlayerLabel(player.getName(), player.getPoints());

//        window.setMaximized(true);
        gameStage.setMinWidth(1296);
        gameStage.setMinHeight(839);
        gameStage.show();

        gameStage.setOnCloseRequest(windowEvent ->  {
            if(server!=null) {
                // do stuff to close connections
                try {
                    server.serverShutDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("server shut down in switchToGameScene");
            }
        });

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

        serverInfo = new Stage();
        serverInfo.setScene(new Scene(FXMLLoader.load(Main.class.getResource("../view/ServerInfoScene.fxml"))));
        serverInfo.show();
    }
    public static void closeOpenStages() {
        if(gameStage!=null) {
            Platform.runLater(() -> gameStage.close());
        }
        if(serverInfo!=null) {
            Platform.runLater(() -> serverInfo.close());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

