package ivs.game.accessories.cards.core.id;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.Validate;

import java.util.stream.IntStream;

/**
 * A utility class that contains constants and validation methods for card IDs.
 * Card IDs range from 0 to 51, representing the 52 cards in a standard deck.
 * Cards are grouped by suit, with 13 cards per suit in ascending order from Two to Ace.
 */
@UtilityClass
public final class CardId {
    /** Number of cards in each suit */
    public static final int CARDS_PER_SUIT = RankId.RANK_COUNT;

    /** The minimum valid card ID (Two of Spades) */
    public static final int MIN_CARD = 0;
    /** The maximum valid card ID (Ace of Hearts) */
    public static final int MAX_CARD = (SuitId.MAX_SUIT + 1) * CARDS_PER_SUIT - 1;

    /** The total number of cards in a standard deck */
    public static final int CARD_COUNT = MAX_CARD - MIN_CARD + 1;

    private static final String INVALID_RANK_RANGE_MESSAGE = "Invalid rank range: from %d to %d";

    /**
     * Converts suit and rank IDs to a card ID.
     *
     * @param rankId the rank ID (where 0 = Two, 12 = Ace)
     * @param suitId the suit ID
     * @return the corresponding card ID
     * @throws IllegalArgumentException if either suit ID or rank ID is invalid
     */
    public static int getCardId(int rankId, int suitId) {
        RankId.validate(rankId);
        SuitId.validate(suitId);
        return suitId * CARDS_PER_SUIT + rankId;
    }

    /**
     * Extracts the suit ID from a card ID.
     *
     * @param cardId the card ID
     * @return the suit ID
     * @throws IllegalArgumentException if the card ID is invalid
     */
    public static int getSuitId(int cardId) {
        validate(cardId);
        return cardId / CARDS_PER_SUIT;
    }

    /**
     * Returns the color ID for the given card ID.
     *
     * @param cardId the card ID
     * @return {@link ColorId#BLACK} for spades and clubs, {@link ColorId#RED} for hearts and diamonds
     * @throws IllegalArgumentException if the card ID is not valid
     */
    public static int getColorId(int cardId) {
        return SuitId.getColorId(getSuitId(cardId));
    }

    /**
     * Extracts the rank ID from a card ID.
     *
     * @param cardId the card ID
     * @return the rank ID (where 0 = Two, 12 = Ace)
     * @throws IllegalArgumentException if the card ID is invalid
     */
    public static int getRankId(int cardId) {
        validate(cardId);
        return cardId % CARDS_PER_SUIT;
    }

    /**
     * Checks if the given card ID is valid.
     *
     * @param cardId the card ID to check
     * @return true if the card ID is valid, false otherwise
     */
    public static boolean isValid(int cardId) {
        return cardId >= MIN_CARD && cardId <= MAX_CARD;
    }

    /**
     * Validates the given card ID.
     *
     * @param cardId the card ID to validate
     * @throws IllegalArgumentException if the card ID is not valid
     */
    public static void validate(int cardId) {
        if (!isValid(cardId)) {
            throw new IllegalArgumentException(String.format("Invalid card ID: %d", cardId));
        }
    }

    /**
     * Returns an array containing all valid card IDs in ascending order, grouped by suit.
     * A new array instance is created for each call.
     *
     * @return an array containing all card IDs
     */
    public static int[] getAllCardIds() {
        return IntStream.range(MIN_CARD, MAX_CARD + 1).toArray();
    }

    /**
     * Returns an array of card IDs for cards within the specified rank range.
     * The returned array is sorted in ascending order by card ID.
     * The cards are ordered by suit and then by rank.
     * If fromRank equals toRank, returns cards of that specific rank.
     *
     * @param fromRank the starting rank (inclusive, 0 = Two)
     * @param toRank   the ending rank (inclusive, 12 = Ace)
     * @return array of card IDs within the specified rank range, sorted in ascending order
     * @throws IllegalArgumentException if either rank is invalid or if fromRank is greater than toRank
     */
    public static int[] getAllCardIdsInRankRange(int fromRank, int toRank) {
        RankId.validate(fromRank);
        RankId.validate(toRank);
        Validate.isTrue(fromRank <= toRank, INVALID_RANK_RANGE_MESSAGE, fromRank, toRank);

        return IntStream.rangeClosed(SuitId.MIN_SUIT, SuitId.MAX_SUIT)
                .flatMap(suitId -> IntStream.rangeClosed(fromRank, toRank)
                        .map(rankId -> CardId.getCardId(rankId, suitId)))
                .toArray();
    }

    /**
     * Returns an array of card IDs for cards of the specified suit within the given rank range.
     * The returned array is sorted in ascending order by card ID.
     * If fromRank equals toRank, returns cards of that specific rank.
     *
     * @param suitId   the suit ID
     * @param fromRank the starting rank (inclusive, 0 = Two)
     * @param toRank   the ending rank (inclusive, 12 = Ace)
     * @return array of card IDs for the specified suit within the given rank range, sorted in ascending order
     * @throws IllegalArgumentException if the suit ID is invalid, either rank is invalid,
     *                                  or if fromRank is greater than toRank
     */
    public static int[] getCardIdsInSuitAndRankRange(int suitId, int fromRank, int toRank) {
        SuitId.validate(suitId);
        RankId.validate(fromRank);
        RankId.validate(toRank);
        Validate.isTrue(fromRank <= toRank, INVALID_RANK_RANGE_MESSAGE, fromRank, toRank);

        return IntStream.rangeClosed(fromRank, toRank)
                .map(rankId -> CardId.getCardId(rankId, suitId))
                .toArray();
    }

    /**
     * Returns a stream of all valid card IDs in ascending order, grouped by suit.
     *
     * @return IntStream of all card IDs
     */
    public static IntStream stream() {
        return IntStream.rangeClosed(MIN_CARD, MAX_CARD);
    }
}