package logic;

import account.User;
import client.GameClient;
import client.MainGameFrame;
import client.MainGameFrame.*;
import logic.GameMessage.*;
import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
                System.out.println("Check");
                client.sendMessageToServer(new GameMessage<TexasHoldem.Options>(GameMessage.MessageType.PLAYER_ACTION, TexasHoldem.Options.CHECK));
            }
            case "Call" -> {
                System.out.println("Call");
                client.sendMessageToServer(new GameMessage<TexasHoldem.Options>(GameMessage.MessageType.PLAYER_ACTION, TexasHoldem.Options.CALL));
            }
            case "Fold" -> {
                System.out.println("Fold");
                client.sendMessageToServer(new GameMessage<TexasHoldem.Options>(GameMessage.MessageType.PLAYER_ACTION, TexasHoldem.Options.FOLD));
            }
            case "Raise" -> {
                System.out.println("Raise");

                String raiseAmtStr = JOptionPane.showInputDialog(
                        null,
                        "Enter raise amount: ",
                        "Raise",
                        JOptionPane.QUESTION_MESSAGE
                );

                if (raiseAmtStr != null && !raiseAmtStr.isEmpty()) {
                    try {
                        int raiseAmt = Integer.parseInt(raiseAmtStr);
                        GameMessage.RaiseAction raiseAction = new RaiseAction(TexasHoldem.Options.RAISE, raiseAmt);
                        client.sendMessageToServer(new GameMessage<>(GameMessage.MessageType.PLAYER_ACTION, raiseAction));
                    } catch (NumberFormatException ex){
                        JOptionPane.showMessageDialog(
                                null,
                                "Invalid raise amount: " + raiseAmtStr,
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    public int promptRaise() {
        String input = JOptionPane.showInputDialog(frame, "Enter raise amount: ");

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void startGame() {
        if (frame == null) {
            System.out.println("NULL FRAME!!!!!!");
        }
        SwingUtilities.invokeLater(() -> {
            frame.setPanel(MainGameFrame.View.GAME);
        });
    }

    public void resetGameGUI(GameState gameState, User user) {
        SwingUtilities.invokeLater(() -> {
            frame.getGamePanel().updateFromGameState(gameState, user);
        });
    }

    public void toggleButtons(boolean state)  {
        SwingUtilities.invokeLater(() -> {
            for (JButton button : frame.getGamePanel().getButtons()) {
                button.setEnabled(state);
            }
        });
    }

    public MainGameFrame getFrame() { return frame; }
}
