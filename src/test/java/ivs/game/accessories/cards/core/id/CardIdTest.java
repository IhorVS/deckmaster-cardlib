package ivs.game.accessories.cards.core.id;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardIdTest {

    @ParameterizedTest(name = "getCardId should return correct ID for suit {0} and rank {1}")
    @DisplayName("getCardId should return correct card ID for valid inputs")
    @MethodSource("validCardParameters")
    void getCardIdShouldReturnCorrectIdForValidInputs(int suitId, int rankId, int expectedCardId) {
        assertEquals(expectedCardId, CardId.getCardId(rankId, suitId),
                String.format("Card ID for suit %d and rank %d should be %d", suitId, rankId, expectedCardId));
    }

    @ParameterizedTest(name = "getCardId should throw exception for invalid suit {0}")
    @DisplayName("getCardId should throw exception for invalid suit ID")
    @MethodSource("invalidSuitIds")
    void getCardIdShouldThrowExceptionForInvalidSuitId(int invalidSuitId) {
        assertThrows(IllegalArgumentException.class,
                () -> CardId.getCardId(RankId.ACE, invalidSuitId),
                String.format("Should throw exception for invalid suit ID: %d", invalidSuitId));
    }

    @ParameterizedTest(name = "getCardId should throw exception for invalid rank {0}")
    @DisplayName("getCardId should throw exception for invalid rank ID")
    @MethodSource("invalidRankIds")
    void getCardIdShouldThrowExceptionForInvalidRankId(int invalidRankId) {
        assertThrows(IllegalArgumentException.class,
                () -> CardId.getCardId(invalidRankId, SuitId.SPADES),
                String.format("Should throw exception for invalid rank ID: %d", invalidRankId));
    }

    @Test
    @DisplayName("getCardId should maintain consistency with getRankId")
    void getCardIdShouldMaintainConsistencyWithGetRankId() {
        int suitId = SuitId.HEARTS;
        int rankId = RankId.KING;
        int cardId = CardId.getCardId(rankId, suitId);
        assertEquals(rankId, CardId.getRankId(cardId),
                "getRankId should return the original rank ID");
    }

    @ParameterizedTest(name = "getSuitId should return {1} for card ID {0}")
    @DisplayName("getSuitId should return correct suit ID for valid card IDs")
    @MethodSource("validCardIdsWithExpectedSuits")
    void getSuitIdShouldReturnCorrectSuitForValidCardIds(int cardId, int expectedSuitId) {
        assertEquals(expectedSuitId, CardId.getSuitId(cardId),
                String.format("Card ID %d should correspond to suit ID %d", cardId, expectedSuitId));
    }

    @ParameterizedTest(name = "getSuitId should throw IllegalArgumentException for invalid card ID {0}")
    @DisplayName("getSuitId should throw exception for invalid card ID")
    @MethodSource("invalidCardIds")
    void getSuitIdShouldThrowExceptionForInvalidCardId(int invalidCardId) {
        assertThrows(IllegalArgumentException.class,
                () -> CardId.getSuitId(invalidCardId),
                String.format("Should throw exception for invalid card ID: %d", invalidCardId));
    }

    @Test
    @DisplayName("getSuitId should maintain consistency with getCardId")
    void getSuitIdShouldMaintainConsistencyWithGetCardId() {
        int originalSuitId = SuitId.DIAMONDS;
        int rankId = RankId.SEVEN;
        int cardId = CardId.getCardId(rankId, originalSuitId);
        assertEquals(originalSuitId, CardId.getSuitId(cardId),
                "getSuitId should return the original suit ID");
    }

    @ParameterizedTest(name = "getColorId should return {1} for card ID {0}")
    @DisplayName("getColorId should return correct color for valid card IDs")
    @MethodSource("validCardIdsWithExpectedColors")
    void getColorIdShouldReturnCorrectColorForValidCardIds(int cardId, int expectedColorId) {
        assertEquals(expectedColorId, CardId.getColorId(cardId),
                String.format("Card ID %d should correspond to color ID %d", cardId, expectedColorId));
    }

    @ParameterizedTest(name = "getColorId should throw IllegalArgumentException for invalid card ID {0}")
    @DisplayName("getColorId should throw exception for invalid card ID")
    @MethodSource("invalidCardIds")
        // Переиспользуем существующий метод
    void getColorIdShouldThrowExceptionForInvalidCardId(int invalidCardId) {
        assertThrows(IllegalArgumentException.class,
                () -> CardId.getColorId(invalidCardId),
                String.format("Should throw exception for invalid card ID: %d", invalidCardId));
    }

    @Test
    @DisplayName("getColorId should maintain consistency through getSuitId")
    void getColorIdShouldMaintainConsistencyThroughSuitId() {
        int suitId = SuitId.DIAMONDS;
        int rankId = RankId.SEVEN;
        int cardId = CardId.getCardId(rankId, suitId);
        assertEquals(SuitId.getColorId(suitId), CardId.getColorId(cardId),
                "getColorId should return same color as SuitId.getColorId");
    }

    @ParameterizedTest(name = "getRankId should return {1} for card ID {0}")
    @DisplayName("getRankId should return correct rank ID for valid card IDs")
    @MethodSource("validCardIdsWithExpectedRanks")
    void getRankIdShouldReturnCorrectRankForValidCardIds(int cardId, int expectedRankId) {
        assertEquals(expectedRankId, CardId.getRankId(cardId),
                String.format("Card ID %d should correspond to rank ID %d", cardId, expectedRankId));
    }

    @ParameterizedTest(name = "getRankId should throw IllegalArgumentException for invalid card ID {0}")
    @DisplayName("getRankId should throw exception for invalid card ID")
    @MethodSource("invalidCardIds")
        // Переиспользуем существующий метод из CardIdTest
    void getRankIdShouldThrowExceptionForInvalidCardId(int invalidCardId) {
        assertThrows(IllegalArgumentException.class,
                () -> CardId.getRankId(invalidCardId),
                String.format("Should throw exception for invalid card ID: %d", invalidCardId));
    }

    @Test
    @DisplayName("getRankId should maintain consistency with getCardId")
    void getRankIdShouldMaintainConsistencyWithGetCardId() {
        int suitId = SuitId.CLUBS;
        int originalRankId = RankId.QUEEN;
        int cardId = CardId.getCardId(originalRankId, suitId);
        assertEquals(originalRankId, CardId.getRankId(cardId),
                "getRankId should return the original rank ID");
    }

    @Test
    @DisplayName("getRankId should return same rank for all suits")
    void getRankIdShouldReturnSameRankForAllSuits() {
        int targetRank = RankId.SEVEN;
        for (int suitId = SuitId.MIN_SUIT; suitId <= SuitId.MAX_SUIT; suitId++) {
            int cardId = CardId.getCardId(targetRank, suitId);
            assertEquals(targetRank, CardId.getRankId(cardId),
                    String.format("Same rank ID should be returned for all suits, card ID: %d", cardId));
        }
    }

    @Test
    @DisplayName("getRankId should return different values for consecutive cards in same suit")
    void getRankIdShouldReturnDifferentValuesForConsecutiveCardsInSuit() {
        int suitId = SuitId.SPADES;
        int firstRankId = RankId.ACE;
        int secondRankId = RankId.TWO;

        int firstCardId = CardId.getCardId(firstRankId, suitId);
        int secondCardId = CardId.getCardId(secondRankId, suitId);

        assertNotEquals(CardId.getRankId(firstCardId), CardId.getRankId(secondCardId),
                "Different rank IDs should be returned for consecutive cards in same suit");
    }

    @Test
    @DisplayName("getAllCardIds should return array with all cards in ascending order")
    void getAllCardIdsShouldReturnAllCardsInAscendingOrder() {
        int[] cardIds = CardId.getAllCardIds();

        assertNotNull(cardIds, "Array should not be null");
        assertEquals(CardId.CARD_COUNT, cardIds.length,
                String.format("Array should contain exactly %d elements", CardId.CARD_COUNT));
        assertEquals(CardIds.TWO_SPADES, cardIds[0], "First element should be ACE of SPADES");
        assertEquals(CardIds.ACE_SPADES, cardIds[12], "Last element of first suit should be KING of SPADES");
        assertEquals(CardIds.TWO_CLUBS, cardIds[13], "First element of second suit should be ACE of CLUBS");
        assertEquals(CardIds.ACE_CLUBS, cardIds[25], "Last element of second suit should be KING of CLUBS");
        assertEquals(CardIds.TWO_DIAMONDS, cardIds[26], "First element of third suit should be ACE of DIAMONDS");
        assertEquals(CardIds.ACE_DIAMONDS, cardIds[38], "Last element of third suit should be KING of DIAMONDS");
        assertEquals(CardIds.TWO_HEARTS, cardIds[39], "First element of fourth suit should be ACE of HEARTS");
        assertEquals(CardIds.ACE_HEARTS, cardIds[51], "Last element should be KING of HEARTS");
    }

    @Test
    @DisplayName("getAllCardIds should return new array instance on each call")
    void getAllCardIdsShouldReturnNewInstance() {
        int[] firstCall = CardId.getAllCardIds();
        int[] secondCall = CardId.getAllCardIds();

        assertNotSame(firstCall, secondCall, "Method should return new array instance each time");
        assertArrayEquals(firstCall, secondCall, "Arrays should have same content");
    }

    @Test
    @DisplayName("getAllCardIds should return cards in sequential order")
    void getAllCardIdsShouldReturnCardsInSequentialOrder() {
        int[] cardIds = CardId.getAllCardIds();

        for (int i = 1; i < cardIds.length; i++) {
            assertEquals(cardIds[i - 1] + 1, cardIds[i],
                    String.format("Cards should be sequential, expected %d after %d",
                            cardIds[i - 1] + 1, cardIds[i - 1]));
        }
    }

    @Test
    @DisplayName("getAllCardIdsInRankRange should return new array instance on each call")
    void getAllCardIdsInRankRangeShouldReturnNewInstance() {
        int[] firstCall = CardId.getAllCardIdsInRankRange(RankId.TWO, RankId.ACE);
        int[] secondCall = CardId.getAllCardIdsInRankRange(RankId.TWO, RankId.ACE);

        assertNotSame(firstCall, secondCall, "Method should return new array instance each time");
        assertArrayEquals(firstCall, secondCall, "Arrays should have same content");
    }

    @Test
    @DisplayName("getAllCardIdsInRankRange should throw exception when fromRank > toRank")
    void getAllCardIdsInRankRangeShouldThrowExceptionWhenFromRankGreaterThanToRank() {
        assertThrows(IllegalArgumentException.class,
                () -> CardId.getAllCardIdsInRankRange(RankId.ACE, RankId.TWO),
                "Should throw exception when fromRank is greater than toRank");
    }

    @ParameterizedTest(name = "getAllCardIdsInRankRange should throw exception for invalid rank {0}")
    @DisplayName("getAllCardIdsInRankRange should throw exception for invalid ranks")
    @MethodSource("invalidRankIds")
    void getAllCardIdsInRankRangeShouldThrowExceptionForInvalidRanks(int invalidRank) {
        assertThrows(IllegalArgumentException.class,
                () -> CardId.getAllCardIdsInRankRange(invalidRank, RankId.KING),
                "Should throw exception for invalid fromRank");

        assertThrows(IllegalArgumentException.class,
                () -> CardId.getAllCardIdsInRankRange(RankId.ACE, invalidRank),
                "Should throw exception for invalid toRank");
    }

    @ParameterizedTest(name = "getAllCardIdsInRankRange should return correct cards for range from {0} to {1}")
    @DisplayName("getAllCardIdsInRankRange should return correct cards for different ranges")
    @MethodSource("rankRangeParameters")
    void getAllCardIdsInRankRangeShouldReturnCorrectCards(int fromRank, int toRank, int expectedLength, String testCase) {
        int[] cardIds = CardId.getAllCardIdsInRankRange(fromRank, toRank);

        // Basic validation
        assertNotNull(cardIds, "Array should not be null");
        assertEquals(expectedLength, cardIds.length,
                String.format("Array should contain exactly %d elements for %s", expectedLength, testCase));

        // Check ascending order
        for (int i = 1; i < cardIds.length; i++) {
            assertTrue(cardIds[i] > cardIds[i - 1],
                    String.format("Cards should be in ascending order, but %d comes after %d",
                            cardIds[i], cardIds[i - 1]));
        }

        // Verify all cards have correct ranks
        for (int cardId : cardIds) {
            int rankId = CardId.getRankId(cardId);
            assertTrue(rankId >= fromRank && rankId <= toRank,
                    String.format("Card %d has rank %d which is outside range [%d, %d]",
                            cardId, rankId, fromRank, toRank));
        }

        // Special check for full range
        if (fromRank == RankId.ACE && toRank == RankId.KING) {
            assertArrayEquals(CardId.getAllCardIds(), cardIds,
                    "Full rank range should return same cards as getAllCardIds");
        }
    }

    @ParameterizedTest(name = "getCardIdsInSuitAndRankRange should return correct cards for suit {0} and ranks from {1} to {2}")
    @DisplayName("getCardIdsInSuitAndRankRange should return correct cards for different ranges")
    @MethodSource("suitAndRankRangeParameters")
    void getCardIdsInSuitAndRankRangeShouldReturnCorrectCards(
            int suitId, int fromRank, int toRank, int expectedLength, String testCase) {
        int[] cardIds = CardId.getCardIdsInSuitAndRankRange(suitId, fromRank, toRank);

        // Basic validation
        assertNotNull(cardIds, "Array should not be null");
        assertEquals(expectedLength, cardIds.length,
                String.format("Array should contain exactly %d elements for %s", expectedLength, testCase));

        // Check ascending order
        for (int i = 1; i < cardIds.length; i++) {
            assertTrue(cardIds[i] > cardIds[i - 1],
                    String.format("Cards should be in ascending order, but %d comes after %d",
                            cardIds[i], cardIds[i - 1]));
        }

        // Verify all cards have correct suit and ranks
        for (int cardId : cardIds) {
            assertEquals(suitId, CardId.getSuitId(cardId),
                    String.format("Card %d should belong to suit %d", cardId, suitId));

            int rankId = CardId.getRankId(cardId);
            assertTrue(rankId >= fromRank && rankId <= toRank,
                    String.format("Card %d has rank %d which is outside range [%d, %d]",
                            cardId, rankId, fromRank, toRank));
        }
    }

    @ParameterizedTest(name = "getCardIdsInSuitAndRankRange should throw exception when fromRank={1} > toRank={2}")
    @DisplayName("getCardIdsInSuitAndRankRange should throw exception for invalid rank range")
    @MethodSource("invalidRankRangeParameters")
    void getCardIdsInSuitAndRankRangeShouldThrowExceptionForInvalidRankRange(
            int suitId, int fromRank, int toRank) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> CardId.getCardIdsInSuitAndRankRange(suitId, fromRank, toRank),
                "Should throw IllegalArgumentException when fromRank is greater than toRank");

        assertEquals(String.format("Invalid rank range: from %d to %d", fromRank, toRank),
                exception.getMessage(),
                "Exception message should match for invalid range");
    }

    @Test
    @DisplayName("stream() should return all card IDs in ascending order, matching getAllCardIds()")
    void streamShouldReturnAllCardIdsInAscendingOrder() {
        int[] fromStream = CardId.stream().toArray();
        int[] expected = CardId.getAllCardIds();
        assertArrayEquals(expected, fromStream, "stream() should return the same IDs as getAllCardIds()");
    }

    @Test
    @DisplayName("stream() should return only unique card IDs")
    void streamShouldReturnUniqueCardIds() {
        int[] ids = CardId.stream().toArray();
        Set<Integer> unique = new HashSet<>();
        for (int id : ids) {
            assertTrue(unique.add(id), "Each card ID should be unique: duplicate found " + id);
        }
    }

    @Test
    @DisplayName("stream() should return correct number of card IDs")
    void streamShouldReturnCorrectNumberOfCardIds() {
        long count = CardId.stream().count();
        assertEquals(CardId.CARD_COUNT, count, "stream() should return CARD_COUNT elements");
    }

    @Test
    @DisplayName("stream() should start with MIN_CARD and end with MAX_CARD")
    void streamShouldStartWithMinAndEndWithMaxCardId() {
        int[] ids = CardId.stream().toArray();
        assertTrue(ids.length > 0, "stream() should not be empty");
        assertEquals(CardId.MIN_CARD, ids[0], "First stream element should be MIN_CARD");
        assertEquals(CardId.MAX_CARD, ids[ids.length - 1], "Last stream element should be MAX_CARD");
    }

    private static Stream<Arguments> invalidRankRangeParameters() {
        return Stream.of(
                // format: suitId, fromRank, toRank
                Arguments.of(SuitId.SPADES, RankId.ACE, RankId.TWO),     // Ace > Twp
                Arguments.of(SuitId.HEARTS, RankId.QUEEN, RankId.JACK),   // Queen > Jack
                Arguments.of(SuitId.DIAMONDS, RankId.TEN, RankId.NINE),   // Ten > Nine
                Arguments.of(SuitId.CLUBS, RankId.FIVE, RankId.TWO)       // Five > Two
        );
    }

    private static Stream<Arguments> rankRangeParameters() {
        return Stream.of(
                // format: fromRank, toRank, expected array length, test case description
                Arguments.of(RankId.SEVEN, RankId.SEVEN, 4, "single rank"), // 4 suits for one rank
                Arguments.of(RankId.TWO, RankId.TEN, 36, "small range"), // 4 suits * 9 ranks
                Arguments.of(RankId.TWO, RankId.ACE, 52, "full range"), // all cards
                Arguments.of(RankId.JACK, RankId.KING, 12, "face cards") // 4 suits * 3 ranks
        );
    }

    private static Stream<Arguments> suitAndRankRangeParameters() {
        return Stream.of(
                // format: suitId, fromRank, toRank, expected array length, test case description
                Arguments.of(SuitId.SPADES, RankId.SEVEN, RankId.SEVEN, 1, "single rank in suit"), // 1 card
                Arguments.of(SuitId.HEARTS, RankId.TWO, RankId.TEN, 9, "numeric ranks"), // 9 ranks in one suit
                Arguments.of(SuitId.DIAMONDS, RankId.TWO, RankId.ACE, 13, "full rank range"), // all ranks in one suit
                Arguments.of(SuitId.CLUBS, RankId.JACK, RankId.KING, 3, "face cards") // 3 face cards in one suit
        );
    }

    private static Stream<Arguments> validCardParameters() {
        return Stream.of(
                // Test boundary cases
                Arguments.of(SuitId.SPADES, RankId.TWO, 0),  // First card
                Arguments.of(SuitId.HEARTS, RankId.ACE, 51), // Last card
                // Test middle cases
                Arguments.of(SuitId.CLUBS, RankId.SEVEN, 18),
                Arguments.of(SuitId.DIAMONDS, RankId.QUEEN, 36)
        );
    }

    private static Stream<Arguments> invalidSuitIds() {
        return Stream.of(
                Arguments.of(Integer.MIN_VALUE),
                Arguments.of(SuitId.MIN_SUIT - 1),
                Arguments.of(SuitId.MAX_SUIT + 1),
                Arguments.of(Integer.MAX_VALUE)
        );
    }

    private static Stream<Arguments> invalidRankIds() {
        return Stream.of(
                Arguments.of(Integer.MIN_VALUE),
                Arguments.of(RankId.MIN_RANK - 1),
                Arguments.of(RankId.MAX_RANK + 1),
                Arguments.of(Integer.MAX_VALUE)
        );
    }

    private static Stream<Arguments> invalidCardIds() {
        return Stream.of(
                Arguments.of(Integer.MIN_VALUE),
                Arguments.of(CardId.MIN_CARD - 1),
                Arguments.of(CardId.MAX_CARD + 1),
                Arguments.of(Integer.MAX_VALUE)
        );
    }

    private static final class CardIds {
        // Spades
        static final int TWO_SPADES = 0;
        static final int ACE_SPADES = 12;
        // Clubs
        static final int TWO_CLUBS = 13;
        static final int ACE_CLUBS = 25;
        // Diamonds
        static final int TWO_DIAMONDS = 26;
        static final int ACE_DIAMONDS = 38;
        // Hearts
        static final int TWO_HEARTS = 39;
        static final int ACE_HEARTS = 51;
    }

    private static Stream<Arguments> validCardIdsWithExpectedSuits() {
        return Stream.of(
                Arguments.of(CardIds.TWO_SPADES, SuitId.SPADES),    // First card of first suit
                Arguments.of(CardIds.ACE_SPADES, SuitId.SPADES),   // Last card of first suit
                Arguments.of(CardIds.TWO_CLUBS, SuitId.CLUBS),      // First card of second suit
                Arguments.of(CardIds.ACE_CLUBS, SuitId.CLUBS),     // Last card of second suit
                Arguments.of(CardIds.TWO_DIAMONDS, SuitId.DIAMONDS), // First card of third suit
                Arguments.of(CardIds.ACE_DIAMONDS, SuitId.DIAMONDS),// Last card of third suit
                Arguments.of(CardIds.TWO_HEARTS, SuitId.HEARTS),    // First card of fourth suit
                Arguments.of(CardIds.ACE_HEARTS, SuitId.HEARTS)    // Last card of deck
        );
    }

    private static Stream<Arguments> validCardIdsWithExpectedColors() {
        return Stream.of(
                Arguments.of(CardIds.TWO_SPADES, ColorId.BLACK),     // First card of spades
                Arguments.of(CardIds.ACE_SPADES, ColorId.BLACK),    // Last card of spades
                Arguments.of(CardIds.TWO_CLUBS, ColorId.BLACK),      // First card of clubs
                Arguments.of(CardIds.ACE_CLUBS, ColorId.BLACK),     // Last card of clubs
                Arguments.of(CardIds.TWO_DIAMONDS, ColorId.RED),     // First card of diamonds
                Arguments.of(CardIds.ACE_DIAMONDS, ColorId.RED),    // Last card of diamonds
                Arguments.of(CardIds.TWO_HEARTS, ColorId.RED),       // First card of hearts
                Arguments.of(CardIds.ACE_HEARTS, ColorId.RED)       // Last card of hearts
        );
    }

    private static Stream<Arguments> validCardIdsWithExpectedRanks() {
        return Stream.of(
                Arguments.of(CardIds.TWO_SPADES, RankId.TWO),       // First card of spades
                Arguments.of(CardIds.ACE_SPADES, RankId.ACE),     // Last card of spades
                Arguments.of(CardIds.TWO_CLUBS, RankId.TWO),        // First card of clubs
                Arguments.of(CardIds.ACE_CLUBS, RankId.ACE),      // Last card of clubs
                Arguments.of(CardIds.TWO_DIAMONDS, RankId.TWO),     // First card of diamonds
                Arguments.of(CardIds.ACE_DIAMONDS, RankId.ACE),   // Last card of diamonds
                Arguments.of(CardIds.TWO_HEARTS, RankId.TWO),       // First card of hearts
                Arguments.of(CardIds.ACE_HEARTS, RankId.ACE)      // Last card of hearts
        );
    }
}