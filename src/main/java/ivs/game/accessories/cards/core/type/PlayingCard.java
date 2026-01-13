package ivs.game.accessories.cards.core.type;

/**
 * Represents a playing card in a standard deck.
 * The interface is sealed and permits only two implementations:
 * {@link StandardCard} for regular cards and {@link JokerCard} for jokers.
 * <p>
 * Standard cards have a suit, rank and color derived from the suit.
 * Joker cards only have a color (black or red) and no suit or rank.
 * <p>
 */
public sealed interface PlayingCard permits StandardCard, JokerCard {

    /**
     * Returns the unique identifier of this card.
     *
     * @return the unique ID of the card
     */
    int getId();

    /**
     * Returns the text symbol representation of this card.
     * For standard cards format is "{rank}{suit}", for jokers - "R{number}".
     *
     * @return the string symbol of the card
     */
    String getSymbol();

    /**
     * Returns the rank of this card.
     *
     * @return the rank of the card or null for jokers
     */
    Rank getRank();

    /**
     * Returns the suit of this card.
     *
     * @return the suit of the card or null for jokers
     */
    Suit getSuit();

    /**
     * Returns the color of this card.
     *
     * @return the color of the card (either black or red)
     */
    Color getColor();

    /**
     * Returns whether this card is a joker.
     *
     * @return true if this is a joker card, false otherwise
     */
    boolean isJoker();
}