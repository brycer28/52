package logic;

import account.LoginData;
import account.LoginPanel;
import client.GameClient;
import client.MainGameFrame;
import client.MainGameFrame.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameControl implements ActionListener {
    MainGameFrame frame;
    GameClient client;

    public GameControl(MainGameFrame frame, GameClient client) {
        this.frame = frame;
        this.client = client;
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        String command = action.getActionCommand();

        switch (command) {
            case "Check" -> {

            }
            case "Call" -> {

            }
            case "Fold" -> {

            }
            case "Raise" -> {

            }
        }
    }

    public void startGame() {
        frame.setPanel(MainGameFrame.View.GAME);
    }

    public void resetGameGUI(GameState state) {

    }
}
