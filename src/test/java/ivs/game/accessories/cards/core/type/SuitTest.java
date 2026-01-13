package ivs.game.accessories.cards.core.type;

import ivs.game.accessories.cards.core.id.SuitId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SuitTest {

    @ParameterizedTest(name = "{0} should have suit ID {1}")
    @DisplayName("Each suit should have correct ID")
    @MethodSource("getSuitAndIdPairs")
    void eachSuitShouldHaveCorrectId(Suit suit, int expectedId) {
        assertEquals(expectedId, suit.getId(),
                String.format("Suit %s should have ID %d", suit, expectedId));
    }

    @ParameterizedTest(name = "getById({1}) should return {0}")
    @DisplayName("getById should return correct suit for valid IDs")
    @MethodSource("getSuitAndIdPairs")
    void getByIdShouldReturnCorrectSuit(Suit expectedSuit, int suitId) {
        assertEquals(expectedSuit, Suit.getById(suitId),
                String.format("getById(%d) should return %s", suitId, expectedSuit));
    }

    @ParameterizedTest(name = "getById should throw IllegalArgumentException for suit ID {0}")
    @DisplayName("getById should throw IllegalArgumentException for invalid suit IDs")
    @ValueSource(ints = {
            Integer.MIN_VALUE, SuitId.MIN_SUIT - 1, SuitId.MAX_SUIT + 1, 100, Integer.MAX_VALUE
    })
    void getByIdShouldThrowForInvalidValues(int invalidSuitId) {
        assertThrows(IllegalArgumentException.class,
                () -> Suit.getById(invalidSuitId),
                "Should throw IllegalArgumentException for invalid suit ID: " + invalidSuitId);
    }

    @ParameterizedTest(name = "{0} should have color {1}")
    @DisplayName("Each suit should have correct color")
    @MethodSource("getSuitAndColorPairs")
    void eachSuitShouldHaveCorrectColor(Suit suit, Color expectedColor) {
        assertEquals(expectedColor, suit.getColor(),
                String.format("%s should have color %s", suit, expectedColor));
    }

    private static Stream<Arguments> getSuitAndIdPairs() {
        return Stream.of(
                Arguments.of(Suit.SPADES, SuitId.SPADES),
                Arguments.of(Suit.CLUBS, SuitId.CLUBS),
                Arguments.of(Suit.DIAMONDS, SuitId.DIAMONDS),
                Arguments.of(Suit.HEARTS, SuitId.HEARTS)
        );
    }

    private static Stream<Arguments> getSuitAndColorPairs() {
        return Stream.of(
                Arguments.of(Suit.SPADES, Color.BLACK),
                Arguments.of(Suit.CLUBS, Color.BLACK),
                Arguments.of(Suit.DIAMONDS, Color.RED),
                Arguments.of(Suit.HEARTS, Color.RED)
        );
    }

    @Test
    @DisplayName("Suit IDs should be sequential")
    void suitIdsShouldBeSequential() {
        Suit[] suits = Suit.values();
        for (int i = 0; i < suits.length - 1; i++) {
            assertEquals(suits[i].getId() + 1, suits[i + 1].getId(),
                    "Suit IDs should be sequential without gaps");
        }
    }

    @ParameterizedTest(name = "getBySymbol(\"{1}\") should return {0}")
    @DisplayName("getBySymbol should return correct suit for valid symbols")
    @MethodSource("getSuitAndSymbolPairs")
    void getBySymbolShouldReturnCorrectSuit(Suit expectedSuit, String symbol) {
        assertEquals(expectedSuit, Suit.getBySymbol(symbol),
                String.format("getBySymbol(\"%s\") should return %s", symbol, expectedSuit));
    }

    @ParameterizedTest(name = "getBySymbol should throw IllegalArgumentException for symbol \"{0}\"")
    @DisplayName("getBySymbol should throw IllegalArgumentException for invalid symbols")
    @ValueSource(strings = {"", "X", "A", "1", "♠", "♣", "♦", "♥"})
    void getBySymbolShouldThrowForInvalidValues(String invalidSymbol) {
        assertThrows(IllegalArgumentException.class,
                () -> Suit.getBySymbol(invalidSymbol),
                "Should throw IllegalArgumentException for invalid symbol: " + invalidSymbol);
    }

    @NullSource
    @DisplayName("getBySymbol should throw NullPointerException for null")
    void getBySymbolShouldThrowForNull(String nullSymbol) {
        assertThrows(NullPointerException.class,
                () -> Suit.getBySymbol(nullSymbol),
                "Should throw NullPointerException for null symbol");
    }

    private static Stream<Arguments> getSuitAndSymbolPairs() {
        return Stream.of(
                Arguments.of(Suit.SPADES, "S"),
                Arguments.of(Suit.CLUBS, "C"),
                Arguments.of(Suit.DIAMONDS, "D"),
                Arguments.of(Suit.HEARTS, "H")
        );
    }

    @Test
    @DisplayName("getBySymbol should throw NullPointerException for null argument")
    void getBySymbolShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> Suit.getBySymbol(null),
                "Should throw NullPointerException when symbol is null"
        );
    }
}