package userInterface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class IntroController {


    @FXML private TextField usernameBox;
    @FXML private PasswordField passwordBox;
    @FXML private Button submitButton;

    public void onLoginSubmit(ActionEvent actionEvent) {
        if(usernameBox.getText().equals("mharvill23") && passwordBox.getText().equals("10231935"))
        UserInterfaceHub.switchToGameScene();

        usernameBox.setText(null);
        passwordBox.setText(null);
    }
}
