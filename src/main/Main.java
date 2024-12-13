package main;

import cards.*;

public class Main {
    public static void main(String[] args) {
        // create cards
        Card c1 = new Card(Card.Suit.SPADES, Card.Rank.FIVE);
        Card c2 = new Card(Card.Suit.DIAMONDS, Card.Rank.TWO);
        Card c3 = new Card(Card.Suit.DIAMONDS, Card.Rank.THREE);
        Card c4 = new Card(Card.Suit.DIAMONDS, Card.Rank.FOUR);
        Card c5 = new Card(Card.Suit.DIAMONDS, Card.Rank.FOUR);
        Card c6 = new Card(Card.Suit.DIAMONDS, Card.Rank.SIX);
        Card c7 = new Card(Card.Suit.DIAMONDS, Card.Rank.EIGHT);

        // create hand
        Hand hand = new Hand();
        hand.add(c1);
        hand.add(c2);
        hand.add(c3);
        hand.add(c4);
        hand.add(c5);
        hand.add(c6);
        hand.add(c7);

        //System.out.println(hand.toString());

        System.out.println("PAIR: " + Hand.isPair(hand));
        System.out.println("TWO PAIR: " + Hand.isTwoPair(hand));
        System.out.println("THREE OF A KIND: " + Hand.isThreeOfAKind(hand));
        System.out.println("STRAIGHT: " + Hand.isStraight(hand));
        System.out.println("FLUSH: " + Hand.isFlush(hand));
        System.out.println("FULL HOUSE: " + Hand.isFullHouse(hand));
        System.out.println("FOUR OF A KIND: " + Hand.isFourOfAKind(hand));
        System.out.println("STRAIGHT FLUSH: " + Hand.isStraightFlush(hand));
        System.out.println("ROYAL FLUSH: " + Hand.isRoyalFlush(hand));

        System.out.println("\nBEST HAND: " + cards.Hand.evaluateHand(hand));
    }
}
