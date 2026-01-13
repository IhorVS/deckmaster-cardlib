package ivs.game.accessories.cards.core.type;

import ivs.game.accessories.cards.core.id.CardId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("DataFlowIssue")
class StandardCardTest {

    @ParameterizedTest(name = "getById should throw IllegalArgumentException for card ID {0}")
    @DisplayName("getById should throw IllegalArgumentException for invalid card IDs")
    @ValueSource(ints = {
            Integer.MIN_VALUE, CardId.MIN_CARD - 1, CardId.MAX_CARD + 1, 100, Integer.MAX_VALUE
    })
    void getByIdShouldThrowForInvalidValues(int invalidCardId) {
        assertThrows(IllegalArgumentException.class,
                () -> StandardCard.getById(invalidCardId),
                "Should throw IllegalArgumentException for invalid card ID: " + invalidCardId);
    }

    @ParameterizedTest(name = "getById({0}) should return {1}")
    @DisplayName("getById should return correct StandardCard constant")
    @MethodSource("getIdCardPairs")
    void getByIdShouldReturnCorrectCard(int cardId, StandardCard expectedCard) {
        assertSame(expectedCard, StandardCard.getById(cardId),
                String.format("getById(%d) should return %s", cardId, expectedCard));
    }

    private static Stream<Arguments> getIdCardPairs() {
        return Stream.of(
                // Spades
                Arguments.of(0, StandardCard.TWO_SPADES),
                Arguments.of(12, StandardCard.ACE_SPADES),

                // Clubs
                Arguments.of(13, StandardCard.TWO_CLUBS),
                Arguments.of(25, StandardCard.ACE_CLUBS),

                // Diamonds
                Arguments.of(26, StandardCard.TWO_DIAMONDS),
                Arguments.of(38, StandardCard.ACE_DIAMONDS),

                // Hearts
                Arguments.of(39, StandardCard.TWO_HEARTS),
                Arguments.of(51, StandardCard.ACE_HEARTS)
        );
    }

    @ParameterizedTest(name = "getByRankAndSuit({0}, {1}) should return {2}")
    @DisplayName("getByRankAndSuit should return correct StandardCard constant")
    @MethodSource("getRankSuitCardTriples")
    void getByRankAndSuitShouldReturnCorrectCard(Rank rank, Suit suit, StandardCard expectedCard) {
        assertSame(expectedCard, StandardCard.getByRankAndSuit(rank, suit),
                String.format("getByRankAndSuit(%s, %s) should return %s", rank, suit, expectedCard));
    }

    private static Stream<Arguments> getRankSuitCardTriples() {
        return Stream.of(
                // Spades
                Arguments.of(Rank.TWO, Suit.SPADES, StandardCard.TWO_SPADES),
                Arguments.of(Rank.ACE, Suit.SPADES, StandardCard.ACE_SPADES),

                // Clubs
                Arguments.of(Rank.TWO, Suit.CLUBS, StandardCard.TWO_CLUBS),
                Arguments.of(Rank.ACE, Suit.CLUBS, StandardCard.ACE_CLUBS),

                // Diamonds
                Arguments.of(Rank.TWO, Suit.DIAMONDS, StandardCard.TWO_DIAMONDS),
                Arguments.of(Rank.ACE, Suit.DIAMONDS, StandardCard.ACE_DIAMONDS),

                // Hearts
                Arguments.of(Rank.TWO, Suit.HEARTS, StandardCard.TWO_HEARTS),
                Arguments.of(Rank.ACE, Suit.HEARTS, StandardCard.ACE_HEARTS)
        );
    }

    @ParameterizedTest(name = "getBySymbol({0}) should return {1}")
    @DisplayName("getBySymbol should return correct StandardCard constant")
    @MethodSource("getSymbolCardPairs")
    void getBySymbolShouldReturnCorrectCard(String symbol, StandardCard expectedCard) {
        assertSame(expectedCard, StandardCard.getBySymbol(symbol),
                String.format("getBySymbol(%s) should return %s", symbol, expectedCard));
    }

    private static Stream<Arguments> getSymbolCardPairs() {
        return Stream.of(
                // Spades
                Arguments.of("2S", StandardCard.TWO_SPADES),
                Arguments.of("AS", StandardCard.ACE_SPADES),

                // Clubs
                Arguments.of("2C", StandardCard.TWO_CLUBS),
                Arguments.of("AC", StandardCard.ACE_CLUBS),

                // Diamonds
                Arguments.of("2D", StandardCard.TWO_DIAMONDS),
                Arguments.of("AD", StandardCard.ACE_DIAMONDS),

                // Hearts
                Arguments.of("2H", StandardCard.TWO_HEARTS),
                Arguments.of("AH", StandardCard.ACE_HEARTS)
        );
    }

    @Test
    @DisplayName("getBySymbol should throw NullPointerException when symbol is null")
    void getBySymbolShouldThrowNullPointerExceptionWhenSymbolIsNull() {
        assertThrows(NullPointerException.class,
                () -> StandardCard.getBySymbol(null),
                "Should throw NullPointerException when symbol is null");
    }

    @Test
    @DisplayName("getByRankAndSuit should throw NullPointerException when parameters are null")
    void getByRankAndSuitShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> StandardCard.getByRankAndSuit(null, Suit.SPADES),
                "Should throw NullPointerException when rank is null");

        assertThrows(NullPointerException.class,
                () -> StandardCard.getByRankAndSuit(Rank.ACE, null),
                "Should throw NullPointerException when suit is null");
    }

    @Test
    @DisplayName("stream should return all 52 standard cards")
    void streamShouldReturnAllCards() {
        Stream<StandardCard> cardStream = StandardCard.stream();

        List<StandardCard> cards = cardStream.collect(Collectors.toList());

        assertAll(
                () -> assertEquals(52, cards.size(), "Should contain exactly 52 cards"),
                () -> assertTrue(cards.contains(StandardCard.TWO_SPADES), "Should contain TWO_SPADES"),
                () -> assertTrue(cards.contains(StandardCard.KING_HEARTS), "Should contain KING_HEARTS"),
                () -> assertEquals(Arrays.asList(StandardCard.values()), cards,
                        "Stream should contain all cards in the same order as values()")
        );
    }

    @Test
    @DisplayName("isJoker should return false for all standard cards")
    void isJokerShouldReturnFalse() {
        for (StandardCard card : StandardCard.values()) {
            assertFalse(card.isJoker(),
                    String.format("Card %s should not be a joker", card));
        }
    }

    @Test
    @DisplayName("Getters should return correct values for boundary cards of each suit")
    void gettersShouldReturnCorrectValues() {
        assertAll(
                // SPADES ♠
                () -> assertAll(
                        // First - Two of Spades
                        () -> assertEquals(0, StandardCard.TWO_SPADES.getId(), "Wrong id for TWO_SPADES"),
                        () -> assertEquals("2S", StandardCard.TWO_SPADES.getSymbol(), "Wrong symbol for TWO_SPADES"),
                        () -> assertEquals(Rank.TWO, StandardCard.TWO_SPADES.getRank(), "Wrong rank for TWO_SPADES"),
                        () -> assertEquals(Suit.SPADES, StandardCard.TWO_SPADES.getSuit(), "Wrong suit for TWO_SPADES"),
                        () -> assertEquals(Color.BLACK, StandardCard.TWO_SPADES.getColor(), "Wrong color for TWO_SPADES"),

                        // Last - Ace of Spades
                        () -> assertEquals(12, StandardCard.ACE_SPADES.getId(), "Wrong id for ACE_SPADES"),
                        () -> assertEquals("AS", StandardCard.ACE_SPADES.getSymbol(), "Wrong symbol for ACE_SPADES"),
                        () -> assertEquals(Rank.ACE, StandardCard.ACE_SPADES.getRank(), "Wrong rank for ACE_SPADES"),
                        () -> assertEquals(Suit.SPADES, StandardCard.ACE_SPADES.getSuit(), "Wrong suit for ACE_SPADES"),
                        () -> assertEquals(Color.BLACK, StandardCard.ACE_SPADES.getColor(), "Wrong color for ACE_SPADES")
                ),

                // CLUBS ♣
                () -> assertAll(
                        // First - Two of Clubs
                        () -> assertEquals(13, StandardCard.TWO_CLUBS.getId(), "Wrong id for TWO_CLUBS"),
                        () -> assertEquals("2C", StandardCard.TWO_CLUBS.getSymbol(), "Wrong symbol for TWO_CLUBS"),
                        () -> assertEquals(Rank.TWO, StandardCard.TWO_CLUBS.getRank(), "Wrong rank for TWO_CLUBS"),
                        () -> assertEquals(Suit.CLUBS, StandardCard.TWO_CLUBS.getSuit(), "Wrong suit for TWO_CLUBS"),
                        () -> assertEquals(Color.BLACK, StandardCard.TWO_CLUBS.getColor(), "Wrong color for TWO_CLUBS"),

                        // Last - Ace of Clubs
                        () -> assertEquals(25, StandardCard.ACE_CLUBS.getId(), "Wrong id for ACE_CLUBS"),
                        () -> assertEquals("AC", StandardCard.ACE_CLUBS.getSymbol(), "Wrong symbol for ACE_CLUBS"),
                        () -> assertEquals(Rank.ACE, StandardCard.ACE_CLUBS.getRank(), "Wrong rank for ACE_CLUBS"),
                        () -> assertEquals(Suit.CLUBS, StandardCard.ACE_CLUBS.getSuit(), "Wrong suit for ACE_CLUBS"),
                        () -> assertEquals(Color.BLACK, StandardCard.ACE_CLUBS.getColor(), "Wrong color for ACE_CLUBS")
                ),

                // DIAMONDS ♦
                () -> assertAll(
                        // First - Two of Diamonds
                        () -> assertEquals(26, StandardCard.TWO_DIAMONDS.getId(), "Wrong id for TWO_DIAMONDS"),
                        () -> assertEquals("2D", StandardCard.TWO_DIAMONDS.getSymbol(), "Wrong symbol for TWO_DIAMONDS"),
                        () -> assertEquals(Rank.TWO, StandardCard.TWO_DIAMONDS.getRank(), "Wrong rank for TWO_DIAMONDS"),
                        () -> assertEquals(Suit.DIAMONDS, StandardCard.TWO_DIAMONDS.getSuit(), "Wrong suit for TWO_DIAMONDS"),
                        () -> assertEquals(Color.RED, StandardCard.TWO_DIAMONDS.getColor(), "Wrong color for TWO_DIAMONDS"),

                        // Last - Ace of Diamonds
                        () -> assertEquals(38, StandardCard.ACE_DIAMONDS.getId(), "Wrong id for ACE_DIAMONDS"),
                        () -> assertEquals("AD", StandardCard.ACE_DIAMONDS.getSymbol(), "Wrong symbol for ACE_DIAMONDS"),
                        () -> assertEquals(Rank.ACE, StandardCard.ACE_DIAMONDS.getRank(), "Wrong rank for ACE_DIAMONDS"),
                        () -> assertEquals(Suit.DIAMONDS, StandardCard.ACE_DIAMONDS.getSuit(), "Wrong suit for ACE_DIAMONDS"),
                        () -> assertEquals(Color.RED, StandardCard.ACE_DIAMONDS.getColor(), "Wrong color for ACE_DIAMONDS")
                ),

                // HEARTS ♥
                () -> assertAll(
                        // First - Two of Hearts
                        () -> assertEquals(39, StandardCard.TWO_HEARTS.getId(), "Wrong id for TWO_HEARTS"),
                        () -> assertEquals("2H", StandardCard.TWO_HEARTS.getSymbol(), "Wrong symbol for TWO_HEARTS"),
                        () -> assertEquals(Rank.TWO, StandardCard.TWO_HEARTS.getRank(), "Wrong rank for TWO_HEARTS"),
                        () -> assertEquals(Suit.HEARTS, StandardCard.TWO_HEARTS.getSuit(), "Wrong suit for TWO_HEARTS"),
                        () -> assertEquals(Color.RED, StandardCard.TWO_HEARTS.getColor(), "Wrong color for TWO_HEARTS"),

                        // Last - Ace of Hearts
                        () -> assertEquals(51, StandardCard.ACE_HEARTS.getId(), "Wrong id for ACE_HEARTS"),
                        () -> assertEquals("AH", StandardCard.ACE_HEARTS.getSymbol(), "Wrong symbol for ACE_HEARTS"),
                        () -> assertEquals(Rank.ACE, StandardCard.ACE_HEARTS.getRank(), "Wrong rank for ACE_HEARTS"),
                        () -> assertEquals(Suit.HEARTS, StandardCard.ACE_HEARTS.getSuit(), "Wrong suit for ACE_HEARTS"),
                        () -> assertEquals(Color.RED, StandardCard.ACE_HEARTS.getColor(), "Wrong color for ACE_HEARTS")
                )
        );
    }
}