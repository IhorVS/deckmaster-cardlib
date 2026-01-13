package ivs.game.accessories.cards.cardholder;

import ivs.game.accessories.cards.core.type.PlayingCard;

import java.util.Collection;

/**
 * Interface for mutable card containers.
 * <p>
 * Provides methods to modify the number of cards present in the cardholder.
 * All card viewing and query operations are inherited from {@link CardViewer}.
 */
public interface CardHolder extends CardViewer {

    /**
     * Adds a single card to this cardholder.
     *
     * @param card the card to add
     * @return {@code true} if the card was added (i.e., the cardholder changed as a result)
     */
    boolean add(PlayingCard card);

    /**
     * Adds all cards from the given collection to this cardholder.
     *
     * @param cards the collection of cards to add
     * @return {@code true} if the cardholder changed as a result
     */
    boolean addAll(Collection<? extends PlayingCard> cards);

    /**
     * Removes a single card from this cardholder, if present.
     *
     * @param card the card to remove
     * @return {@code true} if the card was removed
     */
    boolean remove(PlayingCard card);

    /**
     * Removes all cards that are present in the given collection from this cardholder.
     *
     * @param cards the collection of cards to remove
     * @return {@code true} if the cardholder changed as a result
     */
    boolean removeAll(Collection<? extends PlayingCard> cards);

    /**
     * Removes all cards from this cardholder.
     */
    void clear();
}