package logic;

import java.util.ArrayList;

import account.User;
import cards.Hand;

public class GameState {
    private ArrayList<User> players;
    private Hand communityCards;
    private int pot;
    private int currentBet;
    private boolean isRoundOver;

    public enum GamePhase {
        PRE_FLOP, FLOP, TURN, RIVER, SHOWDOWN, END
    }

    public GameState(ArrayList<User> players, Hand communityCards, int pot, int currentBet, boolean isRoundOver) {
        this.players = players;
        this.communityCards = communityCards;
        this.pot = pot;
        this.currentBet = currentBet;
        this.isRoundOver = isRoundOver; 
    }

    // Getters and Setters
    public List<User> getPlayers() { return players; }

    public void setPlayers(List<User> players) { this.players = players; } 

    public Hand getCommunityCards() { return communityCards; }

    public void setCommunityCards(Hand communityCards) { this.communityCards = communityCards; }

    public int getPot() { return pot; }
}
