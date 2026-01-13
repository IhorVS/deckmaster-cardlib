package ivs.game.accessories.cards.core.type;

import ivs.game.accessories.cards.core.id.ColorId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ColorTest {

    @Test
    @DisplayName("BLACK should have correct color ID")
    void blackShouldHaveCorrectId() {
        assertEquals(ColorId.BLACK, Color.BLACK.getId(),
                "Color BLACK should have ID " + ColorId.BLACK);
    }

    @Test
    @DisplayName("RED should have correct color ID")
    void redShouldHaveCorrectId() {
        assertEquals(ColorId.RED, Color.RED.getId(),
                "Color RED should have ID " + ColorId.RED);
    }

    @Test
    @DisplayName("getById should return BLACK for ColorId.BLACK")
    void getByIdShouldReturnBlackForBlackId() {
        assertEquals(Color.BLACK, Color.getById(ColorId.BLACK),
                String.format("getById(%d) should return BLACK", ColorId.BLACK));
    }

    @Test
    @DisplayName("getById should return RED for ColorId.RED")
    void getByIdShouldReturnRedForRedId() {
        assertEquals(Color.RED, Color.getById(ColorId.RED),
                String.format("getById(%d) should return RED", ColorId.RED));
    }

    @ParameterizedTest(name = "getById should throw IllegalArgumentException for color ID {0}")
    @DisplayName("getById should throw IllegalArgumentException for invalid color IDs")
    @ValueSource(ints = {
            Integer.MIN_VALUE, ColorId.MIN_COLOR - 1, ColorId.MAX_COLOR + 1, 100, Integer.MAX_VALUE
    })
    void getByIdShouldThrowForInvalidValues(int invalidColorId) {
        assertThrows(IllegalArgumentException.class,
                () -> Color.getById(invalidColorId),
                "Should throw IllegalArgumentException for invalid color ID: " + invalidColorId);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    @DisplayName("getOpposite should throw NullPointerException for null input")
    void getOppositeShouldThrowExceptionForNull() {
        assertThrows(NullPointerException.class,
                () -> Color.getOpposite(null),
                "Should throw NullPointerException for null input");
    }

    @ParameterizedTest(name = "getOpposite should return opposite color {1} for input {0}")
    @DisplayName("getOpposite should return opposite color")
    @MethodSource("getOppositeColorPairs")
    void getOppositeShouldReturnOppositeColor(Color input, Color expected) {
        assertEquals(expected, Color.getOpposite(input),
                "Opposite of " + input + " should be " + expected);
    }

    private static Stream<Arguments> getOppositeColorPairs() {
        return Stream.of(
                Arguments.of(Color.BLACK, Color.RED),
                Arguments.of(Color.RED, Color.BLACK)
        );
    }

    @Test
    @DisplayName("Color IDs should be sequential")
    void colorIdsShouldBeSequential() {
        Color[] colors = Color.values();
        for (int i = 0; i < colors.length - 1; i++) {
            assertEquals(colors[i].getId() + 1, colors[i + 1].getId(),
                    "Color IDs should be sequential without gaps");
        }
    }
}