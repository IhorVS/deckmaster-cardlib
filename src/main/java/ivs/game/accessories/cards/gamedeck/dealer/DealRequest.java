package ivs.game.accessories.cards.gamedeck.dealer;

/**
 * Represents a request for card allocation.
 *
 * @param <R> type of recipient (must implement Recipient)
 */
public interface DealRequest<R extends Recipient> {

    /**
     * Returns the recipient of the allocation.
     */
    R getRecipient();

    /**
     * Returns the requested number of cards.
     */
    int getAmount();

    /**
     * If true, allocation is strict: not enough of cards is considered an error.
     */
    boolean isStrict();
}
