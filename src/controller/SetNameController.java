package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.ClientSideConnection;
import model.factory.PlayerFactory;

public class SetNameController {

    private String name;

    @FXML
    private TextField nameTextField;

    public void nameEntered(ActionEvent actionEvent) {
        name = nameTextField.getText();
        UserInterfaceHub.setPlayer(PlayerFactory.getPlayer(name));
        UserInterfaceHub.switchToGameScene();
    }
}
