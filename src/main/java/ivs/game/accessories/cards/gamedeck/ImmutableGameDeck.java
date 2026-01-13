package ivs.game.accessories.cards.gamedeck;

import ivs.game.accessories.cards.core.type.PlayingCard;

/**
 * Represents a read-only view of a card deck, providing basic deck state queries and access to cards as a stream.
 * <p>
 * The deck can be parameterized by a card type that extends PlayingCard, e.g. StandardCard, JokerCard, or the base PlayingCard itself.
 */
public interface ImmutableGameDeck<C extends PlayingCard> {

    /**
     * Returns the number of cards currently in the deck.
     *
     * @return the current size of the deck, i.e. the count of cards present
     */
    int size();

    /**
     * Checks whether the deck contains no cards.
     *
     * @return {@code true} if the deck is empty, {@code false} otherwise
     */
    boolean isEmpty();
}