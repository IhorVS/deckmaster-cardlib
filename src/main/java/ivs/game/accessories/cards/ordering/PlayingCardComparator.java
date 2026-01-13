package ivs.game.accessories.cards.ordering;

import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.core.type.Rank;
import ivs.game.accessories.cards.core.type.Suit;
import lombok.NonNull;

import java.util.Comparator;

/**
 * Comparator for {@link PlayingCard}, using supplied comparators for suit and rank.
 * <p>
 * First compares suits using {@code suitComparator}. If suits are equal,
 * compares card ranks using {@code rankComparator}.
 *
 * <pre>
 * Example usage:
 *   PlayingCardComparator cardComparator =
 *         new PlayingCardComparator(new SuitWeightComparator(), new RankWeightComparator());
 *   cards.sort(cardComparator);
 * </pre>
 *
 * @see SuitWeightComparator
 * @see RankWeightComparator
 */
public class PlayingCardComparator implements Comparator<PlayingCard> {

    private final Comparator<Suit> suitComparator;
    private final Comparator<Rank> rankComparator;

    /**
     * Constructs a card comparator from suit and rank comparators.
     *
     * @param suitComparator comparator for suits
     * @param rankComparator comparator for ranks
     */
    public PlayingCardComparator(@NonNull Comparator<Suit> suitComparator,
                                 @NonNull Comparator<Rank> rankComparator) {
        this.suitComparator = suitComparator;
        this.rankComparator = rankComparator;
    }

    @Override
    public int compare(PlayingCard card1, PlayingCard card2) {
        if (card1 == null || card2 == null) {
            throw new NullPointerException("Null card");
        }
        if (card1.isJoker() || card2.isJoker()) {
            return compareWithJoker(card1, card2);
        }
        int suitCmp = suitComparator.compare(card1.getSuit(), card2.getSuit());
        if (suitCmp != 0) return suitCmp;
        return rankComparator.compare(card1.getRank(), card2.getRank());
    }

    /**
     * This method is intended to be overridden in subclasses if you need to ordering joker cards.
     * <p>
     * By default, throws {@link UnsupportedOperationException} because joker card comparison is not supported
     * in the base implementation.
     * <p>
     * Override this method if your game requires special logic for comparing jokers (for example,
     * comparing two different jokers or comparing a joker to a standard card).
     *
     * @param card1 the first card (at least one card is expected to be a joker)
     * @param card2 the second card
     * @return the comparison result for cards involving jokers (per the {@link Comparator} contract)
     * @throws UnsupportedOperationException if joker comparison is not implemented
     */
    @SuppressWarnings("unused")
    protected int compareWithJoker(PlayingCard card1, PlayingCard card2) {
        throw new UnsupportedOperationException(
                "Comparison involving joker cards is not supported. " +
                        "If joker support is required, override this method in a subclass.");
    }
}