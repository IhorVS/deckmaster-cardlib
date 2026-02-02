package ivs.game.accessories.cards.gamedeck;

import ivs.game.accessories.cards.core.type.JokerCard;
import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.core.type.Rank;
import ivs.game.accessories.cards.core.type.StandardCard;
import lombok.NonNull;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents different types of card decks used in various card games.
 * Each deck type contains a specific range of cards.
 * <p>
 * All cards in a deck, including jokers, must be unique (no duplicates allowed).
 */
public enum DeckTemplate {

    /**
     * Double extended deck (56 cards: full 52-card deck plus 4 Jokers)
     * Used in games like Double-DeckTemplate Canasta
     */
    DOUBLE_EXTENDED(Rank.TWO, Rank.ACE,
            JokerCard.JOKER_1,
            JokerCard.JOKER_2,
            JokerCard.JOKER_3,
            JokerCard.JOKER_4
    ),

    /**
     * Extended deck (54 cards: full 52-card deck plus 2 Jokers)
     * Used in games like Canasta and some variants of Poker
     */
    EXTENDED(Rank.TWO, Rank.ACE,
            JokerCard.JOKER_1, JokerCard.JOKER_2
    ),

    /**
     * Full deck (52 cards, from Two to Ace)
     * Used in games like Poker
     */
    FULL(Rank.TWO, Rank.ACE),

    /**
     * Short deck (36 cards, from Six to Ace)
     * Used in games like Durak
     */
    SHORT(Rank.SIX, Rank.ACE),

    /**
     * Small deck (32 cards, from Seven to Ace)
     * Used in games like Preferans
     */
    SMALL(Rank.SEVEN, Rank.ACE),

    /**
     * Tiny deck (24 cards, from Nine to Ace)
     * Used in games like Thousand
     */
    TINY(Rank.NINE, Rank.ACE);

    private static final int DOUBLE_EXTENDED_SIZE = 56;
    private static final int EXTENDED_SIZE = 54;
    private static final int FULL_SIZE = 52;
    private static final int SHORT_SIZE = 36;
    private static final int SMALL_SIZE = 32;
    private static final int TINY_SIZE = 24;

    private final Rank fromRank;
    private final Rank toRank;
    private final JokerCard[] jokers;

    DeckTemplate(Rank fromRank, Rank toRank) {
        this.fromRank = fromRank;
        this.toRank = toRank;
        this.jokers = null;
    }

    DeckTemplate(Rank fromRank, Rank toRank, JokerCard... jokers) {
        this.fromRank = fromRank;
        this.toRank = toRank;
        this.jokers = jokers;
    }

    /**
     * Returns a set of cards for this deck type.
     *
     * @return set of cards in this deck
     */
    public Set<PlayingCard> get() {
        return switch (this) {
            case FULL -> get(FULL.fromRank, FULL.toRank);
            case SHORT -> get(SHORT.fromRank, SHORT.toRank);
            case SMALL -> get(SMALL.fromRank, SMALL.toRank);
            case TINY -> get(TINY.fromRank, TINY.toRank);
            case EXTENDED -> get(EXTENDED.fromRank, EXTENDED.toRank, EXTENDED.jokers);
            case DOUBLE_EXTENDED -> get(DOUBLE_EXTENDED.fromRank, DOUBLE_EXTENDED.toRank, DOUBLE_EXTENDED.jokers);
        };
    }

    /**
     * Returns a set of cards for the specified range of ranks, optionally including jokers.
     *
     * @param fromRank the lowest rank to include (inclusive)
     * @param toRank   the highest rank to include (inclusive)
     * @param jokers   optional jokers to include in the deck
     * @return set of cards for the specified range
     * @throws IllegalArgumentException if fromRank is higher than toRank
     * @throws NullPointerException     if either rank is null
     */
    public static Set<PlayingCard> get(@NonNull Rank fromRank, @NonNull Rank toRank, JokerCard... jokers) {
        Validate.isTrue(
                fromRank.getId() <= toRank.getId(),
                "From rank (%s) cannot be higher than to rank (%s)", fromRank, toRank);

        Stream<PlayingCard> cardStream = StandardCard.stream()
                .filter(card -> {
                    int rankId = card.getRank().getId();
                    return rankId >= fromRank.getId() && rankId <= toRank.getId();
                }).map(card -> card);

        if (jokers != null && jokers.length > 0) {
            Set<JokerCard> jokerSet = new java.util.HashSet<>();
            for (int i = 0; i < jokers.length; ++i) {
                Validate.isTrue(jokers[i] != null, "Null joker at position " + i);
                Validate.isTrue(jokerSet.add(jokers[i]), "Duplicate joker: " + jokers[i]);
            }
            cardStream = Stream.concat(cardStream, jokerSet.stream());
        }
        return cardStream.collect(Collectors.toSet());
    }

    /**
     * Returns the number of cards in this deck type.
     *
     * @return number of cards in this deck
     */
    public int getSize() {
        return switch (this) {
            case DOUBLE_EXTENDED -> DOUBLE_EXTENDED_SIZE;
            case EXTENDED -> EXTENDED_SIZE;
            case FULL -> FULL_SIZE;
            case SHORT -> SHORT_SIZE;
            case SMALL -> SMALL_SIZE;
            case TINY -> TINY_SIZE;
        };
    }

    public boolean containsCard(@NonNull PlayingCard card) {
        if (card.isJoker()) {
            return switch (this) {
                case FULL, SHORT, SMALL, TINY -> false;
                case DOUBLE_EXTENDED, EXTENDED -> ArrayUtils.contains(jokers, card);
            };
        }
        int cardRankId = card.getRank().getId();
        int fromRankId = this.fromRank.getId();
        int toRankId = this.toRank.getId();
        return fromRankId <= cardRankId && cardRankId <= toRankId;
    }
}