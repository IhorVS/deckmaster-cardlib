package ivs.game.accessories.cards.gamedeck;

import ivs.game.accessories.cards.core.type.CardExportable;
import ivs.game.accessories.cards.core.type.PlayingCard;

import java.util.List;

/**
 * Represents a mutable game deck with basic card extraction operations.
 *
 * @param <C> the type of playing card
 */
public interface GameDeck<C extends PlayingCard>
        extends ImmutableGameDeck<C>, CardExportable<C> {

    /**
     * Removes and returns the card from the top of the deck.
     *
     * @return the card taken from the top
     * @throws GameDeckException if the deck is empty
     */
    C draw();

    /**
     * Removes and returns up to {@code count} cards from the top of the deck, in order from top to bottom.
     *
     * @param count the number of cards to draw
     * @return list of cards taken from the top (exactly {@code count} cards)
     * @throws IllegalArgumentException if count is negative or if there are not enough cards in the deck
     * @throws GameDeckException        if the deck is empty
     */
    List<C> draw(int count);

    /**
     * Returns (but does not remove) the card from the bottom of the deck,
     * or {@code null} if the deck is empty.
     *
     * @return the card at the bottom of the deck or null if deck is empty
     */
    C peekBottom();

    /**
     * Returns (but does not remove) the card on top of the deck,
     * or {@code null} if the deck is empty.
     */
    C peekTop();
}