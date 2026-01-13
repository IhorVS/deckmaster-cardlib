package ivs.game.accessories.cards.gamedeck.cardshoe;

import ivs.game.accessories.cards.core.type.PlayingCard;

/**
 * Read-only interface for a card shoe ("башмак") used in card games.
 * Provides basic information about the current state, cut-card position and end-of-shoe status.
 *
 * @param <C> the type of card in the shoe
 */
public interface ImmutableCardShoe<C extends PlayingCard> {

    /**
     * Returns the number of cards remaining in the shoe.
     *
     * @return the number of cards left
     */
    int size();

    /**
     * Checks if the shoe is empty (no more cards left).
     *
     * @return true if empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Returns true if the cut card has been reached or passed ("cut card is out").
     * This typically means the shoe should be reshuffled soon.
     *
     * @return true if the cut card is out, false otherwise
     */
    boolean isCutCardOut();
}