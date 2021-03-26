package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SetNameController {

    private String name;

    @FXML
    private TextField nameTextField;

    public void nameEntered(ActionEvent actionEvent) throws InterruptedException {
        name = nameTextField.getText();
        Main.getPlayer().setName(name);
        Main.switchToServerConnectScene();
//        Main.switchToGameScene();

    }
}
