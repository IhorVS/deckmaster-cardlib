package ivs.game.accessories.cards.ordering;

import ivs.game.accessories.cards.core.type.JokerCard;
import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.core.type.Rank;
import ivs.game.accessories.cards.core.type.StandardCard;
import ivs.game.accessories.cards.core.type.Suit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Comparator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for PlayingCardComparator constructor null argument validation.
 */
@DisplayName("PlayingCardComparator constructor")
class PlayingCardComparatorTest {

    @Nested
    @DisplayName("should throw NullPointerException")
    class NullArgumentValidation {

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("when suitComparator is null")
        void shouldThrowWhenSuitComparatorIsNull() {
            Comparator<Rank> rankComparator = (r1, r2) -> 0;
            assertThrows(NullPointerException.class, () -> new PlayingCardComparator(null, rankComparator));
        }

        @SuppressWarnings("DataFlowIssue")
        @Test
        @DisplayName("when rankComparator is null")
        void shouldThrowWhenRankComparatorIsNull() {
            Comparator<Suit> suitComparator = (s1, s2) -> 0;
            assertThrows(NullPointerException.class, () -> new PlayingCardComparator(suitComparator, null));
        }

        @Test
        @DisplayName("compareWithJoker should throw UnsupportedOperationException by default")
        void compareWithJokerThrowsException() {
            // Suppose PlayingCardComparator is your class containing the method under test.
            PlayingCardComparator comparator = new PlayingCardComparator((s1, s2) -> 0, (r1, r2) -> 0) {
            };
            assertThrows(
                    UnsupportedOperationException.class,
                    () -> comparator.compareWithJoker(null, null),
                    "Joker cards are not supported by this comparator"
            );
        }

        @ParameterizedTest
        @MethodSource("jokerCombinations")
        @DisplayName("ordering() should throw UnsupportedOperationException when at least one card is a joker")
        void compareWithJokerThrows(PlayingCard card1, PlayingCard card2) {
            PlayingCardComparator comparator = new PlayingCardComparator((s1, s2) -> 0, (r1, r2) -> 0) {
            };
            assertThrows(
                    UnsupportedOperationException.class,
                    () -> comparator.compare(card1, card2)
            );
        }

        private static Stream<Arguments> jokerCombinations() {
            return Stream.of(
                    Arguments.of(JokerCard.JOKER_1, StandardCard.ACE_SPADES),
                    Arguments.of(StandardCard.ACE_SPADES, JokerCard.JOKER_1),
                    Arguments.of(JokerCard.JOKER_1, JokerCard.JOKER_2)
            );
        }

        @ParameterizedTest
        @MethodSource("nullCardCombinations")
        @DisplayName("ordering() should throw NullPointerException when one or both cards are null")
        void compareWithNullCardThrows(PlayingCard card1, PlayingCard card2) {
            PlayingCardComparator comparator = new PlayingCardComparator((s1, s2) -> 0, (r1, r2) -> 0) {
            };
            assertThrows(
                    NullPointerException.class,
                    () -> comparator.compare(card1, card2)
            );
        }

        private static Stream<Arguments> nullCardCombinations() {
            return Stream.of(
                    Arguments.of(null, StandardCard.ACE_SPADES),
                    Arguments.of(StandardCard.ACE_SPADES, null),
                    Arguments.of(null, null)
            );
        }

        @ParameterizedTest
        @MethodSource("standardCardPairs")
        @DisplayName("ordering() correctly compares pairs of standard cards")
        void compareStandardCards(PlayingCard card1, PlayingCard card2, int expected) {
            Comparator<Suit> suitComparator = new SuitWeightComparator();
            Comparator<Rank> rankComparator = new RankWeightComparator();

            PlayingCardComparator comparator = new PlayingCardComparator(suitComparator, rankComparator) {
            };
            int result = comparator.compare(card1, card2);

            // expected: -1, 0, 1
            assertEquals(expected, Integer.signum(result),
                    "Comparison should match expected sign: " + expected);
        }

        private static Stream<Arguments> standardCardPairs() {
            return Stream.of(
                    Arguments.of(StandardCard.ACE_CLUBS, StandardCard.ACE_DIAMONDS, -1),
                    Arguments.of(StandardCard.TEN_HEARTS, StandardCard.KING_HEARTS, -1),
                    Arguments.of(StandardCard.QUEEN_SPADES, StandardCard.QUEEN_SPADES, 0),
                    Arguments.of(StandardCard.KING_SPADES, StandardCard.QUEEN_SPADES, 1)
            );
        }
    }
}