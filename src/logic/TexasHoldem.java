package logic;

import cards.*;
import graphics.TexasHoldemPanel;
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
    private Options playerOption;
    private Options dealerOption;
    private boolean gameRunning = true;

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
                GUI.updateStats();

                boolean playAgain = true;
                playAgain = GUI.displayReplayPrompt();
                if (!playAgain) {
                    gameRunning = false;
                    break;
                }
                gameRunning = true;
                resetGame();
            }
            GUI.endGame();
        }).start();
    }

    public void playRound() {
        playerHand.dealCard(deck);
        dealerHand.dealCard(deck);
        playerHand.dealCard(deck);
        dealerHand.dealCard(deck);
        GUI.updateHands();

        executeBettingRound();
        GUI.updateStats();
        if (!gameRunning) return;

        for (int i = 0; i < 3; i++) {
            communityCards.dealCard(deck);
        }
        GUI.updateCommunityCards();

        executeBettingRound();
        GUI.updateStats();
        if (!gameRunning) return;

        communityCards.dealCard(deck);
        GUI.updateCommunityCards();

        executeBettingRound();
        GUI.updateStats();
        if (!gameRunning) return;

        communityCards.dealCard(deck);
        GUI.updateCommunityCards();

        executeBettingRound();
        GUI.updateStats();
        if (!gameRunning) return;

        determineWinner();
    }

    public void executeBettingRound() {
        currentBet = 0;
        this.latch = new CountDownLatch(1);
        playerOption = null;
        waitForResponse();

        handleOption(true);

        if (playerOption == Options.FOLD) {
            gameRunning = false;
            GUI.displayPlayerFolded(true);
            return;
        }

        handleOption(false);

        if (dealerOption == Options.FOLD) {
            gameRunning = false;
            GUI.displayPlayerFolded(false);
        }
    }

    public void handleOption(boolean isPlayer) {
        validOption = false;

        while (!validOption) {
            Options option;
            if (isPlayer) {
                option = getPlayerOption();
            } else {
                setDealerOption();
                option = getDealerOption();
            }


            switch (option) {
                case CHECK:
                    handleCheck();
                    break;
                case CALL:
                    handleCall(isPlayer);
                    break;
                case RAISE:
                    handleRaise(isPlayer);
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

    public void handleRaise(boolean isPlayer) {
         int chips = isPlayer  ? playerChips : dealerChips;

         if (chips >= currentBet) {
             while(!validRaise) {
                 raiseAmount = getRaiseAmount();
                 if (raiseAmount > 0 && playerChips >= currentBet + raiseAmount) {
                     validRaise = true;
                 }
             }
             currentBet += raiseAmount;
             pot += raiseAmount;

             if (isPlayer) playerChips -= raiseAmount;
             else dealerChips -= raiseAmount;

             validOption = true;
         }
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

    public void setDealerOption() {
        Random rand = new Random();
        int prob = rand.nextInt(20) + 1;
        if (prob == 1) dealerOption = Options.FOLD;

        if (playerOption == Options.RAISE) {
            prob = rand.nextInt(10) + 1;
            if (prob == 1) dealerOption = Options.FOLD;
            else dealerOption = Options.CALL;
        } else {
            dealerOption = Options.CHECK;
        }
    }

    public void determineWinner() {
        // add each community card
        for (Card card : communityCards) {
            playerHand.add(card);
            dealerHand.add(card);
        }

        // get the value of each hand
        Hand.HandValue playerHandValue = playerHand.evaluateHand();
        Hand.HandValue dealerHandValue = dealerHand.evaluateHand();

        // booleans for which player wins
        boolean playerWins = false;
        boolean dealerWins = false;

        // if a player has higher hand, they win
        if (playerHandValue.ordinal() > dealerHandValue.ordinal()) {
            playerWins = true;

        // if dealer has higher hand, they win
        } else if (playerHandValue.ordinal() < dealerHandValue.ordinal()) {
            dealerWins = true;
            
        // if player and dealer have equal ordinal values, they tie
        // must resolve tie to determine winner
        } else {
            // set to Object becasue it could be either a Suit or a Rank depending on the hand
            Object playerBestCard = playerHand.determineTieBreaker();
            Object dealerBestCard = dealerHand.determineTieBreaker();

            // if player and dealer best cards are not same type, tie did not occur
            if (playerBestCard.getClass() != dealerBestCard.getClass()) {
                System.out.println("ERROR: Player and Dealer best cards are not of same type!");
                throw new RuntimeException();
            }  
            
            if (playerBestCard instanceof Card.Suit) {
                Card.Suit playerSuit = (Card.Suit) playerBestCard;
                Card.Suit dealerSuit = (Card.Suit) dealerBestCard;

                if (playerSuit.ordinal() > dealerSuit.ordinal()) {
                    playerWins = true;
                } else if (playerSuit.ordinal() < dealerSuit.ordinal()) {
                    dealerWins = true;
                }

            } else if (playerBestCard instanceof Card.Rank) {
                Card.Rank playerRank = (Card.Rank) playerBestCard;
                Card.Rank dealerRank = (Card.Rank) dealerBestCard;

                if (playerRank.ordinal() > dealerRank.ordinal()) {
                    playerWins = true;
                } else if (playerRank.ordinal() < dealerRank.ordinal()) {
                    dealerWins = true;
                }
            }
        }

        if (playerWins) {
            playerChips += pot;
            dealerChips -= currentBet;
            GUI.displayWinner(true);
        } else if (dealerWins) {
            dealerChips += pot;
            playerChips -= currentBet;
            GUI.displayWinner(false);
        } else {
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
    public Options getDealerOption() { return dealerOption; }
    public void setPlayerOption(Options playerOption) {
        this.playerOption = playerOption;
        latch.countDown();
    }
    public int getRaiseAmount() { return raiseAmount; }
    public void setRaiseAmount(int raiseAmount) { this.raiseAmount = raiseAmount; }

}