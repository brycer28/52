package logic;

import java.io.Serializable;

import java.util.ArrayList;

import account.User;
import cards.Hand;

public class GameState implements Serializable {

    private ArrayList<User> players;
    private Hand communityCards;
    private int pot;
    private int currentBet;
    private int currentPlayerIndex;
    private GamePhase phase;

    public enum GamePhase {
        PRE_FLOP, FLOP, TURN, RIVER, SHOWDOWN, END
    }

    public GameState(ArrayList<User> players, Hand communityCards, int pot, int currentBet, int currentPlayerIndex, GamePhase phase) {
        this.players = players;
        this.communityCards = communityCards;
        this.pot = pot;
        this.currentBet = currentBet;
        this.currentPlayerIndex = currentPlayerIndex;
        this.phase = phase;
    }

    // Getters
    public ArrayList<User> getPlayers() { return players; }
    public Hand getCommunityCards() { return communityCards; }
    public int getPot() { return pot; }
    public int getCurrentBet() { return currentBet; }
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
    public GamePhase getPhase() { return phase; }
}
