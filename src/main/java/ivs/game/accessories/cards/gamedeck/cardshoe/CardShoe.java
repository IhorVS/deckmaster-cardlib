package ivs.game.accessories.cards.gamedeck.cardshoe;

import ivs.game.accessories.cards.core.type.CardExportable;
import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.gamedeck.GameDeckException;

import java.util.List;

/**
 * Interface for a mutable card shoe ("башмак") used in card games.
 * Supports card extraction operations and cut-card monitoring.
 *
 * @param <C> the type of card in the shoe
 */
public interface CardShoe<C extends PlayingCard>
        extends ImmutableCardShoe<C>, CardExportable<C> {

    /**
     * Removes and returns the card from the top (front) of the shoe.
     *
     * @return the drawn card
     * @throws GameDeckException if the shoe is empty
     */
    C draw();

    /**
     * Removes and returns up to {@code count} cards from the top (front) of the shoe, in original order.
     *
     * @param count the number of cards to draw
     * @return list of drawn cards
     * @throws IllegalArgumentException if count is negative or there are not enough cards in the shoe
     * @throws GameDeckException        if the shoe is empty
     */
    List<C> draw(int count);

    /**
     * Returns, but does not remove, the next card in the shoe.
     *
     * @return the next card in the shoe
     * @throws GameDeckException if the shoe is empty
     */
    C peek() throws GameDeckException;
}