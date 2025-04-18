package server;

import account.User;
import cards.Card;
import java.util.ArrayList;

public class Game {
    public User findPlayer(String username) {
    return null;
    }

    public void handleRaise(User player, int raiseAmount) {
    }

    public void handleCall(User player) {
    }

    public void handleFold(User player) {
    }

    public void handleCheck(User player) {
    }

    public String getName() {
    return "test";
    }

    public int getPot() {
    return 0;
    }

    public int getCurrentBet() {
        return 0;
    }

    public String getCurrentPlayerUsername() {
    return "test";
    }

    public ArrayList<Card> getCommunityCards() {
        return null;
    }

    public ArrayList<User> getPlayers() {
        return null;
    }

    public void addPlayer(User player) {
    }

    public void setName(String gameName) {
    }

    public void startGame() {

    }
}