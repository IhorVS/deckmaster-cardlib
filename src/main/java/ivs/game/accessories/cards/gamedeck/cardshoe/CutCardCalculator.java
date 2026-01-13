package ivs.game.accessories.cards.gamedeck.cardshoe;

import lombok.Getter;
import org.apache.commons.lang3.Validate;

import java.util.Random;

/**
 * Utility for calculating the cut-card position in a shoe, using a base percent and fluctuation percent.
 */
public class CutCardCalculator {

    public static final double MIN_PERCENTAGE = 0.0;
    public static final double MAX_PERCENTAGE = 1.0;

    // Default: cut card at 80% of shoe depth, with ±4% fluctuation
    private static final double DEFAULT_BASE_PERCENT = 0.80;
    private static final double DEFAULT_DEVIATION_PERCENT = 0.04;

    private final Random random = new Random();

    @Getter
    private final double basePercent;
    @Getter
    private final double deviationPercent;

    /**
     * Constructs a calculator with default values.
     * Default: 80% base position and 4% fluctuation.
     */
    public CutCardCalculator() {
        this(DEFAULT_BASE_PERCENT, DEFAULT_DEVIATION_PERCENT);
    }

    /**
     * Constructs a calculator with specified base and fluctuation percent.
     *
     * @param basePercent      base position as a fraction (e.g., 0.80 = 80% from the top)
     * @param deviationPercent fluctuation as a fraction (e.g., 0.05 = ±5%)
     * @throws IllegalArgumentException if arguments are not in [0.0, 1.0]
     */
    public CutCardCalculator(double basePercent, double deviationPercent) {
        Validate.inclusiveBetween(MIN_PERCENTAGE, MAX_PERCENTAGE, basePercent, "basePercent must be in [0.0, 1.0]");
        Validate.inclusiveBetween(MIN_PERCENTAGE, MAX_PERCENTAGE, deviationPercent, "fluctuationPercent must be in [0.0, 1.0]");
        Validate.isTrue(basePercent - deviationPercent >= MIN_PERCENTAGE,
                "fluctuationPercent is too big: minimum cut position is less than zero");
        Validate.isTrue(basePercent + deviationPercent <= MAX_PERCENTAGE,
                "fluctuationPercent is too big: maximum cut position exceeds the shoe size");

        this.basePercent = basePercent;
        this.deviationPercent = deviationPercent;
    }

    /**
     * Calculates the actual cut-card position for the given shoe size using basePercent and fluctuation.
     * Returned value is always in [0, shoeSize].
     *
     * @param shoeSize total number of cards in the shoe
     * @return cut card position (number of cards from the top); when cards left < position, cut card considered "out"
     */
    public int calculatePosition(int shoeSize) {
        Validate.isTrue(shoeSize > 0, "Shoe size must be positive");

        int basePosition = (int) Math.round(basePercent * shoeSize);
        int fluctuation = (int) Math.round(deviationPercent * shoeSize);
        int min = Math.max(0, basePosition - fluctuation);
        int max = Math.min(shoeSize, basePosition + fluctuation);
        return min + random.nextInt(max - min + 1);
    }
}