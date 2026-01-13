package ivs.game.accessories.cards.core.type;

import java.util.List;

/**
 * Interface for objects that can export their contained cards
 * as an ordered list (e.g., for serialization or logging).
 *
 * @param <C> the type of card
 */
public interface CardExportable<C extends PlayingCard> {
    /**
     * Returns the current set of cards contained in this object, in order.
     * The returned list must be immutable.
     *
     * @return immutable ordered list of cards
     */
    List<C> exportCards();
}