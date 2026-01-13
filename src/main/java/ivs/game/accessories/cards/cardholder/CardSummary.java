package ivs.game.accessories.cards.cardholder;

import ivs.game.accessories.cards.core.type.JokerCard;
import ivs.game.accessories.cards.core.type.Rank;
import ivs.game.accessories.cards.core.type.Suit;

/**
 * Represents an immutable and independent snapshot of the current state of a collection of playing cards,
 * organized by rank and suit. Implementations of this interface are guaranteed to be read-only and reflect the state
 * of the underlying collection at the time the summary was created. Subsequent modifications to the original collection
 * will not affect an existing {@code CardSummary} instance, and vice versa.
 *
 * <p>
 * This interface provides methods for querying the quantity of cards at the intersection of a specific rank and suit,
 * as well as aggregated quantities by rank, suit, and joker type.
 * </p>
 */
public interface CardSummary {

    /**
     * Returns the quantity of cards for the specified combination of rank and suit.
     *
     * @param rank the rank of the card (e.g. ACE, KING, TWO)
     * @param suit the suit of the card (e.g. HEARTS, CLUBS)
     * @return the number of cards with the given rank and suit,
     *         or zero if there are none
     */
    int getCardQty(Rank rank, Suit suit);

    /**
     * Returns the total quantity of all standard cards (excluding jokers) contained in this summary.
     *
     * @return the number of all non-joker cards in this summary,
     *         or zero if no standard cards are present
     */
    int getCardQty();

    /**
     * Returns the total number of cards for the specified suit, summed over all ranks.
     *
     * @param suit the suit to query (e.g. SPADES)
     * @return the number of cards with the given suit across all ranks,
     *         or zero if there are none
     */
    int getSuitQty(Suit suit);

    /**
     * Returns the total number of cards for the specified rank, summed over all suits.
     *
     * @param rank the rank to query (e.g. JACK)
     * @return the number of cards with the given rank across all suits,
     *         or zero if there are none
     */
    int getRankQty(Rank rank);

    /**
     * Returns the quantity of joker cards of the specified type.
     *
     * @param joker the joker whose quantity to retrieve (must extend {@link JokerCard})
     * @param <J> the joker card type
     * @return the number of joker cards of this type, or zero if none are present
     */
    <J extends JokerCard> int getJokerQty(J joker);

    /**
     * Returns the total quantity of all joker cards present in the set.
     *
     * @return the total number of joker cards, or zero if there are none
     */
    int getJokerQty();

    /**
     * Returns the total quantity of all cards contained in this summary, including both standard cards and jokers.
     *
     * @return the total number of cards (including jokers), or zero if the summary is empty
     */
    int getTotalQty();
}