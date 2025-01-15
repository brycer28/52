package graphics;

import logic.Blackjack;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class BlackjackPanel extends JPanel {
    private Blackjack logic;
    private JPanel playerHandPanel = new JPanel();
    private JPanel dealerHandPanel = new JPanel();
    final int CARD_WIDTH = 100;
    final int GAME_WIDTH = 1500;
    final int GAME_HEIGHT = 1000;
    final int HAND_WIDTH = 220;
    final int HAND_HEIGHT = 150;

    public BlackjackPanel(Blackjack logic) {
        super();
        this.logic = logic;

        // initialize game panel
        setLayout(null);
        setPreferredSize(new Dimension(1500, 1000));
        setBackground(new Color(0, 100, 0));

        List<JPanel> handPanels = initHands();
        for (JPanel panel : handPanels) {
            add(panel);
        }


    }

    public List<JPanel> initHands() {
        // create and add hand panels
        playerHandPanel.setBounds((GAME_WIDTH/2-HAND_WIDTH/2), 600, HAND_WIDTH, HAND_HEIGHT);
        playerHandPanel.setBackground(new Color(0, 150, 0));
        add(playerHandPanel);

        dealerHandPanel.setBounds((GAME_WIDTH/2-HAND_WIDTH/2), 200, HAND_WIDTH, HAND_HEIGHT);
        dealerHandPanel.setBackground(new Color(0, 150, 0));
        add(dealerHandPanel);

        return Arrays.asList(playerHandPanel, dealerHandPanel);
    }
}
