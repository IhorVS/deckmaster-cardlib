package ivs.game.accessories.cards.gamedeck.dealer;

import ivs.game.accessories.cards.core.type.PlayingCard;

import java.util.List;
import java.util.Map;

/**
 * Represents the result of dealing cards to recipients.
 *
 * @param <R> type of recipient
 */
public interface DealResult<R extends Recipient> {

    /**
     * Returns a map from recipient to the list of cards dealt to that recipient.
     *
     * @return map that associates each recipient with their allocated cards
     */
    Map<R, List<PlayingCard>> getAllocations();
}