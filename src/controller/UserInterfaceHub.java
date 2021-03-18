package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UserInterfaceHub extends Application {

    private static Parent gameUIScene;
    private static Parent serverConnectScene;
    private static Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception{

        window = primaryStage;
        gameUIScene = FXMLLoader.load(getClass().getResource("../view/gameInterface.fxml"));
        serverConnectScene = FXMLLoader.load(getClass().getResource("../view/serverConnectScene.fxml"));

        window.setTitle("Connect to Server");
        window.setScene(new Scene(serverConnectScene));
        window.show();
//        switchToGameScene();
    }

    public static void switchToGameScene() {
        window.setTitle("Dominion");
        window.setScene(new Scene(gameUIScene));
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
