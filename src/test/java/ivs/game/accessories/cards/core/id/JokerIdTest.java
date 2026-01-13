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

class JokerIdTest {

    @ParameterizedTest(name = "isValid() should return true for joker ID {0}")
    @MethodSource("getValidJokerIds")
    void isValidShouldReturnTrueForValidIds(int jokerId) {
        assertTrue(JokerId.isValid(jokerId), "Valid joker ID should be accepted");
    }

    @ParameterizedTest(name = "isValid() should return false for joker ID {0}")
    @MethodSource("getInvalidJokerIds")
    void isValidShouldReturnFalseForInvalidIds(int invalidJokerId) {
        assertFalse(JokerId.isValid(invalidJokerId), "Invalid joker ID should be rejected");
    }

    @ParameterizedTest(name = "validate() should not throw for joker ID {0}")
    @MethodSource("getValidJokerIds")
    void validateShouldNotThrowForValidIds(int jokerId) {
        assertDoesNotThrow(() -> JokerId.validate(jokerId), "Valid joker ID should not throw exception");
    }

    @ParameterizedTest(name = "validate() should throw for invalid joker ID {0}")
    @MethodSource("getInvalidJokerIds")
    void validateShouldThrowForInvalidIds(int invalidJokerId) {
        assertThrows(IllegalArgumentException.class,
                () -> JokerId.validate(invalidJokerId),
                "Invalid joker ID should throw IllegalArgumentException");
    }

    @Test
    @DisplayName("getAllJokerIds should return array with all jokers in ascending order")
    void getAllJokerIdsShouldReturnAllJokersInAscendingOrder() {
        int[] jokerIds = JokerId.getAllJokerIds();

        assertNotNull(jokerIds, "Array should not be null");
        assertEquals(JokerId.JOKER_COUNT, jokerIds.length, String.format("Array should contain exactly %d elements", JokerId.JOKER_COUNT));
        assertEquals(JokerId.JOKER_1, jokerIds[0], "First element should be JOKER_1");
        assertEquals(JokerId.JOKER_2, jokerIds[1], "Second element should be JOKER_2");
        assertEquals(JokerId.JOKER_3, jokerIds[2], "Third element should be JOKER_3");
        assertEquals(JokerId.JOKER_4, jokerIds[3], "Fourth element should be JOKER_4");
    }

    @Test
    @DisplayName("getAllJokerIds should return new array instance on each call")
    void getAllJokerIdsShouldReturnNewInstance() {
        int[] firstCall = JokerId.getAllJokerIds();
        int[] secondCall = JokerId.getAllJokerIds();

        assertNotSame(firstCall, secondCall, "Method should return new array instance each time");
        assertArrayEquals(firstCall, secondCall, "Arrays should have same content");
    }

    @ParameterizedTest
    @DisplayName("getColorId should return BLACK for even joker IDs")
    @ValueSource(ints = {JokerId.JOKER_1, JokerId.JOKER_3})
    void getColorIdShouldReturnBlackForEvenIds(int jokerId) {
        assertEquals(ColorId.BLACK, JokerId.getColorId(jokerId),
                "Even joker IDs should return BLACK color");
    }

    @ParameterizedTest
    @DisplayName("getColorId should return RED for odd joker IDs")
    @ValueSource(ints = {JokerId.JOKER_2, JokerId.JOKER_4})
    void getColorIdShouldReturnRedForOddIds(int jokerId) {
        assertEquals(ColorId.RED, JokerId.getColorId(jokerId),
                "Odd joker IDs should return RED color");
    }

    @ParameterizedTest
    @DisplayName("getColorId should throw IllegalArgumentException for invalid joker ID")
    @MethodSource("getInvalidJokerIds")
    void getColorIdShouldThrowForInvalidIds(int invalidJokerId) {
        assertThrows(IllegalArgumentException.class,
                () -> JokerId.getColorId(invalidJokerId),
                "Should throw IllegalArgumentException for invalid joker ID");
    }

    private static IntStream getValidJokerIds() {
        return IntStream.rangeClosed(JokerId.MIN_JOKER, JokerId.MAX_JOKER);
    }

    private static IntStream getInvalidJokerIds() {
        return IntStream.of(
                Integer.MIN_VALUE,
                JokerId.MIN_JOKER - 1,
                JokerId.MAX_JOKER + 1,
                Integer.MAX_VALUE
        );
    }
}