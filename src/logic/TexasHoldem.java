package logic;

import graphics.TexasHoldemPanel;
import cards.*;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class TexasHoldem {
    private TexasHoldemPanel GUI;
    private Deck deck;
    private Hand playerHand = new Hand();
    private Hand dealerHand = new Hand();
    private Hand communityCards = new Hand();
    private int playerChips = 1000;
    private int dealerChips = 1000;
    private int pot = 0;
    private int currentBet = 0;
    private boolean validOption = false;
    private boolean validRaise = false;
    private int raiseAmount = 0;
    private CountDownLatch latch;

    public int playerOption = -1;
    public int dealerOption = -1;
    public boolean gameRunning = true;

    public enum Options {
        CHECK, CALL, RAISE, FOLD
    }

    public TexasHoldem() {
        GUI = new TexasHoldemPanel(this);
        startGame();
    }

    public void startGame() {
        gameRunning = true;
        //resetGame();

        new Thread(() -> {
            while (gameRunning) {
                //playRound();

                boolean playAgain = true;
                //playAgain = GUI.displayReplayPrompt();
                if (!playAgain) {
                    gameRunning = false;
                    break;
                }
                gameRunning = true;
                //resetGame();
            }
            //GUI.endGame();
        }).start();
    }

    public void playRound() {
        dealCard(playerHand);
        dealCard(dealerHand);
        dealCard(playerHand);
        dealCard(dealerHand);
        //GUI.updateHands();

        //executeBettingRound();
    }

    public void dealCard(Hand hand) {
        hand.add(deck.peek());
        deck.pop();
    }

    public void executeBettingRound() {
        currentBet = 0;
        playerOption = -1;
        latch = new CountDownLatch(1);
        waitForResponse();

        handleOption(true);

        if (playerOption == Options.FOLD.ordinal()) {
            gameRunning = false;
            //GUI.displayPlayerFolded(true);
            return;
        }

        handleOption(false);

        if (dealerOption == Options.FOLD.ordinal()) {
            gameRunning = false;
            //GUI.displayPlayerFolded(false);
            return;
        }
    }

    public void handleOption(boolean isPlayer) {
        validOption = false;
        playerOption = -1;
        dealerOption = -1;
        while (!validOption) {
            Options option;
            if (isPlayer) {
                option = getPlayerOption();
            } else option = getDealerOption();

            switch (option) {
                case CHECK:
                    handleCheck();
                    break;
                case CALL:
                    handleCall(isPlayer);
                    break;
                case RAISE:
                    //handleRaise(isPlayer);
                    break;
                case FOLD:
                    handleFold(isPlayer);
                    break;
            }
        }
    }

    public void handleCheck() {
        if (currentBet == 0) validOption = true;
        else validOption = false;
    }

    public void handleCall(boolean isPlayer) {
        if (currentBet > 0) {
            if (isPlayer && playerChips > currentBet) {
                playerChips -= currentBet;
            } else if (!isPlayer && dealerChips > currentBet) {
                dealerChips -= currentBet;
            }
            pot += currentBet;
            validOption = true;
        }
        else validOption = false;
    }

    public void handleFold(boolean isPlayer) {
        pot += currentBet;
        if (isPlayer && playerOption == Options.FOLD.ordinal()) {
            dealerChips += currentBet;
        } else if (!isPlayer && dealerOption == Options.FOLD.ordinal()) {
            playerChips += currentBet;
        }
        currentBet = 0;
        pot = 0;
        validOption = true;
    }

    public Options getDealerOption() {
        Random rand = new Random();
        int prob = rand.nextInt(20) + 1;
        if (prob == 1) return Options.FOLD;

        if (playerOption == Options.RAISE.ordinal()) {
            prob = rand.nextInt(10) + 1;
            if (prob == 1) return Options.FOLD;
            else return Options.CALL;
        }

        return Options.CHECK;
    }

    public Options getPlayerOption() {
        return Options.values()[playerOption];
    }

    private void waitForResponse() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resetGame() {
        deck = new Deck();
        playerHand = new Hand();
        dealerHand = new Hand();
        communityCards = new Hand();
        pot = 0;
        //GUI.resetGUI();
    }
}
