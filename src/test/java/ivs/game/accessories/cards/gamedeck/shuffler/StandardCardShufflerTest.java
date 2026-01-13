package ivs.game.accessories.cards.gamedeck.shuffler;

import ivs.game.accessories.cards.core.type.StandardCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ivs.game.accessories.cards.core.type.StandardCard.ACE_SPADES;
import static ivs.game.accessories.cards.core.type.StandardCard.KING_HEARTS;
import static ivs.game.accessories.cards.core.type.StandardCard.QUEEN_CLUBS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StandardCardShufflerTest {

    @Test
    @DisplayName("shuffleInPlace should modify the list in-place")
    void shuffleInPlaceShouldModifyList() {
        StandardCardShuffler<StandardCard> shuffler = new StandardCardShuffler<>();
        List<StandardCard> cards = new ArrayList<>(Arrays.asList(ACE_SPADES, KING_HEARTS, QUEEN_CLUBS));
        List<StandardCard> orig = new ArrayList<>(cards);

        shuffler.shuffleInPlace(cards);

        assertEquals(orig.size(), cards.size(), "Shuffled list must have the same size");
        assertTrue(cards.containsAll(orig), "Shuffled list must contain all original cards");
    }

    @Test
    @DisplayName("shuffleCopy should return a new shuffled list and not modify the original")
    void shuffleCopyShouldReturnNewListAndPreserveOriginal() {
        StandardCardShuffler<StandardCard> shuffler = new StandardCardShuffler<>();
        List<StandardCard> cards = new ArrayList<>(Arrays.asList(ACE_SPADES, KING_HEARTS, QUEEN_CLUBS));

        List<StandardCard> shuffled = shuffler.shuffleCopy(cards);

        assertEquals(cards.size(), shuffled.size(), "Shuffled copy must have same size as original");
        assertNotSame(cards, shuffled, "Shuffled copy must be a new list");
        assertTrue(shuffled.containsAll(cards), "Shuffled copy must contain all original cards");
        assertEquals(
                Arrays.asList(ACE_SPADES, KING_HEARTS, QUEEN_CLUBS),
                cards,
                "Original list must not be modified"
        );
    }

    @Test
    @DisplayName("shuffleInPlace should throw NullPointerException for null list")
    void shuffleInPlaceShouldThrowForNull() {
        StandardCardShuffler<StandardCard> shuffler = new StandardCardShuffler<>();
        assertThrows(NullPointerException.class,
                () -> shuffler.shuffleInPlace(null),
                "shuffleInPlace must throw NullPointerException if list is null");
    }

    @Test
    @DisplayName("shuffleCopy should throw NullPointerException for null collection")
    void shuffleCopyShouldThrowForNull() {
        StandardCardShuffler<StandardCard> shuffler = new StandardCardShuffler<>();
        assertThrows(NullPointerException.class,
                () -> shuffler.shuffleCopy(null),
                "shuffleCopy must throw NullPointerException if collection is null");
    }
}