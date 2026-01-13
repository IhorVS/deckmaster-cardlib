package ivs.game.accessories.cards.core.id.format;

import ivs.game.accessories.cards.core.id.CardId;
import ivs.game.accessories.cards.core.id.RankId;
import ivs.game.accessories.cards.core.id.SuitId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("DataFlowIssue")
class CardSymbolTest {

    private static final String DELIMITER = ",";

    private static final int ACE_OF_HEARTS = CardId.getCardId(RankId.ACE, SuitId.HEARTS);
    private static final int TWO_OF_DIAMONDS = CardId.getCardId(RankId.TWO, SuitId.DIAMONDS);
    private static final int THREE_OF_CLUBS = CardId.getCardId(RankId.THREE, SuitId.CLUBS);
    private static final int FOUR_OF_SPADES = CardId.getCardId(RankId.FOUR, SuitId.SPADES);
    private static final int KING_OF_SPADES = CardId.getCardId(RankId.KING, SuitId.SPADES);

    @ParameterizedTest(name = "format({0}) should return {1}")
    @DisplayName("format should correctly convert valid card IDs")
    @MethodSource("validCardIdsAndSymbols")
    void formatShouldConvertValidCards(int cardId, String expectedSymbol) {
        assertEquals(expectedSymbol, CardSymbol.format(cardId),
                "Card ID " + cardId + " should be converted to " + expectedSymbol);
    }

    @ParameterizedTest(name = "format({0}) should throw IllegalArgumentException")
    @DisplayName("format should throw exception for invalid card IDs")
    @MethodSource("invalidCardIds")
    void formatShouldThrowExceptionForInvalidIds(int invalidCardId) {
        assertThrows(IllegalArgumentException.class,
                () -> CardSymbol.format(invalidCardId),
                "Should throw exception for invalid card ID: " + invalidCardId);
    }

    @ParameterizedTest(name = "parse({1}) should return {0}")
    @DisplayName("parse should correctly convert valid card symbols")
    @MethodSource("validCardIdsAndSymbols")
    void parseShouldConvertValidSymbols(int expectedCardId, String symbol) {
        assertEquals(expectedCardId, CardSymbol.parse(symbol),
                "Symbol '" + symbol + "' should be converted to card ID " + expectedCardId);
    }

    @Test
    @DisplayName("parse should throw NullPointerException for null symbol")
    void parseShouldThrowExceptionForNull() {
        assertThrows(NullPointerException.class,
                () -> CardSymbol.parse(null),
                "Should throw exception for null symbol");
    }

    @Test
    @DisplayName("parse should throw IllegalArgumentException for empty symbol")
    void parseShouldThrowExceptionForEmptyString() {
        assertThrows(IllegalArgumentException.class,
                () -> CardSymbol.parse(""),
                "Should throw exception for empty symbol");
    }

    @ParameterizedTest(name = "parse({0}) should throw IllegalArgumentException")
    @DisplayName("parse should throw exception for invalid symbols")
    @MethodSource("invalidSymbols")
    void parseShouldThrowExceptionForInvalidSymbols(String invalidSymbol) {
        assertThrows(IllegalArgumentException.class,
                () -> CardSymbol.parse(invalidSymbol),
                "Should throw exception for invalid symbol: " + invalidSymbol);
    }

    @Test
    @DisplayName("parse and format should be inverses")
    void parseAndFormatShouldBeInverses() {
        for (int cardId = CardId.MIN_CARD; cardId <= CardId.MAX_CARD; cardId++) {
            String symbol = CardSymbol.format(cardId);
            assertEquals(cardId, CardSymbol.parse(symbol),
                    "Converting " + cardId + " to symbol and back should return original value");
        }
    }

    @Test
    @DisplayName("formatAll should convert empty stream to empty string")
    void formatAllShouldHandleEmptyStream() {
        assertEquals("", CardSymbol.formatAll(IntStream.empty(), DELIMITER),
                "Empty stream should be converted to empty string");
    }

    @ParameterizedTest(name = "formatAll({0}) should return {1}")
    @DisplayName("formatAll should correctly format single card ID")
    @MethodSource("validCardIdsAndSymbols")
    void formatAllShouldHandleSingleCard(int cardId, String expectedSymbol) {
        assertEquals(expectedSymbol, CardSymbol.formatAll(IntStream.of(cardId), DELIMITER),
                "Single card ID should be converted to its symbol without delimiter");
    }

    @Test
    @DisplayName("formatAll should correctly join multiple cards with delimiter")
    void formatAllShouldJoinMultipleCards() {
        String expected = "AH" + DELIMITER + "KS" + DELIMITER + "2D";
        assertEquals(expected, CardSymbol.formatAll(
                        IntStream.of(ACE_OF_HEARTS, KING_OF_SPADES, TWO_OF_DIAMONDS), DELIMITER),
                "Multiple card IDs should be joined with specified delimiter");
    }

    @Test
    @DisplayName("formatAll should throw NullPointerException for null stream")
    void formatAllShouldThrowExceptionForNullStream() {
        assertThrows(NullPointerException.class,
                () -> CardSymbol.formatAll(null, DELIMITER),
                "Should throw exception for null stream");
    }

    @Test
    @DisplayName("formatAll should throw NullPointerException for null delimiter")
    void formatAllShouldThrowExceptionForNullDelimiter() {
        assertThrows(NullPointerException.class,
                () -> CardSymbol.formatAll(IntStream.of(0), null),
                "Should throw exception for null delimiter");
    }

    @Test
    @DisplayName("parseAll should handle empty string")
    void parseAllShouldHandleEmptyString() {
        int[] result = CardSymbol.parseAll("", DELIMITER).toArray();
        assertArrayEquals(new int[0], result,
                "Empty string should produce empty array");
    }

    @Test
    @DisplayName("parseAll should handle string with only delimiters")
    void parseAllShouldHandleDelimitersOnlyString() {
        String stringToParse = DELIMITER.repeat(3);
        int[] result = CardSymbol.parseAll(stringToParse, DELIMITER).toArray();
        assertArrayEquals(new int[0], result,
                "String with only delimiters should produce empty array");
    }

    @Test
    @DisplayName("parseAll should skip empty delimiter chunks")
    void parseAllShouldSkipEmptyDelimiterChunks() {
        int[] result = CardSymbol.parseAll("AH" + DELIMITER + DELIMITER + "KS" + DELIMITER, DELIMITER).toArray();
        assertArrayEquals(new int[]{ACE_OF_HEARTS, KING_OF_SPADES}, result,
                "Should skip empty strings between delimiters");
    }

    @Test
    @DisplayName("parseAll should correctly parse multiple symbols")
    void parseAllShouldParseMultipleSymbols() {
        String symbols = "AH" + DELIMITER + "KS" + DELIMITER + "2D";
        int[] expected = {ACE_OF_HEARTS, KING_OF_SPADES, TWO_OF_DIAMONDS};

        int[] result = CardSymbol.parseAll(symbols, DELIMITER).toArray();
        assertArrayEquals(expected, result,
                "Multiple symbols should be parsed correctly");
    }

    @Test
    @DisplayName("parseAll should throw NullPointerException for null symbols")
    void parseAllShouldThrowExceptionForNullSymbols() {
        assertThrows(NullPointerException.class,
                () -> CardSymbol.parseAll(null, DELIMITER),
                "Should throw exception for null symbols");
    }

    @Test
    @DisplayName("parseAll should throw NullPointerException for null delimiter")
    void parseAllShouldThrowExceptionForNullDelimiter() {
        assertThrows(NullPointerException.class,
                () -> CardSymbol.parseAll("AH", null),
                "Should throw exception for null delimiter");
    }

    @ParameterizedTest(name = "isValid({0}) should return true")
    @DisplayName("isValid should return true for valid card symbols")
    @MethodSource("validCardSymbols")
    void isValidShouldReturnTrueForValidSymbols(String symbol) {
        assertTrue(CardSymbol.isValid(symbol),
                "Symbol '" + symbol + "' should be considered valid");
    }

    @ParameterizedTest(name = "isValid({0}) should return false")
    @DisplayName("isValid should return false for invalid symbols")
    @MethodSource("invalidSymbols")
    void isValidShouldReturnFalseForInvalidSymbols(String invalidSymbol) {
        assertFalse(CardSymbol.isValid(invalidSymbol),
                "Symbol '" + invalidSymbol + "' should be considered invalid");
    }

    @Test
    @DisplayName("isValid should throw NullPointerException for null symbol")
    void isValidShouldThrowExceptionForNull() {
        assertThrows(NullPointerException.class,
                () -> CardSymbol.isValid(null),
                "Should throw exception for null symbol");
    }

    @Test
    @DisplayName("isValid should return false for empty symbol")
    void isValidShouldReturnFalseForEmptyString() {
        assertFalse(CardSymbol.isValid(""),
                "Empty string should be considered invalid");
    }

    private static Stream<String> invalidSymbols() {
        return Stream.of(
                " ",
                "A",
                "H",
                "1H",
                "AHH",
                "ah",
                "♠A",
                "XX",
                "0H",
                "ZS"
        );
    }

    private static Stream<Arguments> validCardIdsAndSymbols() {
        return Stream.of(
                Arguments.of(ACE_OF_HEARTS, "AH"),    // Ace of Hearts
                Arguments.of(TWO_OF_DIAMONDS, "2D"),   // Two of Diamonds
                Arguments.of(THREE_OF_CLUBS, "3C"),   // Three of Clubs
                Arguments.of(FOUR_OF_SPADES, "4S"),   // Four of Spades
                Arguments.of(KING_OF_SPADES, "KS")    // King of Spades
        );
    }

    private static Stream<String> validCardSymbols() {
        return Stream.of(
                "AH", "2H", "3H", "4H", "5H", "6H", "7H", "8H", "9H", "TH", "JH", "QH", "KH",
                "AS", "KS", "QS", "JS", "TS"
        );
    }

    private static IntStream invalidCardIds() {
        return IntStream.of(
                Integer.MIN_VALUE,
                CardId.MIN_CARD - 1,
                CardId.MAX_CARD + 1,
                Integer.MAX_VALUE
        );
    }
}