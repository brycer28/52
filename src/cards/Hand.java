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

    // return ordinal value of hand value
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
                case TEN: case JACK: case QUEEN: case KING: {
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
            case ROYAL_FLUSH: return isRoyalFlush();
            case STRAIGHT_FLUSH: return isStraightFlush();
            case FOUR_OF_A_KIND: return isFourOfAKind();
            case FULL_HOUSE: return isFullHouse();
            case FLUSH: return isFlush();
            case STRAIGHT: return isStraight();
            case THREE_OF_A_KIND: return isThreeOfAKind();
            case TWO_PAIR: return isTwoPair();
            case PAIR: return isPair();
            case HIGH_CARD: return isHighCard();
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
        if (this.isStraight() == null || this.isFlush() == null ) return null;

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

        if (flushSuitOnlyRanks.containsAll(royalStraight)) return flushSuit;
        return null;
    }

    public Card.Suit isStraightFlush() {
        if (this.isStraight() == null || this.isFlush() == null ) return null;

        Map<Card.Suit, Long> suitCount = this.stream()
                .collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()));

        Card.Suit flushSuit = Collections.max(suitCount.entrySet(), Map.Entry.comparingByValue()).getKey();

        Hand flushSuitOnly = this.stream()
                .filter(card -> card.getSuit().equals(flushSuit))
                .collect(Collectors.toCollection(Hand::new));

        if (flushSuitOnly.isStraight() != null) return flushSuit;

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

        Optional<Card.Rank> pairRank = rankCount.entrySet().stream()
                .filter(entry -> entry.getValue() == 2)
                .map(Map.Entry::getKey)
                .findAny();

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
        if (this.size() < 5) return null;

        Set<Card.Rank> uniqueRanks = this.stream()
                .map(Card::getRank)
                .collect(Collectors.toSet());

        if (uniqueRanks.size() < 5) return null;

        List<Card.Rank> sortedRanks = uniqueRanks.stream()
                .sorted(Comparator.comparingInt(Enum::ordinal))
                .toList();

        for (int i = 0; i <= sortedRanks.size() - 5; i++) {
            boolean isStraight = true;
            for (int j = 0; j < 4; j++) {
                int curr = sortedRanks.get(i + j).ordinal();
                int next = sortedRanks.get(i + j + 1).ordinal();
                if (next != curr + 1) {
                    isStraight = false;
                    break;
                }
            }
            if (isStraight) return sortedRanks.get(i + 4);
        }

        Set<Card.Rank> aceLowStraight = Set.of(Card.Rank.ACE, Card.Rank.TWO, Card.Rank.THREE, Card.Rank.FOUR, Card.Rank.FIVE);
        if (uniqueRanks.containsAll(aceLowStraight)) return Card.Rank.FIVE;

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

        List<Card.Rank> pairRanks = rankCount.entrySet().stream()
                .filter(entry -> entry.getValue() >= 2)
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparingInt(Enum::ordinal))
                .toList();

        if (pairRanks.size() >= 2) return pairRanks.getLast();
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
                .map(Card::getRank)
                .max(Comparator.comparingInt(Enum::ordinal))
                .orElse(null);
    }

    // Added method so ServerClass can call player.getHand().getCards()
    public List<Card> getCards() {
        return this;
    }
}
