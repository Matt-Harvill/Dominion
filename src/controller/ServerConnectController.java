package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.ClientSideConnection;

import java.io.IOException;

public class ServerConnectController {

    @FXML private TextField ipAddressBox, portNumBox;
    @FXML private Button submitButton;
    @FXML private Text errorMessage;

    public void onLoginSubmit(ActionEvent actionEvent) {


        String ipAddressOfHost = ipAddressBox.getText();
        String portOfHost = portNumBox.getText();

        try {
            new ClientSideConnection(ipAddressOfHost,portOfHost);
            UserInterfaceHub.switchToGameScene();
        } catch (Exception ex) {
            errorMessage.setText("Incorrect Host IP and/or Port, Please Try Again");
        }
        ipAddressBox.setText(null);
        portNumBox.setText(null);
    }
}
