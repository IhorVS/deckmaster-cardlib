package ivs.game.accessories.cards.gamedeck.shuffler;

import ivs.game.accessories.cards.core.type.PlayingCard;

import java.util.Collection;
import java.util.List;

/**
 * Interface for shufflers that perform shuffling by creating a new list,
 * leaving the original collection unchanged.
 */
public interface CopyingShuffler<T extends PlayingCard> {
    /**
     * Returns a new shuffled list containing all elements
     * of the given collection. The original collection is not modified.
     *
     * @param cards the collection of items to be shuffled
     * @return a new shuffled list
     */
    List<T> shuffleCopy(Collection<T> cards);
}