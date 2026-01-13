package ivs.game.accessories.cards.core.id;

import lombok.experimental.UtilityClass;

import java.util.stream.IntStream;

/**
 * A utility class that contains constants and validation methods for joker cards.
 * The standard deck includes 4 jokers: two black (IDs 52, 54) and two red (IDs 53, 55).
 * Black jokers have even IDs, red jokers have odd IDs.
 */
@UtilityClass
public final class JokerId {
    public static final int JOKER_1 = 52;
    public static final int JOKER_2 = 53;
    public static final int JOKER_3 = 54;
    public static final int JOKER_4 = 55;

    /** First joker ID. */
    public static final int FIRST_JOKER = JOKER_1;
    /** Last joker ID. */
    public static final int LAST_JOKER = JOKER_4;

    /** The minimum valid joker ID. */
    public static final int MIN_JOKER = FIRST_JOKER;
    /** The maximum valid joker ID. */
    public static final int MAX_JOKER = LAST_JOKER;

    /** The total number of jokers in a standard deck (4). */
    public static final int JOKER_COUNT = LAST_JOKER - FIRST_JOKER + 1;

    /**
     * Checks if the given ID represents a valid joker.
     *
     * @param jokerId the joker ID to check
     * @return true if the ID represents a valid joker, false otherwise
     */
    public static boolean isValid(int jokerId) {
        return jokerId >= MIN_JOKER && jokerId <= MAX_JOKER;
    }

    /**
     * Validates the given joker ID.
     *
     * @param jokerId the joker ID to validate
     * @throws IllegalArgumentException if the joker ID is not valid
     */
    public static void validate(int jokerId) {
        if (!isValid(jokerId)) {
            throw new IllegalArgumentException("Invalid joker ID: " + jokerId);
        }
    }

    /**
     * Returns an array containing all valid joker IDs in ascending order.
     * A new array instance is created for each call.
     *
     * @return an array containing all joker IDs
     */
    public static int[] getAllJokerIds() {
        return IntStream.rangeClosed(MIN_JOKER, MAX_JOKER).toArray();
    }

    /**
     * Returns the color ID for the given joker.
     * Jokers with even IDs are black, odd IDs are red.
     *
     * @param jokerId the joker ID
     * @return {@link ColorId#BLACK} for even IDs, {@link ColorId#RED} for odd IDs
     * @throws IllegalArgumentException if the joker ID is not valid
     */
    public static int getColorId(int jokerId) {
        validate(jokerId);
        return (jokerId % 2 == 0) ? ColorId.BLACK : ColorId.RED;
    }
}