package ivs.game.accessories.cards.core.id;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ColorIdTest {

    @Test
    @DisplayName("isValid should return true for BLACK (0)")
    void isValidShouldReturnTrueForBlack() {
        assertTrue(ColorId.isValid(ColorId.BLACK));
    }

    @Test
    @DisplayName("isValid should return true for RED (1)")
    void isValidShouldReturnTrueForRed() {
        assertTrue(ColorId.isValid(ColorId.RED));
    }

    @ParameterizedTest
    @DisplayName("isValid should return false for invalid color IDs")
    @ValueSource(ints = {
            ColorId.MIN_COLOR - 1,
            ColorId.MAX_COLOR + 1,
            Integer.MIN_VALUE,
            Integer.MAX_VALUE
    })
    void isValidShouldReturnFalseForInvalidValues(int invalidColorId) {
        assertFalse(ColorId.isValid(invalidColorId));
    }

    @Test
    @DisplayName("isValid should return false for value less than MIN_COLOR")
    void isValidShouldReturnFalseForValueLessThanMin() {
        assertFalse(ColorId.isValid(ColorId.MIN_COLOR - 1));
    }

    @Test
    @DisplayName("isValid should return false for value greater than MAX_COLOR")
    void isValidShouldReturnFalseForValueGreaterThanMax() {
        assertFalse(ColorId.isValid(ColorId.MAX_COLOR + 1));
    }


    @Test
    @DisplayName("validate should not throw exception for BLACK")
    void validateShouldNotThrowForBlack() {
        assertDoesNotThrow(() -> ColorId.validate(ColorId.BLACK));
    }

    @Test
    @DisplayName("validate should not throw exception for RED")
    void validateShouldNotThrowForRed() {
        assertDoesNotThrow(() -> ColorId.validate(ColorId.RED));
    }

    @ParameterizedTest
    @DisplayName("validate should throw IllegalArgumentException for invalid values")
    @ValueSource(ints = {
            ColorId.MIN_COLOR - 1,
            ColorId.MAX_COLOR + 1,
            Integer.MIN_VALUE,
            Integer.MAX_VALUE
    })
    void validateShouldThrowForInvalidValues(int invalidColorId) {
        assertThrows(IllegalArgumentException.class, () -> ColorId.validate(invalidColorId));
    }

    @Test
    @DisplayName("validate should throw for value less than MIN_COLOR")
    void validateShouldThrowForValueLessThanMin() {
        int invalidColor = ColorId.MIN_COLOR - 1;
        assertThrows(IllegalArgumentException.class, () -> ColorId.validate(invalidColor));
    }

    @Test
    @DisplayName("validate should throw for value greater than MAX_COLOR")
    void validateShouldThrowForValueGreaterThanMax() {
        int invalidColor = ColorId.MAX_COLOR + 1;
        assertThrows(IllegalArgumentException.class, () -> ColorId.validate(invalidColor));
    }

    @Test
    @DisplayName("getAllColorIds should return array with all colors in ascending order")
    void getAllColorIdsShouldReturnAllColorsInAscendingOrder() {
        int[] colorIds = ColorId.getAllColorIds();

        assertNotNull(colorIds, "Array should not be null");
        assertEquals(2, colorIds.length, "Array should contain exactly 2 elements");
        assertEquals(ColorId.BLACK, colorIds[0], "First element should be BLACK");
        assertEquals(ColorId.RED, colorIds[1], "Second element should be RED");
    }

    @Test
    @DisplayName("getAllColorIds should return new array instance on each call")
    void getAllColorIdsShouldReturnNewInstance() {
        int[] firstCall = ColorId.getAllColorIds();
        int[] secondCall = ColorId.getAllColorIds();

        assertNotSame(firstCall, secondCall, "Method should return new array instance each time");
        assertArrayEquals(firstCall, secondCall, "Arrays should have same content");
    }

    @Test
    @DisplayName("getOppositeColorId should return RED for BLACK")
    void getOppositeColorIdShouldReturnRedForBlack() {
        assertEquals(ColorId.RED, ColorId.getOppositeColorId(ColorId.BLACK), "Opposite of BLACK should be RED");
    }

    @Test
    @DisplayName("getOppositeColorId should return BLACK for RED")
    void getOppositeColorIdShouldReturnBlackForRed() {
        assertEquals(ColorId.BLACK, ColorId.getOppositeColorId(ColorId.RED), "Opposite of RED should be BLACK");
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1, 2, 100, Integer.MAX_VALUE})
    @DisplayName("getOppositeColorId should throw exception for invalid color IDs")
    void getOppositeColorIdShouldThrowExceptionForInvalidColorId(int invalidColorId) {
        assertThrows(IllegalArgumentException.class, () -> ColorId.getOppositeColorId(invalidColorId),
                "Should throw IllegalArgumentException for invalid color ID: " + invalidColorId);
    }
}
