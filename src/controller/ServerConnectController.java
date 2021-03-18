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

    private static String ipAddressOfHost, portOfHost;

    @FXML private TextField ipAddressBox, portNumBox;
    @FXML private Button submitButton;
    @FXML private Text errorMessage;

    public void initialize() {
        errorMessage.setVisible(false);
    }

    public void onLoginSubmit(ActionEvent actionEvent) {

        ipAddressOfHost = ipAddressBox.getText();
        portOfHost = portNumBox.getText();

        try {
            new ClientSideConnection(ipAddressOfHost,portOfHost);
            UserInterfaceHub.switchToGameScene();
        } catch (IOException ex) {
            errorMessage.setText("Incorrect Host IP and/or Port, Please Try Again");
        } catch (NumberFormatException num) {
            errorMessage.setText("Please Enter a Valid Port Number");
        }
        errorMessage.setVisible(true);
        ipAddressBox.setText(null);
        portNumBox.setText(null);
    }
}
