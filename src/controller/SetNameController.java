package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SetNameController {

    private String name;

    @FXML
    private TextField nameTextField;

    public void nameEntered(ActionEvent actionEvent) {
        name = nameTextField.getText();
        UserInterfaceHub.getClient().setName(name);
        UserInterfaceHub.switchToGameScene();
    }
}
