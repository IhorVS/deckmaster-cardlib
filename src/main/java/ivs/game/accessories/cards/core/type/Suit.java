package ivs.game.accessories.cards.core.type;

import ivs.game.accessories.cards.core.id.SuitId;
import ivs.game.accessories.cards.core.id.format.SuitSymbol;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents the suit of a playing card.
 * In a standard deck, there are four suits: spades (♠), clubs (♣), diamonds (♦) and hearts (♥).
 */
@Getter
public enum Suit {
    SPADES(SuitId.SPADES),   // ♠
    CLUBS(SuitId.CLUBS),     // ♣
    DIAMONDS(SuitId.DIAMONDS),// ♦
    HEARTS(SuitId.HEARTS);   // ♥

    private final int id;
    private final Color color;

    /**
     * Creates a new Suit enum constant with the specified ID.
     * Color is automatically determined based on the suit ID.
     *
     * @param id the suit ID from {@link SuitId}
     */
    Suit(int id) {
        this.id = id;
        this.color = Color.getById(SuitId.getColorId(id));
    }

    /**
     * Returns the Suit enum constant associated with the given suit ID.
     *
     * @param suitId the suit ID to look up
     * @return the Suit constant for the given ID
     * @throws IllegalArgumentException if the suit ID is invalid
     */
    public static Suit getById(int suitId) {
        return switch (suitId) {
            case SuitId.SPADES -> SPADES;
            case SuitId.CLUBS -> CLUBS;
            case SuitId.DIAMONDS -> DIAMONDS;
            case SuitId.HEARTS -> HEARTS;
            default -> throw new IllegalArgumentException("Invalid suit ID: " + suitId);
        };
    }

    /**
     * Returns the Suit enum constant associated with the given suit symbol.
     *
     * @param symbol the suit symbol to look up (S, C, D, H)
     * @return the Suit constant for the given symbol
     * @throws IllegalArgumentException if the symbol is invalid
     * @throws NullPointerException     if the symbol is null
     */
    public static Suit getBySymbol(@NonNull String symbol) {
        return getById(SuitSymbol.parse(symbol));
    }
}