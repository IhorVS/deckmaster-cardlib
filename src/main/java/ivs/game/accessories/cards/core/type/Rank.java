package ivs.game.accessories.cards.core.type;

import ivs.game.accessories.cards.core.id.RankId;
import ivs.game.accessories.cards.core.id.format.RankSymbol;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Represents the rank of a playing card.
 * In a standard deck, there are thirteen ranks: Ace (A), 2-10, Jack (J), Queen (Q), and King (K).
 */
@Getter
@RequiredArgsConstructor
public enum Rank {
    TWO(RankId.TWO),      // 2
    THREE(RankId.THREE),  // 3  
    FOUR(RankId.FOUR),    // 4
    FIVE(RankId.FIVE),    // 5
    SIX(RankId.SIX),      // 6
    SEVEN(RankId.SEVEN),  // 7
    EIGHT(RankId.EIGHT),  // 8
    NINE(RankId.NINE),    // 9
    TEN(RankId.TEN),      // 10
    JACK(RankId.JACK),    // J
    QUEEN(RankId.QUEEN),  // Q
    KING(RankId.KING),    // K
    ACE(RankId.ACE);      // A

    private final int id;

    /**
     * Returns the Rank enum constant associated with the given rank ID.
     *
     * @param rankId the rank ID to look up
     * @return the Rank constant for the given ID
     * @throws IllegalArgumentException if the rank ID is invalid
     */
    public static Rank getById(int rankId) {
        return switch (rankId) {
            case RankId.ACE -> ACE;
            case RankId.TWO -> TWO;
            case RankId.THREE -> THREE;
            case RankId.FOUR -> FOUR;
            case RankId.FIVE -> FIVE;
            case RankId.SIX -> SIX;
            case RankId.SEVEN -> SEVEN;
            case RankId.EIGHT -> EIGHT;
            case RankId.NINE -> NINE;
            case RankId.TEN -> TEN;
            case RankId.JACK -> JACK;
            case RankId.QUEEN -> QUEEN;
            case RankId.KING -> KING;
            default -> throw new IllegalArgumentException("Invalid rank ID: " + rankId);
        };
    }

    /**
     * Returns the Rank enum constant associated with the given rank symbol.
     * Valid symbols are: "A" (Ace), "2"-"9", "T" (Ten), "J" (Jack), "Q" (Queen), "K" (King).
     *
     * @param symbol the string symbol representing the rank (e.g., "A" for Ace)
     * @return the Rank constant for the given symbol
     * @throws IllegalArgumentException if the symbol is invalid
     * @throws NullPointerException     if the symbol is null
     */
    public static Rank getBySymbol(@NonNull String symbol) {
        int rankId = RankSymbol.parse(symbol);
        return getById(rankId);
    }
}