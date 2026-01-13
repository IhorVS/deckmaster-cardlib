package ivs.game.accessories.cards.gamedeck;

import ivs.game.accessories.cards.core.type.PlayingCard;

/**
 * Thrown to indicate that an operation requiring a card could not be completed
 * because the deck is empty.
 */
public class GameDeckException extends RuntimeException {

    private static final String DECK_IS_EMPTY = "DeckTemplate is empty";
    private static final String NOT_ENOUGH_CARDS = "Not enough cards in deck (requested: %d, available: %d)";

    /**
     * Constructs a new EmptyDeckException with no detail message.
     */
    public GameDeckException() {
        super();
    }

    /**
     * Constructs a new EmptyDeckException with the specified detail message.
     *
     * @param message the detail message
     */
    public GameDeckException(String message) {
        super(message);
    }

    /**
     * Constructs a new EmptyDeckException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public GameDeckException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new EmptyDeckException with the specified cause.
     *
     * @param cause the cause
     */
    public GameDeckException(Throwable cause) {
        super(cause);
    }

    /**
     * Validates that the specified deck is not empty.
     * <p>
     * If the deck size is zero, this method throws {@link GameDeckException}.
     *
     * @param deck the game deck to validate (must not be null)
     * @throws GameDeckException if the deck is empty
     */
    public static void validateDeckSize(GameDeck<? extends PlayingCard> deck) {
        if (deck.isEmpty()) {
            throw new GameDeckException(DECK_IS_EMPTY);
        }
    }

    /**
     * Validates that the specified deck contains at least the required number of cards.
     * <p>
     * If the deck is empty, this method throws {@link GameDeckException} with a default message.
     * If the deck contains fewer cards than requested, this method throws {@link GameDeckException} with a detailed message
     * indicating how many cards were requested and how many are available.
     *
     * @param deck      the game deck to validate (must not be null)
     * @param drawCount the number of cards required
     * @throws GameDeckException if the deck is empty, or contains fewer cards than {@code drawCount}
     */
    public static void validateDeckSize(GameDeck<? extends PlayingCard> deck, int drawCount) {
        int available = deck.size();
        if (available == 0) {
            throw new GameDeckException(DECK_IS_EMPTY);
        }
        if (available < drawCount) {
            throw new GameDeckException(String.format(NOT_ENOUGH_CARDS, drawCount, available));
        }
    }
}