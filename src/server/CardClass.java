cpackage server;

public class CardClass {
    private String suit;
    private String rank;

    public CardClass(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }
}
