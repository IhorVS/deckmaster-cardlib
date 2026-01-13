package ivs.game.accessories.cards.ordering;

import ivs.game.accessories.cards.core.id.format.SuitSymbol;
import ivs.game.accessories.cards.core.type.Suit;
import lombok.NonNull;
import org.apache.commons.lang3.Validate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;

/**
 * Comparator for {@link Suit} that determines suit order based on configurable weights.
 * <p>
 * You can provide a custom weight mapping to define any suit order relevant for your game.
 * If no custom mapping is specified, suit weights default to their IDs as returned by {@code Suit.getId()}.
 * <p>
 * This comparator is useful when sorting suits, comparing them, or implementing custom rules
 * that depend on suit priority.
 * <p>
 * An {@link IllegalArgumentException} is thrown if a suit is {@code null} or not present in the weight map.
 *
 * <pre>
 * Example:
 * Map&lt;Suit, Integer&gt; customWeights = new EnumMap<>(Map.of(
 *     Suit.SPADES, 1,
 *     Suit.HEARTS, 2,
 *     Suit.DIAMONDS, 3,
 *     Suit.CLUBS, 4
 * ));
 * suitList.sort(new SuitWeightComparator(customWeights));
 * </pre>
 *
 * @see Suit
 */
public class SuitWeightComparator implements Comparator<Suit> {

    private final EnumMap<Suit, Integer> suitWeights = new EnumMap<>(Suit.class);

    /**
     * Creates a SuitWeightComparator with a custom suit-to-weight mapping.
     *
     * @param suitWeights a map assigning an integer weight to each suit;
     *                    the map must contain all suits to be compared
     */
    public SuitWeightComparator(@NonNull EnumMap<Suit, Integer> suitWeights) {
        this.suitWeights.putAll(suitWeights);
    }

    /**
     * Creates a SuitWeightComparator that uses each suit's {@code getId()} as its weight.
     * <p>
     * Suits will be ordered according to their default ID values.
     */
    public SuitWeightComparator() {
        Arrays.stream(Suit.values()).forEach(suit -> suitWeights.put(suit, suit.getId()));
    }

    @Override
    public int compare(Suit suit1, Suit suit2) {
        int weight1 = getWeight(suit1);
        int weight2 = getWeight(suit2);
        return Integer.compare(weight1, weight2);
    }

    // Returns the weight assigned to the specified suit.
    private int getWeight(Suit suit) {
        if (suit == null) {
            throw new IllegalArgumentException("Null suit");
        }
        Integer weight = suitWeights.get(suit);
        if (weight == null) {
            throw new IllegalArgumentException("Unsupported suit: " + suit);
        }
        return weight;
    }

    /**
     * Creates a SuitWeightComparator using the specified suits in the given order.
     * <p>
     * Each suit in {@code order} gets its weight by position (first suit = 0, second = 1, etc.).
     * The comparator will only support the suits provided in the arguments.
     * <p>
     * This method is useful for games with custom or shortened decks,
     * where not all Suit values are in play.
     * <p>
     * Throws:
     * <ul>
     *     <li>NullPointerException if the suit list is null</li>
     *     <li>IllegalArgumentException if the suit list is empty</li>
     *     <li>IllegalArgumentException if any null suit is passed</li>
     *     <li>IllegalArgumentException if any suit appears more than once</li>
     * </ul>
     *
     * @param order the desired order of suits (from lowest to highest)
     * @return a SuitWeightComparator for the given suit order
     * @throws NullPointerException     if {@code order} is null
     * @throws IllegalArgumentException if the list is empty or contains null/duplicate suits
     */
    public static SuitWeightComparator ofOrder(@NonNull Suit... order) {
        Validate.isTrue(order.length > 0, "Suit order must not be empty");

        EnumMap<Suit, Integer> weights = new EnumMap<>(Suit.class);
        for (int i = 0; i < order.length; ++i) {
            Validate.isTrue(order[i] != null, "Null suit at position " + i);
            Validate.isTrue(!weights.containsKey(order[i]), "Duplicate suit: " + order[i]);
            weights.put(order[i], i);
        }
        return new SuitWeightComparator(weights);
    }

    /**
     * Creates a SuitWeightComparator using the specified suit symbols in the given order.
     * <p>
     * Each symbol in {@code symbols} gets its weight by position (first symbol = 0, second = 1, etc.).
     * The comparator will only support the suits represented by the symbols provided in the arguments.
     * <p>
     * This method is useful for games with custom or shortened decks,
     * where not all Suit values are in play.
     * <p>
     * Throws:
     * <ul>
     *     <li>NullPointerException if the suit symbol list is null</li>
     *     <li>IllegalArgumentException if the symbol list is empty</li>
     *     <li>IllegalArgumentException if any null symbol is passed</li>
     *     <li>IllegalArgumentException if any invalid suit symbol is passed</li>
     *     <li>IllegalArgumentException if any symbol represents a suit that appears more than once</li>
     * </ul>
     *
     * @param symbols the desired order of suit symbols (from lowest to highest)
     * @return a SuitWeightComparator for the given symbol order
     * @throws NullPointerException     if {@code symbols} is null
     * @throws IllegalArgumentException if the symbol list is empty, contains null, invalid, or duplicate suit symbols
     */
    public static SuitWeightComparator ofOrder(@NonNull String... symbols) {
        Validate.isTrue(symbols.length > 0, "Suit symbol order must not be empty");

        EnumMap<Suit, Integer> weights = new EnumMap<>(Suit.class);
        for (int i = 0; i < symbols.length; ++i) {
            String symbol = symbols[i];
            Validate.isTrue(symbol != null, "Null suit symbol at position " + i);
            Validate.isTrue(SuitSymbol.isValid(symbol), "Invalid suit symbol at position " + i + ": " + symbol);
            Suit suit = Suit.getBySymbol(symbol);
            Validate.isTrue(!weights.containsKey(suit), "Duplicate suit symbol: " + symbol);
            weights.put(suit, i);
        }
        return new SuitWeightComparator(weights);
    }

    /**
     * Creates a {@link SuitWeightComparator} with the following suit order:
     * Spades, Clubs, Diamonds, Hearts (SCDH).
     *
     * @return a SuitWeightComparator ordered as Spades, Clubs, Diamonds, Hearts
     */
    public static SuitWeightComparator ofOrderSCDH() {
        return SuitWeightComparator.ofOrder(
                Suit.SPADES,
                Suit.CLUBS,
                Suit.DIAMONDS,
                Suit.HEARTS
        );
    }

    /**
     * Creates a {@link SuitWeightComparator} with the following suit order:
     * Spades, Diamonds, Clubs, Hearts (SDCH).
     *
     * @return a SuitWeightComparator ordered as Spades, Diamonds, Clubs, Hearts
     */
    public static SuitWeightComparator ofOrderSDCH() {
        return SuitWeightComparator.ofOrder(
                Suit.SPADES,
                Suit.DIAMONDS,
                Suit.CLUBS,
                Suit.HEARTS
        );
    }
}