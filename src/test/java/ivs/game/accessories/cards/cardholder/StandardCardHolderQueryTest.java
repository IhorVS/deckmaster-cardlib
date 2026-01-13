package ivs.game.accessories.cards.cardholder;

import ivs.game.accessories.cards.core.type.JokerCard;
import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.core.type.Rank;
import ivs.game.accessories.cards.core.type.StandardCard;
import ivs.game.accessories.cards.core.type.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Focuses on methods that query or retrieve data from the cardholder without needing a specific comparator logic.
// Includes tests for finding cards, filtering by suit/rank, contains checks, iterators, streams, or conversions to collections.
@SuppressWarnings("DataFlowIssue")
@ExtendWith(MockitoExtension.class)
class StandardCardHolderQueryTest {

    public static final List<PlayingCard> CARDS = List.of(
            StandardCard.TWO_SPADES,
            StandardCard.TWO_CLUBS,
            StandardCard.TWO_DIAMONDS,
            StandardCard.TWO_HEARTS,
            JokerCard.JOKER_1
    );

    public static final List<StandardCard> CARDS_WITH_NULL = Arrays.asList(
            StandardCard.ACE_SPADES,
            null,
            StandardCard.KING_HEARTS
    );

    public static final List<PlayingCard> NO_CARDS = List.of();

    @Mock
    private Comparator<PlayingCard> cardComparator;

    private StandardCardHolder holder;

    @BeforeEach
    void setUp() {
        holder = new StandardCardHolder(cardComparator);
    }

    @Test
    @DisplayName("contains(null) should throw NullPointerException")
    void containsWithNullThrows() {
        assertThrows(NullPointerException.class, () -> holder.contains(null), "contains(null) should throw NullPointerException");
    }

    @ParameterizedTest(name = "contains({0}) == {1}")
    @MethodSource("containsCardProvider")
    @DisplayName("contains should match expected result for cards present and absent, including jokers")
    void parameterizedContainsTest(PlayingCard card, boolean expected) {
        holder.addAll(CARDS);
        assertEquals(expected, holder.contains(card), "contains(" + card + ") should be " + expected);
    }

    static Stream<Arguments> containsCardProvider() {
        return Stream.of(
                Arguments.of(StandardCard.TWO_SPADES, true),
                Arguments.of(StandardCard.TWO_CLUBS, true),
                Arguments.of(StandardCard.TWO_DIAMONDS, true),
                Arguments.of(StandardCard.TWO_HEARTS, true),
                Arguments.of(JokerCard.JOKER_1, true),
                Arguments.of(StandardCard.ACE_SPADES, false),
                Arguments.of(StandardCard.ACE_CLUBS, false),
                Arguments.of(StandardCard.ACE_DIAMONDS, false),
                Arguments.of(StandardCard.ACE_HEARTS, false),
                Arguments.of(JokerCard.JOKER_4, false)
        );
    }

    @Test
    @DisplayName("containsAll should throw NullPointerException when collection is null")
    void containsAllWithNullCollectionThrows() {
        assertThrows(
                NullPointerException.class,
                () -> holder.containsAll(null),
                "containsAll with null collection should throw NullPointerException"
        );
    }

    @Test
    @DisplayName("containsAll should throw IllegalArgumentException when collection contains a null card")
    void containsAllWithNullCardThrows() {
        assertThrows(
                IllegalArgumentException.class,
                () -> holder.containsAll(CARDS_WITH_NULL),
                "containsAll with null card should throw IllegalArgumentException"
        );
    }

    @Test
    @DisplayName("containsAll should return true when collection is empty")
    void containsAllWithEmptyCollectionReturnsTrue() {
        assertTrue(holder.containsAll(NO_CARDS), "containsAll with empty collection should return true");
    }

    @Test
    @DisplayName("containsAll returns true if all queried cards are present")
    void containsAllReturnsTrueForTwoSpecifiedCards() {
        holder.addAll(CARDS);
        List<PlayingCard> toCheck = List.of(StandardCard.TWO_CLUBS, JokerCard.JOKER_1);
        assertTrue(holder.containsAll(toCheck), "Holder should contain both TWO_CLUBS and JOKER_1");
    }

    @Test
    @DisplayName("containsAll returns false if at least one queried card is missing")
    void containsAllReturnsFalseWhenOneCardMissing() {
        holder.addAll(CARDS);
        List<PlayingCard> toCheck = List.of(StandardCard.TWO_CLUBS, StandardCard.ACE_SPADES); // ACE_SPADES not in holder
        assertFalse(holder.containsAll(toCheck), "Holder should not report containsAll true when one card is absent");
    }

    @Test
    @DisplayName("containsSuit(null) should throw NullPointerException")
    void containsSuitWithNullThrows() {
        assertThrows(NullPointerException.class, () -> holder.containsSuit(null),
                "containsSuit(null) should throw NullPointerException"
        );
    }

    @Test
    @DisplayName("should detect presence of a card with the requested suit")
    void containsSuitHeartsReturnsTrue() {
        holder.addAll(CARDS);
        assertTrue(holder.containsSuit(Suit.HEARTS), "Holder should contain at least one hearts card");
    }

    @Test
    @DisplayName("should detect absence of cards with the specified suit")
    void containsSuitHeartsReturnsFalseAfterRemove() {
        holder.addAll(CARDS);
        holder.remove(StandardCard.TWO_HEARTS);
        assertFalse(holder.containsSuit(Suit.HEARTS), "Holder should not contain any hearts card after removal");
    }

    @Test
    @DisplayName("containsRank(null) should throw NullPointerException")
    void containsRankWithNullThrows() {
        assertThrows(NullPointerException.class, () -> holder.containsRank(null),
                "containsRank(null) should throw NullPointerException"
        );
    }

    @Test
    @DisplayName("should detect presence of a cards of the specified rank")
    void containsRankAceReturnsTrue() {
        holder.addAll(CARDS);
        assertTrue(holder.containsRank(Rank.TWO), "Holder should contain at least one TWO card");
    }

    @Test
    @DisplayName("should detect absence of a cards of the specified rank")
    void containsRankAceReturnsFalseAfterRemove() {
        holder.addAll(CARDS);
        assertFalse(holder.containsRank(Rank.ACE), "Holder should not contain any ACE cards");
    }

    @Test
    @DisplayName("contains should only find the first joker and not the second")
    void containsOnlyFirstJoker() {
        holder.addAll(CARDS);
        assertTrue(holder.contains(JokerCard.JOKER_1), "Should contain the first joker");
        assertFalse(holder.contains(JokerCard.JOKER_2), "Should not contain the second joker");
    }

    @Test
    @DisplayName("countSuit(null) should throw NullPointerException")
    void countSuitWithNullThrows() {
        assertThrows(NullPointerException.class, () -> holder.countSuit(null),
                "countSuit(null) should throw NullPointerException"
        );
    }

    @Test
    @DisplayName("should return the correct count of cards for the specified suit")
    void countSuitSpadesReturnsCorrectCount() {
        holder.addAll(CARDS);
        assertEquals(1, holder.countSuit(Suit.SPADES), "Holder should contain exactly one SPADES card");
    }

    @Test
    @DisplayName("should return zero when no cards of the specified suit are present")
    void countSuitSpadesReturnsZeroAfterRemove() {
        holder.addAll(CARDS);
        holder.remove(StandardCard.TWO_SPADES);
        assertEquals(0, holder.countSuit(Suit.SPADES), "Holder should not contain any SPADES card");
    }

    @Test
    @DisplayName("countRank(null) should throw NullPointerException")
    void countRankWithNullThrows() {
        assertThrows(NullPointerException.class, () -> holder.countRank(null),
                "countRank(null) should throw NullPointerException"
        );
    }

    @Test
    @DisplayName("should return zero when no cards of the specified suit are present")
    void countRankAceReturnsCorrectCount() {
        holder.addAll(CARDS);
        assertEquals(4, holder.countRank(Rank.TWO), "Holder should contain exactly one ACE card");
    }

    @Test
    @DisplayName("should return zero when no cards of the specified rank are present")
    void countRankAceReturnsZeroAfterRemove() {
        holder.addAll(CARDS);
        assertEquals(0, holder.countRank(Rank.ACE), "Holder should not contain any ACE card after removal");
    }

    @Test
    @DisplayName("should detect presence of a joker card")
    void containsJokerReturnsTrueWhenJokerPresent() {
        holder.addAll(CARDS);

        assertTrue(holder.containsJoker(), "Holder should contain a joker card");
    }

    @Test
    @DisplayName("should detect absence of joker cards")
    void containsJokerReturnsFalseWhenNoJokerPresent() {
        // Arrange: add cards without any jokers
        holder.addAll(CARDS);
        holder.removeAll(Arrays.stream(JokerCard.values()).toList());

        // Act & Assert: containsJoker should return false when no jokers are present
        assertFalse(holder.containsJoker(), "Holder should not contain any joker cards");
    }

    @Test
    @DisplayName("countCards(null) should throw NullPointerException")
    void countCardsWithNullThrows() {
        // Act & Assert: should throw NullPointerException when the argument is null
        assertThrows(NullPointerException.class, () -> holder.countCards(null));
    }

    @Test
    @DisplayName("should return the correct count for the specified card")
    void countCardsReturnsCorrectCount() {
        // Arrange: holder with multiple copies of the same card
        holder.add(StandardCard.TWO_SPADES);
        holder.add(StandardCard.TWO_SPADES);
        holder.add(StandardCard.ACE_HEARTS);

        // Act & Assert
        assertEquals(2, holder.countCards(StandardCard.TWO_SPADES), "Holder should return correct count for specified card");
        assertEquals(1, holder.countCards(StandardCard.ACE_HEARTS), "Holder should return correct count for specified card");
        assertEquals(0, holder.countCards(StandardCard.KING_CLUBS), "Holder should return zero for absent card");
    }

    @Test
    @DisplayName("should return correct count of joker cards")
    void countJokerReturnsCorrectCount() {
        // Arrange: add standard cards and jokers
        holder.add(StandardCard.TWO_SPADES);
        holder.add(JokerCard.JOKER_1);
        holder.add(JokerCard.JOKER_2);
        holder.add(JokerCard.JOKER_1);

        // Act & Assert
        assertEquals(3, holder.countJoker(), "Holder should return correct count of joker cards");
    }

    @Test
    @DisplayName("should return zero when no joker cards are present")
    void countJokerReturnsZeroWhenNoJokerPresent() {
        // Arrange: only standard cards
        holder.add(StandardCard.ACE_SPADES);
        holder.add(StandardCard.KING_CLUBS);

        // Act & Assert
        assertEquals(0, holder.countJoker(), "Holder should return zero if there are no jokers");
    }

    @Test
    @DisplayName("stream() should return a stream of all cards in order")
    void streamReturnsCorrectStream() {
        // Arrange: add some cards
        holder.addAll(CARDS);

        // Act: collect cards from a stream
        Stream<PlayingCard> stream = holder.stream();
        assertNotNull(stream, "stream() should return a non-null stream");

        // Assert: stream should return all cards in correct order
        List<PlayingCard> cardsFromStream = stream.toList();
        assertEquals(CARDS, cardsFromStream, "stream() should return all cards in insertion order");
    }

    @Test
    @DisplayName("iterator() should iterate over all cards in order")
    void iteratorReturnsAllCardsInOrder() {
        // Arrange: add some cards
        holder.addAll(CARDS);

        // Act: collect via iterator
        List<PlayingCard> iterated = new ArrayList<>();
        Iterator<PlayingCard> it = holder.iterator();
        while (it.hasNext()) {
            iterated.add(it.next());
        }

        // Assert: iterator should return all cards in the correct order
        assertEquals(CARDS, iterated, "iterator() should yield all cards in insertion order");
    }

    @Test
    @DisplayName("iterator() should return no cards when holder is empty")
    void iteratorReturnsEmptyWhenEmpty() {
        // Act
        Iterator<PlayingCard> it = holder.iterator();

        // Assert
        assertFalse(it.hasNext(), "iterator() should have no elements when holder is empty");
    }

    @Test
    @DisplayName("getSummary should return a non-null value")
    void getSummaryReturnsNonNull() {
        assertNotNull(holder.getSummary(), "getSummary() must return non-null");
    }
}