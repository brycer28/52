package cards;

import cards.Card.Rank;
import java.util.*;
import java.util.stream.Collectors;

public class Hand extends ArrayList<Card> {
    public enum HandValue {
        HIGH_CARD, PAIR, TWO_PAIR, THREE_OF_A_KIND,
        STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND,
        STRAIGHT_FLUSH, ROYAL_FLUSH
    }

    public int getHandValue() {
        int handValue = 0;
        int aceCount = 0;


        for (Card card : this) {
            switch (card.getRank()) {
                case ACE: {
                    handValue += 11;
                    aceCount++;
                    break;
                }
                case Rank.TEN: case Rank.JACK: case Rank.QUEEN: case Rank.KING: {
                    handValue += 10;
                    break;
                }
                default: {
                    handValue += card.getRank().ordinal() + 2;
                    break;
                }
            }
        }
        return handValue;
    }

    public void dealCard(Deck deck) {
        add(deck.peek());
        deck.pop();
    }

    public Object determineTieBreaker() {
        switch (this.evaluateHand()) {
            case HandValue.ROYAL_FLUSH: return isRoyalFlush();
            case HandValue.STRAIGHT_FLUSH: return isStraightFlush();
            case HandValue.FOUR_OF_A_KIND: return isFourOfAKind();
            case HandValue.FULL_HOUSE: return isFullHouse();
            case HandValue.FLUSH: return isFlush();
            case HandValue.STRAIGHT: return isStraight();
            case HandValue.THREE_OF_A_KIND: return isThreeOfAKind();
            case HandValue.TWO_PAIR: return isTwoPair();
            case HandValue.PAIR: return isPair();
            case HandValue.HIGH_CARD: return isHighCard();
        }
        return null;
    }

    public HandValue evaluateHand() {
        if (isRoyalFlush() != null) return HandValue.ROYAL_FLUSH;
        else if (isStraightFlush() != null) return HandValue.STRAIGHT_FLUSH;
        else if (isFourOfAKind() != null) return HandValue.FOUR_OF_A_KIND;
        else if (isFullHouse() != null) return HandValue.FULL_HOUSE;
        else if (isFlush() != null) return HandValue.FLUSH;
        else if (isStraight() != null) return HandValue.STRAIGHT;
        else if (isThreeOfAKind() != null) return HandValue.THREE_OF_A_KIND;
        else if (isTwoPair() != null) return HandValue.TWO_PAIR;
        else if (isPair() != null) return HandValue.PAIR;
        else return HandValue.HIGH_CARD;
    }



    public Card.Suit isRoyalFlush() {
        if (this.isStraight() == null || this.isFlush() == null ) {
            return null;
        }

        Map<Card.Suit, Long> suitCount = this.stream()
                .collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()));

        Card.Suit flushSuit = Collections.max(suitCount.entrySet(), Map.Entry.comparingByValue()).getKey();

        Hand flushSuitOnly = this.stream()
                .filter(card -> card.getSuit().equals(flushSuit))
                .collect(Collectors.toCollection(Hand::new));

        List<Card.Rank> royalStraight = Arrays.asList(
                Card.Rank.TEN, Card.Rank.JACK, Card.Rank.QUEEN, Card.Rank.KING, Card.Rank.ACE);

        List<Card.Rank> flushSuitOnlyRanks = flushSuitOnly.stream()
                .map(Card::getRank)
                .toList();

        if (flushSuitOnlyRanks.containsAll(royalStraight)) {
            return flushSuit;
        }
        return null;
    }

    public Card.Suit isStraightFlush() {
        if (this.isStraight() == null || this.isFlush() == null ) {
            return null;
        }

        Map<Card.Suit, Long> suitCount = this.stream()
                .collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()));

        Card.Suit flushSuit = Collections.max(suitCount.entrySet(), Map.Entry.comparingByValue()).getKey();

        Hand flushSuitOnly = this.stream()
                .filter(card -> card.getSuit().equals(flushSuit))
                .collect(Collectors.toCollection(Hand::new));

        if (flushSuitOnly.isStraight() != null) {
            return flushSuit;
        }

        return null;
    }

    public Card.Rank isFourOfAKind() {
        Map<Card.Rank, Long> rankCount = this.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        return rankCount.entrySet().stream()
                .filter(entry -> entry.getValue() >= 4)
                .map(Map.Entry::getKey)
                .max(Comparator.comparingInt(Enum::ordinal))
                .orElse(null);
    }

    public Card.Rank isFullHouse() {
        Map<Card.Rank, Long> rankCount = this.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        // get rank of the pair
        Optional<Card.Rank> pairRank = rankCount.entrySet().stream()
                .filter(entry -> entry.getValue() == 2)
                .map(Map.Entry::getKey)
                .findAny();

        // get rank of the set
        Optional<Card.Rank> setRank = rankCount.entrySet().stream()
                .filter(entry -> entry.getValue() == 3)
                .map(Map.Entry::getKey)
                .findFirst();

        if (setRank.isPresent() && pairRank.isPresent() && pairRank.get() != setRank.get()) {
            return setRank.get();
        }
        return null;
    }

    public Card.Suit isFlush() {
        Map<Card.Suit, Long> suitCount = this.stream()
                .collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()));

        return suitCount.entrySet().stream()
                .filter(entry -> entry.getValue() >= 5)
                .map(Map.Entry::getKey)
                .max(Comparator.comparingInt(Enum::ordinal))
                .orElse(null);
    }

    public Card.Rank isStraight() {
        // hand must be at contain 5 cards to make a straight
        if (this.size() < 5) return null;

        // get set of unique ranks
        Set<Card.Rank> uniqueRanks = this.stream()
                .map(Card::getRank)
                .collect(Collectors.toSet());

        if (uniqueRanks.size() < 5) return null;

        // sort unique ranks
        List<Card.Rank> sortedRanks = uniqueRanks.stream()
                .sorted((r1, r2) -> Integer.compare(r1.ordinal(), r2.ordinal()))
                .toList();

        // check for regular straights (non-ace low)
        for (int i = 0; i <= sortedRanks.size() -5; i++) {
            boolean isStraight = true;

            for (int j = 0; j < 4; j++) {
                int curr = sortedRanks.get(i + j).ordinal();
                int next = sortedRanks.get(i + j + 1).ordinal();

                if (next != curr + 1) {
                    isStraight = false;
                    break;
                }
            }
            
            if (isStraight) {
                // return highest rank in the straight
                return sortedRanks.get(i + 4);
            }
        }

        // check for an ace-low straight
        Set<Card.Rank> aceLowStraight = Set.of(Card.Rank.ACE, Card.Rank.TWO, Card.Rank.THREE, Card.Rank.FOUR, Card.Rank.FIVE);
        if (uniqueRanks.containsAll(aceLowStraight)) {
            // return a 5 (highest value of an ace low straight)
            return Card.Rank.FIVE;
        }

        return null;
    }

    public Card.Rank isThreeOfAKind() {
        Map<Card.Rank, Long> rankCount = this.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        return rankCount.entrySet().stream()
                .filter(entry -> entry.getValue() >= 3)
                .map(Map.Entry::getKey)
                .max(Comparator.comparingInt(Enum::ordinal))
                .orElse(null);
    }

    public Card.Rank isTwoPair() {
        Map<Card.Rank, Long> rankCount = this.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        // count the number of pairs in the hand
        List<Card.Rank> pairRanks = rankCount.entrySet().stream()
                .filter(entry -> entry.getValue() >= 2)
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparingInt(Enum::ordinal))
                .toList();

        if (pairRanks.size() >= 2) {
            return pairRanks.getFirst();
        }
        return null;
    }

    public Card.Rank isPair() {
        Map<Card.Rank, Long> rankCount = this.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        return rankCount.entrySet().stream()
                .filter(entry -> entry.getValue() >= 2)
                .map(Map.Entry::getKey)
                .max(Comparator.comparingInt(Enum::ordinal))
                .orElse(null);
    }

    public Card.Rank isHighCard() {
        return this.stream()
                .map((Card::getRank))
                .max(Comparator.comparingInt(Enum::ordinal))
                .orElse(null);
    }
}
