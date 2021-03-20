package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    @FXML private TextArea chatLog, gameLog;
    @FXML private TextField chatType;

    private List<String> chatDisplayStrings, gameDisplayStrings;

    public void initialize() {
        chatDisplayStrings = new ArrayList<>();
        gameDisplayStrings = new ArrayList<>();

    }

    //-------------------Internal Updates------------------------//

    public void chatSend(ActionEvent actionEvent) {
        String newChat = UserInterfaceHub.getClient().getName() + ": " + chatType.getText();
        addMessageToChatLog(newChat);
//        UserInterfaceHub.getClientSideConnection().send("chat " + newChat);
    }



    //-------------------External Updates------------------------//

    public void addMessageToChatLog(String msg) {
        if(chatDisplayStrings.size()>=7) chatDisplayStrings.remove(0);
        chatDisplayStrings.add(msg);
        chatType.setText(null);
        StringBuilder builder = new StringBuilder();
        for(String s: chatDisplayStrings) {
            builder.append(s); builder.append("\n");
        }
        chatLog.setText(builder.toString());
    }
    public void addMessageToGameLog(String msg) {
        if(gameDisplayStrings.size()>=7) gameDisplayStrings.remove(0);
        gameDisplayStrings.add(msg);
        gameLog.setText(null);
        StringBuilder builder = new StringBuilder();
        for(String s: gameDisplayStrings) {
            builder.append(s); builder.append("\n");
        }
        gameLog.setText(builder.toString());
    }
}
