package logic;

import account.User;
import cards.*;
import cards.Hand.HandValue;
import graphics.TexasHoldemPanel;
import java.io.IOException;
import java.util.*;

import logic.GameMessage.*;

import server.GameServer;

public class TexasHoldem {
    private Deck deck = new Deck();
    private Hand communityCards = new Hand();
    private int pot = 0;
    private int currentBet = 0;
    private int raiseAmount = 0;
    private boolean validOption;
    private boolean validRaise;
    private GameServer server;
    private ArrayList<User> players = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private int playersToAct = 0;
    private GameState.GamePhase phase;

    public enum Options {
        CHECK, CALL, RAISE, FOLD
    }

    public TexasHoldem(GameServer gs) {
        this.server = gs;
        this.players = new ArrayList<>(gs.getClients().values());
    }

    public void startGame() {
        resetGame();

        for (User user : players) {
            user.clearHand();
            user.getHand().dealCard(deck);
            user.getHand().dealCard(deck);
        }

        try {
            playRound();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void playRound() throws IOException {
        server.sendToAllClients(new GameMessage<>(MessageType.START_GAME, this.getGameState()));
        server.sendToAllClients(new GameMessage<>(MessageType.STATE_UPDATE, this.getGameState()));

        // execute one betting round (pre-flop) - end of betting round should broadcast state update again to update pot, bet, etc
        this.phase = GameState.GamePhase.PRE_FLOP;
        executeBettingRound();

        nextPhase();
    }

    public void executeBettingRound() throws IOException {
        currentBet = 0;
        currentPlayerIndex = 0;

        playersToAct = (int) players.stream().filter(User::isActive).count();

        // recursive method to handle turns
        promptCurrentUser();
    }

    public void nextPhase() throws IOException {
        switch (phase) {
            case PRE_FLOP -> {
                System.out.println("Pre flop");
                phase = GameState.GamePhase.FLOP;
            }
            case FLOP -> {
                System.out.println("Flop");
                communityCards.clear();
                for (int i = 0; i < 3; i++) {
                    communityCards.dealCard(deck);
                }
                phase = GameState.GamePhase.TURN;
            }
            case TURN -> {
                System.out.println("Turn");
                communityCards.dealCard(deck);
                phase = GameState.GamePhase.RIVER;
            }
            case RIVER -> {
                System.out.println("River");
                communityCards.dealCard(deck);
                phase = GameState.GamePhase.SHOWDOWN;
                determineWinner();
                return;
            }
        }

        server.sendToAllClients(new GameMessage<>(MessageType.STATE_UPDATE, this.getGameState() ));
        executeBettingRound();
    }

    public void promptCurrentUser() throws IOException {
        // broadcast state update if end of betting round
        if (playersToAct == 0) {
            server.sendToAllClients(new GameMessage<>(MessageType.STATE_UPDATE, this.getGameState()));
            return;
        }

        User currentUser = players.get(currentPlayerIndex);

        if (!currentUser.isActive()) {
            System.out.println("You are not in active state");
            promptNextUser();
            return;
        }

        try {
            server.sendToUser(new GameMessage<>(MessageType.NOTIFY_TURN, currentUser), currentUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void promptNextUser() throws IOException {
        playersToAct--;

        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } while (!players.get(currentPlayerIndex).isActive());

        promptCurrentUser(); // call this after action is recieved
    }

    // this method should only take in GameMessage(PLAYER_ACTION, User)
    public void handleOption(Options opt, User user, int raiseAmount) throws IOException {
        validOption = false;

        switch (opt) {
            case CHECK ->
                handleCheck(user);
            case CALL ->
                handleCall(user);
            case RAISE ->
                handleRaise(user, raiseAmount);
            case FOLD ->
                handleFold(user);
        }

        if (validOption) {
            server.sendToAllClients(new GameMessage<>(MessageType.STATE_UPDATE, this.getGameState()));

            if (players.stream().filter(User::isActive).count() == 1) {
                User winner = players.stream().filter(User::isActive).findFirst().get();
                winner.setBalance(winner.getBalance() + pot);
                server.sendToAllClients(new GameMessage<>(MessageType.WINNER, winner));
                return;
            }

            if (--playersToAct <= 0) {
                nextPhase();
            } else {
                promptNextUser();
            }
        }
    }

    public void handleCheck(User user) {
        if (getCurrentBet() == 0) {
            validOption = true;
        }
    }

    public void handleCall(User user) {
        // ensure there is a current bet and player has enough chips
        if (currentBet > 0) {
            int amtToCall = currentBet - user.getCurrentBet();

            if (user.getBalance() >= amtToCall) {
                user.setBalance(user.getBalance() - amtToCall);
                pot += amtToCall;
            }
            validOption = true;
        }
    }

    public void handleRaise(User user, int raiseAmount) throws IOException {
        // ensure player has enough to raise
        if (raiseAmount > 0 && (user.getBalance() >= currentBet + raiseAmount)) {
            user.setBalance(user.getBalance() - raiseAmount); // may need to change this to cBet + raiseAmt
            currentBet += raiseAmount;
            pot += raiseAmount;
            validOption = true;
        } else {
            server.sendToUser(new GameMessage<>(MessageType.ERROR, "ERROR" ), user);
        }
    }


    public void handleFold(User user) {
        user.setActive(false);
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
        currentBet = 0;
        raiseAmount = 0;
        currentPlayerIndex = 0;
        playersToAct = 0;
        phase = GameState.GamePhase.PRE_FLOP;
    }

    // getters and setters
    public GameState getGameState() {
        return new GameState(
                this.getPlayerList(),
                this.getCommunityCards(),
                this.getPot(),
                this.getCurrentBet(),
                this.getCurrentPlayerIndex(),
                this.getCurrentPhase()
        );
    }

    public boolean isRoundOver() {
        long activePlayers = players.stream().filter(User::isActive).count();
        return activePlayers <= 1 || playersToAct == 0;
    }
    public Hand getCommunityCards() { return communityCards; }
    public int getPot() { return pot; }
    public int getCurrentBet() { return currentBet; }
    public int getRaiseAmount() { return raiseAmount; }
    public ArrayList<User> getPlayerList() { return players; }
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
    public GameState.GamePhase getCurrentPhase() { return phase; }
}