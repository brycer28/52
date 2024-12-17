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
    private boolean faceUp = true;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public String toString() {
        String cardRank = rank.toString().toLowerCase();
        String cardSuit = suit.toString().toLowerCase();

        return cardRank + "_of_" + cardSuit;
    }


    public boolean isFaceUp() { return faceUp; }
    public void setFaceUp(boolean faceUp) { this.faceUp = faceUp; }
    public Suit getSuit() { return suit; }
    public Rank getRank() { return rank; }
}
