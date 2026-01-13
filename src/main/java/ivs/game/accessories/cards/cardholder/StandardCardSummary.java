package ivs.game.accessories.cards.cardholder;

import ivs.game.accessories.cards.core.id.JokerId;
import ivs.game.accessories.cards.core.id.RankId;
import ivs.game.accessories.cards.core.id.SuitId;
import ivs.game.accessories.cards.core.type.JokerCard;
import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.core.type.Rank;
import ivs.game.accessories.cards.core.type.Suit;
import lombok.NonNull;
import org.apache.commons.lang3.Validate;

import java.util.Collection;

/**
 * Package-private implementation of {@link CardSummary} backed by arrays.
 * <p>
 * The matrix layout:
 * <ul>
 *   <li>Rows: ranks in their natural order, index is {@code rank.id}</li>
 *   <li>Columns: suits in their natural order, index is {@code suit.id}</li>
 * </ul>
 * <p>
 * Used for fast counting and aggregation of card quantities.
 */
public final class StandardCardSummary implements CardSummary {

    private final int[][] matrix;   // [rankId][suitId]
    private final int[] suits; // [suitId]
    private final int[] ranks; // [rankId]
    private final int[] jokers;

    private final int jokerTotal;
    private final int cardTotal;
    private final int total;

    /**
     * Constructs a card matrix counting all occurrences in the given collection.
     * Jokers are ignored.
     *
     * @param cards collection of cards to be analyzed (may be empty)
     */
    private StandardCardSummary(Collection<? extends PlayingCard> cards) {
        matrix = new int[RankId.RANK_COUNT][SuitId.SUIT_COUNT];
        suits = new int[SuitId.SUIT_COUNT];
        ranks = new int[RankId.RANK_COUNT];
        jokers = new int[JokerId.JOKER_COUNT];

        int jokerCount = 0;
        int cardCount = 0;

        for (PlayingCard card : cards) {
            if (card.isJoker()) {
                int jokerIndex = getJokerIndex(card);
                jokers[jokerIndex]++;
                jokerCount++;
            } else {
                Rank rank = card.getRank();
                Suit suit = card.getSuit();

                int rankId = rank.getId();
                int suitId = suit.getId();

                matrix[rankId][suitId]++;
                ranks[rankId]++;
                suits[suitId]++;
                cardCount++;
            }
        }

        jokerTotal = jokerCount;
        cardTotal = cardCount;
        total = cardCount + jokerCount;
    }

    @Override
    public int getCardQty(@NonNull Rank rank, @NonNull Suit suit) {
        return matrix[rank.getId()][suit.getId()];
    }

    @Override
    public int getCardQty() {
        return cardTotal;
    }

    @Override
    public int getSuitQty(@NonNull Suit suit) {
        return suits[suit.getId()];
    }

    @Override
    public int getRankQty(@NonNull Rank rank) {
        return ranks[rank.getId()];
    }

    @Override
    public <J extends JokerCard> int getJokerQty(@NonNull J joker) {
        return jokers[getJokerIndex(joker)] ;
    }

    @Override
    public int getJokerQty() {
        return jokerTotal;
    }

    @Override
    public int getTotalQty() {
        return total;
    }

    private static int getJokerIndex(PlayingCard card) {
        return card.getId() - JokerId.MIN_JOKER;
    }

    /**
     * Creates a new {@code CardSummary} based on the contents of the provided collection of cards.
     * <p>
     * The collection must not be {@code null} and must not contain {@code null} elements.
     *
     * @param cards the collection of playing cards to analyze
     * @return a {@code CardSummary} instance representing the card counts
     * @throws NullPointerException     if the collection is {@code null}
     * @throws IllegalArgumentException if the collection contains {@code null} elements
     */
    public static StandardCardSummary of(@NonNull Collection<? extends PlayingCard> cards) {
        Validate.noNullElements(cards, "Cards collection cannot contain null elements");
        return new StandardCardSummary(cards);
    }
}