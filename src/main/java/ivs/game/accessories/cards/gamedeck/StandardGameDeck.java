package ivs.game.accessories.cards.gamedeck;

import ivs.game.accessories.cards.core.type.PlayingCard;
import lombok.NonNull;
import org.apache.commons.lang3.Validate;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

/**
 * Standard mutable implementation of a game deck.
 * Thread-safety is not guaranteed.
 * <p>
 * The top card is the first card in the deck, i.e., the card that will be drawn or peeked with {@code draw()} or {@code peekTop()}.
 * The bottom card is the last card in the deck, returned by {@code peekBottom()}.
 * When initializing the deck from a collection, the first element in the collection becomes the top card, and the last element becomes the bottom card.
 *
 * @param <C> the type of the playing card
 */
public class StandardGameDeck<C extends PlayingCard> implements GameDeck<C> {

    private final Deque<C> deque;

    /**
     * Creates a new standard game deck with the given cards, preserving their order
     * (if the collection maintains order).
     * The first card in the collection becomes the top of the deck.
     *
     * @param cards collection of cards to initialize the deck (top-first order)
     * @throws NullPointerException if cards is null
     */
    public StandardGameDeck(@NonNull Collection<C> cards) {
        Validate.noNullElements(cards, "Cards collection cannot contain null elements");
        this.deque = new ArrayDeque<>(cards);
    }

    /**
     * Copy constructor. Creates a new deck as a shallow copy of another StandardGameDeck.
     * The cards in the new deck will be the same objects as in the original.
     *
     * @param other the deck to copy
     * @throws NullPointerException if other is null
     */
    public StandardGameDeck(@NonNull StandardGameDeck<C> other) {
        this.deque = new ArrayDeque<>(other.deque);
    }

    @Override
    public C draw() {
        GameDeckException.validateDeckSize(this);
        return deque.pollFirst();
    }

    @Override
    public List<C> draw(int count) {
        Validate.isTrue(count >= 0, "Count cannot be negative");
        GameDeckException.validateDeckSize(this, count);

        List<C> drawn = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            drawn.add(deque.pollFirst());
        }
        return drawn;
    }

    @Override
    public C peekBottom() {
        GameDeckException.validateDeckSize(this);
        return deque.peekLast();
    }

    @Override
    public C peekTop() {
        GameDeckException.validateDeckSize(this);
        return deque.peekFirst();
    }

    @Override
    public int size() {
        return deque.size();
    }

    @Override
    public boolean isEmpty() {
        return deque.isEmpty();
    }

    @Override
    public List<C> exportCards() {
        return deque.stream().toList();
    }
}