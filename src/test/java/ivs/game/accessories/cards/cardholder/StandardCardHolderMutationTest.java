// Contains tests for operations that mutate the cardholder, such as adding,
// removing, clearing cards, and checking size or emptiness.
// These tests ensure the holder updates its state correctly when modified.
package ivs.game.accessories.cards.cardholder;

import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.core.type.StandardCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("DataFlowIssue")
@ExtendWith(MockitoExtension.class)
class StandardCardHolderMutationTest {

    public static final List<StandardCard> CARDS = List.of(
            StandardCard.ACE_SPADES,
            StandardCard.KING_HEARTS,
            StandardCard.JACK_CLUBS
    );

    public static final List<StandardCard> CARDS_WITH_NULL = Arrays.asList(
            StandardCard.ACE_SPADES,
            null,
            StandardCard.KING_HEARTS
    );

    @Mock
    private Comparator<PlayingCard> cardComparator;

    private StandardCardHolder holder;

    @BeforeEach
    void setUp() {
        holder = new StandardCardHolder(cardComparator);
    }

    @Test
    @DisplayName("add should throw NullPointerException when adding null")
    void addNullCardThrows() {
        assertThrows(NullPointerException.class, () -> holder.add(null), "add(null) should throw NullPointerException");
    }

    @Test
    @DisplayName("add should add a single StandardCard to an empty holder")
    void addSingleCardToEmptyHolder() {
        assertEquals(0, holder.size(), "Initial holder size should be 0");

        assertTrue(holder.add(StandardCard.ACE_SPADES), "add should return true when adding to empty holder");

        assertEquals(1, holder.size(), "Holder size should be 1 after adding a card");
    }

    @Test
    @DisplayName("add should add multiple different cards instances sequentially")
    void addMultipleDifferentCardsSequentially() {
        assertEquals(0, holder.size(), "Initial holder size should be 0");

        assertTrue(holder.add(StandardCard.KING_HEARTS), "add should return true when adding KING_HEARTS");
        assertEquals(1, holder.size(), "Holder size should be 1 after adding 1st card");

        assertTrue(holder.add(StandardCard.QUEEN_CLUBS), "add should return true when adding QUEEN_CLUBS");
        assertEquals(2, holder.size(), "Holder size should be 2 after adding 2nd card");

        assertTrue(holder.add(StandardCard.ACE_SPADES), "add should return true when adding ACE_SPADES");
        assertEquals(3, holder.size(), "Holder size should be 3 after adding 3d card");
    }

    @Test
    @DisplayName("add should allow adding duplicate card instances sequentially")
    void addDuplicateCardsSequentially() {
        assertEquals(0, holder.size(), "Initial holder size should be 0");

        assertTrue(holder.add(StandardCard.QUEEN_CLUBS), "add should return true when adding QUEEN_CLUBS");
        assertEquals(1, holder.size(), "Holder size should be 1 after adding 1st card");

        assertTrue(holder.add(StandardCard.KING_HEARTS), "add should return true when adding KING_HEARTS the first time");
        assertEquals(2, holder.size(), "Holder size should be 2 after adding 2nd card");

        assertTrue(holder.add(StandardCard.KING_HEARTS), "add should return true when adding KING_HEARTS the second time");
        assertEquals(3, holder.size(), "Holder size should be 3 after adding third card");
    }

    @Test
    @DisplayName("addAll should throw NullPointerException when adding null collection")
    void addAllNullCollectionThrows() {
        assertThrows(NullPointerException.class, () -> holder.addAll(null), "addAll(null) should throw NullPointerException");
    }

    @Test
    @DisplayName("addAll should throw IllegalArgumentException when collection contains a null card")
    void addAllWithNullCardThrows() {
        assertThrows(IllegalArgumentException.class, () -> holder.addAll(CARDS_WITH_NULL));
    }

    @Test
    @DisplayName("addAll should add multiple cards to empty holder")
    void addAllMultipleCardsToEmptyHolder() {
        assertEquals(0, holder.size(), "Initial holder size should be 0");
        assertTrue(holder.addAll(CARDS), "addAll should return true when adding to empty holder");
        assertEquals(3, holder.size(), "Holder size should reflect number of added cards");
    }

    @Test
    @DisplayName("addAll should allow adding duplicate cards")
    void addAllAllowsDuplicates() {
        assertEquals(0, holder.size(), "Initial holder size should be 0");

        var cardsToAdd = List.of(StandardCard.ACE_SPADES, StandardCard.ACE_SPADES, StandardCard.KING_HEARTS);
        assertTrue(holder.addAll(cardsToAdd), "addAll should return true when adding duplicates");
        assertTrue(holder.addAll(cardsToAdd), "addAll should return true when adding duplicates");

        assertEquals(6, holder.size(), "Holder size should count duplicates");
    }

    @Test
    @DisplayName("remove should throw NullPointerException when removing null card")
    void removeNullCardThrows() {
        assertThrows(NullPointerException.class, () -> holder.remove(null), "remove(null) should throw NullPointerException");
    }

    @Test
    @DisplayName("remove should return false when trying to remove a card from an empty holder")
    void removeSingleCardFromEmptyHolder() {
        assertEquals(0, holder.size(), "Initial holder size should be 0");

        assertFalse(holder.remove(StandardCard.KING_HEARTS), "remove should return false when removing from empty holder");

        assertEquals(0, holder.size(), "Holder size should remain 0 after remove attempt");
    }

    @Test
    @DisplayName("remove should return true and make holder empty when removing only existing card")
    void removeSingleCardPresent() {
        assertTrue(holder.add(StandardCard.KING_HEARTS), "add should return true for a new card");
        assertEquals(1, holder.size(), "Holder should contain one card after adding");

        assertTrue(holder.remove(StandardCard.KING_HEARTS), "remove should return true for present card");

        assertEquals(0, holder.size(), "Holder should be empty after removing last card");
    }

    @Test
    @DisplayName("consecutive remove should correctly remove multiple cards and update size")
    void removeMultipleCardsSequentially() {
        holder.add(StandardCard.KING_HEARTS);
        holder.add(StandardCard.QUEEN_DIAMONDS);
        holder.add(StandardCard.JACK_CLUBS);
        assertEquals(3, holder.size(), "Holder should contain three cards after adding");

        assertTrue(holder.remove(StandardCard.QUEEN_DIAMONDS), "QUEEN_DIAMONDS should be removed");
        assertEquals(2, holder.size(), "Holder size should decrease after first removal");

        assertTrue(holder.remove(StandardCard.JACK_CLUBS), "JACK_CLUBS should be removed");
        assertEquals(1, holder.size(), "Holder size should decrease after second removal");

        assertTrue(holder.remove(StandardCard.KING_HEARTS), "KING_HEARTS should be removed");
        assertEquals(0, holder.size(), "Holder size should be zero after last removal");
    }

    @Test
    @DisplayName("removeAll should throw NullPointerException when cards collection is null")
    void removeAllNullCollectionThrows() {
        assertThrows(NullPointerException.class, () -> holder.removeAll(null), "removeAll(null) should throw NullPointerException");
    }

    @Test
    @DisplayName("removeAll should throw IllegalArgumentException when cards collection contains a null card")
    void removeAllWithNullCardThrows() {
        assertThrows(IllegalArgumentException.class, () -> holder.removeAll(CARDS_WITH_NULL), "removeAll should throw IllegalArgumentException when collection contains null card");
    }

    @Test
    @DisplayName("removeAll on empty holder with CARDS should return false and size should remain zero")
    void removeAllCardsFromEmptyHolder() {
        assertEquals(0, holder.size(), "Holder should initially be empty");

        assertFalse(holder.removeAll(CARDS), "removeAll should return false when removing from empty holder");

        assertEquals(0, holder.size(), "Holder size should remain 0 after removeAll on empty holder");
    }

    @Test
    @DisplayName("removeAll should return false when none of the holder's cards removed")
    void removeAllWithNonOverlappingCards() {
        // Add cards to the holder that are not present in CARDS
        holder.add(StandardCard.NINE_DIAMONDS);
        holder.add(StandardCard.TEN_CLUBS);

        int initialSize = holder.size();
        assertFalse(holder.removeAll(CARDS), "removeAll should return false when none of the cards match");
        assertEquals(initialSize, holder.size(), "Holder size should not change when removing non-overlapping cards");
    }

    @Test
    @DisplayName("removeAll should remove only the matching cards")
    void removeAllWithPartialOverlap() {
        // Add cards: two from CARDS, one not in CARDS
        holder.add(StandardCard.ACE_SPADES); // in CARDS
        holder.add(StandardCard.KING_HEARTS); // in CARDS
        holder.add(StandardCard.NINE_DIAMONDS); // not in CARDS

        int initialSize = holder.size();

        assertTrue(holder.removeAll(CARDS), "removeAll should return true when some cards match");
        assertEquals(initialSize - 2, holder.size(), "Holder size should be reduced by number of removed cards");
        assertFalse(holder.contains(StandardCard.ACE_SPADES), "ACE_SPADES should be removed");
        assertFalse(holder.contains(StandardCard.KING_HEARTS), "KING_HEARTS should be removed");
        assertTrue(holder.contains(StandardCard.NINE_DIAMONDS), "NINE_DIAMONDS should remain");
    }

    @Test
    @DisplayName("removeAll should remove all cards when holder content exactly matches the package")
    void removeAllWithExactMatch() {
        // Fill the holder with exactly the cards from CARDS
        holder.addAll(CARDS);

        assertTrue(holder.removeAll(CARDS), "removeAll should return true for exact match");
        assertEquals(0, holder.size(), "Holder should be empty after removing all cards");
    }

    @Test
    @DisplayName("removeAll should remove all duplicates of a card even when only one is specified in the package")
    void removeAllRemovesAllDuplicates() {
        // Add the same card multiple times
        holder.add(StandardCard.ACE_SPADES);
        holder.add(StandardCard.KING_HEARTS);
        holder.add(StandardCard.ACE_SPADES);
        holder.add(StandardCard.ACE_SPADES);

        // Create a package with only one occurrence of ACE_SPADES
        List<PlayingCard> packageToRemove = List.of(StandardCard.ACE_SPADES);

        assertTrue(holder.removeAll(packageToRemove), "removeAll should return true if any cards are removed");
        assertEquals(1, holder.size(), "All duplicates of ACE_SPADES should be removed");
        assertFalse(holder.contains(StandardCard.ACE_SPADES), "No ACE_SPADES should remain");
        assertTrue(holder.contains(StandardCard.KING_HEARTS), "KING_HEARTS card should remain");
    }

    @Test
    @DisplayName("removeAll should return false and not modify holder when given an empty collection")
    void removeAllWithEmptyCollection() {
        holder.addAll(CARDS); // add some cards to the holder
        int originalSize = holder.size();

        boolean result = holder.removeAll(List.of());

        assertFalse(result, "removeAll with empty collection should return false");
        assertEquals(originalSize, holder.size(), "Holder size should not change after removeAll with empty collection");
    }

    @Test
    @DisplayName("clear should remove all cards and holder should be empty afterwards")
    void clearRemovesAllCards() {
        holder.addAll(CARDS);
        assertFalse(holder.isEmpty(), "Holder should not be empty after adding cards");
        assertEquals(3, holder.size(), "Holder size should be 3 after adding cards");

        holder.clear();

        assertTrue(holder.isEmpty(), "Holder should be empty after clear");
        assertEquals(0, holder.size(), "Holder size should be zero after clear");
    }

    @Test
    @DisplayName("clear should not fail on empty holder and holder should remain empty")
    void clearOnEmptyHolder() {
        assertTrue(holder.isEmpty(), "Holder should be empty initially");
        assertEquals(0, holder.size(), "Holder size should be zero before clear on empty holder");

        holder.clear();

        assertTrue(holder.isEmpty(), "Holder should still be empty after clear");
        assertEquals(0, holder.size(), "Holder size should be zero after clear on empty holder");
    }
}
