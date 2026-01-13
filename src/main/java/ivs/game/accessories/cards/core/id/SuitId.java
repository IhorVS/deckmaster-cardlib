package ivs.game.accessories.cards.core.id;

import lombok.experimental.UtilityClass;

/**
 * A utility class that contains constants and validation methods for card suits.
 * Suit IDs represent the four suits in a standard deck of playing cards.
 */
@UtilityClass
public final class SuitId {

    /** Spades suit ID (♠). */
    public static final int SPADES = 0;
    /** Clubs suit ID (♣). */
    public static final int CLUBS = 1;
    /** Diamonds suit ID (♦). */
    public static final int DIAMONDS = 2;
    /** Hearts suit ID (♥). */
    public static final int HEARTS = 3;

    /** The minimum valid suit ID value. */
    public static final int MIN_SUIT = SPADES;
    /** The maximum valid suit ID value. */
    public static final int MAX_SUIT = HEARTS;

    /** The total number of suits in a standard deck of playing cards. */
    public static final int SUIT_COUNT = MAX_SUIT - MIN_SUIT + 1;

    /**
     * Checks if the given suit ID is valid.
     * A valid suit ID must be between {@link #MIN_SUIT} and {@link #MAX_SUIT}.
     *
     * @param suitId the suit ID to check
     * @return true if the suit ID is valid, false otherwise
     */
    public static boolean isValid(int suitId) {
        return suitId >= MIN_SUIT && suitId <= MAX_SUIT;
    }

    /**
     * Validates the given suit ID.
     *
     * @param suitId the suit ID to validate
     * @throws IllegalArgumentException if the suit ID is not valid
     */
    public static void validate(int suitId) {
        if (!isValid(suitId)) {
            throw new IllegalArgumentException("Invalid suit ID: " + suitId);
        }
    }

    /**
     * Returns an array containing all valid suit IDs in ascending order.
     * A new array instance is created for each call.
     *
     * @return an array containing all suit IDs from {@link #SPADES} to {@link #HEARTS}
     */
    public static int[] getAllSuitIds() {
        return new int[]{SPADES, CLUBS, DIAMONDS, HEARTS};
    }

    /**
     * Returns the color ID for the given suit.
     *
     * @param suitId the suit ID
     * @return {@link ColorId#BLACK} for spades and clubs, {@link ColorId#RED} for hearts and diamonds
     * @throws IllegalArgumentException if the suit ID is not valid
     */
    public static int getColorId(int suitId) {
        return switch (suitId) {
            case SPADES, CLUBS -> ColorId.BLACK;
            case DIAMONDS, HEARTS -> ColorId.RED;
            default -> throw new IllegalArgumentException("Invalid suit ID: " + suitId);
        };
    }
}