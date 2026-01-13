package ivs.game.accessories.cards.core.type;

import ivs.game.accessories.cards.core.id.CardId;
import ivs.game.accessories.cards.core.id.format.CardSymbol;
import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Represents standard playing cards in a deck (non-jokers).
 */
@Getter
public enum StandardCard implements PlayingCard {

    TWO_SPADES(Rank.TWO, Suit.SPADES),
    THREE_SPADES(Rank.THREE, Suit.SPADES),
    FOUR_SPADES(Rank.FOUR, Suit.SPADES),
    FIVE_SPADES(Rank.FIVE, Suit.SPADES),
    SIX_SPADES(Rank.SIX, Suit.SPADES),
    SEVEN_SPADES(Rank.SEVEN, Suit.SPADES),
    EIGHT_SPADES(Rank.EIGHT, Suit.SPADES),
    NINE_SPADES(Rank.NINE, Suit.SPADES),
    TEN_SPADES(Rank.TEN, Suit.SPADES),
    JACK_SPADES(Rank.JACK, Suit.SPADES),
    QUEEN_SPADES(Rank.QUEEN, Suit.SPADES),
    KING_SPADES(Rank.KING, Suit.SPADES),
    ACE_SPADES(Rank.ACE, Suit.SPADES),

    TWO_CLUBS(Rank.TWO, Suit.CLUBS),
    THREE_CLUBS(Rank.THREE, Suit.CLUBS),
    FOUR_CLUBS(Rank.FOUR, Suit.CLUBS),
    FIVE_CLUBS(Rank.FIVE, Suit.CLUBS),
    SIX_CLUBS(Rank.SIX, Suit.CLUBS),
    SEVEN_CLUBS(Rank.SEVEN, Suit.CLUBS),
    EIGHT_CLUBS(Rank.EIGHT, Suit.CLUBS),
    NINE_CLUBS(Rank.NINE, Suit.CLUBS),
    TEN_CLUBS(Rank.TEN, Suit.CLUBS),
    JACK_CLUBS(Rank.JACK, Suit.CLUBS),
    QUEEN_CLUBS(Rank.QUEEN, Suit.CLUBS),
    KING_CLUBS(Rank.KING, Suit.CLUBS),
    ACE_CLUBS(Rank.ACE, Suit.CLUBS),

    TWO_DIAMONDS(Rank.TWO, Suit.DIAMONDS),
    THREE_DIAMONDS(Rank.THREE, Suit.DIAMONDS),
    FOUR_DIAMONDS(Rank.FOUR, Suit.DIAMONDS),
    FIVE_DIAMONDS(Rank.FIVE, Suit.DIAMONDS),
    SIX_DIAMONDS(Rank.SIX, Suit.DIAMONDS),
    SEVEN_DIAMONDS(Rank.SEVEN, Suit.DIAMONDS),
    EIGHT_DIAMONDS(Rank.EIGHT, Suit.DIAMONDS),
    NINE_DIAMONDS(Rank.NINE, Suit.DIAMONDS),
    TEN_DIAMONDS(Rank.TEN, Suit.DIAMONDS),
    JACK_DIAMONDS(Rank.JACK, Suit.DIAMONDS),
    QUEEN_DIAMONDS(Rank.QUEEN, Suit.DIAMONDS),
    KING_DIAMONDS(Rank.KING, Suit.DIAMONDS),
    ACE_DIAMONDS(Rank.ACE, Suit.DIAMONDS),

    TWO_HEARTS(Rank.TWO, Suit.HEARTS),
    THREE_HEARTS(Rank.THREE, Suit.HEARTS),
    FOUR_HEARTS(Rank.FOUR, Suit.HEARTS),
    FIVE_HEARTS(Rank.FIVE, Suit.HEARTS),
    SIX_HEARTS(Rank.SIX, Suit.HEARTS),
    SEVEN_HEARTS(Rank.SEVEN, Suit.HEARTS),
    EIGHT_HEARTS(Rank.EIGHT, Suit.HEARTS),
    NINE_HEARTS(Rank.NINE, Suit.HEARTS),
    TEN_HEARTS(Rank.TEN, Suit.HEARTS),
    JACK_HEARTS(Rank.JACK, Suit.HEARTS),
    QUEEN_HEARTS(Rank.QUEEN, Suit.HEARTS),
    KING_HEARTS(Rank.KING, Suit.HEARTS),
    ACE_HEARTS(Rank.ACE, Suit.HEARTS);

    private static final StandardCard[] CARDS = StandardCard.values();

    private final int id;
    private final String symbol;
    private final Rank rank;
    private final Suit suit;
    private final Color color;
    private final boolean joker = false;

    /**
     * Creates a new standard card with the specified rank and suit.
     * Card ID, symbol and color are automatically determined.
     *
     * @param rank the rank of the card
     * @param suit the suit of the card
     */
    StandardCard(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.id = CardId.getCardId(rank.getId(), suit.getId());
        this.symbol = CardSymbol.format(id);
        this.color = suit.getColor();
    }

    /**
     * Returns the StandardCard enum constant associated with the given card ID.
     * Uses direct array indexing for optimal performance.
     *
     * @param cardId the card ID to look up
     * @return the StandardCard constant for the given ID
     * @throws IllegalArgumentException if the card ID is invalid
     */
    public static StandardCard getById(int cardId) {
        CardId.validate(cardId);
        return CARDS[cardId];
    }

    /**
     * Returns the StandardCard enum constant associated with the given rank and suit.
     * Uses the static CARDS array and CardId utility for optimal performance.
     *
     * @param rank the rank of the card
     * @param suit the suit of the card
     * @return the StandardCard constant for the given rank and suit combination
     * @throws IllegalArgumentException if either rank or suit is invalid
     * @throws NullPointerException     if either rank or suit is null
     */
    public static StandardCard getByRankAndSuit(@NonNull Rank rank, @NonNull Suit suit) {
        int cardId = CardId.getCardId(rank.getId(), suit.getId());
        return CARDS[cardId];
    }

    /**
     * Returns the StandardCard enum constant associated with the given card symbol.
     * The symbol should follow the common poker notation where a card is represented by two characters:
     * the first character for the rank (A, 2-9, T, J, Q, K) and the second for the suit (S, C, D, H).
     * For example, "AS" represents the Ace of Spades, "TH" represents the Ten of Hearts.
     *
     * @param symbol the string symbol representing the card (e.g., "AS" for Ace of Spades)
     * @return the StandardCard constant for the given symbol
     * @throws IllegalArgumentException if the symbol is invalid
     * @throws NullPointerException     if the symbol is null
     */
    public static StandardCard getBySymbol(@NonNull String symbol) {
        int cardId = CardSymbol.parse(symbol);
        return CARDS[cardId];
    }

    /**
     * Returns a stream of all StandardCard enum constants.
     *
     * @return stream of all standard cards
     */
    public static Stream<StandardCard> stream() {
        return Arrays.stream(CARDS);
    }
}