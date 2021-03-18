package userInterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UserInterfaceHub extends Application {

    private static Parent gameUIScene;
    private static Parent loginScene;
    private static Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception{

        window = primaryStage;
        gameUIScene = FXMLLoader.load(getClass().getResource("sample.fxml"));
        loginScene = FXMLLoader.load(getClass().getResource("introScene.fxml"));

//        window.setTitle("Login Authentication");
//        window.setScene(new Scene(loginScene));
//        window.show();
        switchToGameScene();
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

class Player {
    public void print() {
        System.out.println("Player printed");
    }
}
