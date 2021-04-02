package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import model.card.ActionCard;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;


public class HostJoinController {

    @FXML
    BorderPane setGameDetails, setName, connectToServer;

    @FXML
    TextField nameTextField, hostIPTextField, portTextField;

    @FXML
    Text incorrectServerInfoAlert, selectNumPlayersError, enterNameError;

    @FXML
    ChoiceBox<Integer> numPlayersChoiceBox;

    @FXML TextField cardSlot1,cardSlot2,cardSlot3,cardSlot4,cardSlot5,cardSlot6,cardSlot7,cardSlot8,cardSlot9,cardSlot10;
    TextField[] cardSlots;

    private int maxNumPlayers;

    public void initialize() {
        setGameDetails.setVisible(false);
        setName.setVisible(false);
        connectToServer.setVisible(false);
        cardSlots = new TextField[]{cardSlot1,cardSlot2,cardSlot3,cardSlot4,cardSlot5,cardSlot6,cardSlot7,cardSlot8,cardSlot9,cardSlot10};
        for(int i=2;i<=6;i++) {
            numPlayersChoiceBox.getItems().add(i);
        }

        nameTextField.setText(null);
    }

    public void hostGame(ActionEvent actionEvent) {
        Main.createServer();

        List<ActionCard> cardsChosen = Main.getServer().getActionCardsInGame();
        updateActionCardInGameSlots(cardsChosen);
        setGameDetails.setVisible(true);
    }
    public void joinGame(ActionEvent actionEvent) {
        setName.setVisible(true);
    }
    public void submitConnectRequest(ActionEvent actionEvent) {
        String ipAddress = hostIPTextField.getText();
        String port = portTextField.getText();
        try {
            ClientSideConnection csc = new ClientSideConnection(ipAddress,port);
            Main.setClientSideConnection(csc);
            Main.serverToNull();
            Main.switchToGameScene();
        } catch (Exception ex) {
            incorrectServerInfoAlert.setVisible(true);
        }
        hostIPTextField.setText(null);
        portTextField.setText(null);
    }
    public void selectActionCards(ActionEvent actionEvent) {
        Main.selectCardsPopup();
    }

    public void backSetGameDetails(ActionEvent actionEvent) {
        setGameDetails.setVisible(false);
        Main.closeCardSelect();
    }
    public void nextSetGameDetails(ActionEvent actionEvent) {
        if(numPlayersChoiceBox.getValue()!=null) {
            maxNumPlayers = numPlayersChoiceBox.getValue();
            setName.setVisible(true);
            selectNumPlayersError.setVisible(false);
            Main.closeCardSelect();
        } else {
            selectNumPlayersError.setVisible(true);
        }
    }
    public void backSetName(ActionEvent actionEvent) {
        setName.setVisible(false);
    }
    public void nextSetName(ActionEvent actionEvent) {
        if(checkName(nameTextField.getText())) {
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
    private boolean checkName(String name) {
        if(name==null || name=="") {
            enterNameError.setVisible(true);
            return false;
        } else {
            Main.getPlayer().setName(name);
            enterNameError.setVisible(false);
            return true;
        }
    }

    public void updateActionCardInGameSlots(List<ActionCard> cardsChosen) {
        int index=0;
        if(cardsChosen==null) return;
        while(index < cardsChosen.size()) {
            cardSlots[index].setText(cardsChosen.get(index).getName());
            index++;
        }
        while(index < cardSlots.length) {
            cardSlots[index].setText(null);
            index++;
        }
    }
}
