package ivs.game.accessories.cards.core.id.format;

import ivs.game.accessories.cards.core.id.CardId;
import lombok.NonNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Contains utility methods for converting between card IDs and their string symbol representations.
 * The card symbols follow the common poker notation where a card is represented by two characters:
 * the first character for the rank (A, 2-9, T, J, Q, K) and the second for the suit (S, C, D, H).
 *
 * <p>For example, "AS" represents the Ace of Spades, "TH" represents the Ten of Hearts.
 * This class only works with the standard playing cards (no jokers).
 */
public class CardSymbol {
    // Record to store the bijective mapping between a card ID and its symbol representation
    private record CardMapping(int id, String symbol) {
    }

    // Map for quick ID-to-symbol lookups
    // Size is set to exact card count for memory optimization
    // Load factor 1.0f since we know the exact element count
    private static final Map<Integer, CardMapping> BY_ID = new HashMap<>(CardId.CARD_COUNT, 1.0f);

    // Map for quick symbol-to-ID lookups
    // Same size optimization and load factor as BY_ID
    private static final Map<String, CardMapping> BY_SYMBOL = new HashMap<>(CardId.CARD_COUNT, 1.0f);

    /*
     * Static initialization block populates both maps.
     * For each possible card ID:
     * 1. Creates symbol representation from rank and suit
     * 2. Creates CardMapping object linking ID and symbol
     * 3. Adds entries to both maps for bidirectional lookups
     */
    static {
        for (int cardId = CardId.MIN_CARD; cardId <= CardId.MAX_CARD; cardId++) {
            String symbol = RankSymbol.format(CardId.getRankId(cardId)) +
                    SuitSymbol.format(CardId.getSuitId(cardId));
            CardMapping mapping = new CardMapping(cardId, symbol);
            BY_ID.put(cardId, mapping);
            BY_SYMBOL.put(symbol, mapping);
        }
    }

    /**
     * Converts a card ID to its string symbol representation.
     *
     * @param cardId the card ID from {@link CardId}
     * @return the string symbol representing the card (rank plus suit)
     * @throws IllegalArgumentException if the cardId is invalid
     */
    public static String format(int cardId) {
        CardMapping mapping = BY_ID.get(cardId);
        if (mapping == null) {
            throw new IllegalArgumentException(String.format("Invalid card ID: %d", cardId));
        }
        return mapping.symbol();
    }

    /**
     * Parses a card symbol into its corresponding card ID.
     *
     * @param symbol the string symbol representing the card
     * @return the corresponding card ID from {@link CardId}
     * @throws IllegalArgumentException if the symbol is invalid
     * @throws NullPointerException     if the symbol is null
     */
    public static int parse(@NonNull String symbol) {
        CardMapping mapping = BY_SYMBOL.get(symbol);
        if (mapping == null) {
            throw new IllegalArgumentException("Invalid card symbol: " + symbol);
        }
        return mapping.id();
    }

    /**
     * Converts a stream of card IDs into a string of corresponding symbols with a specified delimiter.
     * The symbols are ordered according to the order of card IDs in the stream.
     *
     * @param cardIds   stream of card IDs to convert
     * @param delimiter the string to use as a delimiter between symbols
     * @return a string containing all card symbols separated by the delimiter
     * @throws IllegalArgumentException if any card ID in the stream is invalid
     * @throws NullPointerException     if either cardIds or delimiter is null
     */
    public static String formatAll(@NonNull IntStream cardIds, @NonNull String delimiter) {
        return cardIds
                .mapToObj(CardSymbol::format)
                .collect(Collectors.joining(delimiter));
    }

    /**
     * Parses a string of card symbols into a stream of card IDs.
     * The symbols in the string should be separated by the specified delimiter.
     * Empty strings between delimiters are ignored.
     *
     * @param symbols   string containing card symbols separated by delimiter
     * @param delimiter the delimiter string used to separate symbols
     * @return stream of card IDs
     * @throws IllegalArgumentException if any symbol in the string is invalid
     * @throws NullPointerException     if symbols or delimiter is null
     */
    public static IntStream parseAll(@NonNull String symbols, @NonNull String delimiter) {
        return Arrays.stream(symbols.split(delimiter))
                .filter(s -> !s.isEmpty())
                .mapToInt(CardSymbol::parse);
    }

    /**
     * Checks if the given string is a valid card symbol.
     *
     * @param symbol the string to check
     * @return true if the string is a valid card symbol, false otherwise
     * @throws NullPointerException if the symbol is null
     */
    public static boolean isValid(@NonNull String symbol) {
        return BY_SYMBOL.containsKey(symbol);
    }
}