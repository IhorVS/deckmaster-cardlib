package ivs.game.accessories.cards.gamedeck.shuffler;

import ivs.game.accessories.cards.core.type.PlayingCard;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Package-private implementation of both InPlaceShuffler and CopyingShuffler interfaces.
 * <p>
 * This class is stateless and thread-safe.
 * Uses ThreadLocalRandom for shuffling.
 */
class StandardCardShuffler<T extends PlayingCard> implements InPlaceShuffler<T>, CopyingShuffler<T> {

    /**
     * Shuffles the provided list in place.
     * The original list will be modified.
     *
     * @param cards the list of items to be shuffled
     */
    @Override
    public void shuffleInPlace(@NonNull List<T> cards) {
        Collections.shuffle(cards, ThreadLocalRandom.current());
    }

    /**
     * Returns a new shuffled list containing all elements
     * of the given collection. The original collection is not modified.
     *
     * @param cards the collection of items to be shuffled
     * @return a new shuffled list
     */
    @Override
    public List<T> shuffleCopy(@NonNull Collection<T> cards) {
        List<T> copy = new ArrayList<>(cards);
        Collections.shuffle(copy, ThreadLocalRandom.current());
        return copy;
    }
}