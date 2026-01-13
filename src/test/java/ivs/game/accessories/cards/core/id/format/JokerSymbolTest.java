package ivs.game.accessories.cards.core.id.format;

import ivs.game.accessories.cards.core.id.JokerId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("DataFlowIssue")
class JokerSymbolTest {

    private static final String DELIMITER = ",";

    @ParameterizedTest(name = "format({0}) should return {1}")
    @DisplayName("format should correctly convert valid joker IDs")
    @MethodSource("validJokerIdsAndSymbols")
    void formatShouldConvertValidJokers(int jokerId, String expectedSymbol) {
        assertEquals(expectedSymbol, JokerSymbol.format(jokerId),
                "Joker ID " + jokerId + " should be converted to " + expectedSymbol);
    }

    @ParameterizedTest(name = "format({0}) should throw IllegalArgumentException")
    @DisplayName("format should throw exception for invalid joker IDs")
    @MethodSource("invalidJokerIds")
    void format(int invalidJokerId) {
        assertThrows(IllegalArgumentException.class,
                () -> JokerSymbol.format(invalidJokerId),
                "Should throw exception for invalid joker ID: " + invalidJokerId);
    }

    @ParameterizedTest(name = "parse({1}) should return {0}")
    @DisplayName("parse should correctly convert valid joker symbols")
    @MethodSource("validJokerIdsAndSymbols")
    void parseShouldConvertValidSymbols(int expectedJokerId, String symbol) {
        assertEquals(expectedJokerId, JokerSymbol.parse(symbol),
                "Symbol '" + symbol + "' should be converted to joker ID " + expectedJokerId);
    }

    @Test
    @DisplayName("parse should throw NullPointerException for null symbol")
    void parseShouldThrowExceptionForNull() {
        assertThrows(NullPointerException.class,
                () -> JokerSymbol.parse(null),
                "Should throw exception for null symbol");
    }

    @Test
    @DisplayName("parse should throw IllegalArgumentException for empty symbol")
    void parseShouldThrowExceptionForEmptyString() {
        assertThrows(IllegalArgumentException.class,
                () -> JokerSymbol.parse(""),
                "Should throw exception for empty symbol");
    }

    @ParameterizedTest(name = "parse({0}) should throw IllegalArgumentException")
    @DisplayName("parse should throw exception for invalid symbols")
    @MethodSource("invalidSymbols")
    void parseShouldThrowExceptionForInvalidSymbols(String invalidSymbol) {
        assertThrows(IllegalArgumentException.class,
                () -> JokerSymbol.parse(invalidSymbol),
                "Should throw exception for invalid symbol: " + invalidSymbol);
    }

    @Test
    @DisplayName("parse and format should be inverses")
    void parseAndFormatShouldBeInverses() {
        for (int jokerId = JokerId.MIN_JOKER; jokerId <= JokerId.MAX_JOKER; jokerId++) {
            String symbol = JokerSymbol.format(jokerId);
            assertEquals(jokerId, JokerSymbol.parse(symbol),
                    "Converting " + jokerId + " to symbol and back should return original value");
        }
    }

    @Test
    @DisplayName("formatAll should convert empty stream to empty string")
    void formatAllShouldHandleEmptyStream() {
        assertEquals("", JokerSymbol.formatAll(IntStream.empty(), DELIMITER),
                "Empty stream should be converted to empty string");
    }

    @ParameterizedTest(name = "formatAll({0}) should return {1}")
    @DisplayName("formatAll should correctly format single joker ID")
    @MethodSource("validJokerIdsAndSymbols")
    void formatAllShouldHandleSingleJoker(int jokerId, String expectedSymbol) {
        assertEquals(expectedSymbol, JokerSymbol.formatAll(IntStream.of(jokerId), DELIMITER),
                "Single joker ID should be converted to its symbol without delimiter");
    }

    @Test
    @DisplayName("formatAll should correctly join multiple jokers with delimiter")
    void formatAllShouldJoinMultipleJokers() {
        String expected = JokerSymbol.JOKER_1 + DELIMITER + JokerSymbol.JOKER_2 + DELIMITER + JokerSymbol.JOKER_3;
        assertEquals(expected, JokerSymbol.formatAll(
                        IntStream.of(JokerId.JOKER_1, JokerId.JOKER_2, JokerId.JOKER_3), DELIMITER),
                "Multiple joker IDs should be joined with specified delimiter");
    }

    @Test
    @DisplayName("formatAll should throw NullPointerException for null stream")
    void formatAllShouldThrowExceptionForNullStream() {
        assertThrows(NullPointerException.class,
                () -> JokerSymbol.formatAll(null, DELIMITER),
                "Should throw exception for null stream");
    }

    @Test
    @DisplayName("formatAll should throw NullPointerException for null delimiter")
    void formatAllShouldThrowExceptionForNullDelimiter() {
        assertThrows(NullPointerException.class,
                () -> JokerSymbol.formatAll(IntStream.of(JokerId.JOKER_1), null),
                "Should throw exception for null delimiter");
    }

    @Test
    @DisplayName("parseAll should handle empty string")
    void parseAllShouldHandleEmptyString() {
        int[] result = JokerSymbol.parseAll("", DELIMITER).toArray();
        assertArrayEquals(new int[0], result,
                "Empty string should produce empty array");
    }

    @Test
    @DisplayName("parseAll should handle string with only delimiters")
    void parseAllShouldHandleDelimitersOnlyString() {
        String stringToParse = DELIMITER.repeat(3);
        int[] result = JokerSymbol.parseAll(stringToParse, DELIMITER).toArray();
        assertArrayEquals(new int[0], result,
                "String with only delimiters should produce empty array");
    }

    @Test
    @DisplayName("parseAll with empty delimiter chunks should skip them")
    void parseAllShouldSkipEmptyDelimiterChunks() {
        int[] result = JokerSymbol.parseAll(JokerSymbol.JOKER_1 + DELIMITER + DELIMITER + JokerSymbol.JOKER_2 + DELIMITER, DELIMITER).toArray();
        assertArrayEquals(new int[]{JokerId.JOKER_1, JokerId.JOKER_2}, result,
                "Should skip empty strings between delimiters");
    }

    @Test
    @DisplayName("parseAll should correctly parse multiple symbols")
    void parseAllShouldParseMultipleSymbols() {
        String symbols = JokerSymbol.JOKER_1 + DELIMITER + JokerSymbol.JOKER_2 + DELIMITER + JokerSymbol.JOKER_3;
        int[] expected = {JokerId.JOKER_1, JokerId.JOKER_2, JokerId.JOKER_3};

        int[] result = JokerSymbol.parseAll(symbols, DELIMITER).toArray();
        assertArrayEquals(expected, result,
                "Multiple symbols should be parsed correctly");
    }

    @Test
    @DisplayName("parseAll should throw NullPointerException for null symbols")
    void parseAllShouldThrowExceptionForNullSymbols() {
        assertThrows(NullPointerException.class,
                () -> JokerSymbol.parseAll(null, DELIMITER),
                "Should throw exception for null symbols");
    }

    @Test
    @DisplayName("parseAll should throw NullPointerException for null delimiter")
    void parseAllShouldThrowExceptionForNullDelimiter() {
        assertThrows(NullPointerException.class,
                () -> JokerSymbol.parseAll(JokerSymbol.JOKER_1, null),
                "Should throw exception for null delimiter");
    }

    @ParameterizedTest(name = "isValid({0}) should return true")
    @DisplayName("isValid should return true for valid joker symbols")
    @ValueSource(strings = {JokerSymbol.JOKER_1, JokerSymbol.JOKER_2, JokerSymbol.JOKER_3, JokerSymbol.JOKER_4})
    void isValidShouldReturnTrueForValidSymbols(String symbol) {
        assertTrue(JokerSymbol.isValid(symbol),
                "Symbol '" + symbol + "' should be considered valid");
    }

    @ParameterizedTest(name = "isValid({0}) should return false")
    @DisplayName("isValid should return false for invalid symbols")
    @MethodSource("invalidSymbols")
    void isValidShouldReturnFalseForInvalidSymbols(String invalidSymbol) {
        assertFalse(JokerSymbol.isValid(invalidSymbol),
                "Symbol '" + invalidSymbol + "' should be considered invalid");
    }

    @Test
    @DisplayName("isValid should throw NullPointerException for null symbol")
    void isValidShouldThrowExceptionForNull() {
        assertThrows(NullPointerException.class,
                () -> JokerSymbol.isValid(null),
                "Should throw exception for null symbol");
    }

    @Test
    @DisplayName("isValid should return false for empty symbol")
    void isValidShouldReturnFalseForEmptyString() {
        assertFalse(JokerSymbol.isValid(""),
                "Empty string should be considered invalid");
    }

    private static Stream<String> invalidSymbols() {
        return Stream.of(
                " ",
                "0",
                "RR",
                "Joker",
                "🃏",
                "#",
                "r1",
                "r2",
                "r3",
                "r4",
                "R0",
                "R5"
        );
    }

    private static Stream<Arguments> validJokerIdsAndSymbols() {
        return Stream.of(
                Arguments.of(JokerId.JOKER_1, JokerSymbol.JOKER_1),
                Arguments.of(JokerId.JOKER_2, JokerSymbol.JOKER_2),
                Arguments.of(JokerId.JOKER_3, JokerSymbol.JOKER_3),
                Arguments.of(JokerId.JOKER_4, JokerSymbol.JOKER_4)
        );
    }

    private static IntStream invalidJokerIds() {
        return IntStream.of(
                Integer.MIN_VALUE,
                JokerId.MIN_JOKER - 1,
                JokerId.MAX_JOKER + 1,
                Integer.MAX_VALUE
        );
    }
}