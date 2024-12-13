package cards;

import java.util.Collections;
import java.util.Stack;

public class Deck extends Stack<Card> {
    public Deck() {
        super();
        createDeck();
        shuffleDeck();
    }

    public void createDeck() {
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                add(new Card(suit, rank));
            }
        }
    }

    public void shuffleDeck() { Collections.shuffle(this); }
}
