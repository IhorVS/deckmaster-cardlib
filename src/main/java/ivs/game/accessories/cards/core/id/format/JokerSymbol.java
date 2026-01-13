package ivs.game.accessories.cards.core.id.format;

import ivs.game.accessories.cards.core.id.JokerId;
import lombok.NonNull;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Contains standard string symbols for joker cards.
 * These symbols follow the "R1" to "R4" notation where "R" stands for jokeR
 * to distinguish it from Jack (J) in poker notation.
 *
 * <p>All constants are implemented as strings to maintain consistency and
 * avoid mixing of character and string representations.
 */
public class JokerSymbol {

    /** Symbol representing the first joker */
    public static final String JOKER_1 = "R1";

    /** Symbol representing the second joker */
    public static final String JOKER_2 = "R2";

    /** Symbol representing the third joker */
    public static final String JOKER_3 = "R3";

    /** Symbol representing the fourth joker */
    public static final String JOKER_4 = "R4";

    /**
     * Converts a joker ID to its string symbol representation.
     *
     * @param jokerId the joker ID from {@link JokerId}
     * @return the string symbol representing the joker
     * @throws IllegalArgumentException if the jokerId is invalid
     */
    public static String format(int jokerId) {
        return switch (jokerId) {
            case JokerId.JOKER_1 -> JOKER_1;
            case JokerId.JOKER_2 -> JOKER_2;
            case JokerId.JOKER_3 -> JOKER_3;
            case JokerId.JOKER_4 -> JOKER_4;
            default -> throw new IllegalArgumentException("Invalid joker ID: " + jokerId);
        };
    }

    /**
     * Parses a joker symbol into its corresponding joker ID.
     *
     * @param symbol the string symbol representing the joker
     * @return the corresponding joker ID from {@link JokerId}
     * @throws IllegalArgumentException if the symbol is invalid or null
     */
    public static int parse(@NonNull String symbol) {
        return switch (symbol) {
            case JOKER_1 -> JokerId.JOKER_1;
            case JOKER_2 -> JokerId.JOKER_2;
            case JOKER_3 -> JokerId.JOKER_3;
            case JOKER_4 -> JokerId.JOKER_4;
            default -> throw new IllegalArgumentException("Invalid joker symbol: " + symbol);
        };
    }

    /**
     * Converts a stream of joker IDs into a string of corresponding symbols with a specified delimiter.
     * The symbols are ordered according to the order of joker IDs in the stream.
     *
     * @param jokerIds  stream of joker IDs to convert
     * @param delimiter the string to use as a delimiter between symbols
     * @return a string containing all joker symbols separated by the delimiter
     * @throws IllegalArgumentException if any joker ID in the stream is invalid
     * @throws NullPointerException     if either jokerIds or delimiter is null
     */
    public static String formatAll(@NonNull IntStream jokerIds, @NonNull String delimiter) {
        return jokerIds
                .mapToObj(JokerSymbol::format)
                .collect(Collectors.joining(delimiter));
    }

    /**
     * Parses a string of joker symbols into a stream of joker IDs.
     * The symbols in the string should be separated by the specified delimiter.
     * Empty strings between delimiters are ignored.
     *
     * @param symbols   string containing joker symbols separated by delimiter
     * @param delimiter the delimiter string used to separate symbols
     * @return stream of joker IDs
     * @throws IllegalArgumentException if any symbol in the string is invalid
     * @throws NullPointerException     if symbols or delimiter is null
     */
    public static IntStream parseAll(@NonNull String symbols, @NonNull String delimiter) {
        return Arrays.stream(symbols.split(delimiter))
                .filter(s -> !s.isEmpty())
                .mapToInt(JokerSymbol::parse);
    }

    /**
     * Checks if the given string is a valid joker symbol.
     *
     * @param symbol the string to check
     * @return true if the string is a valid joker symbol, false otherwise
     * @throws NullPointerException if symbol is null
     */
    public static boolean isValid(@NonNull String symbol) {
        return switch (symbol) {
            case JOKER_1, JOKER_2, JOKER_3, JOKER_4 -> true;
            default -> false;
        };
    }
}