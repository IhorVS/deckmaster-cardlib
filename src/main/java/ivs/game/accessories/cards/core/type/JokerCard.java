package ivs.game.accessories.cards.core.type;

import ivs.game.accessories.cards.core.id.JokerId;
import ivs.game.accessories.cards.core.id.format.JokerSymbol;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents joker cards in a standard deck.
 * There are four jokers in total: two black (IDs 52, 54) and two red (IDs 53, 55).
 * Jokers do not have suits or ranks.
 */
@Getter
public enum JokerCard implements PlayingCard {

    JOKER_1(JokerId.JOKER_1),
    JOKER_2(JokerId.JOKER_2),
    JOKER_3(JokerId.JOKER_3),
    JOKER_4(JokerId.JOKER_4);

    private final int id;
    private final String symbol;
    private final Color color;
    private final boolean joker = true;  // Always returns true as this is a joker card.

    /**
     * Creates a new joker card with the specified ID.
     * Color and symbol are automatically determined from the ID.
     *
     * @param id the joker ID from {@link JokerId}
     */
    JokerCard(int id) {
        this.id = id;
        this.symbol = JokerSymbol.format(id);
        this.color = Color.getById(JokerId.getColorId(id));
    }

    /**
     * Always throws UnsupportedOperationException as jokers do not have ranks.
     *
     * @throws UnsupportedOperationException jokers don't have ranks
     */
    @Override
    public Rank getRank() {
        throw new UnsupportedOperationException("Jokers don't have ranks");
    }

    /**
     * Always throws UnsupportedOperationException as jokers do not have suits.
     *
     * @throws UnsupportedOperationException jokers don't have suits
     */
    @Override
    public Suit getSuit() {
        throw new UnsupportedOperationException("Jokers don't have suits");
    }

    /**
     * Returns the JokerCard enum constant associated with the given joker ID.
     *
     * @param jokerId the joker ID to look up
     * @return the JokerCard constant for the given ID
     * @throws IllegalArgumentException if the joker ID is invalid
     */
    public static JokerCard getById(int jokerId) {
        return switch (jokerId) {
            case JokerId.JOKER_1 -> JOKER_1;
            case JokerId.JOKER_2 -> JOKER_2;
            case JokerId.JOKER_3 -> JOKER_3;
            case JokerId.JOKER_4 -> JOKER_4;
            default -> throw new IllegalArgumentException("Invalid joker ID: " + jokerId);
        };
    }

    /**
     * Returns the JokerCard enum constant associated with the given joker symbol.
     * The symbol should follow the "R1" to "R4" notation where "R" stands for jokeR.
     * For example, "R1" represents the first joker, "R2" represents the second joker.
     *
     * @param symbol the string symbol representing the joker (e.g., "R1")
     * @return the JokerCard constant for the given symbol
     * @throws IllegalArgumentException if the symbol is invalid
     * @throws NullPointerException     if the symbol is null
     */
    public static JokerCard getBySymbol(@NonNull String symbol) {
        int jokerId = JokerSymbol.parse(symbol);
        return getById(jokerId);
    }
}