package ivs.game.accessories.cards.core.id;

import lombok.experimental.UtilityClass;

/**
 * A utility class that contains constants and validation methods for card suit colors.
 * Color IDs represent the colors of the suits in a standard deck of playing cards.
 */
@UtilityClass
public final class ColorId {

    /** Black color ID (spades, clubs). */
    public static final int BLACK = 0;
    /** Red color ID (hearts, diamonds). */
    public static final int RED = 1;

    /** The minimum valid color ID value. */
    public static final int MIN_COLOR = BLACK;
    /** The maximum valid color ID value. */
    public static final int MAX_COLOR = RED;

    /**
     * Checks if the given color ID is valid.
     * A valid color ID must be between {@link #MIN_COLOR} and {@link #MAX_COLOR}.
     *
     * @param colorId the color ID to check
     * @return true if the color ID is valid, false otherwise
     */
    public static boolean isValid(int colorId) {
        return colorId >= MIN_COLOR && colorId <= MAX_COLOR;
    }

    /**
     * Validates the given color ID.
     *
     * @param colorId the color ID to validate
     * @throws IllegalArgumentException if the color ID is not valid
     */
    public static void validate(int colorId) {
        if (!isValid(colorId)) {
            throw new IllegalArgumentException("Invalid color ID: " + colorId);
        }
    }

    /**
     * Returns an array containing all valid color IDs in ascending order.
     * A new array instance is created for each call.
     *
     * @return an array containing {@link #BLACK} and {@link #RED} values
     */
    public static int[] getAllColorIds() {
        return new int[]{BLACK, RED};
    }

    /**
     * Returns the opposite color ID for the given color ID.
     *
     * @param colorId the color ID to find the opposite for
     * @return {@link #RED} if the input is {@link #BLACK}, or {@link #BLACK} if the input is {@link #RED}
     * @throws IllegalArgumentException if the color ID is not valid
     */
    public static int getOppositeColorId(int colorId) {
        return switch (colorId) {
            case BLACK -> RED;
            case RED -> BLACK;
            default -> throw new IllegalArgumentException("Invalid color ID: " + colorId);
        };
    }
}