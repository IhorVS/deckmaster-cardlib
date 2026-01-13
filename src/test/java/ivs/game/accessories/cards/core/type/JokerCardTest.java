package ivs.game.accessories.cards.core.type;

import ivs.game.accessories.cards.core.id.JokerId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JokerCardTest {

    @ParameterizedTest(name = "{0} should have joker ID {1}")
    @DisplayName("Each joker should have correct ID")
    @MethodSource("getJokerAndIdPairs")
    void eachJokerShouldHaveCorrectId(JokerCard joker, int expectedId) {
        assertEquals(expectedId, joker.getId(),
                String.format("%s enum should have JokerId.%s as id", joker, joker));
    }

    @ParameterizedTest(name = "getById({1}) should return {0}")
    @DisplayName("getById should return correct joker for valid IDs")
    @MethodSource("getJokerAndIdPairs")
    void getByIdShouldReturnCorrectJoker(JokerCard expectedJoker, int jokerId) {
        assertEquals(expectedJoker, JokerCard.getById(jokerId),
                String.format("Should return %s for JokerId.%s", expectedJoker, expectedJoker));
    }

    @ParameterizedTest(name = "getById should throw IllegalArgumentException for joker ID {0}")
    @DisplayName("getById should throw IllegalArgumentException for invalid joker IDs")
    @ValueSource(ints = {
            Integer.MIN_VALUE, JokerId.MIN_JOKER - 1, JokerId.MAX_JOKER + 1, 100, Integer.MAX_VALUE
    })
    void getByIdShouldThrowForInvalidValues(int invalidJokerId) {
        assertThrows(IllegalArgumentException.class,
                () -> JokerCard.getById(invalidJokerId),
                "Should throw IllegalArgumentException for invalid joker ID: " + invalidJokerId);
    }

    @ParameterizedTest(name = "{0} should have correct symbol")
    @DisplayName("Each joker should have correct symbol format")
    @MethodSource("getJokerAndSymbolPairs")
    void eachJokerShouldHaveCorrectSymbol(JokerCard joker, String expectedSymbol) {
        assertEquals(expectedSymbol, joker.getSymbol(),
                String.format("%s should have symbol %s", joker, expectedSymbol));
    }

    @ParameterizedTest(name = "getBySymbol({1}) should return {0}")
    @DisplayName("getBySymbol should return correct joker for valid symbols")
    @MethodSource("getJokerAndSymbolPairs")
    void getBySymbolShouldReturnCorrectJoker(JokerCard expectedJoker, String symbol) {
        assertEquals(expectedJoker, JokerCard.getBySymbol(symbol),
                String.format("Should return %s for symbol %s", expectedJoker, symbol));
    }

    @ParameterizedTest(name = "getBySymbol should throw IllegalArgumentException for invalid symbol {0}")
    @DisplayName("getBySymbol should throw IllegalArgumentException for invalid symbols")
    @ValueSource(strings = {"", "R", "R0", "R5", "J1", "XX"})
    void getBySymbolShouldThrowForInvalidValues(String invalidSymbol) {
        assertThrows(IllegalArgumentException.class,
                () -> JokerCard.getBySymbol(invalidSymbol),
                "Should throw IllegalArgumentException for invalid joker symbol: " + invalidSymbol);
    }

    @Test
    @DisplayName("getBySymbol should throw NullPointerException for null symbol")
    void getBySymbolShouldThrowForNullSymbol() {
        assertThrows(NullPointerException.class,
                () -> JokerCard.getBySymbol(null),
                "Should throw NullPointerException for null symbol");
    }

    @Test
    @DisplayName("getRank should throw UnsupportedOperationException")
    void getRankShouldThrowException() {
        assertThrows(UnsupportedOperationException.class,
                JokerCard.JOKER_1::getRank,
                "Jokers don't have ranks");
    }

    @Test
    @DisplayName("getSuit should throw UnsupportedOperationException")
    void getSuitShouldThrowException() {
        assertThrows(UnsupportedOperationException.class,
                JokerCard.JOKER_1::getSuit,
                "Jokers don't have suits");
    }

    @Test
    @DisplayName("isJoker should return true")
    void isJokerShouldReturnTrue() {
        assertTrue(JokerCard.JOKER_1.isJoker(), "isJoker should always return true for jokers");
    }

    @ParameterizedTest(name = "{0} should have color {1}")
    @DisplayName("Each joker should have correct color")
    @MethodSource("getJokerAndColorPairs")
    void eachJokerShouldHaveCorrectColor(JokerCard joker, Color expectedColor) {
        assertEquals(expectedColor, joker.getColor(),
                String.format("%s should have color %s", joker, expectedColor));
    }

    private static Stream<Arguments> getJokerAndIdPairs() {
        return Stream.of(
                Arguments.of(JokerCard.JOKER_1, JokerId.JOKER_1),
                Arguments.of(JokerCard.JOKER_2, JokerId.JOKER_2),
                Arguments.of(JokerCard.JOKER_3, JokerId.JOKER_3),
                Arguments.of(JokerCard.JOKER_4, JokerId.JOKER_4)
        );
    }

    private static Stream<Arguments> getJokerAndSymbolPairs() {
        return Stream.of(
                Arguments.of(JokerCard.JOKER_1, "R1"),
                Arguments.of(JokerCard.JOKER_2, "R2"),
                Arguments.of(JokerCard.JOKER_3, "R3"),
                Arguments.of(JokerCard.JOKER_4, "R4")
        );
    }

    private static Stream<Arguments> getJokerAndColorPairs() {
        return Stream.of(
                Arguments.of(JokerCard.JOKER_1, Color.BLACK),
                Arguments.of(JokerCard.JOKER_2, Color.RED),
                Arguments.of(JokerCard.JOKER_3, Color.BLACK),
                Arguments.of(JokerCard.JOKER_4, Color.RED)
        );
    }
}