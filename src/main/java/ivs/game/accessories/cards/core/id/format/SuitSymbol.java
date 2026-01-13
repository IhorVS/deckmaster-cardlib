package ivs.game.accessories.cards.core.id.format;

import ivs.game.accessories.cards.core.id.SuitId;
import lombok.NonNull;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Contains standard single-character string symbols for playing card suits.
 * These symbols follow common poker notation where suits are represented
 * by single characters (S, C, D, H).
 *
 * <p>All constants are implemented as strings to maintain consistency and
 * avoid mixing of character and string representations.
 */
public class SuitSymbol {

    /** Symbol representing Spades suit */
    public static final String SPADES = "S";

    /** Symbol representing Clubs suit */
    public static final String CLUBS = "C";

    /** Symbol representing Diamonds suit */
    public static final String DIAMONDS = "D";

    /** Symbol representing Hearts suit */
    public static final String HEARTS = "H";

    /**
     * Converts a suit ID to its string symbol representation.
     *
     * @param suitId the suit ID from {@link SuitId}
     * @return the string symbol representing the suit
     * @throws IllegalArgumentException if the suitId is invalid
     */
    public static String format(int suitId) {
        return switch (suitId) {
            case SuitId.HEARTS -> HEARTS;
            case SuitId.DIAMONDS -> DIAMONDS;
            case SuitId.CLUBS -> CLUBS;
            case SuitId.SPADES -> SPADES;
            default -> throw new IllegalArgumentException("Invalid suit ID: " + suitId);
        };
    }

    /**
     * Parses a suit symbol into its corresponding suit ID.
     *
     * @param symbol the string symbol representing the suit
     * @return the corresponding suit ID from {@link SuitId}
     * @throws IllegalArgumentException if the symbol is invalid or null
     */
    public static int parse(@NonNull String symbol) {
        return switch (symbol) {
            case HEARTS -> SuitId.HEARTS;
            case DIAMONDS -> SuitId.DIAMONDS;
            case CLUBS -> SuitId.CLUBS;
            case SPADES -> SuitId.SPADES;
            default -> throw new IllegalArgumentException("Invalid suit symbol: " + symbol);
        };
    }

    /**
     * Converts a stream of suit IDs into a string of corresponding symbols with a specified delimiter.
     * The symbols are ordered according to the order of suit IDs in the stream.
     *
     * @param suitIds   stream of suit IDs to convert
     * @param delimiter the string to use as a delimiter between symbols
     * @return a string containing all suit symbols separated by the delimiter
     * @throws IllegalArgumentException if any suit ID in the stream is invalid
     * @throws NullPointerException     if either suitIds or delimiter is null
     */
    public static String formatAll(@NonNull IntStream suitIds, @NonNull String delimiter) {
        return suitIds
                .mapToObj(SuitSymbol::format)
                .collect(Collectors.joining(delimiter));
    }

    /**
     * Parses a string of suit symbols into a stream of suit IDs.
     * The symbols in the string should be separated by the specified delimiter.
     * Empty strings between delimiters are ignored.
     *
     * @param symbols   string containing suit symbols separated by delimiter
     * @param delimiter the delimiter string used to separate symbols
     * @return stream of suit IDs
     * @throws IllegalArgumentException if any symbol in the string is invalid
     * @throws NullPointerException     if symbols or delimiter is null
     */
    public static IntStream parseAll(@NonNull String symbols, @NonNull String delimiter) {
        return Arrays.stream(symbols.split(delimiter))
                .filter(s -> !s.isEmpty())
                .mapToInt(SuitSymbol::parse);
    }

    /**
     * Checks if the given string is a valid suit symbol.
     *
     * @param symbol the string to check
     * @return true if the string is a valid suit symbol, false otherwise
     * @throws NullPointerException if symbol is null
     */
    public static boolean isValid(@NonNull String symbol) {
        return switch (symbol) {
            case HEARTS, DIAMONDS, CLUBS, SPADES -> true;
            default -> false;
        };
    }
}