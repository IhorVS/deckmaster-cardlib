package ivs.game.accessories.cards.gamedeck.dealer.impl;

import ivs.game.accessories.cards.gamedeck.dealer.DealRequest;
import ivs.game.accessories.cards.gamedeck.dealer.Recipient;
import lombok.NonNull;
import lombok.Value;
import org.apache.commons.lang3.Validate;

/**
 * Immutable implementation of DealRequest.
 *
 * @param <R> type of recipient
 */
@Value
public class StandardDealRequest<R extends Recipient> implements DealRequest<R> {
    R recipient;
    int amount;
    boolean strict;

    public StandardDealRequest(@NonNull R recipient, int amount, boolean strict) {
        Validate.isTrue(amount >= 0, "amount must be non-negative");

        this.recipient = recipient;
        this.amount = amount;
        this.strict = strict;
    }

    /**
     * Creates a new {@link StandardDealRequest} with {@code strict} mode enabled.
     * In strict mode, the request will require the exact amount of cards to be dealt.
     *
     * @param recipient the recipient of the cards; must not be null
     * @param amount the number of cards to be dealt (must be non-negative)
     * @param <R> the type of the recipient
     * @return a new {@code StandardDealRequest} in strict mode
     */
    public static <R extends Recipient> StandardDealRequest<R> strictOf(R recipient, int amount) {
        return new StandardDealRequest<>(recipient, amount, true);
    }

    /**
     * Creates a new {@link StandardDealRequest} with {@code strict} mode disabled (lenient mode).
     * In lenient mode, the request may allow dealing fewer cards than requested if necessary.
     *
     * @param recipient the recipient of the cards; must not be null
     * @param amount the number of cards to be dealt (must be non-negative)
     * @param <R> the type of the recipient
     * @return a new {@code StandardDealRequest} in lenient mode
     */
    public static <R extends Recipient> StandardDealRequest<R> lenientOf(R recipient, int amount) {
        return new StandardDealRequest<>(recipient, amount, false);
    }
}