package ivs.game.accessories.cards.gamedeck.dealer.impl;

import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.gamedeck.GameDeck;
import ivs.game.accessories.cards.gamedeck.dealer.CardDealer;
import ivs.game.accessories.cards.gamedeck.dealer.DealRequest;
import ivs.game.accessories.cards.gamedeck.dealer.DealResult;
import ivs.game.accessories.cards.gamedeck.dealer.Recipient;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Standard implementation of CardDealer.
 * <p>
 * This class is stateless and thread-safe.
 *
 * @param <D> type of deck
 * @param <R> type of recipient
 */
public class StandardCardDealer<D extends GameDeck<? extends PlayingCard>, R extends Recipient>
        implements CardDealer<D, R> {

    @Override
    public DealResult<R> deal(@NonNull D deck, @NonNull List<DealRequest<R>> requests) {
        Map<R, List<PlayingCard>> allocations = new HashMap<>();

        for (DealRequest<R> request : requests) {
            validateCardsLeft(deck, request);
            List<PlayingCard> requestedCards = getRequestedCards(deck, request);

            List<PlayingCard> allocated = allocations.computeIfAbsent(request.getRecipient(), r -> new ArrayList<>());
            allocated.addAll(requestedCards);
        }
        return new StandardDealResult<>(allocations);
    }

    private List<PlayingCard> getRequestedCards(D deck, DealRequest<R> request) {
        List<PlayingCard> dealt = new ArrayList<>();
        while (dealt.size() < request.getAmount() && !deck.isEmpty()) {
            dealt.add(deck.draw());
        }
        return dealt;
    }

    private void validateCardsLeft(D deck, DealRequest<R> request) {
        int requestAmount = request.getAmount();
        int cardsLeft = deck.size();
        if (request.isStrict() && requestAmount > cardsLeft) {
            throw new IllegalStateException(
                    "Not enough cards for recipient " + request.getRecipient() +
                            " (requested: " + requestAmount + ", cards left: " + cardsLeft + ")"
            );
        }
    }
}