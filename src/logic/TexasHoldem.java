package logic;

import AccountFiles.User;
import cards.*;
import cards.Hand.HandValue;
import graphics.TexasHoldemPanel;
import java.util.*;
import java.util.concurrent.*;

public class TexasHoldem {
    private TexasHoldemPanel GUI;
    private Deck deck;
    private Hand communityCards = new Hand();
    private int pot = 0;
    private int currentBet = 0;
    private int raiseAmount = 0;
    private CountDownLatch latch;
    private boolean validOption;
    private boolean validRaise;
    private Options playerOption;
    private Options dealerOption;
    private boolean gameRunning = true;
    private ArrayList<User> players = new ArrayList<>(); 
    private int currentPlayerIndex = 0;

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
        for (User user : players) {
            user.getHand().dealCard(deck);
            user.getHand().dealCard(deck);
        }
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
        // playerOption = null;
        // waitForResponse();

        // handleOption(true);

        // if (playerOption == Options.FOLD) {
        //     gameRunning = false;
        //     GUI.displayPlayerFolded(true);
        //     return;
        // }

        // handleOption(false);

        // if (dealerOption == Options.FOLD) {
        //     gameRunning = false;
        //     GUI.displayPlayerFolded(false);
        // }

        for (User user : players) {
            Options option = null;
            if (!user.isActive()) break;

            latch = new CountDownLatch(1); // this will probably not work with Client/Server
            
            // get each players option

            // handle option accordingly

            waitForResponse();
        }
    }

    public void handleOption(User user) {
        validOption = false;

        while (!validOption) {
            Options option = user.getOption(); // decide how to 'get option'

            switch (option) {
                case CHECK:
                    handleCheck(user);
                    break;
                case CALL:
                    handleCall(user);
                    break;
                case RAISE:
                    handleRaise(user);
                    break;
                case FOLD:
                    handleFold(user);
                    break;
            }
        }
    }

    public void handleCheck(User user) {
        if (getCurrentBet() == 0) {
            // remove player from active player list
            validOption = true;
        }
    }

    public void handleCall(User user) {
        // ensure there is a current bet and player has enough chips
        if (currentBet > 0 && (user.getBalance() > currentBet)) {
            user.setBalance(user.getBalance() - currentBet);

            pot += currentBet;
            validOption = true;
        }
    }

    public void handleRaise(User user) {
        // ensure player has enough to raise
        if (user.getBalance() > currentBet) {
            while(!validRaise) {
                raiseAmount = getRaiseAmount();
                
                if (raiseAmount > 0 && (user.getBalance() >= currentBet + raiseAmount)) {
                    user.setBalance(user.getBalance() - raiseAmount); // may need to change this to cBet + raiseAmt
                    currentBet += raiseAmount;
                    pot += raiseAmount;
                    validRaise = true;
                }
            }
        }
    }

    public void handleFold(User user) {
        user.setBalance(user.getBalance() - currentBet); // deduct current bet from player balance
        pot += currentBet;
        currentBet = 0;
        validOption = true;
    }

    public void determineWinner() {
        HashMap<User, HandValue> handValueMap = new HashMap<>();

        // add community cards to each active players hand
        for (User user : players) {
            if (user.isActive()) {
                // add community cards to each players hand
                for (Card c : communityCards) {
                    user.appendHand(c);
                }

                handValueMap.put(user, user.getHand().evaluateHand());
            }
        }        

        // create a map of frequencies of each hand type
        HashMap<HandValue, Integer> handValueFreqencies = new HashMap<>();
        for (HandValue hv : handValueMap.values()) {
            handValueFreqencies.put(hv, handValueFreqencies.getOrDefault(hv, 0) + 1);
        }

        // check for duplicate HandValues - if one found, set it to be the duplicateValue
        HandValue duplicateValue = null;
        for (Map.Entry<HandValue, Integer> entry : handValueFreqencies.entrySet()) {
            if (entry.getValue() > 1) {
                duplicateValue = entry.getKey();
                break;
            }
        }

        // if a duplicate value is found, handle a tie
        if (duplicateValue != null) {
            Object bestCardType;
            Object bestCard = null; 
            HashMap<User, Object> tieBreakerCards = new HashMap<>();

            // get each players tie breaker card
            for (User user : players) {
                // if player has the card causing the tie
                if (user.getHand().evaluateHand() == duplicateValue) {
                    tieBreakerCards.put(user, user.getHand().determineTieBreaker());
                }
            }

            // determine best card by finding the max value in tieBreakerCards sorting by the type of the maps values
            // *all values are inherently the same type*
            bestCardType = tieBreakerCards.values().iterator().next().getClass();
            if (bestCardType instanceof Card.Suit) {
                bestCard = Collections.max(tieBreakerCards.values(), Comparator.comparingInt(suit -> ((Card.Suit) suit).ordinal()));

            } else if (bestCardType instanceof Card.Rank) {
                bestCard = Collections.max(tieBreakerCards.values(), Comparator.comparingInt(rank -> ((Card.Rank) rank).ordinal()));
            }

            // find the user with the best card
            User winner = null;
            for (Map.Entry<User, Object> entry : tieBreakerCards.entrySet()) {
                if (entry.getValue() == bestCard) {
                    winner = entry.getKey();
                    break;
                } 
            }

            // handle player winning round
            for (User user : players) {
                if (user == winner) {
                    user.setBalance(user.getBalance() + pot);
                } else {
                    user.setBalance(user.getBalance() - currentBet); // not certain if this is correct
                }
            }
            
            // GUI.displayWinner(winner)
            // GUI.showDealerHand();  ---> GUI.showAllHands();
        }
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
        communityCards = new Hand();
        for (User user : players) { user.clearHand(); } // clear all players hands
        pot = 0;
        GUI.resetGUI();
    }

    // getters and setters
    public Hand getCommunityCards() { return communityCards; }
    public TexasHoldemPanel getTexasHoldemPanel() { return GUI; }
    public int getPot() { return pot; }
    public int getCurrentBet() { return currentBet; }
    public Options getPlayerOption() { return playerOption; }
    public Options getDealerOption() { return dealerOption; }
    public void setPlayerOption(Options playerOption) {
        this.playerOption = playerOption;
        latch.countDown();
    }
    public int getRaiseAmount() { return raiseAmount; }
    public void setRaiseAmount(int raiseAmount) { this.raiseAmount = raiseAmount; }

}