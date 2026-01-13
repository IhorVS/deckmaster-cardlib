package ivs.game.accessories.cards.core.id;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class SuitIdTest {

    @ParameterizedTest(name = "isValid should return true for suit ID {0}")
    @DisplayName("isValid should return true for all valid suits")
    @MethodSource("allPossibleSuitIds")
    void isValidShouldReturnTrueForValidSuits(int suitId) {
        assertTrue(SuitId.isValid(suitId), "Suit ID " + suitId + " should be valid");
    }

    @ParameterizedTest(name = "isValid should return false for suit ID {0}")
    @DisplayName("isValid should return false for invalid suit IDs")
    @MethodSource("invalidSuitIds")
    void isValidShouldReturnFalseForInvalidValues(int invalidSuitId) {
        assertFalse(SuitId.isValid(invalidSuitId), "Suit ID " + invalidSuitId + " should be invalid");
    }

    @Test
    @DisplayName("isValid should return false for value less than MIN_SUIT")
    void isValidShouldReturnFalseForValueLessThanMin() {
        assertFalse(SuitId.isValid(SuitId.MIN_SUIT - 1), "Value less than MIN_SUIT should be invalid");
    }

    @Test
    @DisplayName("isValid should return false for value greater than MAX_SUIT")
    void isValidShouldReturnFalseForValueGreaterThanMax() {
        assertFalse(SuitId.isValid(SuitId.MAX_SUIT + 1), "Value greater than MAX_SUIT should be invalid");
    }

    @ParameterizedTest(name = "validate should not throw exception for suit ID {0}")
    @DisplayName("validate should not throw exception for valid suit IDs")
    @MethodSource("allPossibleSuitIds")
    void validateShouldNotThrowExceptionForValidSuitIds(int suitId) {
        assertDoesNotThrow(() -> SuitId.validate(suitId), "Validation should pass for valid suit ID: " + suitId);
    }

    @Test
    @DisplayName("validate should throw IllegalArgumentException for suit ID above MAX_SUIT")
    void validateShouldThrowExceptionForSuitIdAboveMax() {
        assertThrows(IllegalArgumentException.class,
                () -> SuitId.validate(SuitId.MAX_SUIT + 1),
                "Should throw exception for suit ID above MAX_SUIT");
    }

    @Test
    @DisplayName("validate should throw IllegalArgumentException for suit ID below MIN_SUIT")
    void validateShouldThrowExceptionForSuitIdBelowMin() {
        assertThrows(IllegalArgumentException.class,
                () -> SuitId.validate(SuitId.MIN_SUIT - 1),
                "Should throw exception for suit ID below MIN_SUIT");
    }

    @Test
    @DisplayName("getAllSuitIds should return array with all suits in ascending order")
    void getAllSuitIdsShouldReturnAllSuitsInAscendingOrder() {
        int[] suitIds = SuitId.getAllSuitIds();

        assertNotNull(suitIds, "Array should not be null");
        assertEquals(4, suitIds.length, "Array should contain exactly 4 elements");
        assertEquals(SuitId.SPADES, suitIds[0], "First element should be SPADES");
        assertEquals(SuitId.CLUBS, suitIds[1], "Second element should be CLUBS");
        assertEquals(SuitId.DIAMONDS, suitIds[2], "Third element should be DIAMONDS");
        assertEquals(SuitId.HEARTS, suitIds[3], "Fourth element should be HEARTS");
    }

    @Test
    @DisplayName("getAllSuitIds should return new array instance on each call")
    void getAllSuitIdsShouldReturnNewInstance() {
        int[] firstCall = SuitId.getAllSuitIds();
        int[] secondCall = SuitId.getAllSuitIds();

        assertNotSame(firstCall, secondCall, "Method should return new array instance each time");
        assertArrayEquals(firstCall, secondCall, "Arrays should have same content");
    }

    @ParameterizedTest(name = "getColorId should return BLACK for suit ID {0}")
    @DisplayName("getColorId should return BLACK for black suits")
    @ValueSource(ints = {SuitId.SPADES, SuitId.CLUBS})
    void getColorIdShouldReturnBlackForBlackSuits(int suitId) {
        assertEquals(ColorId.BLACK, SuitId.getColorId(suitId),
                "Black suits should return BLACK color");
    }

    @ParameterizedTest(name = "getColorId should return WHITE for suit ID {0}")
    @DisplayName("getColorId should return RED for red suits")
    @ValueSource(ints = {SuitId.DIAMONDS, SuitId.HEARTS})
    void getColorIdShouldReturnRedForRedSuits(int suitId) {
        assertEquals(ColorId.RED, SuitId.getColorId(suitId),
                "Red suits should return RED color");
    }

    @ParameterizedTest(name = "getColorId should throw IllegalArgumentException for invalid suit ID {0}")
    @DisplayName("getColorId should throw IllegalArgumentException for invalid suit IDs")
    @MethodSource("invalidSuitIds")
    void getColorIdShouldThrowExceptionForInvalidSuitId(int invalidSuitId) {
        assertThrows(IllegalArgumentException.class,
                () -> SuitId.getColorId(invalidSuitId),
                "Should throw exception for invalid suit ID: " + invalidSuitId);
    }

    private static IntStream invalidSuitIds() {
        return IntStream.of(
                Integer.MIN_VALUE,
                SuitId.MIN_SUIT - 1,
                SuitId.MAX_SUIT + 1,
                Integer.MAX_VALUE
        );
    }

    private static IntStream allPossibleSuitIds() {
        return IntStream.rangeClosed(SuitId.MIN_SUIT, SuitId.MAX_SUIT);
    }
}