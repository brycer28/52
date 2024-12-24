package logic;

import cards.*;

import java.util.concurrent.CountDownLatch;

public class Blackjack {
    private Hand playerHand = new Hand();
    private Hand dealerHand = new Hand();
    private Deck deck;
    private CountDownLatch latch;


    public Blackjack() {
        //resetGame();

        new Thread(() -> {

        }).start();
    }

    public void playRound() {
        playerHand.dealCard(deck);
        dealerHand.dealCard(deck);

        System.out.println("Would you like to hit or stand? - Current hand: " + playerHand.)
    }

    public void
}
