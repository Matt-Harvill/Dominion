package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;


public class HostJoinController {

    @FXML
    BorderPane setGameDetails, setName, connectToServer;

    @FXML
    TextField nameTextField, hostIPTextField, portTextField;


    public void initialize() {
        setGameDetails.setVisible(false);
        setName.setVisible(false);
        connectToServer.setVisible(false);
    }

    public void hostGame(ActionEvent actionEvent) {
        setGameDetails.setVisible(true);
    }

    public void joinGame(ActionEvent actionEvent) {
        setName.setVisible(true);
    }


    public void backSetGameDetails(ActionEvent actionEvent) {
        setGameDetails.setVisible(false);
    }

    public void nextSetGameDetails(ActionEvent actionEvent) {
        setName.setVisible(true);
    }

    public void backSetName(ActionEvent actionEvent) {
        setName.setVisible(false);
    }

    public void nextSetName(ActionEvent actionEvent) {
        if(setGameDetails.isVisible()) {
            System.out.println("You hosted the game");
            //start the server and load up the game
        } else {
            System.out.println("You joined the game");
            connectToServer.setVisible(true);
        }
    }

    public void submitName(ActionEvent actionEvent) {
        String name = nameTextField.getText();
        if(name!=null) {
            System.out.println(name);
        }
        nameTextField.setText(null);
    }

    public void backConnectToServer(ActionEvent actionEvent) {
        connectToServer.setVisible(false);
    }

    public void submitConnectRequest(ActionEvent actionEvent) {
        String ipAddress = hostIPTextField.getText();
        String port = portTextField.getText();
        System.out.println(ipAddress);
        System.out.println(port);
        hostIPTextField.setText(null);
        portTextField.setText(null);
    }
}
