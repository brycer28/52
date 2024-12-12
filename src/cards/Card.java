package cards;

public class Card {
    public enum Suit {
        CLUBS, DIAMONDS, HEARTS, SPADES;
    }
    public enum Rank {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT,
        NINE, TEN, JACK, QUEEN, KING, ACE;
    }
    private Suit suit;
    private Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }
}
