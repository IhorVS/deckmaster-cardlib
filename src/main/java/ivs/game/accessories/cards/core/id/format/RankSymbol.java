package ivs.game.accessories.cards.core.id.format;

import ivs.game.accessories.cards.core.id.RankId;
import lombok.NonNull;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Contains standard single-character string symbols for playing card ranks.
 * These symbols follow common poker notation where all ranks are represented
 * by single characters (A, 2-9, T, J, Q, K).
 *
 * <p>All constants are implemented as strings to maintain consistency and
 * avoid mixing of character and string representations.
 */
public class RankSymbol {
    /** Symbol representing Ace rank */
    public static final String ACE = "A";

    /** Symbol representing Two rank */
    public static final String TWO = "2";

    /** Symbol representing Three rank */
    public static final String THREE = "3";

    /** Symbol representing Four rank */
    public static final String FOUR = "4";

    /** Symbol representing Five rank */
    public static final String FIVE = "5";

    /** Symbol representing Six rank */
    public static final String SIX = "6";

    /** Symbol representing Seven rank */
    public static final String SEVEN = "7";

    /** Symbol representing Eight rank */
    public static final String EIGHT = "8";

    /** Symbol representing Nine rank */
    public static final String NINE = "9";

    /** Symbol representing Ten rank. Uses 'T' as per standard poker notation */
    public static final String TEN = "T";

    /** Symbol representing Jack rank */
    public static final String JACK = "J";

    /** Symbol representing Queen rank */
    public static final String QUEEN = "Q";

    /** Symbol representing King rank */
    public static final String KING = "K";

    /**
     * Converts a rank ID to its string symbol representation.
     *
     * @param rankId the rank ID from {@link RankId}
     * @return the string symbol representing the rank
     * @throws IllegalArgumentException if the rankId is invalid
     */
    public static String format(int rankId) {
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
     * Parses a rank symbol into its corresponding rank ID.
     *
     * @param symbol the string symbol representing the rank
     * @return the corresponding rank ID from {@link RankId}
     * @throws IllegalArgumentException if the symbol is invalid or null
     */
    public static int parse(@NonNull String symbol) {
        return switch (symbol) {
            case ACE -> RankId.ACE;
            case TWO -> RankId.TWO;
            case THREE -> RankId.THREE;
            case FOUR -> RankId.FOUR;
            case FIVE -> RankId.FIVE;
            case SIX -> RankId.SIX;
            case SEVEN -> RankId.SEVEN;
            case EIGHT -> RankId.EIGHT;
            case NINE -> RankId.NINE;
            case TEN -> RankId.TEN;
            case JACK -> RankId.JACK;
            case QUEEN -> RankId.QUEEN;
            case KING -> RankId.KING;
            default -> throw new IllegalArgumentException("Invalid rank symbol: " + symbol);
        };
    }

    /**
     * Converts a stream of rank IDs into a string of corresponding symbols with a specified delimiter.
     * The symbols are ordered according to the order of rank IDs in the stream.
     *
     * @param rankIds   stream of rank IDs to convert
     * @param delimiter the string to use as a delimiter between symbols
     * @return a string containing all rank symbols separated by the delimiter
     * @throws IllegalArgumentException if any rank ID in the stream is invalid
     * @throws NullPointerException     if either rankIds or delimiter is null
     */
    public static String formatAll(@NonNull IntStream rankIds, @NonNull String delimiter) {
        return rankIds
                .mapToObj(RankSymbol::format)
                .collect(Collectors.joining(delimiter));
    }

    /**
     * Parses a string of rank symbols into a stream of rank IDs.
     * The symbols in the string should be separated by the specified delimiter.
     * Empty strings between delimiters are ignored.
     *
     * @param symbols   string containing rank symbols separated by delimiter
     * @param delimiter the delimiter string used to separate symbols
     * @return stream of rank IDs
     * @throws IllegalArgumentException if any symbol in the string is invalid
     * @throws NullPointerException     if symbols or delimiter is null
     */
    public static IntStream parseAll(@NonNull String symbols, @NonNull String delimiter) {
        return Arrays.stream(symbols.split(delimiter))
                .filter(s -> !s.isEmpty())
                .mapToInt(RankSymbol::parse);
    }

    /**
     * Checks if the given string is a valid rank symbol.
     *
     * @param symbol the string to check
     * @return true if the string is a valid rank symbol, false otherwise
     * @throws NullPointerException if symbol is null
     */
    public static boolean isValid(@NonNull String symbol) {
        return switch (symbol) {
            case ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,
                 TEN, JACK, QUEEN, KING -> true;
            default -> false;
        };
    }
}