package ivs.game.accessories.cards.core.id.format;

import ivs.game.accessories.cards.core.id.RankId;
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
class RankSymbolTest {

    private static final String DELIMITER = ",";

    @ParameterizedTest(name = "fromRankId({0}) should return {1}")
    @DisplayName("fromRankId should correctly convert valid rank IDs")
    @MethodSource("validRankIdsAndSymbols")
    void formatShouldConvertValidRanks(int rankId, String expectedSymbol) {
        assertEquals(expectedSymbol, RankSymbol.format(rankId),
                "Rank ID " + rankId + " should be converted to " + expectedSymbol);
    }

    @ParameterizedTest(name = "fromRankId({0}) should throw IllegalArgumentException")
    @DisplayName("fromRankId should throw exception for invalid rank IDs")
    @MethodSource("invalidRankIds")
    void format(int invalidRankId) {
        assertThrows(IllegalArgumentException.class,
                () -> RankSymbol.format(invalidRankId),
                "Should throw exception for invalid rank ID: " + invalidRankId);
    }

    @ParameterizedTest(name = "parseSymbol({1}) should return {0}")
    @DisplayName("parseSymbol should correctly convert valid rank symbols")
    @MethodSource("validRankIdsAndSymbols")
    void parseShouldConvertValidSymbols(int expectedRankId, String symbol) {
        assertEquals(expectedRankId, RankSymbol.parse(symbol),
                "Symbol '" + symbol + "' should be converted to rank ID " + expectedRankId);
    }

    @Test
    @DisplayName("parseSymbol should throw IllegalArgumentException for null symbol")
    void parseSymbolShouldThrowExceptionForNull() {
        assertThrows(NullPointerException.class,
                () -> RankSymbol.parse(null),
                "Should throw exception for null symbol");
    }

    @Test
    @DisplayName("parseSymbol should throw IllegalArgumentException for empty symbol")
    void parseSymbolShouldThrowExceptionForEmptyString() {
        assertThrows(IllegalArgumentException.class,
                () -> RankSymbol.parse(""),
                "Should throw exception for empty symbol");
    }

    @ParameterizedTest(name = "parseSymbol({0}) should throw IllegalArgumentException")
    @DisplayName("parseSymbol should throw exception for invalid symbols")
    @MethodSource("invalidSymbols")
    void parseShouldThrowExceptionForInvalidSymbols(String invalidSymbol) {
        assertThrows(IllegalArgumentException.class,
                () -> RankSymbol.parse(invalidSymbol),
                "Should throw exception for invalid symbol: " + invalidSymbol);
    }

    @Test
    @DisplayName("parseSymbol and toSymbol should be inverses")
    void parseSymbolAndToShouldBeInverses() {
        for (int rankId = RankId.MIN_RANK; rankId <= RankId.MAX_RANK; rankId++) {
            String symbol = RankSymbol.format(rankId);
            assertEquals(rankId, RankSymbol.parse(symbol),
                    "Converting " + rankId + " to symbol and back should return original value");
        }
    }

    @Test
    @DisplayName("formatAll should convert empty stream to empty string")
    void formatAllShouldHandleEmptyStream() {
        assertEquals("", RankSymbol.formatAll(IntStream.empty(), DELIMITER),
                "Empty stream should be converted to empty string");
    }

    @ParameterizedTest(name = "formatAll({0}) should return {1}")
    @DisplayName("formatAll should correctly format single rank ID")
    @MethodSource("validRankIdsAndSymbols")
    void formatAllShouldHandleSingleRank(int rankId, String expectedSymbol) {
        assertEquals(expectedSymbol, RankSymbol.formatAll(IntStream.of(rankId), DELIMITER),
                "Single rank ID should be converted to its symbol without delimiter");
    }

    @Test
    @DisplayName("formatAll should correctly join multiple ranks with delimiter")
    void formatAllShouldJoinMultipleRanks() {
        String expected = RankSymbol.ACE + DELIMITER + RankSymbol.KING + DELIMITER + RankSymbol.QUEEN;
        assertEquals(expected, RankSymbol.formatAll(
                        IntStream.of(RankId.ACE, RankId.KING, RankId.QUEEN), DELIMITER),
                "Multiple rank IDs should be joined with specified delimiter");
    }

    @Test
    @DisplayName("formatAll should throw NullPointerException for null stream")
    void formatAllShouldThrowExceptionForNullStream() {
        assertThrows(NullPointerException.class,
                () -> RankSymbol.formatAll(null, DELIMITER),
                "Should throw exception for null stream");
    }

    @Test
    @DisplayName("formatAll should throw NullPointerException for null delimiter")
    void formatAllShouldThrowExceptionForNullDelimiter() {
        assertThrows(NullPointerException.class,
                () -> RankSymbol.formatAll(IntStream.of(RankId.ACE), null),
                "Should throw exception for null delimiter");
    }

    @ParameterizedTest(name = "formatAll({0}) should throw IllegalArgumentException")
    @DisplayName("formatAll should throw exception for invalid rank IDs")
    @MethodSource("invalidRankIds")
    void formatAllShouldThrowExceptionForInvalidRankId(int invalidRankId) {
        assertThrows(IllegalArgumentException.class,
                () -> RankSymbol.formatAll(IntStream.of(invalidRankId), DELIMITER),
                "Should throw exception for invalid rank ID: " + invalidRankId);
    }

    @Test
    @DisplayName("parseAll should handle empty string")
    void parseAllShouldHandleEmptyString() {
        int[] result = RankSymbol.parseAll("", DELIMITER).toArray();
        assertArrayEquals(new int[0], result,
                "Empty string should produce empty array");
    }

    @Test
    @DisplayName("parseAll should handle string with only delimiters")
    void parseAllShouldHandleDelimitersOnlyString() {
        String stringToParse = DELIMITER.repeat(3);
        int[] result = RankSymbol.parseAll(stringToParse, DELIMITER).toArray();
        assertArrayEquals(new int[0], result,
                "String with only delimiters should produce empty array");
    }

    @Test
    @DisplayName("parseAll with empty delimiter chunks should skip them")
    void parseAllShouldSkipEmptyDelimiterChunks() {
        int[] result = RankSymbol.parseAll(RankSymbol.ACE + DELIMITER + DELIMITER + RankSymbol.KING + DELIMITER, DELIMITER).toArray();
        assertArrayEquals(new int[]{RankId.ACE, RankId.KING}, result,
                "Should skip empty strings between delimiters");
    }

    @ParameterizedTest(name = "parseAll({1}) should return [{0}]")
    @DisplayName("parseAll should correctly parse single symbol")
    @MethodSource("validRankIdsAndSymbols")
    void parseAllShouldParseSingleSymbol(int expectedRankId, String symbol) {
        int[] result = RankSymbol.parseAll(symbol, DELIMITER).toArray();
        assertArrayEquals(new int[]{expectedRankId}, result,
                "Single symbol should be parsed correctly");
    }

    @Test
    @DisplayName("parseAll should correctly parse multiple symbols")
    void parseAllShouldParseMultipleSymbols() {
        String symbols = RankSymbol.ACE + DELIMITER + RankSymbol.KING + DELIMITER + RankSymbol.QUEEN;
        int[] expected = {RankId.ACE, RankId.KING, RankId.QUEEN};

        int[] result = RankSymbol.parseAll(symbols, DELIMITER).toArray();
        assertArrayEquals(expected, result,
                "Multiple symbols should be parsed correctly");
    }

    @Test
    @DisplayName("parseAll should throw NullPointerException for null symbols")
    void parseAllShouldThrowExceptionForNullSymbols() {
        assertThrows(NullPointerException.class,
                () -> RankSymbol.parseAll(null, DELIMITER),
                "Should throw exception for null symbols");
    }

    @Test
    @DisplayName("parseAll should throw NullPointerException for null delimiter")
    void parseAllShouldThrowExceptionForNullDelimiter() {
        assertThrows(NullPointerException.class,
                () -> RankSymbol.parseAll(RankSymbol.ACE, null),
                "Should throw exception for null delimiter");
    }

    @ParameterizedTest(name = "parseAll({0}) should throw IllegalArgumentException")
    @DisplayName("parseAll should throw exception for invalid symbols")
    @MethodSource("invalidSymbols")
    void parseAllShouldThrowExceptionForInvalidSymbol(String invalidSymbol) {
        assertThrows(IllegalArgumentException.class,
                RankSymbol.parseAll(invalidSymbol, DELIMITER)::toArray,
                "Should throw exception for invalid symbol");
    }

    @ParameterizedTest(name = "isValid({0}) should return true")
    @DisplayName("isValid should return true for valid rank symbols")
    @ValueSource(strings = {
            RankSymbol.TWO, RankSymbol.THREE, RankSymbol.FOUR, RankSymbol.FIVE,
            RankSymbol.SIX, RankSymbol.SEVEN, RankSymbol.EIGHT, RankSymbol.NINE, RankSymbol.TEN,
            RankSymbol.JACK, RankSymbol.QUEEN, RankSymbol.KING, RankSymbol.ACE})
    void isValidShouldReturnTrueForValidSymbols(String symbol) {
        assertTrue(RankSymbol.isValid(symbol), "Symbol '" + symbol + "' should be considered valid");
    }

    @ParameterizedTest(name = "isValid({0}) should return false")
    @DisplayName("isValid should return false for invalid symbols")
    @MethodSource("invalidSymbols")
    void isValidShouldReturnFalseForInvalidSymbols(String invalidSymbol) {
        assertFalse(RankSymbol.isValid(invalidSymbol), "Symbol '" + invalidSymbol + "' should be considered invalid");
    }

    @Test
    @DisplayName("isValid should throw NullPointerException for null symbol")
    void isValidShouldThrowExceptionForNull() {
        assertThrows(NullPointerException.class,
                () -> RankSymbol.isValid(null),
                "Should throw exception for null symbol");
    }

    @Test
    @DisplayName("isValid should return false for empty symbol")
    void isValidShouldReturnFalseForEmptyString() {
        assertFalse(RankSymbol.isValid(""), "Empty string should be considered invalid");
    }

    private static Stream<String> invalidSymbols() {
        return Stream.of(
                " ",
                "a", // case-sensitive check
                "0",
                "11",
                "AA",
                "Invalid",
                "♠",
                "#"
        );
    }

    private static Stream<Arguments> validRankIdsAndSymbols() {
        return Stream.of(
                Arguments.of(RankId.ACE, RankSymbol.ACE),
                Arguments.of(RankId.TWO, RankSymbol.TWO),
                Arguments.of(RankId.THREE, RankSymbol.THREE),
                Arguments.of(RankId.FOUR, RankSymbol.FOUR),
                Arguments.of(RankId.FIVE, RankSymbol.FIVE),
                Arguments.of(RankId.SIX, RankSymbol.SIX),
                Arguments.of(RankId.SEVEN, RankSymbol.SEVEN),
                Arguments.of(RankId.EIGHT, RankSymbol.EIGHT),
                Arguments.of(RankId.NINE, RankSymbol.NINE),
                Arguments.of(RankId.TEN, RankSymbol.TEN),
                Arguments.of(RankId.JACK, RankSymbol.JACK),
                Arguments.of(RankId.QUEEN, RankSymbol.QUEEN),
                Arguments.of(RankId.KING, RankSymbol.KING)
        );
    }

    private static IntStream invalidRankIds() {
        return IntStream.of(
                Integer.MIN_VALUE,
                RankId.MIN_RANK - 1,
                RankId.MAX_RANK + 1,
                Integer.MAX_VALUE
        );
    }

}