package ivs.game.accessories.cards.gamedeck.shuffler;

import ivs.game.accessories.cards.core.type.PlayingCard;

import java.util.List;

/**
 * Interface for shufflers that perform in-place shuffling,
 * directly modifying the provided list of items.
 */
public interface InPlaceShuffler<T extends PlayingCard> {
    /**
     * Shuffles the provided list in place.
     * The original list will be modified.
     *
     * @param cards the list of items to be shuffled
     */
    void shuffleInPlace(List<T> cards);
}