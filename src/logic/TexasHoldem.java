package logic;

import account.User;
import cards.*;
import cards.Hand.HandValue;
import graphics.TexasHoldemPanel;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import logic.GameMessage.MessageType;

import server.GameServer;

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
    private GameServer server;
    private ArrayList<User> players = new ArrayList<>();

    public enum Options {
        CHECK, CALL, RAISE, FOLD
    }

    public TexasHoldem(GameServer gs) {
        this.server = gs;
    }

    public void startGame() {
        resetGame();

        // set up initial game state - initialize users using their account balance

//        server.sendToAllClients(new GameMessage<>(MessageType.START_GAME, new GameState())); // need fixes to make this functional

//        playRound();
    }

    public void playRound() throws IOException {
        // ############## Pre-Flop ####################

        for (User user : players) {
            user.getHand().dealCard(deck);
            user.getHand().dealCard(deck);
        }
        
        // broadcast new game state to update client views
        server.sendToAllClients(new GameMessage<>(MessageType.STATE_UPDATE, this.getGameState() ));

        // execute one betting round (pre-flop) - end of betting round should broadcast state update again to update pot, bet, etc
        executeBettingRound();  

        // ############## Post-Flop ####################

        for (int i = 0; i < 3; i++) {
            communityCards.dealCard(deck);
        }

        server.sendToAllClients(new GameMessage<>(MessageType.STATE_UPDATE, this.getGameState() ));

        executeBettingRound();

        // ############## Turn ####################

        communityCards.dealCard(deck);

        server.sendToAllClients(new GameMessage<>(MessageType.STATE_UPDATE, this.getGameState() ));

        executeBettingRound();

        // ############## River ####################

        communityCards.dealCard(deck);

        server.sendToAllClients(new GameMessage<>(MessageType.STATE_UPDATE, this.getGameState() ));

        executeBettingRound();

        // ############## Showdown ####################

        // check each players hand to determine a winner of this round - method should handle broadcast of winner
        determineWinner();
    }

    public void executeBettingRound() {
        currentBet = 0;

        for (User user : players) {
            if (!user.isActive()) continue;

            // notify player that it is their turn
            GameMessage<User> msg = new GameMessage<>(MessageType.NOTIFY_TURN, user);
            try {
                // server has a map of User-ConnectionToClient - send a turn notification to signal prompt for option
                server.sendToUser(msg, user);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // server SHOULD be doing the handleOption when clients respond to notification
        }
    }

    // this method should only take in GameMessage(PLAYER_ACTION, User)
    public void handleOption(Options opt, User user) {

        validOption = false;

        while (!validOption) {
            switch (opt) {
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

    public void resetGame() {
        deck = new Deck();
        communityCards = new Hand();
        for (User user : players) { user.resetAfterRound(); } // clear all players hands
        pot = 0;
        GUI.resetGUI();
    }

    // getters and setters
    public GameState getGameState() {
        return new GameState(
                this.getPlayerList(),
                this.getCommunityCards(),
                this.getPot(),
                this.getCurrentBet()
        );
    }

    public Hand getCommunityCards() { return communityCards; }
    public TexasHoldemPanel getTexasHoldemPanel() { return GUI; }
    public int getPot() { return pot; }
    public int getCurrentBet() { return currentBet; }
    public int getRaiseAmount() { return raiseAmount; }
    public ArrayList<User> getPlayerList() { return players; }

    public void setPlayerOption(Options playerOption) {
        this.playerOption = playerOption;
        latch.countDown();
    }
    public void setRaiseAmount(int raiseAmount) { this.raiseAmount = raiseAmount; }
}