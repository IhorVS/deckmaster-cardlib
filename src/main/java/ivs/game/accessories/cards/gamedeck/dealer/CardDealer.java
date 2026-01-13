package ivs.game.accessories.cards.gamedeck.dealer;

import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.gamedeck.GameDeck;

import java.util.List;

/**
 * Universal interface for card distribution mechanisms.
 * <p>
 * Normally used for dealing cards in card games.
 *
 * @param <D> GameDeck type
 * @param <R> the type of recipient
 */
public interface CardDealer<D extends GameDeck<? extends PlayingCard>, R extends Recipient> {

    /**
     * Deals cards according to the given requests and returns the result.
     *
     * @param deck     the mutable deck to deal from (cards may be removed)
     * @param requests the list of deal requests
     * @return the result of the deal
     * @throws IllegalStateException if a strict request cannot be satisfied
     */
    DealResult<R> deal(D deck, List<DealRequest<R>> requests);
}