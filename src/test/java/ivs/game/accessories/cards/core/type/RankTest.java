package ivs.game.accessories.cards.core.type;

import ivs.game.accessories.cards.core.id.RankId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RankTest {

    @ParameterizedTest(name = "{0} should have rank ID {1}")
    @DisplayName("Each rank should have correct ID")
    @MethodSource("getRankAndIdPairs")
    void eachRankShouldHaveCorrectId(Rank rank, int expectedId) {
        assertEquals(expectedId, rank.getId(),
                String.format("Rank %s should have ID %d", rank, expectedId));
    }

    @ParameterizedTest(name = "getById({1}) should return {0}")
    @DisplayName("getById should return correct rank for valid IDs")
    @MethodSource("getRankAndIdPairs")
    void getByIdShouldReturnCorrectRank(Rank expectedRank, int rankId) {
        assertEquals(expectedRank, Rank.getById(rankId),
                String.format("getById(%d) should return %s", rankId, expectedRank));
    }

    @ParameterizedTest(name = "getById should throw IllegalArgumentException for rank ID {0}")
    @DisplayName("getById should throw IllegalArgumentException for invalid rank IDs")
    @ValueSource(ints = {
            Integer.MIN_VALUE, RankId.MIN_RANK - 1, RankId.MAX_RANK + 1, 100, Integer.MAX_VALUE
    })
    void getByIdShouldThrowForInvalidValues(int invalidRankId) {
        assertThrows(IllegalArgumentException.class,
                () -> Rank.getById(invalidRankId),
                "Should throw IllegalArgumentException for invalid rank ID: " + invalidRankId);
    }

    @Test
    @DisplayName("Rank IDs should be sequential")
    void rankIdsShouldBeSequential() {
        Rank[] ranks = Rank.values();
        for (int i = 0; i < ranks.length - 1; i++) {
            assertEquals(ranks[i].getId() + 1, ranks[i + 1].getId(),
                    "Rank IDs should be sequential without gaps");
        }
    }

    @ParameterizedTest(name = "getBySymbol({1}) should return {0}")
    @DisplayName("getBySymbol should return correct rank for valid symbols")
    @MethodSource("getRankAndSymbolPairs")
    void getBySymbolShouldReturnCorrectRank(Rank expectedRank, String symbol) {
        assertEquals(expectedRank, Rank.getBySymbol(symbol),
                String.format("getBySymbol(%s) should return %s", symbol, expectedRank));
    }

    @ParameterizedTest(name = "getBySymbol should throw IllegalArgumentException for invalid symbol {0}")
    @DisplayName("getBySymbol should throw IllegalArgumentException for invalid symbols")
    @ValueSource(strings = {"", "1", "10", "B", "C", "X", "Z"})
    void getBySymbolShouldThrowForInvalidValues(String invalidSymbol) {
        assertThrows(IllegalArgumentException.class,
                () -> Rank.getBySymbol(invalidSymbol),
                "Should throw IllegalArgumentException for invalid rank symbol: " + invalidSymbol);
    }

    @Test
    @DisplayName("getBySymbol should throw NullPointerException for null symbol")
    void getBySymbolShouldThrowForNullSymbol() {
        assertThrows(NullPointerException.class,
                () -> Rank.getBySymbol(null),
                "Should throw NullPointerException for null symbol");
    }

    private static Stream<Arguments> getRankAndIdPairs() {
        return Stream.of(
                Arguments.of(Rank.ACE, RankId.ACE),
                Arguments.of(Rank.TWO, RankId.TWO),
                Arguments.of(Rank.THREE, RankId.THREE),
                Arguments.of(Rank.FOUR, RankId.FOUR),
                Arguments.of(Rank.FIVE, RankId.FIVE),
                Arguments.of(Rank.SIX, RankId.SIX),
                Arguments.of(Rank.SEVEN, RankId.SEVEN),
                Arguments.of(Rank.EIGHT, RankId.EIGHT),
                Arguments.of(Rank.NINE, RankId.NINE),
                Arguments.of(Rank.TEN, RankId.TEN),
                Arguments.of(Rank.JACK, RankId.JACK),
                Arguments.of(Rank.QUEEN, RankId.QUEEN),
                Arguments.of(Rank.KING, RankId.KING)
        );
    }

    private static Stream<Arguments> getRankAndSymbolPairs() {
        return Stream.of(
                Arguments.of(Rank.ACE, "A"),
                Arguments.of(Rank.TWO, "2"),
                Arguments.of(Rank.THREE, "3"),
                Arguments.of(Rank.FOUR, "4"),
                Arguments.of(Rank.FIVE, "5"),
                Arguments.of(Rank.SIX, "6"),
                Arguments.of(Rank.SEVEN, "7"),
                Arguments.of(Rank.EIGHT, "8"),
                Arguments.of(Rank.NINE, "9"),
                Arguments.of(Rank.TEN, "T"),
                Arguments.of(Rank.JACK, "J"),
                Arguments.of(Rank.QUEEN, "Q"),
                Arguments.of(Rank.KING, "K")
        );
    }
}