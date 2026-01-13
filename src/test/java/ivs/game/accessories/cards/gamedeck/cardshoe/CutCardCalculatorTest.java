package ivs.game.accessories.cards.gamedeck.cardshoe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CutCardCalculatorTest {

    @ParameterizedTest(name = "basePercent={0}, expectedCutPosition={1}")
    @MethodSource("basePercentAndExpectedPosition")
    @DisplayName("Constructor with zero deviation and cut position calculation for 100 cards")
    void testConstructorAndCutCardPosition(double basePercent, int expectedPosition) {
        CutCardCalculator calc = new CutCardCalculator(basePercent, 0.0);
        assertEquals(basePercent, calc.getBasePercent(), 1e-10, "Base percent must match");
        assertEquals(0.0, calc.getDeviationPercent(), 1e-10, "Deviation percent must be zero");

        int actualPosition = calc.calculatePosition(100);
        assertEquals(expectedPosition, actualPosition, "Cut position for basePercent=" + basePercent);
    }

    static Stream<Arguments> basePercentAndExpectedPosition() {
        return Stream.of(
                Arguments.of(0.0, 0),
                Arguments.of(1.0, 100),
                Arguments.of(0.75, 75),
                Arguments.of(0.01, 1),
                Arguments.of(0.405, 41) // rounding
        );
    }

    @ParameterizedTest(name = "basePercent={0}, minExpected={1}, maxExpected={2}")
    @MethodSource("basePercentCases")
    @DisplayName("For the specified base percentage and a deviation of 0.05, the divider position is expected to be within the given range.")
    void testCutCardPosition_inExpectedRange(double basePercent, int minExpected, int maxExpected) {
        CutCardCalculator calc = new CutCardCalculator(basePercent, 0.05);
        int pos = calc.calculatePosition(100);

        assertTrue(
                pos >= minExpected && pos <= maxExpected,
                () -> "Position should be in range [" + minExpected + "; " + maxExpected + "], but was: " + pos
        );
    }

    static Stream<Arguments> basePercentCases() {
        return Stream.of(
                // basePercent, minExpected, maxExpected
                Arguments.of(0.90, 85, 95),
                Arguments.of(0.80, 75, 85),
                Arguments.of(0.70, 65, 75)
        );
    }

    @ParameterizedTest(name = "{2}: basePercent={0}, deviationPercent={1}")
    @MethodSource("invalidParameters")
    @DisplayName("Constructor throws IllegalArgumentException on invalid parameters")
    void testConstructorThrowsOnInvalidParams(double basePercent, double deviationPercent, String caseDesc) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new CutCardCalculator(basePercent, deviationPercent),
                "Should throw IllegalArgumentException for invalid parameters: " + caseDesc
        );
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> invalidParameters() {
        return Stream.of(
                // basePercent, deviationPercent, description
                Arguments.of(-0.1, 0.05, "basePercent < 0"),
                Arguments.of(1.2, 0.0, "basePercent > 1"),
                Arguments.of(0.8, -0.01, "deviationPercent < 0"),
                Arguments.of(0.6, 1.01, "deviationPercent > 1"),
                Arguments.of(0.04, 0.05, "basePercent - deviationPercent < 0"),
                Arguments.of(0.98, 0.05, "basePercent + deviationPercent > 1")
        );
    }

    @Test
    @DisplayName("Default constructor sets basePercent=0.80 and deviationPercent=0.04")
    void testDefaultConstructorValues() {
        CutCardCalculator calc = new CutCardCalculator();
        assertEquals(0.80, calc.getBasePercent(), 1e-10, "Default basePercent should be 0.80");
        assertEquals(0.04, calc.getDeviationPercent(), 1e-10, "Default deviationPercent should be 0.04");
    }

    @ParameterizedTest
    @DisplayName("calculatePosition should throw IllegalArgumentException for zero and negative shoeSize")
    @MethodSource("invalidShoeSizes")
    void testCalculatePositionThrowsOnInvalidShoeSize(int shoeSize) {
        CutCardCalculator calc = new CutCardCalculator(0.8, 0.04);
        assertThrows(IllegalArgumentException.class,
                () -> calc.calculatePosition(shoeSize),
                "Should throw IllegalArgumentException for shoeSize=" + shoeSize
        );
    }

    static Stream<Arguments> invalidShoeSizes() {
        return Stream.of(
                Arguments.of(0),
                Arguments.of(-1),
                Arguments.of(Integer.MIN_VALUE)
        );
    }
}