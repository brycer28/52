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

    public Options playerOption;
    public Options dealerOption;
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
        resetGame();

        new Thread(() -> {
            while (gameRunning) {
                playRound();

                boolean playAgain = true;
                playAgain = GUI.displayReplayPrompt();
                if (!playAgain) {
                    gameRunning = false;
                    break;
                }
                gameRunning = true;
                resetGame();
            }
            //GUI.endGame();
        }).start();
    }

    public void playRound() {
        dealCard(playerHand);
        dealCard(dealerHand);
        dealCard(playerHand);
        dealCard(dealerHand);
        GUI.updateHands();

        executeBettingRound();
        GUI.updateStats();
        if (!gameRunning) return;

        for (int i = 0; i < 3; i++) {
            dealCard(communityCards);
        }
        GUI.updateCommunityCards();

        executeBettingRound();
        GUI.updateStats();
        if (!gameRunning) return;

        dealCard(communityCards);
        GUI.updateCommunityCards();

        executeBettingRound();
        GUI.updateStats();
        if (!gameRunning) return;

        dealCard(communityCards);
        GUI.updateCommunityCards();

        executeBettingRound();
        GUI.updateStats();
        if (!gameRunning) return;

        determineWinner();
    }

    public void dealCard(Hand hand) {
        hand.add(deck.peek());
        deck.pop();
    }

    public void executeBettingRound() {
        currentBet = 0;
        this.latch = new CountDownLatch(1);
        playerOption = null;
        waitForResponse();

        handleOption(true);

        if (playerOption == Options.FOLD) {
            gameRunning = false;
            //GUI.displayPlayerFolded(true);
            return;
        }

        handleOption(false);

        if (dealerOption == Options.FOLD) {
            gameRunning = false;
            //GUI.displayPlayerFolded(false);
            return;
        }
    }

    public void handleOption(boolean isPlayer) {
        validOption = false;

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
        if (isPlayer && playerOption == Options.FOLD) {
            dealerChips += currentBet;
        } else if (!isPlayer && dealerOption == Options.FOLD) {
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

        if (playerOption == Options.RAISE) {
            prob = rand.nextInt(10) + 1;
            if (prob == 1) return Options.FOLD;
            else return Options.CALL;
        }

        return Options.CHECK;
    }

    public void determineWinner() {
        for (Card card : communityCards) {
            playerHand.add(card);
            dealerHand.add(card);
        }

        Hand.HandValue playerHandValue = Hand.evaluateHand(playerHand);
        Hand.HandValue dealerHandValue = Hand.evaluateHand(dealerHand);

        if (playerHandValue.ordinal() > dealerHandValue.ordinal()) {
            playerChips += pot;
            dealerChips -= currentBet;
        } else if (playerHandValue.ordinal() < dealerHandValue.ordinal()) {
            dealerChips += pot;
            playerChips -= currentBet;
        } else {
            // handle tie eventually
            System.out.println("NO WINNER");
        }

        GUI.showDealerHand();
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
        GUI.resetGUI();
    }


    // getters and setters
    public Hand getPlayerHand() { return playerHand; }
    public Hand getDealerHand() { return dealerHand; }
    public Hand getCommunityCards() { return communityCards; }
    public TexasHoldemPanel getTexasHoldemPanel() { return GUI; }
    public int getPot() { return pot; }
    public int getCurrentBet() { return currentBet; }
    public int getDealerChips() { return dealerChips; }
    public int getPlayerChips() { return playerChips; }
    public Options getPlayerOption() { return playerOption; }
    public void setPlayerOption(Options playerOption) {
        this.playerOption = playerOption;
        latch.countDown();
    }
    public int getRaiseAmount() { return raiseAmount; }
    public void setRaiseAmount(int raiseAmount) { this.raiseAmount = raiseAmount; }

}
