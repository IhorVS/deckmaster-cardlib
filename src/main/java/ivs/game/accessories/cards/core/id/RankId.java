package ivs.game.accessories.cards.core.id;

import lombok.experimental.UtilityClass;

/**
 * A utility class that contains constants and validation methods for rank IDs.
 * Rank IDs represent the thirteen ranks in a standard deck of playing cards.
 */
@UtilityClass
public final class RankId {

    /** Two rank ID (2). */
    public static final int TWO = 0;
    /** Three rank ID (3). */
    public static final int THREE = 1;
    /** Four rank ID (4). */
    public static final int FOUR = 2;
    /** Five rank ID (5). */
    public static final int FIVE = 3;
    /** Six rank ID (6). */
    public static final int SIX = 4;
    /** Seven rank ID (7). */
    public static final int SEVEN = 5;
    /** Eight rank ID (8). */
    public static final int EIGHT = 6;
    /** Nine rank ID (9). */
    public static final int NINE = 7;
    /** Ten rank ID (10). */
    public static final int TEN = 8;
    /** Jack rank ID (J). */
    public static final int JACK = 9;
    /** Queen rank ID (Q). */
    public static final int QUEEN = 10;
    /** King rank ID (K). */
    public static final int KING = 11;
    /** Ace rank ID (A). */
    public static final int ACE = 12;

    /** The minimum valid rank ID value. */
    public static final int MIN_RANK = TWO;
    /** The maximum valid rank ID value. */
    public static final int MAX_RANK = ACE;

    /** The total number of ranks in a standard deck of playing cards. */
    public static final int RANK_COUNT = MAX_RANK - MIN_RANK + 1;

    /**
     * Checks if the given rank ID is valid.
     * A valid rank ID must be between {@link #MIN_RANK} and {@link #MAX_RANK}.
     *
     * @param rankId the rank ID to check
     * @return true if the rank ID is valid, false otherwise
     */
    public static boolean isValid(int rankId) {
        return rankId >= MIN_RANK && rankId <= MAX_RANK;
    }

    /**
     * Validates the given rank ID.
     *
     * @param rankId the rank ID to validate
     * @throws IllegalArgumentException if the rank ID is not valid
     */
    public static void validate(int rankId) {
        if (!isValid(rankId)) {
            throw new IllegalArgumentException("Invalid rank ID: " + rankId);
        }
    }

    /**
     * Returns an array containing all valid rank IDs in ascending order.
     * A new array instance is created for each call.
     *
     * @return an array containing all rank IDs from {@link #ACE} to {@link #KING}
     */
    public static int[] getAllRankIds() {
        return new int[] {TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE};
    }
}