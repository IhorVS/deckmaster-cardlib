package ivs.game.accessories.cards.core.id;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RankIdTest {

    @ParameterizedTest(name = "isValid should return false for rank ID {0}")
    @DisplayName("isValid should return false for invalid rank IDs")
    @MethodSource("invalidRankIds")
    void isValidShouldReturnFalseForInvalidValues(int invalidRankId) {
        assertFalse(RankId.isValid(invalidRankId), "Rank ID " + invalidRankId + " should be invalid");
    }

    @ParameterizedTest(name = "validate should not throw exception for rank ID {0}")
    @DisplayName("isValid should return true for all valid ranks")
    @MethodSource("allPossibleRankIds")
    void isValidShouldReturnTrueForValidRanks(int rankId) {
        assertTrue(RankId.isValid(rankId), "Rank ID " + rankId + " should be valid");
    }

    @Test
    @DisplayName("isValid should return false for value less than MIN_RANK")
    void isValidShouldReturnFalseForValueLessThanMin() {
        assertFalse(RankId.isValid(RankId.MIN_RANK - 1), "Value less than MIN_RANK should be invalid");
    }

    @Test
    @DisplayName("isValid should return false for value greater than MAX_RANK")
    void isValidShouldReturnFalseForValueGreaterThanMax() {
        assertFalse(RankId.isValid(RankId.MAX_RANK + 1), "Value greater than MAX_RANK should be invalid");
    }

    @ParameterizedTest(name = "validate should not throw exception for rank ID {0}")
    @DisplayName("validate should not throw exception for valid rank IDs")
    @MethodSource("allPossibleRankIds")
    void validateShouldNotThrowExceptionForValidRankIds(int rankId) {
        assertDoesNotThrow(() -> RankId.validate(rankId),
                "Validation should pass for valid rank ID: " + rankId);
    }

    @Test
    @DisplayName("validate should throw IllegalArgumentException for rank ID above MAX_RANK")
    void validateShouldThrowExceptionForRankIdAboveMax() {
        assertThrows(IllegalArgumentException.class,
                () -> RankId.validate(RankId.MAX_RANK + 1),
                "Should throw exception for rank ID above MAX_RANK");
    }

    @Test
    @DisplayName("validate should throw IllegalArgumentException for rank ID below MIN_RANK")
    void validateShouldThrowExceptionForRankIdBelowMin() {
        assertThrows(IllegalArgumentException.class,
                () -> RankId.validate(RankId.MIN_RANK - 1),
                "Should throw exception for rank ID below MIN_RANK");
    }

    @ParameterizedTest(name = "validate should throw IllegalArgumentException for invalid rank ID {0}")
    @DisplayName("validate should throw IllegalArgumentException for invalid rank IDs")
    @MethodSource("invalidRankIds")
    void validateShouldThrowExceptionForInvalidRankId(int invalidRankId) {
        assertThrows(IllegalArgumentException.class,
                () -> RankId.validate(invalidRankId),
                "Should throw exception for invalid rank ID: " + invalidRankId);
    }

    @Test
    @DisplayName("getAllRankIds should return array with all ranks in ascending order")
    void getAllRankIdsShouldReturnAllRanksInAscendingOrder() {
        int[] rankIds = RankId.getAllRankIds();

        assertNotNull(rankIds, "Array should not be null");
        assertEquals(RankId.RANK_COUNT, rankIds.length, String.format("Array should contain exactly %d elements", RankId.RANK_COUNT));
        assertEquals(RankId.TWO,   rankIds[0], "First element should be TWO");
        assertEquals(RankId.THREE, rankIds[1], "Second element should be THREE");
        assertEquals(RankId.FOUR,  rankIds[2], "Third element should be FOUR");
        assertEquals(RankId.FIVE,  rankIds[3], "Fourth element should be FIVE");
        assertEquals(RankId.SIX,   rankIds[4], "Fifth element should be SIX");
        assertEquals(RankId.SEVEN, rankIds[5], "Sixth element should be SEVEN");
        assertEquals(RankId.EIGHT, rankIds[6], "Seventh element should be EIGHT");
        assertEquals(RankId.NINE,  rankIds[7], "Eighth element should be NINE");
        assertEquals(RankId.TEN,   rankIds[8], "Ninth element should be TEN");
        assertEquals(RankId.JACK,  rankIds[9], "Tenth element should be JACK");
        assertEquals(RankId.QUEEN, rankIds[10], "Eleventh element should be QUEEN");
        assertEquals(RankId.KING,  rankIds[11], "Twelfth element should be KING");
        assertEquals(RankId.ACE,   rankIds[12], "Thirteenth element should be ACE");
    }

    @Test
    @DisplayName("getAllRankIds should return new array instance on each call")
    void getAllRankIdsShouldReturnNewInstance() {
        int[] firstCall = RankId.getAllRankIds();
        int[] secondCall = RankId.getAllRankIds();

        assertNotSame(firstCall, secondCall, "Method should return new array instance each time");
        assertArrayEquals(firstCall, secondCall, "Arrays should have same content");
    }

    private static IntStream allPossibleRankIds() {
        return IntStream.rangeClosed(RankId.MIN_RANK, RankId.MAX_RANK);
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