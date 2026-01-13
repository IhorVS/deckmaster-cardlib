package ivs.game.accessories.cards.cardholder;

import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.core.type.Rank;
import ivs.game.accessories.cards.core.type.Suit;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Read-only interface for viewing a collection of playing cards.
 * Supports querying, iteration, min/max lookup and streaming.
 */
public interface CardViewer extends Iterable<PlayingCard> {

    /**
     * Returns the number of cards in the cardholder.
     *
     * @return the number of cards
     */
    int size();

    /**
     * Returns {@code true} if the cardholder is empty.
     *
     * @return {@code true} if there are no cards, otherwise {@code false}
     */
    boolean isEmpty();

    /**
     * Returns {@code true} if the cardholder contains the specified card.
     *
     * @param card the card to check for presence
     * @return {@code true} if the card is present, otherwise {@code false}
     */
    boolean contains(PlayingCard card);

    /**
     * Returns {@code true} if the cardholder contains all of the cards in the specified collection.
     *
     * @param cards the collection of cards to check for presence
     * @return {@code true} if all cards are present in the cardholder, otherwise {@code false}
     */
    boolean containsAll(Collection<? extends PlayingCard> cards);

    /**
     * Checks whether this collection contains at least one card of the specified suit.
     *
     * @param suit the suit to check for; must not be null
     * @return true if at least one card of the given suit is present, false otherwise
     */
    boolean containsSuit(Suit suit);

    /**
     * Checks whether this collection contains at least one card of the specified rank.
     *
     * @param rank the rank to check for; must not be null
     * @return true if at least one card of the given rank is present, false otherwise
     */
    boolean containsRank(Rank rank);

    /**
     * Checks whether this collection contains at least one joker card.
     *
     * @return true if at least one joker is present, false otherwise
     */
    boolean containsJoker();

    /**
     * Returns the number of cards of the specified suit contained in this collection.
     *
     * @param suit the suit to count cards for; must not be null
     * @return the number of cards with the given suit
     */
    int countSuit(Suit suit);

    /**
     * Returns the number of cards of the specified rank contained in this collection.
     *
     * @param rank the rank to count cards for; must not be null
     * @return the number of cards with the given rank
     */
    int countRank(Rank rank);

    /**
     * Returns the number of cards in this collection that are equal to the specified card.
     *
     * @param card the card to count occurrences of; must not be null
     * @return the number of cards equal to {@code card}
     */
    int countCards(PlayingCard card);

    /**
     * Returns the number of joker cards contained in this collection.
     *
     * @return the number of jokers
     */
    int countJoker();

    /**
     * Returns the lowest card of the specified suit in this viewer, if present.
     *
     * @param suit the suit to search for
     * @return an {@code Optional} containing the minimal card of the specified suit, or empty if none
     */
    Optional<PlayingCard> findMin(Suit suit);

    /**
     * Returns the highest card of the specified suit in this viewer, if present.
     *
     * @param suit the suit to search for
     * @return an {@code Optional} containing the maximal card of the specified suit, or empty if none
     */
    Optional<PlayingCard> findMax(Suit suit);

    /**
     * Finds the greatest card of the same suit that is strictly less than the given reference card.
     *
     * @param reference the card to ordering with (its suit will be used)
     * @return an {@code Optional} containing the greatest lower card of the same suit, or empty if none found
     */
    Optional<PlayingCard> findClosestLower(PlayingCard reference);

    /**
     * Finds the smallest card of the same suit that is strictly greater than the given reference card.
     *
     * @param reference the card to ordering with (its suit will be used)
     * @return an {@code Optional} containing the smallest higher card of the same suit, or empty if none found
     */
    Optional<PlayingCard> findClosestHigher(PlayingCard reference);

    /**
     * Returns all cards of the same suit that are strictly less than the given reference card.
     * The cards are not removed from the original collection.
     *
     * @param reference the card to ordering with (its suit will be used)
     * @return a list of all strictly lower cards of the same suit; never null, may be empty
     */
    List<PlayingCard> getAllLowerOfSuit(PlayingCard reference);

    /**
     * Returns all cards of the same suit that are strictly greater than the given reference card.
     * The cards are not removed from the original collection.
     *
     * @param reference the card to ordering with (its suit will be used)
     * @return a list of all strictly higher cards of the same suit; never null, may be empty
     */
    List<PlayingCard> getAllHigherOfSuit(PlayingCard reference);

    /**
     * Returns a sequential {@code Stream} containing all cards in the viewer.
     *
     * @return a stream of cards
     */
    Stream<PlayingCard> stream();

    /**
     * Returns a summary of all cards currently contained in this cardholder,
     * including the counts for each rank, suit, and joker type.
     * <p>
     * Provides an aggregated and structured view over the current state of the holder.
     *
     * @return a summary object representing the quantity of each card type
     */
    CardSummary getSummary();
}