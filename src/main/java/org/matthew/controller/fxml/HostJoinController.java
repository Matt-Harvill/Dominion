package org.matthew.controller.fxml;

import  org.matthew.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import org.matthew.model.card.Card;
import org.matthew.server.ClientSideConnection;

import java.net.InetAddress;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

        List<Card> cardsChosen = Main.getServer().getCardsInGame().getCollection();
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
            Main.setServerToNull();
            Main.goToGameScene();
        } catch (Exception ex) {
            incorrectServerInfoAlert.setVisible(true);
        }
        hostIPTextField.setText(null);
        portTextField.setText(null);
    }
    public void selectActionCards(ActionEvent actionEvent) {
        Main.openCardSelectStage();
    }

    public void backSetGameDetails(ActionEvent actionEvent) {
        setGameDetails.setVisible(false);
        Main.closeCardSelectStage();
    }
    public void nextSetGameDetails(ActionEvent actionEvent) {
        if(numPlayersChoiceBox.getValue()!=null) {
            maxNumPlayers = numPlayersChoiceBox.getValue();
            setName.setVisible(true);
            selectNumPlayersError.setVisible(false);
            Main.closeCardSelectStage();
        } else {
            selectNumPlayersError.setVisible(true);
        }
    }
    public void backSetName(ActionEvent actionEvent) {
        setName.setVisible(false);
        enterNameError.setVisible(false);
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
            Main.goToGameScene();
        } catch (Exception ex) {
            incorrectServerInfoAlert.setVisible(true);
        }
    }
    private boolean checkName(String name) {

        if(name == null || name.equals("")) {
            enterNameError.setVisible(true);
            nameTextField.setText(null);
            return false;
        }

        Pattern p = Pattern.compile("[\\W]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(name);
        boolean wrongChar = m.find();

        if(wrongChar) {
            enterNameError.setVisible(true);
            nameTextField.setText(null);
            return false;
        } else {
            Main.getPlayer().setName(name);
            enterNameError.setVisible(false);
            return true;
        }
    }

    public void updateActionCardInGameSlots(List<Card> cardsChosen) {
        int index=0;
        if(cardsChosen==null) return;
        while(index < cardsChosen.size()) {
            System.out.println("index: " + index + " @HJC_updateACIGSlots");
            cardSlots[index].setText(cardsChosen.get(index).getName());
            System.out.println("card: " + cardsChosen.get(index).getName() + " @HJC_updateACIGSlots");
            index++;
        }
        while(index < cardSlots.length) {
            cardSlots[index].setText(null);
            index++;
        }
    }
}
