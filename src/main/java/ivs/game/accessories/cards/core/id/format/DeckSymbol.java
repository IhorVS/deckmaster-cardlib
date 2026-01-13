package ivs.game.accessories.cards.core.id.format;

import ivs.game.accessories.cards.core.id.JokerId;
import lombok.NonNull;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Contains utility methods for converting between deck element IDs and their string symbol representations.
 * Handles both standard playing cards and jokers.
 *
 * <p>For cards, uses the common poker notation where a card is represented by two characters:
 * the first character for the rank (A, 2-9, T, J, Q, K) and the second for the suit (S, C, D, H).
 * For jokers, uses the notation defined in {@link JokerSymbol}.
 *
 * <p>For example: "AS" represents Ace of Spades, "TH" represents Ten of Hearts, "BJ" represents Black Joker.
 */
public class DeckSymbol {

    /**
     * Converts a deck element ID (card or joker) to its string symbol representation.
     *
     * @param id the ID of the deck element (card or joker)
     * @return the string symbol representing the element
     * @throws IllegalArgumentException if the elementId is invalid
     */
    public static String format(int id) {
        return JokerId.isValid(id)
                ? JokerSymbol.format(id)
                : CardSymbol.format(id);
    }

    /**
     * Parses a deck element symbol into its corresponding ID.
     *
     * @param symbol the string symbol representing the deck element
     * @return the corresponding element ID
     * @throws IllegalArgumentException if the symbol is invalid
     * @throws NullPointerException     if the symbol is null
     */
    public static int parse(@NonNull String symbol) {
        return JokerSymbol.isValid(symbol)
                ? JokerSymbol.parse(symbol)
                : CardSymbol.parse(symbol);
    }

    /**
     * Converts a stream of deck element IDs into a string of corresponding symbols with a specified delimiter.
     * The symbols are ordered according to the order of element IDs in the stream.
     *
     * @param ids       stream of element IDs to convert
     * @param delimiter the string to use as a delimiter between symbols
     * @return a string containing all element symbols separated by the delimiter
     * @throws IllegalArgumentException if any element ID in the stream is invalid
     * @throws NullPointerException     if either elementIds or delimiter is null
     */
    public static String formatAll(@NonNull IntStream ids, @NonNull String delimiter) {
        return ids
                .mapToObj(DeckSymbol::format)
                .collect(Collectors.joining(delimiter));
    }

    /**
     * Parses a string of deck element symbols into a stream of element IDs.
     * The symbols in the string should be separated by the specified delimiter.
     * Empty strings between delimiters are ignored.
     *
     * @param symbols   string containing element symbols separated by delimiter
     * @param delimiter the delimiter string used to separate symbols
     * @return stream of element IDs
     * @throws IllegalArgumentException if any symbol in the string is invalid
     * @throws NullPointerException     if symbols or delimiter is null
     */
    public static IntStream parseAll(@NonNull String symbols, @NonNull String delimiter) {
        return Arrays.stream(symbols.split(delimiter))
                .filter(s -> !s.isEmpty())
                .mapToInt(DeckSymbol::parse);
    }

    /**
     * Checks if the given string is a valid deck element symbol.
     *
     * @param symbol the string to check
     * @return true if the string is a valid deck element symbol, false otherwise
     * @throws NullPointerException if the symbol is null
     */
    public static boolean isValid(@NonNull String symbol) {
        return CardSymbol.isValid(symbol) || JokerSymbol.isValid(symbol);
    }
}