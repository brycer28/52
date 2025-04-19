package account;

import cards.Card;
import cards.Hand;
import logic.TexasHoldem.Options;

public class User implements java.io.Serializable {
    private String username;
    private Hand hand;
    private int balance;
    private boolean isActive;
    private int bet;

    public User(String username, int startingBalance) {
        this.username = username;
        this.hand = new Hand();
        this.balance = startingBalance;
        this.isActive = true;
        this.bet = 0;

    }

    // Placeholer method
    public Options getOption() {
        return Options.CHECK; // stub value for now
    }

    public String getUsername() {
        return username;
    }

    public Hand getHand() {
        return hand;
    }

    public void appendHand(Card c) {
        this.hand.add(c);
    }

    public int getBalance() {
        return balance;
    }

    public void updateBalance(int amount) {
        this.balance += amount;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getCurrentBet() {
        return bet;
    }

    public void setCurrentBet(int bet) {
        this.bet = bet;
    }

    public void placeBet(int amount) {
        if (amount > balance) {
            throw new IllegalArgumentException("Not enough balance to bet");

        }
        System.out.println("Player " + username + "betting: " + amount);
    }

    public void fold() {
        isActive = false;
    }

    public void resetCurrentBet(){
        bet = 0;
    }

    public void clearHand() {
        hand.clear();
    }

    @Override
    public String toString() {
        return username + "-Balance $" + balance + ", Betting: $" + bet + ", Hand: " + hand.getHandValue() + ", Active: " + isActive;
    }


}
