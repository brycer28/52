package logic;

import java.util.ArrayList;

import account.User;
import cards.Hand;

public class GameState {
    private ArrayList<User> players;
    private Hand communityCards;
    private int pot;
    private int currentBet;


    public enum GamePhase {
        PRE_FLOP, FLOP, TURN, RIVER, SHOWDOWN, END
    }

    public GameState(ArrayList<User> players, Hand communityCards, int pot, int currentBet) {
        this.players = players;
        this.communityCards = communityCards;
        this.pot = pot;
        this.currentBet = currentBet;

    }

    // Getters
    public ArrayList<User> getPlayers() { return players; }
    public Hand getCommunityCards() { return communityCards; }
    public int getPot() { return pot; }
    public int getCurrentBet() { return currentBet; }

}
