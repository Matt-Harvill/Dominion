package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ServerConnectController {

    @FXML private TextField ipAddressBox, portNumBox;
    @FXML private Text errorMessage;

    public void onLoginSubmit(ActionEvent actionEvent) {

        String ipAddressOfHost = ipAddressBox.getText();
        String portOfHost = portNumBox.getText();

        try {
            ClientSideConnection csc = new ClientSideConnection(ipAddressOfHost,portOfHost);
            Main.setClientSideConnection(csc);
            Main.switchToGameScene();
        } catch (Exception ex) {
            errorMessage.setText("Incorrect Host IP and/or Port, Please Try Again");
        }
        ipAddressBox.setText(null);
        portNumBox.setText(null);
    }
}
