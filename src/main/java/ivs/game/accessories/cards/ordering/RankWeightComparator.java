package ivs.game.accessories.cards.ordering;

import ivs.game.accessories.cards.core.id.format.RankSymbol;
import ivs.game.accessories.cards.core.type.Rank;
import lombok.NonNull;
import org.apache.commons.lang3.Validate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;

/**
 * Comparator for {@link Rank} that determines rank order based on configurable weights.
 * <p>
 * You can provide a custom weight mapping to define any rank order relevant for your game.
 * If no custom mapping is specified, rank weights default to their IDs as returned by {@code Rank.getId()}.
 * <p>
 * This comparator is useful when sorting ranks, comparing them, or implementing custom rules
 * that depend on rank priority.
 * <p>
 * An {@link IllegalArgumentException} is thrown if a rank is {@code null} or not present in the weight map.
 *
 * <pre>
 * Example:
 * Map&lt;Rank, Integer&gt; customWeights = new EnumMap<>(Map.of(
 *     Rank.ACE, 1,
 *     Rank.KING, 2,
 *     Rank.QUEEN, 3,
 *     Rank.JACK, 4,
 *     // ... other ranks
 * ));
 * rankList.sort(new RankWeightComparator(customWeights));
 * </pre>
 *
 * @see Rank
 */
public class RankWeightComparator implements Comparator<Rank> {

    private final EnumMap<Rank, Integer> rankWeights = new EnumMap<>(Rank.class);

    /**
     * Creates a RankWeightComparator with a custom rank-to-weight mapping.
     *
     * @param rankWeights a map assigning an integer weight to each rank;
     *                    the map must contain all ranks to be compared
     */
    public RankWeightComparator(@NonNull EnumMap<Rank, Integer> rankWeights) {
        this.rankWeights.putAll(rankWeights);
    }

    /**
     * Creates a RankWeightComparator that uses each rank's {@code getId()} as its weight.
     * <p>
     * Ranks will be ordered according to their default ID values.
     */
    public RankWeightComparator() {
        Arrays.stream(Rank.values()).forEach(rank -> rankWeights.put(rank, rank.getId()));
    }

    @Override
    public int compare(Rank rank1, Rank rank2) {
        int weight1 = getWeight(rank1);
        int weight2 = getWeight(rank2);
        return Integer.compare(weight1, weight2);
    }

    // Returns the weight assigned to the specified rank.
    private int getWeight(Rank rank) {
        if (rank == null) {
            throw new IllegalArgumentException("Null rank");
        }
        Integer weight = rankWeights.get(rank);
        if (weight == null) {
            throw new IllegalArgumentException("Unsupported rank: " + rank);
        }
        return weight;
    }

    /**
     * Creates a RankWeightComparator using the specified ranks in the given order.
     * <p>
     * Each rank in {@code order} gets its weight by position (first rank = 0, second = 1, etc.).
     * The comparator will only support the ranks provided in the arguments.
     * <p>
     * This method is useful for games with custom or shortened decks,
     * where not all Rank values are in play (for example, games using only 6 through Ace).
     * <p>
     * Throws:
     * <ul>
     *     <li>NullPointerException if the rank list is null</li>
     *     <li>IllegalArgumentException if the rank list is empty</li>
     *     <li>IllegalArgumentException if any null rank is passed</li>
     *     <li>IllegalArgumentException if any rank appears more than once</li>
     * </ul>
     *
     * @param order the desired order of ranks (from lowest to highest)
     * @return a RankWeightComparator for the given rank order
     * @throws NullPointerException     if {@code order} is null
     * @throws IllegalArgumentException if the list is empty or contains null/duplicate ranks
     */
    public static RankWeightComparator ofOrder(@NonNull Rank... order) {
        Validate.isTrue(order.length > 0, "Rank order must not be empty");

        EnumMap<Rank, Integer> weights = new EnumMap<>(Rank.class);
        for (int i = 0; i < order.length; ++i) {
            Validate.isTrue(order[i] != null, "Null rank at position " + i);
            Validate.isTrue(!weights.containsKey(order[i]), "Duplicate rank: " + order[i]);
            weights.put(order[i], i);
        }
        return new RankWeightComparator(weights);
    }

    /**
     * Creates a RankWeightComparator using the specified rank symbols in the given order.
     * <p>
     * Each symbol in {@code symbols} gets its weight by position (first symbol = 0, second = 1, etc.).
     * The comparator will only support the ranks represented by the symbols provided in the arguments.
     * <p>
     * This method is useful for games with custom or shortened decks,
     * where not all rank values are in play (for example, games using only 6 through Ace).
     * <p>
     * Throws:
     * <ul>
     *     <li>NullPointerException if the rank symbol list is null</li>
     *     <li>IllegalArgumentException if the symbol list is empty</li>
     *     <li>IllegalArgumentException if any null symbol is passed</li>
     *     <li>IllegalArgumentException if any invalid rank symbol is passed</li>
     *     <li>IllegalArgumentException if any symbol represents a rank that appears more than once</li>
     * </ul>
     *
     * @param symbols the desired order of rank symbols (from lowest to highest)
     * @return a RankWeightComparator for the given symbol order
     * @throws NullPointerException     if {@code symbols} is null
     * @throws IllegalArgumentException if the symbol list is empty, contains null, invalid, or duplicate rank symbols
     */
    public static RankWeightComparator ofOrder(@NonNull String... symbols) {
        Validate.isTrue(symbols.length > 0, "Rank symbol order must not be empty");

        EnumMap<Rank, Integer> weights = new EnumMap<>(Rank.class);
        for (int i = 0; i < symbols.length; ++i) {
            String symbol = symbols[i];
            Validate.isTrue(symbol != null, "Null rank symbol at position " + i);
            Validate.isTrue(RankSymbol.isValid(symbol), "Invalid rank symbol at position " + i + ": " + symbol);
            Rank rank = Rank.getBySymbol(symbol);
            Validate.isTrue(!weights.containsKey(rank), "Duplicate rank symbol: " + symbol);
            weights.put(rank, i);
        }
        return new RankWeightComparator(weights);
    }

    /**
     * Creates a RankWeightComparator with the natural enum order of Rank.
     * The natural order is: "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A".
     *
     * @return RankWeightComparator with natural order
     */
    public static RankWeightComparator naturalOrder() {
        return ofOrder(Rank.values());
    }

    /**
     * Creates a RankWeightComparator with ace as the lowest rank and king as the highest.
     * The order is: "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K".
     *
     * @return RankWeightComparator with ace-low order
     */
    public static RankWeightComparator aceLowOrder() {
        return ofOrder(
            Rank.ACE,
            Rank.TWO,
            Rank.THREE,
            Rank.FOUR,
            Rank.FIVE,
            Rank.SIX,
            Rank.SEVEN,
            Rank.EIGHT,
            Rank.NINE,
            Rank.TEN,
            Rank.JACK,
            Rank.QUEEN,
            Rank.KING
        );
    }
}