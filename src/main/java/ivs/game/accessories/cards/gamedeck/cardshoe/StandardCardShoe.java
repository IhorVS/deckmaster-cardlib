package ivs.game.accessories.cards.gamedeck.cardshoe;

import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.gamedeck.GameDeck;
import ivs.game.accessories.cards.gamedeck.StandardGameDeck;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang3.Validate;

import java.util.Collection;
import java.util.List;

/**
 * Represents a shoe (multi-deck) for dealing playing cards, typically used in casino games.
 * The shoe encapsulates an internal deck and manages operations such as drawing, peeking, and checking size.
 * Cards are copied from input sources and maintained in a specific order (first card is considered on top).
 * <p>
 * The {@code cutCardPosition} defines the number of cards remaining at which the cut-card is considered "out".
 * For example, if {@code cutCardPosition} is 10, then once the shoe contains fewer than 10 cards,
 * {@link #isCutCardOut()} will return {@code true}.
 * If set to {@link #NO_CUT_CARD}, the cut card is never considered out, regardless of how many cards remain.
 *
 * @param <C> the type of playing cards in the shoe
 */
public class StandardCardShoe<C extends PlayingCard> implements CardShoe<C> {
    /**
     * Special value for cut-card position meaning "no cut card": the cut card will never be considered out,
     * even if the shoe is empty.
     */
    public static final int NO_CUT_CARD = 0;

    // Internal mutable deck instance for all operations
    private final StandardGameDeck<C> gameDeck;

    // Cut-card position: the remaining number of cards in the shoe at which the cut card is considered "out"
    @Getter
    private final int cutCardPosition;

    /**
     * Constructs a card shoe from a collection of cards.
     * Cards are copied, original collection is not modified.
     * The card order is preserved, first is on top.
     *
     * @param cards input cards (top-first order)
     */
    public StandardCardShoe(@NonNull Collection<C> cards, int cutCardPosition) {
        Validate.noNullElements(cards, "Cards cannot contain null elements");
        Validate.isTrue(cutCardPosition >= 0, "Cut-card position cannot be negative");

        this.gameDeck = new StandardGameDeck<>(List.copyOf(cards));
        this.cutCardPosition = cutCardPosition;
    }

    /**
     * Constructs a card shoe from another deck.
     * Cards are copied, original deck is not modified.
     *
     * @param gameDeck input deck (cards copied in order)
     */
    public StandardCardShoe(GameDeck<C> gameDeck, int cutCardPosition) {
        this(gameDeck.exportCards(), cutCardPosition);
    }

    /**
     * Copy constructor for StandardCardShoe.
     * Performs a shallow copy of cards.
     *
     * @param other the shoe to clone
     */
    public StandardCardShoe(@NonNull StandardCardShoe<C> other) {
        this.gameDeck = new StandardGameDeck<>(other.gameDeck);
        this.cutCardPosition = other.cutCardPosition;
    }

    @Override
    public C draw() {
        return gameDeck.draw();
    }

    @Override
    public List<C> draw(int count) {
        return gameDeck.draw(count);
    }

    @Override
    public C peek() {
        return gameDeck.peekTop();
    }

    @Override
    public List<C> exportCards() {
        return gameDeck.exportCards();
    }

    @Override
    public int size() {
        return gameDeck.size();
    }

    @Override
    public boolean isEmpty() {
        return gameDeck.isEmpty();
    }

    @Override
    public boolean isCutCardOut() {
        return gameDeck.size() < cutCardPosition;
    }
}