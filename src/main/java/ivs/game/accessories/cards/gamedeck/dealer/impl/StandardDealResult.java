package ivs.game.accessories.cards.gamedeck.dealer.impl;

import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.gamedeck.dealer.DealResult;
import ivs.game.accessories.cards.gamedeck.dealer.Recipient;
import lombok.NonNull;
import lombok.Value;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Immutable implementation of DealResult.
 *
 * @param <R> type of recipient
 */
@Value
public class StandardDealResult<R extends Recipient> implements DealResult<R> {

    Map<R, List<PlayingCard>> allocations;

    public StandardDealResult(@NonNull Map<R, List<PlayingCard>> allocations) {
        Map<R, List<PlayingCard>> copiedAllocations = new HashMap<>();

        for (Map.Entry<R, List<PlayingCard>> entry : allocations.entrySet()) {
            R recipient = Validate.notNull(entry.getKey(), "Recipient cannot be null");

            final List<PlayingCard> playingCards = entry.getValue();
            Validate.notNull(playingCards, "List of cards allocated to recipient " + recipient + " cannot be null");
            Validate.noNullElements(playingCards, "List of cards allocated to recipient " + recipient + " cannot contain null elements");

            copiedAllocations.put(recipient, List.copyOf(playingCards));
        }
        this.allocations = Map.copyOf(copiedAllocations);
    }
}