package ivs.game.accessories.cards.cardholder;

import ivs.game.accessories.cards.ordering.PlayingCardComparator;
import ivs.game.accessories.cards.ordering.RankWeightComparator;
import ivs.game.accessories.cards.ordering.SuitWeightComparator;
import ivs.game.accessories.cards.core.type.JokerCard;
import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.core.type.StandardCard;
import ivs.game.accessories.cards.core.type.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Covers tests for functionality that directly depends on the card comparator.
// This includes finding minimum and maximum cards and any other operation relying on comparator-defined ordering.
@SuppressWarnings("DataFlowIssue")
class StandardCardHolderComparatorTest {

    private static final Comparator<PlayingCard> CARD_COMPARATOR =
            new PlayingCardComparator(
                    SuitWeightComparator.ofOrder("S", "D", "C", "H"),
                    RankWeightComparator.ofOrder("A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K")
            );

    private StandardCardHolder cardHolder;

    @BeforeEach
    void setUp() {
        cardHolder = new StandardCardHolder(CARD_COMPARATOR);
    }

    @Test
    @DisplayName("findMin(suit) should throw NullPointerException if argument is null")
    void findMinThrowsIfNullSuit() {
        assertThrows(NullPointerException.class, () -> cardHolder.findMin(null));
    }

    @Test
    @DisplayName("findMax(suit) should throw NullPointerException if argument is null")
    void findMaxThrowsIfNullSuit() {
        assertThrows(NullPointerException.class, () -> cardHolder.findMax(null));
    }

    @Test
    @DisplayName("findMin(suit) should return Optional.empty() if no cards with specified suit")
    void findMinReturnsEmptyIfNoCardsOfSuit() {
        // Arrange: only clubs and jokers
        cardHolder.add(StandardCard.TWO_CLUBS);
        cardHolder.add(StandardCard.THREE_CLUBS);
        cardHolder.add(JokerCard.JOKER_1);

        // Act & Assert: should be empty for SPADES
        assertTrue(cardHolder.findMin(Suit.SPADES).isEmpty(), "Should return empty if there is no card with given suit");
    }

    @Test
    @DisplayName("findMax(suit) should return Optional.empty() if no cards with specified suit")
    void findMaxReturnsEmptyIfNoCardsOfSuit() {
        // Arrange: only diamonds and jokers
        cardHolder.add(StandardCard.TWO_DIAMONDS);
        cardHolder.add(StandardCard.KING_DIAMONDS);
        cardHolder.add(JokerCard.JOKER_2);

        // Act & Assert: should be empty for CLUBS
        assertTrue(cardHolder.findMax(Suit.CLUBS).isEmpty(), "Should return empty if there is no card with given suit");
    }

    @Test
    @DisplayName("findMin(suit) should skip jokers and return lowest card by comparator for suit")
    void findMinReturnsLowestCardOfSuit() {
        // Arrange
        cardHolder.add(StandardCard.FOUR_HEARTS);
        cardHolder.add(StandardCard.ACE_HEARTS);
        cardHolder.add(StandardCard.KING_HEARTS);
        cardHolder.add(JokerCard.JOKER_1);

        // Act
        var result = cardHolder.findMin(Suit.HEARTS);

        // Assert
        assertTrue(result.isPresent(), "Should return present Optional");
        assertEquals(StandardCard.ACE_HEARTS, result.get(), "Should return card with minimal rank for suit (by comparator)");
    }

    @Test
    @DisplayName("findMax(suit) should skip jokers and return highest card by comparator for suit")
    void findMaxReturnsHighestCardOfSuit() {
        // Arrange
        cardHolder.add(StandardCard.ACE_SPADES);
        cardHolder.add(StandardCard.KING_HEARTS);
        cardHolder.add(StandardCard.KING_SPADES);
        cardHolder.add(JokerCard.JOKER_2);

        // Act
        var result = cardHolder.findMax(Suit.SPADES);

        // Assert
        assertTrue(result.isPresent(), "Should return present Optional");
        assertEquals(StandardCard.KING_SPADES, result.get(), "Should return card with maximal rank for suit (by comparator)");
    }

    @Test
    @DisplayName("findMin(suit) and findMax(suit) should ignore jokers regardless of order")
    void findMinMaxIgnoresJokers() {
        // Arrange: only jokers for diamonds
        cardHolder.add(JokerCard.JOKER_1);
        cardHolder.add(JokerCard.JOKER_2);

        // Act & Assert
        assertTrue(cardHolder.findMin(Suit.DIAMONDS).isEmpty(), "Should not consider jokers for min");
        assertTrue(cardHolder.findMax(Suit.DIAMONDS).isEmpty(), "Should not consider jokers for max");
    }

    @Test
    @DisplayName("findClosestLower(reference) should return Optional.empty() when no lower card in suit")
    void findClosestLowerReturnsEmptyWhenNoLower() {
        // Arrange: only a single card of the suit
        cardHolder.add(StandardCard.FOUR_SPADES);

        // Act
        var result = cardHolder.findClosestLower(StandardCard.FOUR_SPADES);

        // Assert
        assertTrue(result.isEmpty(), "Should return empty if there is no lower card in the same suit");
    }

    @Test
    @DisplayName("findClosestHigher(reference) should return Optional.empty() when no higher card in suit")
    void findClosestHigherReturnsEmptyWhenNoHigher() {
        // Arrange: only a single card of the suit
        cardHolder.add(StandardCard.ACE_HEARTS);

        // Act
        var result = cardHolder.findClosestHigher(StandardCard.ACE_HEARTS);

        // Assert
        assertTrue(result.isEmpty(), "Should return empty if there is no higher card in the same suit");
    }

    @Test
    @DisplayName("findClosestLower(reference) should return the closest strictly lower card of same suit (by comparator)")
    void findClosestLowerReturnsCorrectCard() {
        // Arrange: add multiple same-suit cards
        cardHolder.add(StandardCard.THREE_HEARTS);
        cardHolder.add(StandardCard.TWO_CLUBS);
        cardHolder.add(StandardCard.THREE_CLUBS);
        cardHolder.add(StandardCard.FIVE_CLUBS);
        cardHolder.add(StandardCard.NINE_CLUBS);
        cardHolder.add(JokerCard.JOKER_1);

        // Act
        var result = cardHolder.findClosestLower(StandardCard.FIVE_CLUBS);

        // Assert
        assertTrue(result.isPresent(), "Should return a lower card");
        assertEquals(StandardCard.THREE_CLUBS, result.get(), "Should return the greatest less than reference");
    }

    @Test
    @DisplayName("findClosestHigher(reference) should return the closest strictly higher card of same suit (by comparator)")
    void findClosestHigherReturnsCorrectCard() {
        // Arrange: add multiple same-suit cards
        cardHolder.add(StandardCard.THREE_DIAMONDS);
        cardHolder.add(StandardCard.FIVE_DIAMONDS);
        cardHolder.add(StandardCard.TEN_DIAMONDS);
        cardHolder.add(StandardCard.JACK_DIAMONDS);
        cardHolder.add(StandardCard.TEN_SPADES);
        cardHolder.add(JokerCard.JOKER_4);

        // Act
        var result = cardHolder.findClosestHigher(StandardCard.FIVE_DIAMONDS);

        // Assert
        assertTrue(result.isPresent(), "Should return a higher card");
        assertEquals(StandardCard.TEN_DIAMONDS, result.get(), "Should return the smallest greater than reference");
    }

    @Test
    @DisplayName("findClosestLower(reference) should throw NullPointerException if reference is null")
    void findClosestLowerThrowsIfNull() {
        assertThrows(NullPointerException.class, () -> cardHolder.findClosestLower(null));
    }

    @Test
    @DisplayName("findClosestHigher(reference) should throw NullPointerException if reference is null")
    void findClosestHigherThrowsIfNull() {
        assertThrows(NullPointerException.class, () -> cardHolder.findClosestHigher(null));
    }

    @Test
    @DisplayName("getAllLowerOfSuit(reference) should throw NullPointerException if reference is null")
    void getAllLowerOfSuitThrowsIfNullReference() {
        assertThrows(NullPointerException.class, () -> cardHolder.getAllLowerOfSuit(null));
    }

    @Test
    @DisplayName("getAllHigherOfSuit(reference) should throw NullPointerException if reference is null")
    void getAllHigherOfSuitThrowsIfNullReference() {
        assertThrows(NullPointerException.class, () -> cardHolder.getAllHigherOfSuit(null));
    }

    @Test
    @DisplayName("getAllLowerOfSuit returns empty list when no lower cards of same suit")
    void getAllLowerOfSuitReturnsEmptyIfNoLower() {
        cardHolder.add(StandardCard.THREE_SPADES);

        var lower = cardHolder.getAllLowerOfSuit(StandardCard.THREE_SPADES);

        assertTrue(lower.isEmpty(), "Should return empty list if no lower cards of suit are present");
    }

    @Test
    @DisplayName("getAllHigherOfSuit returns empty list when no higher cards of same suit")
    void getAllHigherOfSuitReturnsEmptyIfNoHigher() {
        cardHolder.add(StandardCard.KING_HEARTS);

        var higher = cardHolder.getAllHigherOfSuit(StandardCard.KING_HEARTS);

        assertTrue(higher.isEmpty(), "Should return empty list if no higher cards of suit are present");
    }

    @Test
    @DisplayName("getAllLowerOfSuit returns all strictly lower cards (same suit, ordered by comparator)")
    void getAllLowerOfSuitReturnsStrictlyLowerOrdered() {
        cardHolder.add(StandardCard.FOUR_CLUBS);
        cardHolder.add(StandardCard.TWO_CLUBS);
        cardHolder.add(StandardCard.FIVE_CLUBS);
        cardHolder.add(StandardCard.NINE_CLUBS);
        cardHolder.add(StandardCard.FIVE_SPADES);
        cardHolder.add(JokerCard.JOKER_1);

        var actual = cardHolder.getAllLowerOfSuit(StandardCard.NINE_CLUBS);

        List<StandardCard> expected = List.of(StandardCard.TWO_CLUBS, StandardCard.FOUR_CLUBS, StandardCard.FIVE_CLUBS);
        assertEquals(expected, actual,
                "Should return all (and only) strictly lower cards of the same suit, sorted"
        );
    }

    @Test
    @DisplayName("getAllHigherOfSuit returns all strictly higher cards (same suit, ordered by comparator)")
    void getAllHigherOfSuitReturnsStrictlyHigherOrdered() {
        cardHolder.add(StandardCard.THREE_DIAMONDS);
        cardHolder.add(StandardCard.TEN_DIAMONDS);
        cardHolder.add(StandardCard.FIVE_DIAMONDS);
        cardHolder.add(StandardCard.FIVE_SPADES);
        cardHolder.add(JokerCard.JOKER_1);

        var actual = cardHolder.getAllHigherOfSuit(StandardCard.THREE_DIAMONDS);

        List<StandardCard> expected = List.of(StandardCard.FIVE_DIAMONDS, StandardCard.TEN_DIAMONDS);
        assertEquals(expected, actual,
                "Should return all (and only) strictly higher cards of the same suit, sorted"
        );
    }
}