package ivs.game.accessories.cards.gamedeck.cardshoe;

import ivs.game.accessories.cards.core.type.StandardCard;
import ivs.game.accessories.cards.gamedeck.GameDeckException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static ivs.game.accessories.cards.core.type.StandardCard.ACE_SPADES;
import static ivs.game.accessories.cards.core.type.StandardCard.JACK_CLUBS;
import static ivs.game.accessories.cards.core.type.StandardCard.KING_HEARTS;
import static ivs.game.accessories.cards.core.type.StandardCard.QUEEN_DIAMONDS;
import static ivs.game.accessories.cards.core.type.StandardCard.TEN_SPADES;
import static ivs.game.accessories.cards.gamedeck.cardshoe.StandardCardShoe.NO_CUT_CARD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StandardCardShoeBehaviourTest {
    @Test
    @DisplayName("draw() should remove cards one by one, update size/isEmpty, and throw GameDeckException when empty")
    void drawRemovesCardsAndThrowsOnEmpty() {
        StandardCardShoe<StandardCard> shoe = new StandardCardShoe<>(List.of(ACE_SPADES, KING_HEARTS), NO_CUT_CARD);

        // Initial size and emptiness
        assertEquals(2, shoe.size(), "Shoe should initially contain 2 cards");
        assertFalse(shoe.isEmpty(), "Shoe should not be empty before draws");

        // First draw
        StandardCard firstCard = shoe.draw();
        assertEquals(ACE_SPADES, firstCard, "First draw should return the top card");
        assertEquals(1, shoe.size(), "Shoe should contain 1 card after first draw");
        assertFalse(shoe.isEmpty(), "Shoe should not be empty after first draw");

        // Second draw
        StandardCard secondCard = shoe.draw();
        assertEquals(KING_HEARTS, secondCard, "Second draw should return the last card");
        assertEquals(0, shoe.size(), "Shoe should be empty after second draw");
        assertTrue(shoe.isEmpty(), "Shoe must be empty after all cards are drawn");

        // Third draw should throw GameDeckException
        assertThrows(
                GameDeckException.class,
                shoe::draw,
                "draw() on empty shoe must throw GameDeckException"
        );
    }

    @Test
    @DisplayName("draw(count) should remove correct number of cards, update size/isEmpty, throw on insufficient cards")
    void drawMultipleRemovesCardsAndThrowsOnEmpty() {
        List<StandardCard> cards = List.of(
                ACE_SPADES, KING_HEARTS,
                QUEEN_DIAMONDS, JACK_CLUBS
        );
        StandardCardShoe<StandardCard> shoe = new StandardCardShoe<>(cards, NO_CUT_CARD);

        // First call: draw 2 cards from 4
        List<StandardCard> first = shoe.draw(2);
        assertEquals(List.of(ACE_SPADES, KING_HEARTS), first, "First draw(2) should return the first two cards");
        assertEquals(2, shoe.size(), "After first draw(2) there should be 2 cards left");
        assertFalse(shoe.isEmpty(), "After first draw(2) shoe should not be empty");

        // Second call: draw the last 2 cards
        List<StandardCard> second = shoe.draw(2);
        assertEquals(List.of(QUEEN_DIAMONDS, JACK_CLUBS), second, "Second draw(2) should return the remaining cards");
        assertEquals(0, shoe.size(), "After second draw(2) shoe should be empty");
        assertTrue(shoe.isEmpty(), "After second draw(2) shoe should be empty");

        // Third call: trying to draw 2 from an empty shoe should throw exception
        assertThrows(
                GameDeckException.class,
                () -> shoe.draw(2),
                "draw(2) on empty shoe should throw GameDeckException"
        );
    }

    @Test
    @DisplayName("draw(count) should throw when trying to draw more cards than are present in the shoe")
    void drawMoreThanPresentThrowsException() {
        StandardCardShoe<StandardCard> shoe = new StandardCardShoe<>(List.of(ACE_SPADES, KING_HEARTS), NO_CUT_CARD);

        // Attempting to draw more cards than available should throw GameDeckException
        assertThrows(
                GameDeckException.class,
                () -> shoe.draw(3),
                "draw(3) should throw GameDeckException if only 2 cards are present in the shoe"
        );
    }

    @Test
    @DisplayName("isCutCardOut should not trigger if there is no cut card (NO_CUT_CARD)")
    void isCutCardOutNoCutCardShouldNotTrigger() {
        StandardCardShoe<StandardCard> shoe = new StandardCardShoe<>(List.of(ACE_SPADES, KING_HEARTS), NO_CUT_CARD);

        // Draw all cards one by one
        shoe.draw();
        assertFalse(shoe.isCutCardOut(), "isCutCardOut should be false after drawing first card when no cut card is set");

        shoe.draw();
        assertFalse(shoe.isCutCardOut(), "isCutCardOut should still be false after all cards are drawn with no cut card");
    }

    @Test
    @DisplayName("isCutCardOut should be true when last card is drawn and cut card is at 1")
    void isCutCardOutTriggersOnLastCard() {
        List<StandardCard> cards = List.of(
                ACE_SPADES, KING_HEARTS, QUEEN_DIAMONDS, JACK_CLUBS, TEN_SPADES
        );
        int cutCardPosition = 1;
        StandardCardShoe<StandardCard> shoe = new StandardCardShoe<>(cards, cutCardPosition);

        // Draw until only one card is left (cut card position == 1)
        for (int i = 0; i < 4; i++) {
            shoe.draw();
            assertFalse(shoe.isCutCardOut(), "isCutCardOut should be false until the last card is left");
        }
        // One card remains, draw it
        shoe.draw();
        assertTrue(shoe.isCutCardOut(), "isCutCardOut should be true once cut card is out (last card drawn)");
    }

    @Test
    @DisplayName("isCutCardOut should be true immediately if cut card is at full deck size")
    void isCutCardOutTriggersOnFullDeck() {
        List<StandardCard> cards = List.of(
                ACE_SPADES, KING_HEARTS, QUEEN_DIAMONDS, JACK_CLUBS, TEN_SPADES
        );
        int cutCardPosition = 5;
        StandardCardShoe<StandardCard> shoe = new StandardCardShoe<>(cards, cutCardPosition);

        // Draw one card (size=4), immediately below cut position
        shoe.draw();
        assertTrue(shoe.isCutCardOut(), "isCutCardOut should be true once below cut card position from the start");
    }

    @Test
    @DisplayName("isCutCardOut should be true when below cut card in the middle of the shoe")
    void isCutCardOutTriggersInTheMiddle() {
        List<StandardCard> cards = List.of(
                ACE_SPADES, KING_HEARTS, QUEEN_DIAMONDS, JACK_CLUBS, TEN_SPADES
        );
        int cutCardPosition = 3;
        StandardCardShoe<StandardCard> shoe = new StandardCardShoe<>(cards, cutCardPosition);

        // Draw two cards (size=3), should still be false
        shoe.draw();
        shoe.draw();
        assertFalse(shoe.isCutCardOut(), "isCutCardOut should be false at cut card position");

        // Draw third card (size=2), now below cut card position
        shoe.draw();
        assertTrue(shoe.isCutCardOut(), "isCutCardOut should be true below cut card position (in the middle)");
    }

    @Test
    @DisplayName("exportCards should return a new list with the same card order as the original, but not the same object")
    void exportCardsReturnsNewListWithSameOrder() {
        List<StandardCard> originalCards = List.of(ACE_SPADES, KING_HEARTS);
        StandardCardShoe<StandardCard> shoe = new StandardCardShoe<>(originalCards, 2);

        List<StandardCard> exportedCards = shoe.exportCards();

        // The exported list should have the same elements in the same order
        assertEquals(originalCards, exportedCards, "exportCards should preserve card order");
        // But it should not be the same object as the original list
        assertNotSame(originalCards, exportedCards, "exportCards should return a new list, not the original");
    }

    @Test
    @DisplayName("peek() should return the next card for a non-empty shoe, without removing it")
    void peekReturnsCardForNonEmptyShoe() {
        StandardCardShoe<StandardCard> shoe = new StandardCardShoe<>(List.of(ACE_SPADES, KING_HEARTS), NO_CUT_CARD);

        assertEquals(ACE_SPADES, shoe.peek(), "peek() should return the top card without removing it");
        assertEquals(2, shoe.size(), "peek() should not change the shoe size");
    }

    @Test
    @DisplayName("peek() should throw GameDeckException when called on an empty shoe")
    void peekThrowsExceptionOnEmptyShoe() {
        StandardCardShoe<StandardCard> shoe = new StandardCardShoe<>(List.of(), NO_CUT_CARD);

        assertThrows(
                GameDeckException.class,
                shoe::peek,
                "peek() on an empty shoe should throw GameDeckException"
        );
    }
}