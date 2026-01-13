package ivs.game.accessories.cards.gamedeck.shuffler;

import ivs.game.accessories.cards.core.type.PlayingCard;

/**
 * Utility class for creating different types of card shufflers.
 * <p>
 * Provides factory methods for creating instances of shufflers:
 * - In-place shufflers that modify the original list;
 * - Copying shufflers that return a new shuffled copy of the collection.
 * <p>
 * Supports initialization with default, seeded.
 */
public class ShufflerFactory {

    /**
     * Creates a new in-place shuffler for playing cards using the default random number generator.
     *
     * @param <T> the type of playing card
     * @return an in-place shuffler instance
     */
    public static <T extends PlayingCard> InPlaceShuffler<T> getInPlaceShuffler() {
        return new StandardCardShuffler<>();
    }

    /**
     * Creates a new copying shuffler for playing cards using the default random number generator.
     *
     * @param <T> the type of playing card
     * @return a copying shuffler instance
     */
    public static <T extends PlayingCard> CopyingShuffler<T> getCopyingShuffler() {
        return new StandardCardShuffler<>();
    }
}