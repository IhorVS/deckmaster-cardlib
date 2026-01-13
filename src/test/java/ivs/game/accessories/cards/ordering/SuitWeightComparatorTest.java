package ivs.game.accessories.cards.ordering;

import ivs.game.accessories.cards.core.type.Suit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("DataFlowIssue")
class SuitWeightComparatorTest {

    @Test
    @DisplayName("Default constructor should sort suits in natural order")
    void testSortDefaultOrder() {
        // Arrange: Start with order HEARTS, DIAMONDS, CLUBS, SPADES
        List<Suit> suits = new ArrayList<>(List.of(Suit.HEARTS, Suit.DIAMONDS, Suit.CLUBS, Suit.SPADES
        ));

        SuitWeightComparator comparator = new SuitWeightComparator();
        suits.sort(comparator);

        // Assert: Order should be SPADES, CLUBS, DIAMONDS, HEARTS
        List<Suit> expected = List.of(Suit.SPADES, Suit.CLUBS, Suit.DIAMONDS, Suit.HEARTS
        );
        assertEquals(expected, suits,
                "Default suit order should be SPADES, CLUBS, DIAMONDS, HEARTS after sorting.");
    }

    @Test
    @DisplayName("Custom weights: sorts according to provided map")
    void testSortWithCustomWeights() {
        // Arrange
        // Let's define custom order: HEARTS (10), DIAMONDS (20), CLUBS (30), SPADES (40)
        EnumMap<Suit, Integer> customWeights = new EnumMap<>(Map.of(
                Suit.HEARTS, 10,
                Suit.DIAMONDS, 20,
                Suit.CLUBS, 30,
                Suit.SPADES, 40
        ));
        SuitWeightComparator comparator = new SuitWeightComparator(customWeights);

        List<Suit> shuffled = Arrays.asList(Suit.SPADES, Suit.CLUBS, Suit.DIAMONDS, Suit.HEARTS);
        shuffled.sort(comparator);

        // Assert: order must be [HEARTS, DIAMONDS, CLUBS, SPADES]
        List<Suit> expected = List.of(Suit.HEARTS, Suit.DIAMONDS, Suit.CLUBS, Suit.SPADES);
        assertEquals(expected, shuffled,
                "Suits should be sorted according to explicit weights mapping");
    }

    @Test
    @DisplayName("Throws IllegalArgumentException for missing suits in weights map")
    void testThrowsOnMissingInWeightsMap() {
        // Arrange: Only partial map
        EnumMap<Suit, Integer> partialMap = new EnumMap<>(Map.of(
                Suit.HEARTS, 1,
                Suit.CLUBS, 2
        ));
        SuitWeightComparator comparator = new SuitWeightComparator(partialMap);

        assertThrows(IllegalArgumentException.class,
                () -> comparator.compare(Suit.HEARTS, Suit.SPADES),
                "Should throw for SPADES not present in weights map");
    }

    @Test
    @DisplayName("Throws IllegalArgumentException if suit is null")
    void testThrowsOnNullSuit() {
        // Arrange: Map for all, to isolate a null case
        EnumMap<Suit, Integer> all = new EnumMap<>(Map.of(
                Suit.HEARTS, 1,
                Suit.DIAMONDS, 2,
                Suit.CLUBS, 3,
                Suit.SPADES, 4
        ));
        SuitWeightComparator comparator = new SuitWeightComparator(all);

        assertThrows(IllegalArgumentException.class,
                () -> comparator.compare(null, Suit.HEARTS),
                "Should throw if suit1 is null"
        );
    }

    @Test
    @DisplayName("Constructor should throw NullPointerException if suitWeights map is null")
    void constructorShouldThrowIfMapIsNull() {
        assertThrows(NullPointerException.class,
                () -> new SuitWeightComparator(null),
                "Constructor must throw if suitWeights map is null"
        );
    }

    @Test
    @DisplayName("ofOrder: Throws NullPointerException if suit list is null")
    void ofOrderThrowsIfSuitListIsNull() {
        assertThrows(NullPointerException.class,
                () -> SuitWeightComparator.ofOrder((Suit[]) null),
                "Should throw NullPointerException if suit list is null");
    }

    @Test
    @DisplayName("ofOrder: Throws IllegalArgumentException if suit list is empty")
    void ofOrderThrowsIfSuitListIsEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> SuitWeightComparator.ofOrder(new Suit[]{}),
                "Should throw IllegalArgumentException if suit list is empty");
    }

    @Test
    @DisplayName("ofOrder: Throws IllegalArgumentException if any suit is null")
    void ofOrderThrowsIfAnySuitIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> SuitWeightComparator.ofOrder(Suit.SPADES, null, Suit.HEARTS),
                "Should throw IllegalArgumentException if any suit is null");
    }

    @Test
    @DisplayName("ofOrder: Throws IllegalArgumentException if any suit appears more than once")
    void ofOrderThrowsIfDuplicateSuits() {
        assertThrows(IllegalArgumentException.class,
                () -> SuitWeightComparator.ofOrder(Suit.SPADES, Suit.HEARTS, Suit.SPADES),
                "Should throw IllegalArgumentException if a suit appears more than once");
    }

    /**
     * Positive scenario: three suits with custom order.
     * They should be sorted by the given explicit order.
     */
    @Test
    @DisplayName("ofOrder: Sorts suits according to explicit order")
    void ofOrderSortsAccordingToExplicitOrder() {
        // Given: explicit order HEARTS < DIAMONDS < CLUBS
        SuitWeightComparator comparator = SuitWeightComparator.ofOrder(
                Suit.HEARTS, Suit.DIAMONDS, Suit.CLUBS);

        // List in another order
        List<Suit> suits = new ArrayList<>(List.of(Suit.CLUBS, Suit.HEARTS, Suit.DIAMONDS));
        suits.sort(comparator);

        // Should follow explicitOrder
        assertEquals(List.of(Suit.HEARTS, Suit.DIAMONDS, Suit.CLUBS), suits,
                "Suits must be sorted by the provided explicit order");
    }

    @Test
    @DisplayName("ofOrder(String...): Throws NullPointerException if symbol list is null")
    void ofOrderSymbolsThrowsIfListIsNull() {
        assertThrows(NullPointerException.class,
                () -> SuitWeightComparator.ofOrder((String[]) null),
                "Should throw NullPointerException if symbol list is null");
    }

    @Test
    @DisplayName("ofOrder(String...): Throws IllegalArgumentException if symbol list is empty")
    void ofOrderSymbolsThrowsIfListIsEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> SuitWeightComparator.ofOrder(new String[]{}),
                "Should throw IllegalArgumentException if symbol list is empty");
    }

    @Test
    @DisplayName("ofOrder(String...): Throws IllegalArgumentException if any symbol is null")
    void ofOrderSymbolsThrowsIfSymbolIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> SuitWeightComparator.ofOrder("S", null, "H"),
                "Should throw IllegalArgumentException if any symbol is null");
    }

    @Test
    @DisplayName("ofOrder(String...): Throws IllegalArgumentException if any symbol is invalid")
    void ofOrderSymbolsThrowsIfSymbolIsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> SuitWeightComparator.ofOrder("S", "X", "C"),
                "Should throw IllegalArgumentException if any symbol is invalid");
    }

    @Test
    @DisplayName("ofOrder(String...): Throws IllegalArgumentException if any symbol represents a duplicate suit")
    void ofOrderSymbolsThrowsIfDuplicate() {
        assertThrows(IllegalArgumentException.class,
                () -> SuitWeightComparator.ofOrder("S", "H", "S"),
                "Should throw IllegalArgumentException if a symbol (suit) appears more than once");
    }

    /**
     * Positive scenario: three suit symbols with custom order.
     * They should be sorted by the given explicit symbol order.
     */
    @Test
    @DisplayName("ofOrder(String...): Sorts suits according to explicit symbol order")
    void ofOrderSymbolsSortsAccordingToExplicitOrder() {
        // Given: explicit order CLUBS < HEARTS < DIAMONDS ("C", "H", "D")
        SuitWeightComparator comparator = SuitWeightComparator.ofOrder("C", "H", "D");

        // List in another order
        List<Suit> suits = new ArrayList<>(List.of(
                Suit.getBySymbol("D"),
                Suit.getBySymbol("C"),
                Suit.getBySymbol("H")
        ));
        suits.sort(comparator);

        // Should follow explicit symbol order
        List<Suit> expected = List.of(
                Suit.getBySymbol("C"),
                Suit.getBySymbol("H"),
                Suit.getBySymbol("D")
        );
        assertEquals(expected, suits, "Suits must be sorted by the provided explicit symbol order");
    }

    @Test
    @DisplayName("ofOrderSCDH returns comparator with order: SPADES, CLUBS, DIAMONDS, HEARTS")
    void testOfOrderSCDH() {
        SuitWeightComparator comparator = SuitWeightComparator.ofOrderSCDH();
        List<Suit> suits = new ArrayList<>(List.of(Suit.HEARTS, Suit.DIAMONDS, Suit.CLUBS, Suit.SPADES));
        suits.sort(comparator);

        List<Suit> expected = List.of(Suit.SPADES, Suit.CLUBS, Suit.DIAMONDS, Suit.HEARTS);
        assertEquals(expected, suits,
                "ofOrderSCDH should sort as SPADES, CLUBS, DIAMONDS, HEARTS");
    }

    @Test
    @DisplayName("ofOrderSDCH returns comparator with order: SPADES, DIAMONDS, CLUBS, HEARTS")
    void testOfOrderSDCH() {
        SuitWeightComparator comparator = SuitWeightComparator.ofOrderSDCH();
        List<Suit> suits = new ArrayList<>(List.of(Suit.HEARTS, Suit.DIAMONDS, Suit.CLUBS, Suit.SPADES));
        suits.sort(comparator);

        List<Suit> expected = List.of(Suit.SPADES, Suit.DIAMONDS, Suit.CLUBS, Suit.HEARTS);
        assertEquals(expected, suits,
                "ofOrderSDCH should sort as SPADES, DIAMONDS, CLUBS, HEARTS");
    }
}