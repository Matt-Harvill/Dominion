package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class HostJoinController {

    @FXML
    BorderPane setGameDetails, setName, connectToServer;

    @FXML
    TextField nameTextField, hostIPTextField, portTextField;

    @FXML
    Text incorrectServerInfoAlert;

    @FXML
    ChoiceBox<Integer> numPlayersChoiceBox;

    private int maxNumPlayers;

    public void initialize() {
        setGameDetails.setVisible(false);
        setName.setVisible(false);
        connectToServer.setVisible(false);
        for(int i=2;i<=6;i++) {
            numPlayersChoiceBox.getItems().add(i);
        }
    }

    public void hostGame(ActionEvent actionEvent) {
        setGameDetails.setVisible(true);
    }
    public void joinGame(ActionEvent actionEvent) {
        setName.setVisible(true);
    }
    public void submitName(ActionEvent actionEvent) {
        String name = nameTextField.getText();
        if(name!=null) {
            Main.getPlayer().setName(name);
        }
        nameTextField.setText(null);
    }
    public void submitConnectRequest(ActionEvent actionEvent) {
        String ipAddress = hostIPTextField.getText();
        String port = portTextField.getText();
        try {
            ClientSideConnection csc = new ClientSideConnection(ipAddress,port);
            Main.setClientSideConnection(csc);
            Main.switchToGameScene();
        } catch (Exception ex) {
            incorrectServerInfoAlert.setVisible(true);
        }
        hostIPTextField.setText(null);
        portTextField.setText(null);
    }

    public void backSetGameDetails(ActionEvent actionEvent) {
        setGameDetails.setVisible(false);
    }
    public void nextSetGameDetails(ActionEvent actionEvent) {
        setName.setVisible(true);
        maxNumPlayers = numPlayersChoiceBox.getValue();
    }
    public void backSetName(ActionEvent actionEvent) {
        setName.setVisible(false);
    }
    public void nextSetName(ActionEvent actionEvent) {
        if(setGameDetails.isVisible()) {
            System.out.println("You hosted the game");
            try {
                Main.startServer(maxNumPlayers);
                connectToServer(InetAddress.getLocalHost().getHostAddress(),String.valueOf(Main.getServer().getServerSocket().getLocalPort()));
            } catch (Exception ex) {
                System.out.println("Error starting/connecting to your own game!");
            }
        } else {
            System.out.println("You joined the game");
            connectToServer.setVisible(true);
        }
    }
    public void backConnectToServer(ActionEvent actionEvent) {
        connectToServer.setVisible(false);
    }

    private void connectToServer(String ipAddress, String port) {
        try {
            ClientSideConnection csc = new ClientSideConnection(ipAddress,port);
            Main.setClientSideConnection(csc);
            Main.switchToGameScene();
        } catch (Exception ex) {
            incorrectServerInfoAlert.setVisible(true);
        }
    }

}
