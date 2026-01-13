package ivs.game.accessories.cards.core.id.format;

import ivs.game.accessories.cards.core.id.SuitId;
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
class SuitSymbolTest {

    public static final String DELIMITER = ",";

    @ParameterizedTest(name = "format({0}) should return {1}")
    @DisplayName("format should correctly convert valid suit IDs")
    @MethodSource("validSuitIdsAndSymbols")
    void formatShouldConvertValidSuits(int suitId, String expectedSymbol) {
        assertEquals(expectedSymbol, SuitSymbol.format(suitId),
                "Suit ID " + suitId + " should be converted to " + expectedSymbol);
    }

    @ParameterizedTest(name = "format({0}) should throw IllegalArgumentException")
    @DisplayName("format should throw exception for invalid suit IDs")
    @MethodSource("invalidSuitIds")
    void format(int invalidSuitId) {
        assertThrows(IllegalArgumentException.class,
                () -> SuitSymbol.format(invalidSuitId),
                "Should throw exception for invalid suit ID: " + invalidSuitId);
    }

    @ParameterizedTest(name = "parse({1}) should return {0}")
    @DisplayName("parse should correctly convert valid suit symbols")
    @MethodSource("validSuitIdsAndSymbols")
    void parseShouldConvertValidSymbols(int expectedSuitId, String symbol) {
        assertEquals(expectedSuitId, SuitSymbol.parse(symbol),
                "Symbol '" + symbol + "' should be converted to suit ID " + expectedSuitId);
    }

    @Test
    @DisplayName("parse should throw NullPointerException for null symbol")
    void parseShouldThrowExceptionForNull() {
        assertThrows(NullPointerException.class,
                () -> SuitSymbol.parse(null),
                "Should throw exception for null symbol");
    }

    @Test
    @DisplayName("parse should throw IllegalArgumentException for empty symbol")
    void parseShouldThrowExceptionForEmptyString() {
        assertThrows(IllegalArgumentException.class,
                () -> SuitSymbol.parse(""),
                "Should throw exception for empty symbol");
    }

    @ParameterizedTest(name = "parse({0}) should throw IllegalArgumentException")
    @DisplayName("parse should throw exception for invalid symbols")
    @MethodSource("invalidSymbols")
    void parseShouldThrowExceptionForInvalidSymbols(String invalidSymbol) {
        assertThrows(IllegalArgumentException.class,
                () -> SuitSymbol.parse(invalidSymbol),
                "Should throw exception for invalid symbol: " + invalidSymbol);
    }

    @Test
    @DisplayName("parse and format should be inverses")
    void parseAndFormatShouldBeInverses() {
        for (int suitId = SuitId.MIN_SUIT; suitId <= SuitId.MAX_SUIT; suitId++) {
            String symbol = SuitSymbol.format(suitId);
            assertEquals(suitId, SuitSymbol.parse(symbol),
                    "Converting " + suitId + " to symbol and back should return original value");
        }
    }

    @Test
    @DisplayName("parse should be case-sensitive")
    void parseShouldBeCaseSensitive() {
        assertThrows(IllegalArgumentException.class,
                () -> SuitSymbol.parse("h"),
                "Should throw exception for lowercase 'h'");
    }

    @Test
    @DisplayName("formatAll should convert empty stream to empty string")
    void formatAllShouldHandleEmptyStream() {
        assertEquals("", SuitSymbol.formatAll(IntStream.empty(), DELIMITER),
                "Empty stream should be converted to empty string");
    }

    @ParameterizedTest(name = "formatAll({0}) should return {1}")
    @DisplayName("formatAll should correctly format single suit ID")
    @MethodSource("validSuitIdsAndSymbols")
    void formatAllShouldHandleSingleSuit(int suitId, String expectedSymbol) {
        assertEquals(expectedSymbol, SuitSymbol.formatAll(IntStream.of(suitId), DELIMITER),
                "Single suit ID should be converted to its symbol without delimiter");
    }

    @Test
    @DisplayName("formatAll should correctly join multiple suits with delimiter")
    void formatAllShouldJoinMultipleSuits() {
        String expected = SuitSymbol.HEARTS + DELIMITER + SuitSymbol.SPADES + DELIMITER + SuitSymbol.DIAMONDS;
        assertEquals(expected, SuitSymbol.formatAll(
                        IntStream.of(SuitId.HEARTS, SuitId.SPADES, SuitId.DIAMONDS), DELIMITER),
                "Multiple suit IDs should be joined with specified delimiter");
    }

    @Test
    @DisplayName("formatAll should throw NullPointerException for null stream")
    void formatAllShouldThrowExceptionForNullStream() {
        assertThrows(NullPointerException.class,
                () -> SuitSymbol.formatAll(null, DELIMITER),
                "Should throw exception for null stream");
    }

    @Test
    @DisplayName("formatAll should throw NullPointerException for null delimiter")
    void formatAllShouldThrowExceptionForNullDelimiter() {
        assertThrows(NullPointerException.class,
                () -> SuitSymbol.formatAll(IntStream.of(SuitId.HEARTS), null),
                "Should throw exception for null delimiter");
    }

    @Test
    @DisplayName("parseAll should handle empty string")
    void parseAllShouldHandleEmptyString() {
        int[] result = SuitSymbol.parseAll("", DELIMITER).toArray();
        assertArrayEquals(new int[0], result,
                "Empty string should produce empty array");
    }

    @Test
    @DisplayName("parseAll should handle string with only delimiters")
    void parseAllShouldHandleDelimitersOnlyString() {
        String stringToParse = DELIMITER.repeat(3);
        int[] result = SuitSymbol.parseAll(stringToParse, DELIMITER).toArray();
        assertArrayEquals(new int[0], result,
                "String with only delimiters should produce empty array");
    }

    @Test
    @DisplayName("parseAll with empty delimiter chunks should skip them")
    void parseAllShouldSkipEmptyDelimiterChunks() {
        int[] result = SuitSymbol.parseAll(SuitSymbol.HEARTS + DELIMITER + DELIMITER + SuitSymbol.SPADES + DELIMITER, DELIMITER).toArray();
        assertArrayEquals(new int[]{SuitId.HEARTS, SuitId.SPADES}, result,
                "Should skip empty strings between delimiters");
    }

    @Test
    @DisplayName("parseAll should correctly parse multiple symbols")
    void parseAllShouldParseMultipleSymbols() {
        String symbols = SuitSymbol.HEARTS + DELIMITER + SuitSymbol.SPADES + DELIMITER + SuitSymbol.DIAMONDS;
        int[] expected = {SuitId.HEARTS, SuitId.SPADES, SuitId.DIAMONDS};

        int[] result = SuitSymbol.parseAll(symbols, DELIMITER).toArray();
        assertArrayEquals(expected, result,
                "Multiple symbols should be parsed correctly");
    }

    @Test
    @DisplayName("parseAll should throw NullPointerException for null symbols")
    void parseAllShouldThrowExceptionForNullSymbols() {
        assertThrows(NullPointerException.class,
                () -> SuitSymbol.parseAll(null, DELIMITER),
                "Should throw exception for null symbols");
    }

    @Test
    @DisplayName("parseAll should throw NullPointerException for null delimiter")
    void parseAllShouldThrowExceptionForNullDelimiter() {
        assertThrows(NullPointerException.class,
                () -> SuitSymbol.parseAll(SuitSymbol.HEARTS, null),
                "Should throw exception for null delimiter");
    }

    @ParameterizedTest(name = "isValid({0}) should return true")
    @DisplayName("isValid should return true for valid suit symbols")
    @ValueSource(strings = {SuitSymbol.HEARTS, SuitSymbol.DIAMONDS, SuitSymbol.CLUBS, SuitSymbol.SPADES})
    void isValidShouldReturnTrueForValidSymbols(String symbol) {
        assertTrue(SuitSymbol.isValid(symbol),
                "Symbol '" + symbol + "' should be considered valid");
    }

    @ParameterizedTest(name = "isValid({0}) should return false")
    @DisplayName("isValid should return false for invalid symbols")
    @MethodSource("invalidSymbols")
    void isValidShouldReturnFalseForInvalidSymbols(String invalidSymbol) {
        assertFalse(SuitSymbol.isValid(invalidSymbol), "Symbol '" + invalidSymbol + "' should be considered invalid");
    }

    @Test
    @DisplayName("isValid should throw NullPointerException for null symbol")
    void isValidShouldThrowExceptionForNull() {
        assertThrows(NullPointerException.class,
                () -> SuitSymbol.isValid(null),
                "Should throw exception for null symbol");
    }

    @Test
    @DisplayName("isValid should return false for empty symbol")
    void isValidShouldReturnFalseForEmptyString() {
        assertFalse(SuitSymbol.isValid(""), "Empty string should be considered invalid");
    }

    private static Stream<String> invalidSymbols() {
        return Stream.of(
                " ",
                "0",
                "HH",
                "Hearts",
                "♠",
                "#",
                "h",
                "d",
                "c",
                "s"
        );
    }

    private static Stream<Arguments> validSuitIdsAndSymbols() {
        return Stream.of(
                Arguments.of(SuitId.HEARTS, SuitSymbol.HEARTS),
                Arguments.of(SuitId.DIAMONDS, SuitSymbol.DIAMONDS),
                Arguments.of(SuitId.CLUBS, SuitSymbol.CLUBS),
                Arguments.of(SuitId.SPADES, SuitSymbol.SPADES)
        );
    }

    private static IntStream invalidSuitIds() {
        return IntStream.of(
                Integer.MIN_VALUE,
                SuitId.MIN_SUIT - 1,
                SuitId.MAX_SUIT + 1,
                Integer.MAX_VALUE
        );
    }
}