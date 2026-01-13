package ivs.game.accessories.cards.cardholder;

import ivs.game.accessories.cards.core.type.JokerCard;
import ivs.game.accessories.cards.core.type.Rank;
import ivs.game.accessories.cards.core.type.StandardCard;
import ivs.game.accessories.cards.core.type.Suit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StandardCardSummaryTest {

    @Test
    @DisplayName("StandardCardSummary.of throws NullPointerException for null collection")
    void ofThrowsForNullCollection() {
        assertThrows(NullPointerException.class, () -> StandardCardSummary.of(null),
                "Should throw NullPointerException if the input collection is null");
    }

    @Test
    @DisplayName("StandardCardSummary.of throws IllegalArgumentException for collection containing null element")
    void ofThrowsForNullElementInCollection() {
        var cardsWithNull = Arrays.asList(StandardCard.ACE_SPADES, null, StandardCard.KING_HEARTS);
        assertThrows(IllegalArgumentException.class, () -> StandardCardSummary.of(cardsWithNull),
                "Should throw IllegalArgumentException if the collection contains a null element");
    }

    @Test
    @DisplayName("getCardQty(Rank, Suit) should return correct quantities for specified cards")
    void getCardQtyReturnsCorrectCountForPresentCards() {
        List<StandardCard> cards = List.of(
                StandardCard.ACE_SPADES,
                StandardCard.ACE_SPADES,
                StandardCard.TWO_HEARTS
        );
        CardSummary summary = StandardCardSummary.of(cards);

        assertEquals(2, summary.getCardQty(Rank.ACE, Suit.SPADES), "ACE of SPADES should be counted twice");
        assertEquals(1, summary.getCardQty(Rank.TWO, Suit.HEARTS), "TWO of HEARTS should be present once");
        assertEquals(0, summary.getCardQty(Rank.THREE, Suit.CLUBS), "THREE of CLUBS is not present");
        assertEquals(0, summary.getCardQty(Rank.KING, Suit.DIAMONDS), "KING of DIAMONDS is not present");
    }

    @Test
    @DisplayName("getCardQty should throw NullPointerException for null rank or suit")
    void getCardQtyThrowsForNullRankOrSuit() {
        List<StandardCard> cards = List.of(StandardCard.ACE_SPADES);
        CardSummary summary = StandardCardSummary.of(cards);

        assertThrows(NullPointerException.class, () ->
                summary.getCardQty(null, Suit.SPADES), "Should throw if rank is null");
        assertThrows(NullPointerException.class, () ->
                summary.getCardQty(Rank.ACE, null), "Should throw if suit is null");
        assertThrows(NullPointerException.class, () ->
                summary.getCardQty(null, null), "Should throw if both rank and suit are null");
    }

    @Test
    @DisplayName("getCardQty() ignores jokers: counts only standard cards in the collection")
    void getCardQtyIgnoresJokersInCount() {
        var cards = List.of(
                StandardCard.ACE_SPADES,
                StandardCard.TWO_HEARTS,
                JokerCard.JOKER_1,
                JokerCard.JOKER_4
        );
        CardSummary summary = StandardCardSummary.of(cards);

        // Only 2 standard cards, so expect 2
        assertEquals(2, summary.getCardQty());
    }

    @Test
    @DisplayName("getSuitQty should throw NullPointerException for null suit")
    void getSuitQtyThrowsForNullSuit() {
        var summary = StandardCardSummary.of(List.of());
        assertThrows(NullPointerException.class, () -> summary.getSuitQty(null));
    }

    @Test
    @DisplayName("getSuitQty counts all cards of given suit (with duplicates)")
    void getSuitQtyReturnsCountForPresentSuits() {
        var cards = List.of(
                StandardCard.TWO_SPADES,
                StandardCard.ACE_SPADES,
                StandardCard.ACE_SPADES,
                StandardCard.KING_SPADES,
                StandardCard.TWO_HEARTS,
                StandardCard.KING_HEARTS,
                JokerCard.JOKER_3
        );
        var summary = StandardCardSummary.of(cards);

        assertEquals(4, summary.getSuitQty(Suit.SPADES));
        assertEquals(2, summary.getSuitQty(Suit.HEARTS));
        assertEquals(0, summary.getSuitQty(Suit.DIAMONDS));
    }

    @Test
    @DisplayName("getRankQty should throw NullPointerException for null rank")
    void getRankQtyThrowsForNullRank() {
        var summary = StandardCardSummary.of(List.of());
        assertThrows(NullPointerException.class, () -> summary.getRankQty(null));
    }

    @Test
    @DisplayName("getRankQty counts all cards of given rank (with duplicates)")
    void getRankQtyReturnsCountForPresentRanks() {
        var cards = List.of(
                StandardCard.ACE_SPADES,
                StandardCard.ACE_SPADES,
                StandardCard.ACE_HEARTS,
                StandardCard.KING_CLUBS,
                StandardCard.KING_SPADES,
                JokerCard.JOKER_3
        );
        var summary = StandardCardSummary.of(cards);

        // ACE occurs 3 times (2 SPADES, 1 HEARTS)
        assertEquals(3, summary.getRankQty(Rank.ACE));
        // KING occurs 2 times (CLUBS, SPADES)
        assertEquals(2, summary.getRankQty(Rank.KING));
        // QUEEN occurs 0 times
        assertEquals(0, summary.getRankQty(Rank.QUEEN));
    }

    @Test
    @DisplayName("getJokerQty(JokerCard) should return correct count for each joker")
    void getJokerQtyReturnsCorrectCountForPresentJokers() {
        var cards = List.of(
                JokerCard.JOKER_1,
                JokerCard.JOKER_1,
                JokerCard.JOKER_2,
                JokerCard.JOKER_4,
                StandardCard.ACE_HEARTS
        );
        var summary = StandardCardSummary.of(cards);

        assertEquals(2, summary.getJokerQty(JokerCard.JOKER_1));
        assertEquals(1, summary.getJokerQty(JokerCard.JOKER_2));
        assertEquals(0, summary.getJokerQty(JokerCard.JOKER_3));
        assertEquals(1, summary.getJokerQty(JokerCard.JOKER_4));
    }

    @Test
    @DisplayName("getJokerQty(JokerCard) should throw NullPointerException if joker is null")
    void getJokerQtyThrowsForNullJoker() {
        var summary = StandardCardSummary.of(List.of());
        assertThrows(NullPointerException.class, () -> summary.getJokerQty(null));
    }

    @Test
    @DisplayName("getJokerQty() returns total quantity of all jokers")
    void getJokerQtyNoArgsReturnsTotalJokerCount() {
        var cards = List.of(
                StandardCard.TWO_CLUBS,
                JokerCard.JOKER_1,
                JokerCard.JOKER_2,
                JokerCard.JOKER_2
        );
        var summary = StandardCardSummary.of(cards);

        assertEquals(3, summary.getJokerQty());
    }

    @Test
    @DisplayName("getTotalQty returns correct total for standard cards and jokers")
    void getTotalQtyReturnsFullCount() {
        var cards = List.of(
                StandardCard.ACE_SPADES,
                StandardCard.TWO_HEARTS,
                JokerCard.JOKER_1,
                JokerCard.JOKER_1,
                JokerCard.JOKER_2
        );
        var summary = StandardCardSummary.of(cards);

        // 2 standard + 3 jokers = 5
        assertEquals(5, summary.getTotalQty());
    }

    @Test
    @DisplayName("getTotalQty returns 0 for an empty collection")
    void getTotalQtyReturnsZeroForEmpty() {
        var summary = StandardCardSummary.of(List.of());
        assertEquals(0, summary.getTotalQty());
    }

}