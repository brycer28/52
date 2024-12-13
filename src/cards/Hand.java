package cards;

import java.util.*;
import java.util.stream.Collectors;

public class Hand extends ArrayList<Card> {
    public enum HandValue {
        HIGH_CARD, PAIR, TWO_PAIR, THREE_OF_A_KIND,
        STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND,
        STRAIGHT_FLUSH, ROYAL_FLUSH
    }

    public static HandValue evaluateHand(Hand hand) {
        if (hand == null) {
            throw new NullPointerException("Hand is null");
        }

        if (isRoyalFlush(hand) != null) {
            return HandValue.ROYAL_FLUSH;
        } else if (isStraightFlush(hand) != null) {
            return HandValue.STRAIGHT_FLUSH;
        } else if (isFourOfAKind(hand) != null) {
            return HandValue.FOUR_OF_A_KIND;
        } else if (isFullHouse(hand) != null) {
            return HandValue.FULL_HOUSE;
        } else if (isFlush(hand) != null) {
            return HandValue.FLUSH;
        } else if (isStraight(hand) != null) {
            return HandValue.STRAIGHT;
        } else if (isThreeOfAKind(hand) != null) {
            return HandValue.THREE_OF_A_KIND;
        } else if (isTwoPair(hand) != null) {
            return HandValue.TWO_PAIR;
        } else if (isPair(hand) != null) {
            return HandValue.PAIR;
        } else {
            return HandValue.HIGH_CARD;
        }
    }

    public static Card.Suit isRoyalFlush(Hand hand) {
        if (isStraight(hand) == null || isFlush(hand) == null ) {
            return null;
        }

        Map<Card.Suit, Long> suitCount = hand.stream()
                .collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()));

        Card.Suit flushSuit = Collections.max(suitCount.entrySet(), Map.Entry.comparingByValue()).getKey();

        Hand flushSuitOnly = hand.stream()
                .filter(card -> card.getSuit().equals(flushSuit))
                .collect(Collectors.toCollection(Hand::new));

        List<Card.Rank> royalStraight = Arrays.asList(
                Card.Rank.TEN, Card.Rank.JACK, Card.Rank.QUEEN, Card.Rank.QUEEN, Card.Rank.KING, Card.Rank.ACE);

        List<Card.Rank> flushSuitOnlyRanks = flushSuitOnly.stream()
                .map(Card::getRank)
                .toList();

        if (flushSuitOnlyRanks.containsAll(royalStraight)) {
            return flushSuit;
        }
        return null;
    }

    public static Card.Suit isStraightFlush(Hand hand) {
        if (isStraight(hand) == null || isFlush(hand) == null ) {
            return null;
        }

        Map<Card.Suit, Long> suitCount = hand.stream()
                .collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()));

        Card.Suit flushSuit = Collections.max(suitCount.entrySet(), Map.Entry.comparingByValue()).getKey();

        Hand flushSuitOnly = hand.stream()
                .filter(card -> card.getSuit().equals(flushSuit))
                .collect(Collectors.toCollection(Hand::new));

        if (isStraight(flushSuitOnly) != null) {
            return flushSuit;
        }
        return null;
    }

    public static Card.Rank isFourOfAKind(Hand hand) {
        Map<Card.Rank, Long> rankCount = hand.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        return rankCount.entrySet().stream()
                .filter(entry -> entry.getValue() >= 4)
                .map(Map.Entry::getKey)
                .max(Comparator.comparingInt(Enum::ordinal))
                .orElse(null);
    }

    public static Card.Rank isFullHouse(Hand hand) {
        Map<Card.Rank, Long> rankCount = hand.stream()
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

    public static Card.Suit isFlush(Hand hand) {
        Map<Card.Suit, Long> suitCount = hand.stream()
                .collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()));

        return suitCount.entrySet().stream()
                .filter(entry -> entry.getValue() >= 5)
                .map(Map.Entry::getKey)
                .max(Comparator.comparingInt(Enum::ordinal))
                .orElse(null);
    }

    public static Card.Rank isStraight(Hand hand) {
        // hand must be at contain 5 cards to make a straight
        if (hand.size() < 5) return null;

        // get set of unique ranks
        Set<Card.Rank> uniqueRanks = hand.stream()
                .map(Card::getRank)
                .collect(Collectors.toSet());

        if (uniqueRanks.size() < 5) return null;

        // sort unique ranks
        List<Card.Rank> sortedRanks = uniqueRanks.stream()
                .sorted((r1, r2) -> Integer.compare(r1.ordinal(), r2.ordinal()))
                .toList();


        return null;
    }

//    // helper method for isStraight, checks if first 5 elements of a subarray are consecutive
//    public static boolean hasFiveConsecutive(List<Card> hand) {
//        if (hand.size() != 5) {
//            return false;
//        }
//
//        // cast to a set to check for duplicates
//        Set<Card.Rank> uniqueRanks = new HashSet<>();
//        for (Card card : hand) {
//            uniqueRanks.add(card.getRank());
//        }
//
//        if (uniqueRanks.size() < 5) {
//            return false;
//        }
//
//        // sort unique ranks into sorted lists
//        List<Card.Rank> sortedRanks = new ArrayList<>(uniqueRanks);
//        Collections.sort(sortedRanks);
//
//        for (int i = 0; i < 4; i++) {
//            int currentRank = sortedRanks.get(i).ordinal();
//            int nextRank = sortedRanks.get(i + 1).ordinal();
//
//            if (nextRank != currentRank + 1) {
//                // special case for checking ace (can be 1 or 14)
//                if (!(currentRank == 12 && nextRank == 0)) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }

    public static Card.Rank isThreeOfAKind(Hand hand) {
        Map<Card.Rank, Long> rankCount = hand.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        return rankCount.entrySet().stream()
                .filter(entry -> entry.getValue() >= 3)
                .map(Map.Entry::getKey)
                .max(Comparator.comparingInt(Enum::ordinal))
                .orElse(null);
    }

    public static Card.Rank isTwoPair(Hand hand) {
        Map<Card.Rank, Long> rankCount = hand.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        // count the number of pairs in the hand
        List<Card.Rank> pairRanks = rankCount.entrySet().stream()
                .filter(entry -> entry.getValue() >= 2)
                .map(Map.Entry::getKey)
                .sorted((r1, r2) -> Integer.compare(r1.ordinal(), r2.ordinal()))
                .toList();

        if (pairRanks.size() >= 2) {
            return pairRanks.get(0);
        }
        return null;
    }

    public static Card.Rank isPair(Hand hand) {
        Map<Card.Rank, Long> rankCount = hand.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        return rankCount.entrySet().stream()
                .filter(entry -> entry.getValue() >= 2)
                .map(Map.Entry::getKey)
                .max(Comparator.comparingInt(Enum::ordinal))
                .orElse(null);
    }

    public static Card.Rank isHighCard(Hand hand) {
        Card.Rank rank = hand.stream()
                .map((Card::getRank))
                .max(Comparator.comparingInt(Enum::ordinal))
                .orElse(null);

        return rank;
    }
}
