package ivs.game.accessories.cards.cardholder;

import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.core.type.Rank;
import ivs.game.accessories.cards.core.type.Suit;
import lombok.NonNull;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A mutable container for playing cards that supports querying and modification operations.
 * <p>
 * The {@code StandardCardHolder} holds an ordered list of playing cards, and provides methods for
 * adding, removing, and querying cards, as well as various utility and search operations.
 * <p>
 * This class does not allow {@code null} cards. All card operations will validate their arguments.
 *
 * <p>
 * Thread safety: This implementation is not thread-safe. If multiple threads access a
 * {@code StandardCardHolder} concurrently and at least one of the threads modifies it,
 * external synchronization is required.
 */
public class StandardCardHolder implements CardHolder {

    public static final String CONTAIN_NULL_ELEMENTS = "Cards collection cannot contain null elements";

    private final List<PlayingCard> cards = new ArrayList<>();
    private final Comparator<PlayingCard> cardComparator;

    /**
     * Constructs an empty cardholder using the specified card comparator for ordering.
     *
     * @param cardComparator comparator for ordering the cards in this cardholder; must not be {@code null}
     * @throws NullPointerException if {@code cardComparator} is {@code null}
     */
    public StandardCardHolder(@NonNull Comparator<PlayingCard> cardComparator) {
        this.cardComparator = cardComparator;
    }

    /**
     * Adds a single card to this cardholder.
     *
     * @param card the card to add
     * @return {@code true} if the card was added (i.e., the cardholder changed as a result)
     */
    @Override
    public boolean add(@NonNull PlayingCard card) {
        return cards.add(card);
    }

    /**
     * Adds all cards from the given collection to this cardholder.
     *
     * @param cards the collection of cards to add
     * @return {@code true} if the cardholder changed as a result
     */
    @Override
    public boolean addAll(@NonNull Collection<? extends PlayingCard> cards) {
        Validate.noNullElements(cards, CONTAIN_NULL_ELEMENTS);
        return this.cards.addAll(cards);
    }

    /**
     * Removes a single card from this cardholder, if present.
     *
     * @param card the card to remove
     * @return {@code true} if the card was removed
     */
    @Override
    public boolean remove(@NonNull PlayingCard card) {
        return cards.remove(card);
    }

    /**
     * Removes all cards that are present in the given collection from this cardholder.
     *
     * @param cards the collection of cards to remove
     * @return {@code true} if the cardholder changed as a result
     */
    @Override
    public boolean removeAll(@NonNull Collection<? extends PlayingCard> cards) {
        Validate.noNullElements(cards, CONTAIN_NULL_ELEMENTS);
        return this.cards.removeAll(cards);
    }

    /**
     * Removes all cards from this cardholder.
     */
    @Override
    public void clear() {
        cards.clear();
    }

    /**
     * Returns the number of cards in the cardholder.
     *
     * @return the number of cards
     */
    @Override
    public int size() {
        return cards.size();
    }

    /**
     * Returns {@code true} if the cardholder is empty.
     *
     * @return {@code true} if there are no cards, otherwise {@code false}
     */
    @Override
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Returns {@code true} if the cardholder contains the specified card.
     *
     * @param card the card to check for presence
     * @return {@code true} if the card is present, otherwise {@code false}
     */
    @Override
    public boolean contains(@NonNull PlayingCard card) {
        return cards.contains(card);
    }

    /**
     * Returns {@code true} if the cardholder contains all of the cards in the specified collection.
     *
     * @param cards the collection of cards to check for presence
     * @return {@code true} if all cards are present in the cardholder, otherwise {@code false}
     */
    @Override
    public boolean containsAll(@NonNull Collection<? extends PlayingCard> cards) {
        Validate.noNullElements(cards, CONTAIN_NULL_ELEMENTS);
        return new HashSet<>(this.cards).containsAll(cards);
    }

    /**
     * Checks whether this collection contains at least one card of the specified suit.
     *
     * @param suit the suit to check for; must not be null
     * @return true if at least one card of the given suit is present, false otherwise
     */
    @Override
    public boolean containsSuit(@NonNull Suit suit) {
        return cards.stream()
                .filter(card -> !card.isJoker())
                .anyMatch(card -> card.getSuit() == suit);
    }

    /**
     * Checks whether this collection contains at least one card of the specified rank.
     *
     * @param rank the rank to check for; must not be null
     * @return true if at least one card of the given rank is present, false otherwise
     */
    @Override
    public boolean containsRank(@NonNull Rank rank) {
        return cards.stream()
                .filter(card -> !card.isJoker())
                .anyMatch(card -> card.getRank() == rank);
    }

    /**
     * Checks whether this collection contains at least one joker card.
     *
     * @return true if at least one joker is present, false otherwise
     */
    @Override
    public boolean containsJoker() {
        return cards.stream().anyMatch(PlayingCard::isJoker);
    }

    /**
     * Returns the number of cards of the specified suit contained in this collection.
     *
     * @param suit the suit to count cards for; must not be null
     * @return the number of cards with the given suit
     */
    @Override
    public int countSuit(@NonNull Suit suit) {
        return (int) cards.stream()
                .filter(card -> !card.isJoker())
                .filter(card -> card.getSuit() == suit)
                .count();
    }

    /**
     * Returns the number of cards of the specified rank contained in this collection.
     *
     * @param rank the rank to count cards for; must not be null
     * @return the number of cards with the given rank
     */
    @Override
    public int countRank(@NonNull Rank rank) {
        return (int) cards.stream()
                .filter(card -> !card.isJoker())
                .filter(card -> card.getRank() == rank)
                .count();
    }

    /**
     * Returns the number of cards in this collection that are equal to the specified card.
     *
     * @param card the card to count occurrences of; must not be null
     * @return the number of cards equal to {@code card}
     */
    @Override
    public int countCards(@NonNull PlayingCard card) {
        return (int) cards.stream()
                .filter(c -> c.equals(card))
                .count();
    }

    /**
     * Returns the number of joker cards contained in this collection.
     *
     * @return the number of jokers
     */
    @Override
    public int countJoker() {
        return (int) cards.stream().filter(PlayingCard::isJoker).count();
    }

    /**
     * Returns the lowest card of the specified suit in this viewer, if present.
     *
     * @param suit the suit to search for
     * @return an {@code Optional} containing the minimal card of the specified suit, or empty if none
     */
    @Override
    public Optional<PlayingCard> findMin(@NonNull Suit suit) {
        return cards.stream()
                .filter(card -> !card.isJoker())
                .filter(card -> card.getSuit() == suit)
                .min(cardComparator);
    }

    /**
     * Returns the highest card of the specified suit in this viewer, if present.
     *
     * @param suit the suit to search for
     * @return an {@code Optional} containing the maximal card of the specified suit, or empty if none
     */
    @Override
    public Optional<PlayingCard> findMax(@NonNull Suit suit) {
        return cards.stream()
                .filter(card -> !card.isJoker())
                .filter(card -> card.getSuit() == suit)
                .max(cardComparator);
    }

    /**
     * Finds the greatest card of the same suit that is strictly less than the given reference card.
     *
     * @param reference the card to ordering with (its suit will be used)
     * @return an {@code Optional} containing the greatest lower card of the same suit, or empty if none found
     */
    @Override
    public Optional<PlayingCard> findClosestLower(@NonNull PlayingCard reference) {
        Suit suit = reference.getSuit();
        return cards.stream()
                .filter(card -> !card.isJoker())
                .filter(card -> card.getSuit() == suit)
                .filter(card -> cardComparator.compare(card, reference) < 0)
                .max(cardComparator);
    }

    /**
     * Finds the smallest card of the same suit that is strictly greater than the given reference card.
     *
     * @param reference the card to ordering with (its suit will be used)
     * @return an {@code Optional} containing the smallest higher card of the same suit, or empty if none found
     */
    @Override
    public Optional<PlayingCard> findClosestHigher(@NonNull PlayingCard reference) {
        Suit suit = reference.getSuit();
        return cards.stream()
                .filter(card -> !card.isJoker())
                .filter(card -> card.getSuit() == suit)
                .filter(card -> cardComparator.compare(card, reference) > 0)
                .min(cardComparator);
    }

    /**
     * Returns all cards of the same suit that are strictly less than the given reference card.
     * The cards are not removed from the original collection.
     *
     * @param reference the card to ordering with (its suit will be used)
     * @return a list of all strictly lower cards of the same suit; never null, may be empty
     */
    @Override
    public List<PlayingCard> getAllLowerOfSuit(@NonNull PlayingCard reference) {
        Suit suit = reference.getSuit();
        return cards.stream()
                .filter(card -> !card.isJoker())
                .filter(card -> card.getSuit() == suit)
                .filter(card -> cardComparator.compare(card, reference) < 0)
                .sorted(cardComparator)
                .toList();
    }

    /**
     * Returns all cards of the same suit that are strictly greater than the given reference card.
     * The cards are not removed from the original collection.
     *
     * @param reference the card to ordering with (its suit will be used)
     * @return a list of all strictly higher cards of the same suit; never null, may be empty
     */
    @Override
    public List<PlayingCard> getAllHigherOfSuit(@NonNull PlayingCard reference) {
        Suit suit = reference.getSuit();
        return cards.stream()
                .filter(card -> !card.isJoker())
                .filter(card -> card.getSuit() == suit)
                .filter(card -> cardComparator.compare(card, reference) > 0)
                .sorted(cardComparator)
                .toList();
    }

    /**
     * Returns a sequential {@code Stream} containing all cards in the viewer.
     *
     * @return a stream of cards
     */
    @Override
    public Stream<PlayingCard> stream() {
        return cards.stream();
    }

    /**
     * Returns a summary of all cards currently contained in this cardholder,
     * including the counts for each rank, suit, and joker type.
     * <p>
     * Provides an aggregated and structured view over the current state of the holder.
     *
     * @return a summary object representing the quantity of each card type
     */
    @Override
    public CardSummary getSummary() {
        return StandardCardSummary.of(cards);
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<PlayingCard> iterator() {
        return cards.iterator();
    }
}
