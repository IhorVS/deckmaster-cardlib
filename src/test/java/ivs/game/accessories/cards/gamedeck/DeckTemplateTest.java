package ivs.game.accessories.cards.gamedeck;

import ivs.game.accessories.cards.core.type.JokerCard;
import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.core.type.Rank;
import ivs.game.accessories.cards.core.type.StandardCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeckTemplateTest {

    private static final int DOUBLE_EXTENDED_SIZE = 56;
    private static final int EXTENDED_SIZE = 54;
    private static final int FULL_SIZE = 52;
    private static final int SHORT_SIZE = 36;
    private static final int SMALL_SIZE = 32;
    private static final int TINY_SIZE = 24;

    private static final int JOKER_COUNT_4 = 4;
    private static final int JOKER_COUNT_2 = 2;

    @ParameterizedTest(name = "{0} deckTemplate should have size {1}")
    @DisplayName("getSize() should return correct size for each deckTemplate type")
    @MethodSource("deckTypeAndSizeProvider")
    void getSizeShouldReturnExpectedValue(DeckTemplate deckTemplate, int expectedSize) {
        assertEquals(expectedSize, deckTemplate.getSize(),
                () -> deckTemplate + " deckTemplate: getSize() should return " + expectedSize);
    }

    private static Stream<Arguments> deckTypeAndSizeProvider() {
        return Stream.of(
                Arguments.of(DeckTemplate.DOUBLE_EXTENDED, DOUBLE_EXTENDED_SIZE),
                Arguments.of(DeckTemplate.EXTENDED, EXTENDED_SIZE),
                Arguments.of(DeckTemplate.FULL, FULL_SIZE),
                Arguments.of(DeckTemplate.SHORT, SHORT_SIZE),
                Arguments.of(DeckTemplate.SMALL, SMALL_SIZE),
                Arguments.of(DeckTemplate.TINY, TINY_SIZE)
        );
    }

    @Test
    @DisplayName("DOUBLE_EXTENDED deck should contain 52 standard cards and 4 different jokers")
    void doubleExtendedDeckShouldContain52StandardCardsAnd4Jokers() {
        Set<PlayingCard> cards = DeckTemplate.DOUBLE_EXTENDED.get();
        assertEquals(DOUBLE_EXTENDED_SIZE, cards.size(), "DOUBLE_EXTENDED deck must have 56 cards (52+4Jokers)");
        assertTrue(cards.containsAll(EnumSet.of(
                        JokerCard.JOKER_1, JokerCard.JOKER_2, JokerCard.JOKER_3, JokerCard.JOKER_4)),
                "DOUBLE_EXTENDED should include all 4 jokers");
        long jokerCount = cards.stream().filter(card -> card instanceof JokerCard).count();
        assertEquals(JOKER_COUNT_4, jokerCount, "DOUBLE_EXTENDED should contain exactly 4 jokers");
    }

    @Test
    @DisplayName("EXTENDED deck should contain 52 standard cards and 2 different jokers")
    void extendedDeckShouldContain52StandardCardsAnd2Jokers() {
        Set<PlayingCard> cards = DeckTemplate.EXTENDED.get();
        assertEquals(EXTENDED_SIZE, cards.size(), "EXTENDED deck must have 54 cards (52+2Jokers)");
        assertTrue(cards.contains(JokerCard.JOKER_1), "EXTENDED deck must include JOKER_1");
        assertTrue(cards.contains(JokerCard.JOKER_2), "EXTENDED deck must include JOKER_2");
        long jokerCount = cards.stream().filter(card -> card instanceof JokerCard).count();
        assertEquals(JOKER_COUNT_2, jokerCount, "EXTENDED deck should contain exactly 2 jokers");
    }

    @Test
    @DisplayName("FULL deck should contain all 52 standard cards (no jokers)")
    void fullDeckShouldContainStandardCardsOnly() {
        Set<PlayingCard> cards = DeckTemplate.FULL.get();
        assertEquals(FULL_SIZE, cards.size(), "FULL deck must have 52 cards");
        assertTrue(cards.stream().noneMatch(card -> card instanceof JokerCard), "FULL deck should not contain jokers");
    }

    @Test
    @DisplayName("SHORT deck should contain cards from SIX to ACE (36 cards)")
    void shortDeckShouldContain36CardsFromSixToAce() {
        Set<PlayingCard> cards = DeckTemplate.SHORT.get();
        assertEquals(SHORT_SIZE, cards.size(), "SHORT deck must contain 36 cards");
        assertTrue(cards.stream().allMatch(card ->
                        card.getRank().getId() >= Rank.SIX.getId() &&
                                card.getRank().getId() <= Rank.ACE.getId()),
                "All cards in SHORT deck must be from SIX to ACE");
    }

    @Test
    @DisplayName("SMALL deck should contain cards from SEVEN to ACE (32 cards)")
    void smallDeckShouldContain32CardsFromSevenToAce() {
        Set<PlayingCard> cards = DeckTemplate.SMALL.get();
        assertEquals(SMALL_SIZE, cards.size(), "SMALL deck must contain 32 cards");
        assertTrue(cards.stream().allMatch(card ->
                        card.getRank().getId() >= Rank.SEVEN.getId() &&
                                card.getRank().getId() <= Rank.ACE.getId()),
                "All cards in SMALL deck must be from SEVEN to ACE");
    }

    @Test
    @DisplayName("TINY deck should contain cards from NINE to ACE (24 cards)")
    void tinyDeckShouldContain24CardsFromNineToAce() {
        Set<PlayingCard> cards = DeckTemplate.TINY.get();
        assertEquals(TINY_SIZE, cards.size(), "TINY deck must contain 24 cards");
        assertTrue(cards.stream().allMatch(card ->
                        card.getRank().getId() >= Rank.NINE.getId() &&
                                card.getRank().getId() <= Rank.ACE.getId()),
                "All cards in TINY deck must be from NINE to ACE");
    }

    @ParameterizedTest(name = "get should throw IllegalArgumentException when fromRank={0}, toRank={1}")
    @DisplayName("GameDeck.get() should throw IllegalArgumentException if fromRank > toRank")
    @MethodSource("badRankRanges")
    void getShouldThrowForInvalidRankRange(Rank from, Rank to) {
        assertThrows(IllegalArgumentException.class, () -> DeckTemplate.get(from, to),
                "Should throw if fromRank > toRank");
    }

    private static Stream<Arguments> badRankRanges() {
        return Stream.of(
                Arguments.of(Rank.KING, Rank.TEN),
                Arguments.of(Rank.ACE, Rank.SIX)
        );
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    @DisplayName("GameDeck.get() should throw NullPointerException if rank arguments are null")
    void getShouldThrowForNullRanks() {
        assertThrows(NullPointerException.class, () -> DeckTemplate.get(null, Rank.ACE),
                "Should throw NPE for null fromRank");
        assertThrows(NullPointerException.class, () -> DeckTemplate.get(Rank.TWO, null),
                "Should throw NPE for null toRank");
    }

    @Test
    @DisplayName("GameDeck.get() should throw IllegalArgumentException for duplicate jokers")
    void getShouldThrowForDuplicateJokers() {
        assertThrows(IllegalArgumentException.class,
                () -> DeckTemplate.get(Rank.TWO, Rank.ACE, JokerCard.JOKER_1, JokerCard.JOKER_1),
                "Should throw for duplicate joker cards in arguments");
    }

    @Test
    @DisplayName("GameDeck.get() should accept multiple different jokers without exception")
    void getShouldAcceptMultipleDifferentJokers() {
        assertDoesNotThrow(() -> {
            Set<PlayingCard> cards = DeckTemplate.get(Rank.TWO, Rank.ACE, JokerCard.JOKER_4, JokerCard.JOKER_3);
            assertTrue(cards.contains(JokerCard.JOKER_3), "Result should contain JOKER_3");
            assertTrue(cards.contains(JokerCard.JOKER_4), "Result should contain JOKER_4");
        }, "Should not throw for multiple unique jokers");
    }

    @SuppressWarnings("RedundantArrayCreation")
    @Test
    @DisplayName("GameDeck.get(from, to) should return the same set as GameDeck.get(from, to, new JokerCard[0])")
    void getWithAndWithoutEmptyJokersArrayShouldReturnSameDeck() {
        Set<PlayingCard> withoutJokers = DeckTemplate.get(Rank.TWO, Rank.ACE);
        Set<PlayingCard> withEmptyJokers = DeckTemplate.get(Rank.TWO, Rank.ACE, new JokerCard[0]);
        assertEquals(withoutJokers, withEmptyJokers,
                "Calling get(from, to) and get(from, to, new JokerCard[0]) should produce the same set of cards");
    }

    @Test
    @DisplayName("GameDeck.get(from, to) should return the same set as GameDeck.get(from, to, null)")
    void getWithAndWithoutNullJokersArrayShouldReturnSameDeck() {
        Set<PlayingCard> withoutJokers = DeckTemplate.get(Rank.TWO, Rank.ACE);
        Set<PlayingCard> withNullJokers = DeckTemplate.get(Rank.TWO, Rank.ACE, (JokerCard[]) null);
        assertEquals(withoutJokers, withNullJokers,
                "Calling get(from, to) and get(from, to, null) should produce the same set of cards");
    }

    @Test
    @DisplayName("GameDeck.get() should throw IllegalArgumentException if one of jokers is null")
    void getShouldThrowIfOneOfJokersIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> DeckTemplate.get(Rank.TWO, Rank.ACE, JokerCard.JOKER_1, null, JokerCard.JOKER_2),
                "Should throw IllegalArgumentException if one of joker arguments is null");
    }

    @Test
    @DisplayName("get() should return 4 cards (one per suit) when fromRank equals toRank")
    void getShouldReturnFourCardsForEqualRanks() {
        Set<PlayingCard> cards = DeckTemplate.get(Rank.TWO, Rank.TWO);
        assertEquals(4, cards.size(), "Should return 4 cards when fromRank equals toRank");
        assertTrue(cards.stream().allMatch(card -> card.getRank() == Rank.TWO),
                "All returned cards must have rank TWO");
    }

    @ParameterizedTest(name = "TINY deck containsCard({0}) should be {1}")
    @MethodSource("tinyDeckContainsCardProvider")
    @DisplayName("TINY deck: корректно сообщает о содержании карт и джокеров")
    void tinyDeckContainsCardShouldWork(PlayingCard card, boolean expected) {
        assertEquals(expected, DeckTemplate.TINY.containsCard(card),
                () -> "TINY deck " + (expected ? "должна содержать " : "не должна содержать ") + card);
    }

    static Stream<Arguments> tinyDeckContainsCardProvider() {
        return Stream.of(
                // Cards below the included range (eights)
                Arguments.of(StandardCard.EIGHT_SPADES, false),
                Arguments.of(StandardCard.EIGHT_CLUBS, false),
                Arguments.of(StandardCard.EIGHT_DIAMONDS, false),
                Arguments.of(StandardCard.EIGHT_HEARTS, false),
                // First included cards (nines)
                Arguments.of(StandardCard.NINE_SPADES, true),
                Arguments.of(StandardCard.NINE_CLUBS, true),
                Arguments.of(StandardCard.NINE_DIAMONDS, true),
                Arguments.of(StandardCard.NINE_HEARTS, true),
                // Last included cards (aces)
                Arguments.of(StandardCard.ACE_SPADES, true),
                Arguments.of(StandardCard.ACE_CLUBS, true),
                Arguments.of(StandardCard.ACE_DIAMONDS, true),
                Arguments.of(StandardCard.ACE_HEARTS, true),
                // Jokers are not included
                Arguments.of(JokerCard.JOKER_1, false),
                Arguments.of(JokerCard.JOKER_2, false),
                Arguments.of(JokerCard.JOKER_3, false),
                Arguments.of(JokerCard.JOKER_4, false)
        );
    }

    @ParameterizedTest(name = "SMALL deck containsCard({0}) should be {1}")
    @MethodSource("smallDeckContainsCardProvider")
    @DisplayName("SMALL deck: correctly reports card and joker containment")
    void smallDeckContainsCardShouldWork(PlayingCard card, boolean expected) {
        assertEquals(expected, DeckTemplate.SMALL.containsCard(card),
            () -> "SMALL deck " + (expected ? "should contain " : "should not contain ") + card);
    }

    static Stream<Arguments> smallDeckContainsCardProvider() {
        return Stream.of(
                // Cards below the range (sixes)
                Arguments.of(StandardCard.SIX_SPADES, false),
                Arguments.of(StandardCard.SIX_CLUBS, false),
                Arguments.of(StandardCard.SIX_DIAMONDS, false),
                Arguments.of(StandardCard.SIX_HEARTS, false),
                // First included cards (sevens)
                Arguments.of(StandardCard.SEVEN_SPADES, true),
                Arguments.of(StandardCard.SEVEN_CLUBS, true),
                Arguments.of(StandardCard.SEVEN_DIAMONDS, true),
                Arguments.of(StandardCard.SEVEN_HEARTS, true),
                // Last included cards (aces)
                Arguments.of(StandardCard.ACE_SPADES, true),
                Arguments.of(StandardCard.ACE_CLUBS, true),
                Arguments.of(StandardCard.ACE_DIAMONDS, true),
                Arguments.of(StandardCard.ACE_HEARTS, true),
                // Jokers are not included
                Arguments.of(JokerCard.JOKER_1, false),
                Arguments.of(JokerCard.JOKER_2, false),
                Arguments.of(JokerCard.JOKER_3, false),
                Arguments.of(JokerCard.JOKER_4, false)
        );
    }

    @ParameterizedTest(name = "SHORT deck containsCard({0}) should be {1}")
    @MethodSource("shortDeckContainsCardProvider")
    @DisplayName("SHORT deck: correctly reports card and joker containment")
    void shortDeckContainsCardShouldWork(PlayingCard card, boolean expected) {
        assertEquals(expected, DeckTemplate.SHORT.containsCard(card),
                () -> "SHORT deck " + (expected ? "should contain " : "should not contain ") + card);
    }

    static Stream<Arguments> shortDeckContainsCardProvider() {
        return Stream.of(
                // Cards below the range (fives)
                Arguments.of(StandardCard.FIVE_SPADES, false),
                Arguments.of(StandardCard.FIVE_CLUBS, false),
                Arguments.of(StandardCard.FIVE_DIAMONDS, false),
                Arguments.of(StandardCard.FIVE_HEARTS, false),
                // First included cards (sixes)
                Arguments.of(StandardCard.SIX_SPADES, true),
                Arguments.of(StandardCard.SIX_CLUBS, true),
                Arguments.of(StandardCard.SIX_DIAMONDS, true),
                Arguments.of(StandardCard.SIX_HEARTS, true),
                // Middle cards (tens)
                Arguments.of(StandardCard.TEN_SPADES, true),
                Arguments.of(StandardCard.TEN_CLUBS, true),
                Arguments.of(StandardCard.TEN_DIAMONDS, true),
                Arguments.of(StandardCard.TEN_HEARTS, true),
                // Last included cards (aces)
                Arguments.of(StandardCard.ACE_SPADES, true),
                Arguments.of(StandardCard.ACE_CLUBS, true),
                Arguments.of(StandardCard.ACE_DIAMONDS, true),
                Arguments.of(StandardCard.ACE_HEARTS, true),
                // Jokers are not included
                Arguments.of(JokerCard.JOKER_1, false),
                Arguments.of(JokerCard.JOKER_2, false),
                Arguments.of(JokerCard.JOKER_3, false),
                Arguments.of(JokerCard.JOKER_4, false)
        );
    }

    @ParameterizedTest(name = "FULL deck containsCard({0}) should be {1}")
    @MethodSource("fullDeckContainsCardProvider")
    @DisplayName("FULL deck: correctly reports card and joker containment")
    void fullDeckContainsCardShouldWork(PlayingCard card, boolean expected) {
        assertEquals(expected, DeckTemplate.FULL.containsCard(card),
                () -> "FULL deck " + (expected ? "should contain " : "should not contain ") + card);
    }

    static Stream<Arguments> fullDeckContainsCardProvider() {
        return Stream.of(
                // First included cards (twos)
                Arguments.of(StandardCard.TWO_SPADES, true),
                Arguments.of(StandardCard.TWO_CLUBS, true),
                Arguments.of(StandardCard.TWO_DIAMONDS, true),
                Arguments.of(StandardCard.TWO_HEARTS, true),
                // Middle cards (tens)
                Arguments.of(StandardCard.TEN_SPADES, true),
                Arguments.of(StandardCard.TEN_CLUBS, true),
                Arguments.of(StandardCard.TEN_DIAMONDS, true),
                Arguments.of(StandardCard.TEN_HEARTS, true),
                // Last included cards (aces)
                Arguments.of(StandardCard.ACE_SPADES, true),
                Arguments.of(StandardCard.ACE_CLUBS, true),
                Arguments.of(StandardCard.ACE_DIAMONDS, true),
                Arguments.of(StandardCard.ACE_HEARTS, true),
                // Jokers are not included
                Arguments.of(JokerCard.JOKER_1, false),
                Arguments.of(JokerCard.JOKER_2, false),
                Arguments.of(JokerCard.JOKER_3, false),
                Arguments.of(JokerCard.JOKER_4, false)
        );
    }

    @ParameterizedTest(name = "EXTENDED deck containsCard({0}) should be {1}")
    @MethodSource("extendedDeckContainsCardProvider")
    @DisplayName("EXTENDED deck: correctly reports card and joker containment")
    void extendedDeckContainsCardShouldWork(PlayingCard card, boolean expected) {
        assertEquals(expected, DeckTemplate.EXTENDED.containsCard(card),
                () -> "EXTENDED deck " + (expected ? "should contain " : "should not contain ") + card);
    }

    static Stream<Arguments> extendedDeckContainsCardProvider() {
        return Stream.of(
                // First included cards (twos)
                Arguments.of(StandardCard.TWO_SPADES, true),
                Arguments.of(StandardCard.TWO_CLUBS, true),
                Arguments.of(StandardCard.TWO_DIAMONDS, true),
                Arguments.of(StandardCard.TWO_HEARTS, true),
                // Middle cards (tens)
                Arguments.of(StandardCard.TEN_SPADES, true),
                Arguments.of(StandardCard.TEN_CLUBS, true),
                Arguments.of(StandardCard.TEN_DIAMONDS, true),
                Arguments.of(StandardCard.TEN_HEARTS, true),
                // Last included cards (aces)
                Arguments.of(StandardCard.ACE_SPADES, true),
                Arguments.of(StandardCard.ACE_CLUBS, true),
                Arguments.of(StandardCard.ACE_DIAMONDS, true),
                Arguments.of(StandardCard.ACE_HEARTS, true),
                // First two jokers are included
                Arguments.of(JokerCard.JOKER_1, true),
                Arguments.of(JokerCard.JOKER_2, true),
                // Other jokers are not included
                Arguments.of(JokerCard.JOKER_3, false),
                Arguments.of(JokerCard.JOKER_4, false)
        );
    }

    @ParameterizedTest(name = "DOUBLE_EXTENDED deck containsCard({0}) should be {1}")
    @MethodSource("doubleExtendedDeckContainsCardProvider")
    @DisplayName("DOUBLE_EXTENDED deck: correctly reports card and joker containment")
    void doubleExtendedDeckContainsCardShouldWork(PlayingCard card, boolean expected) {
        assertEquals(expected, DeckTemplate.DOUBLE_EXTENDED.containsCard(card),
                () -> "DOUBLE_EXTENDED deck " + (expected ? "should contain " : "should not contain ") + card);
    }

    static Stream<Arguments> doubleExtendedDeckContainsCardProvider() {
        return Stream.of(
                // First included cards (twos)
                Arguments.of(StandardCard.TWO_SPADES, true),
                Arguments.of(StandardCard.TWO_CLUBS, true),
                Arguments.of(StandardCard.TWO_DIAMONDS, true),
                Arguments.of(StandardCard.TWO_HEARTS, true),
                // Middle cards (tens)
                Arguments.of(StandardCard.TEN_SPADES, true),
                Arguments.of(StandardCard.TEN_CLUBS, true),
                Arguments.of(StandardCard.TEN_DIAMONDS, true),
                Arguments.of(StandardCard.TEN_HEARTS, true),
                // Last included cards (aces)
                Arguments.of(StandardCard.ACE_SPADES, true),
                Arguments.of(StandardCard.ACE_CLUBS, true),
                Arguments.of(StandardCard.ACE_DIAMONDS, true),
                Arguments.of(StandardCard.ACE_HEARTS, true),
                // All jokers are included
                Arguments.of(JokerCard.JOKER_1, true),
                Arguments.of(JokerCard.JOKER_2, true),
                Arguments.of(JokerCard.JOKER_3, true),
                Arguments.of(JokerCard.JOKER_4, true)
        );
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    @DisplayName("containsCard(null) should throw NullPointerException")
    void containsCardWithNullThrows() {
        assertThrows(NullPointerException.class, () -> DeckTemplate.DOUBLE_EXTENDED.containsCard(null),
                "containsCard(null) should throw NullPointerException");
    }
}