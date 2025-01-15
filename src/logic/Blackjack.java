package logic;

import cards.*;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class Blackjack {
    private Scanner scanner = new Scanner(System.in);
    private Hand playerHand = new Hand();
    private Hand dealerHand = new Hand();
    private Deck deck = new Deck();
    private CountDownLatch latch;

    public Blackjack() {
        playRound();
    }

    public void playRound() {
        playerHand.dealCard(deck);
        dealerHand.dealCard(deck);


        System.out.println("Player Hand: " + playerHand.toString());

        boolean roundGoing = true;
        while (roundGoing) {
            System.out.println("Would you like to hit (0) or stand (1)? - Current hand: " + playerHand.getHandValue() + playerHand.toString());
            int playerOption = scanner.nextInt();

            switch (playerOption) {
                case 0: {
                    hit(playerHand);
                    if (playerHand.getHandValue() > 21) roundGoing = false;
                    break;
                }
                case 1: {
                    stand(playerHand);
                    roundGoing = false;
                    break;
                }
                default: {}
            };
        }

        if (playerHand.getHandValue() > 21) {
            System.out.println("BUST! You lose.");
        }
    }

    public void hit(Hand hand) {
        hand.dealCard(deck);

        System.out.println("Hit!");
    }

    public void stand(Hand hand) {
        //do nothing

        System.out.println("Stand!");
    }
}
