package ivs.game.accessories.cards.ordering;

import ivs.game.accessories.cards.core.type.Rank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("DataFlowIssue")
class RankWeightComparatorTest {

    @Test
    @DisplayName("Default constructor should sort ranks in natural order")
    void testSortDefaultOrder() {
        // Arrange: Reverse the order of Rank constants
        List<Rank> ranks = new ArrayList<>(Arrays.asList(Rank.values()));
        Collections.reverse(ranks);

        RankWeightComparator comparator = new RankWeightComparator();
        ranks.sort(comparator);

        List<Rank> expected = Arrays.asList(Rank.values());
        assertEquals(expected, ranks, "Default rank order should follow Rank enum order after sorting.");
    }

    @Test
    @DisplayName("Custom weights: sorts according to provided map")
    void testSortWithCustomWeights() {
        // Arrange: Custom weights, ACE (10), KING (20), QUEEN (30), JACK (40), rest as 50+
        EnumMap<Rank, Integer> customWeights = new EnumMap<>(Rank.class);
        customWeights.put(Rank.ACE, 10);
        customWeights.put(Rank.KING, 20);
        customWeights.put(Rank.QUEEN, 30);
        customWeights.put(Rank.JACK, 40);
        // For demonstration, fill the rest arbitrarily with higher values
        int weight = 50;
        for (Rank r : Rank.values()) {
            if (!customWeights.containsKey(r)) {
                customWeights.put(r, weight++);
            }
        }
        List<Rank> shuffled = new ArrayList<>(Arrays.asList(Rank.values()));
        Collections.shuffle(shuffled, new Random(123));

        RankWeightComparator comparator = new RankWeightComparator(customWeights);
        shuffled.sort(comparator);

        List<Rank> expected = new ArrayList<>(Arrays.asList(Rank.ACE, Rank.KING, Rank.QUEEN, Rank.JACK));
        for (Rank r : Rank.values()) {
            if (!expected.contains(r)) {
                expected.add(r);
            }
        }
        assertEquals(expected, shuffled, "Ranks should be sorted according to explicit weights mapping");
    }

    @Test
    @DisplayName("Throws IllegalArgumentException for missing ranks in weights map")
    void testThrowsOnMissingInWeightsMap() {
        // Arrange: Only partial map (e.g. ACE and KING)
        EnumMap<Rank, Integer> partialMap = new EnumMap<>(Rank.class);
        partialMap.put(Rank.ACE, 1);
        partialMap.put(Rank.KING, 2);
        RankWeightComparator comparator = new RankWeightComparator(partialMap);

        // This should throw for QUEEN not present in the weight map
        assertThrows(IllegalArgumentException.class,
                () -> comparator.compare(Rank.ACE, Rank.QUEEN),
                "Should throw for QUEEN not present in weights map");
    }

    @Test
    @DisplayName("Throws IllegalArgumentException if rank is null")
    void testThrowsOnNullRank() {
        // Arrange: Full map for isolation of a null case
        EnumMap<Rank, Integer> all = new EnumMap<>(Rank.class);
        int i = 1;
        for (Rank rank : Rank.values()) {
            all.put(rank, i++);
        }
        RankWeightComparator comparator = new RankWeightComparator(all);

        assertThrows(IllegalArgumentException.class,
                () -> comparator.compare(null, Rank.ACE),
                "Should throw if rank1 is null");
    }

    @Test
    @DisplayName("Constructor should throw NullPointerException if rankWeights map is null")
    void constructorShouldThrowIfMapIsNull() {
        assertThrows(NullPointerException.class,
                () -> new RankWeightComparator(null),
                "Constructor must throw if rankWeights map is null"
        );
    }

    @Test
    @DisplayName("ofOrder: Throws NullPointerException if rank list is null")
    void ofOrderThrowsIfRankListIsNull() {
        assertThrows(NullPointerException.class,
                () -> RankWeightComparator.ofOrder((Rank[]) null),
                "Should throw NullPointerException if rank list is null");
    }

    @Test
    @DisplayName("ofOrder: Throws IllegalArgumentException if rank list is empty")
    void ofOrderThrowsIfRankListIsEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> RankWeightComparator.ofOrder(new Rank[]{}),
                "Should throw IllegalArgumentException if rank list is empty");
    }

    @Test
    @DisplayName("ofOrder: Throws IllegalArgumentException if any rank is null")
    void ofOrderThrowsIfAnyRankIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> RankWeightComparator.ofOrder(Rank.ACE, null, Rank.KING),
                "Should throw IllegalArgumentException if any rank is null");
    }

    @Test
    @DisplayName("ofOrder: Throws IllegalArgumentException if any rank appears more than once")
    void ofOrderThrowsIfDuplicateRanks() {
        assertThrows(IllegalArgumentException.class,
                () -> RankWeightComparator.ofOrder(Rank.ACE, Rank.KING, Rank.ACE),
                "Should throw IllegalArgumentException if a rank appears more than once");
    }

    /**
     * Positive scenario: three ranks with custom order, input in random order.
     * They should be sorted by the given explicit order.
     */
    @Test
    @DisplayName("ofOrder: Sorts ranks according to explicit order")
    void ofOrderSortsAccordingToExplicitOrder() {
        // Given: explicit order QUEEN < NINE < ACE
        RankWeightComparator comparator = RankWeightComparator.ofOrder(
                Rank.QUEEN, Rank.NINE, Rank.ACE);

        // List in random order
        List<Rank> ranks = Arrays.asList(Rank.ACE, Rank.QUEEN, Rank.NINE);
        ranks.sort(comparator);

        // Should follow explicitOrder
        assertEquals(List.of(Rank.QUEEN, Rank.NINE, Rank.ACE), ranks,
                "Ranks must be sorted by the provided explicit order");

    }

    @Test
    @DisplayName("ofOrder(String...): Throws NullPointerException if symbol list is null")
    void ofOrderSymbolsThrowsIfListIsNull() {
        assertThrows(NullPointerException.class,
                () -> RankWeightComparator.ofOrder((String[]) null),
                "Should throw NullPointerException if symbol list is null");
    }

    @Test
    @DisplayName("ofOrder(String...): Throws IllegalArgumentException if symbol list is empty")
    void ofOrderSymbolsThrowsIfListIsEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> RankWeightComparator.ofOrder(new String[]{}),
                "Should throw IllegalArgumentException if symbol list is empty");
    }

    @Test
    @DisplayName("ofOrder(String...): Throws IllegalArgumentException if any symbol is null")
    void ofOrderSymbolsThrowsIfSymbolIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> RankWeightComparator.ofOrder("A", null, "K"),
                "Should throw IllegalArgumentException if any symbol is null");
    }

    @Test
    @DisplayName("ofOrder(String...): Throws IllegalArgumentException if any symbol is invalid")
    void ofOrderSymbolsThrowsIfSymbolIsInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> RankWeightComparator.ofOrder("A", "X", "K"),
                "Should throw IllegalArgumentException if any symbol is invalid");
    }

    @Test
    @DisplayName("ofOrder(String...): Throws IllegalArgumentException if any symbol represents a duplicate rank")
    void ofOrderSymbolsThrowsIfDuplicate() {
        assertThrows(IllegalArgumentException.class,
                () -> RankWeightComparator.ofOrder("A", "Q", "A"),
                "Should throw IllegalArgumentException if a symbol (rank) appears more than once");
    }

    /**
     * Positive scenario: three rank symbols with custom order, input in random order.
     * They should be sorted by the given explicit symbol order.
     */
    @Test
    @DisplayName("ofOrder(String...): Sorts ranks according to explicit symbol order")
    void ofOrderSymbolsSortsAccordingToExplicitOrder() {
        // Given: explicit order QUEEN < NINE < ACE ("Q", "9", "A")
        RankWeightComparator comparator = RankWeightComparator.ofOrder("Q", "9", "A");

        // List in random order
        List<Rank> ranks = Arrays.asList(
                Rank.getBySymbol("A"),
                Rank.getBySymbol("Q"),
                Rank.getBySymbol("9")
        );
        // Shuffle to ensure order is not already sorted
        Collections.shuffle(ranks, new Random(12345));
        ranks.sort(comparator);

        // Should follow explicit symbol order
        List<Rank> expected = List.of(
                Rank.getBySymbol("Q"),
                Rank.getBySymbol("9"),
                Rank.getBySymbol("A")
        );
        assertEquals(expected, ranks, "Ranks must be sorted by the provided explicit symbol order");
    }

    @Test
    @DisplayName("naturalOrder(): sorts ranks in Poker natural order (2..A)")
    void testNaturalOrder() {
        // Arrange: Explicit reversed ace-low sequence (K, Q, ..., A)
        List<Rank> ranks = new ArrayList<>(Arrays.asList(
                Rank.KING,
                Rank.QUEEN,
                Rank.JACK,
                Rank.TEN,
                Rank.NINE,
                Rank.EIGHT,
                Rank.SEVEN,
                Rank.SIX,
                Rank.FIVE,
                Rank.FOUR,
                Rank.THREE,
                Rank.TWO,
                Rank.ACE
        ));

        ranks.sort(RankWeightComparator.naturalOrder());

        List<Rank> expectedOrder = Arrays.asList(
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
                Rank.KING,
                Rank.ACE
        );

        assertEquals(expectedOrder, ranks, "naturalOrder() should sort from 2 to Ace");
    }

    @Test
    @DisplayName("aceLowOrder(): sorts ranks as in ace-low games (A..K)")
    void testAceLowOrder() {
        // Arrange: Explicit reversed ace-low sequence (K, Q, ..., A)
        List<Rank> ranks = new ArrayList<>(Arrays.asList(
                Rank.KING,
                Rank.QUEEN,
                Rank.JACK,
                Rank.TEN,
                Rank.NINE,
                Rank.EIGHT,
                Rank.SEVEN,
                Rank.SIX,
                Rank.FIVE,
                Rank.FOUR,
                Rank.THREE,
                Rank.TWO,
                Rank.ACE
        ));

        ranks.sort(RankWeightComparator.aceLowOrder());

        List<Rank> expectedOrder = Arrays.asList(
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

        assertEquals(expectedOrder, ranks, "aceLowOrder() should sort from Ace to King (Ace low)");
    }
}