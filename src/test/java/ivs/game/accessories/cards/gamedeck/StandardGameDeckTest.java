package ivs.game.accessories.cards.gamedeck;

import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.core.type.StandardCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StandardGameDeckTest {

    @SuppressWarnings("DataFlowIssue")
    @Test
    @DisplayName("Constructor should throw NullPointerException if card list is null")
    void constructorShouldThrowIfCardListIsNull() {
        assertThrows(NullPointerException.class,
                () -> new StandardGameDeck<>((List<PlayingCard>) null),
                "Constructor must throw NullPointerException when cards list is null");
    }

    @Test
    @DisplayName("Constructor should create empty deck from empty card list")
    void constructorShouldCreateEmptyDeckFromEmptyList() {
        StandardGameDeck<PlayingCard> deck = new StandardGameDeck<>(Collections.emptyList());

        assertTrue(deck.isEmpty(), "isEmpty() should return true for empty deck");
        assertEquals(0, deck.size(), "size() should return 0 for empty deck");
    }

    @Test
    @DisplayName("Constructor should create non-empty deck with correct size from non-empty card list")
    void constructorShouldCreateNonEmptyDeckFromNonEmptyList() {
        List<StandardCard> cards = List.of(StandardCard.ACE_SPADES, StandardCard.KING_HEARTS);

        StandardGameDeck<StandardCard> deck = new StandardGameDeck<>(cards);

        assertFalse(deck.isEmpty(), "isEmpty() should return false for non-empty deck");
        assertEquals(cards.size(), deck.size(), "size() should match the number of cards provided");
    }

    @Test
    @DisplayName("draw() should throw EmptyDeckException for empty deck")
    void drawShouldThrowForEmptyDeck() {
        StandardGameDeck<PlayingCard> deck = new StandardGameDeck<>(Collections.emptyList());
        assertThrows(GameDeckException.class,
                deck::draw,
                "draw() must throw EmptyDeckException when deck is empty");
    }

    @Test
    @DisplayName("draw() should return the top card for non-empty deck and decrease size")
    void drawShouldReturnTopCardForNonEmptyDeck() {
        List<StandardCard> cards = List.of(StandardCard.ACE_SPADES, StandardCard.KING_HEARTS);
        StandardGameDeck<StandardCard> deck = new StandardGameDeck<>(cards);

        assertEquals(StandardCard.ACE_SPADES, deck.draw(), "draw() should return the first card (top of the deck)");
        assertEquals(1, deck.size(), "size() should decrease after draw()");
        assertFalse(deck.isEmpty(), "isEmpty() should return false if there are still cards in the deck");

        assertEquals(StandardCard.KING_HEARTS, deck.draw(), "draw() should return the second card (top of the deck)");
        assertEquals(0, deck.size(), "size() should decrease after draw()");
        assertTrue(deck.isEmpty(), "isEmpty() should return true if there are no cards in the deck");
    }

    @Test
    @DisplayName("draw(int) should throw if count is negative")
    void drawShouldThrowIfCountNegative() {
        StandardGameDeck<StandardCard> deck = new StandardGameDeck<>(List.of(StandardCard.ACE_SPADES));
        assertThrows(IllegalArgumentException.class, () -> deck.draw(-1),
                "draw(-1) must throw IllegalArgumentException");
    }

    @Test
    @DisplayName("draw(int) should throw EmptyDeckException for empty deck")
    void drawShouldThrowIfDeckIsEmpty() {
        StandardGameDeck<StandardCard> deck = new StandardGameDeck<>(Collections.emptyList());
        assertThrows(GameDeckException.class, () -> deck.draw(1),
                "draw(1) must throw EmptyDeckException if deck is empty");
    }

    @Test
    @DisplayName("draw(int) should throw if count greater than size")
    void drawShouldThrowIfNotEnoughCards() {
        List<StandardCard> cards = List.of(StandardCard.ACE_SPADES, StandardCard.KING_HEARTS);
        StandardGameDeck<StandardCard> deck = new StandardGameDeck<>(cards);

        assertThrows(GameDeckException.class, () -> deck.draw(3),
                "draw(3) must throw IllegalArgumentException if not enough cards in deck");
    }

    @Test
    @DisplayName("draw(int) should return empty list and not throw if count is zero")
    void drawShouldReturnEmptyListIfCountZero() {
        List<StandardCard> cards = List.of(StandardCard.ACE_SPADES, StandardCard.KING_HEARTS);
        StandardGameDeck<StandardCard> deck = new StandardGameDeck<>(cards);

        List<StandardCard> drawn = deck.draw(0);
        assertTrue(drawn.isEmpty(), "draw(0) should return an empty list");
        assertEquals(2, deck.size(), "deck size shouldn't change after draw(0)");
    }

    @Test
    @DisplayName("draw(int) should return correct cards from the top in order")
    void drawShouldReturnProperCardsInOrder() {
        List<StandardCard> cards = List.of(StandardCard.ACE_SPADES, StandardCard.KING_HEARTS, StandardCard.QUEEN_DIAMONDS);
        StandardGameDeck<StandardCard> deck = new StandardGameDeck<>(cards);

        List<StandardCard> drawn = deck.draw(2);
        assertEquals(List.of(StandardCard.ACE_SPADES, StandardCard.KING_HEARTS), drawn,
                "draw(2) should return the first two cards (top to bottom)");
        assertEquals(1, deck.size(), "deck size should decrease after draw(2)");
        assertEquals(StandardCard.QUEEN_DIAMONDS, deck.draw(), "DeckTemplate should now have only the last card");
        assertTrue(deck.isEmpty(), "DeckTemplate should be empty after drawing all cards");
    }

    @Test
    @DisplayName("should throw EmptyDeckException if deck is empty")
    void peekBottomShouldThrowIfDeckIsEmpty() {
        StandardGameDeck<StandardCard> deck = new StandardGameDeck<>(Collections.emptyList());
        assertThrows(GameDeckException.class, deck::peekBottom);
    }

    @Test
    @DisplayName("should return the bottom (last) card for non-empty deck")
    void peekBottomShouldReturnLastCard() {
        List<StandardCard> cards = List.of(StandardCard.KING_HEARTS, StandardCard.ACE_SPADES, StandardCard.QUEEN_DIAMONDS);
        StandardGameDeck<StandardCard> deck = new StandardGameDeck<>(cards);

        StandardCard bottomCard = deck.peekBottom();

        assertEquals(StandardCard.QUEEN_DIAMONDS, bottomCard, "peekBottom should return the last card in the deck");
        assertEquals(3, deck.size(), "peekBottom should not change deck size");
    }

    @Test
    @DisplayName("should return the only card for single-card deck")
    void peekBottomShouldReturnOnlyCardIfOneCard() {
        StandardGameDeck<StandardCard> deck = new StandardGameDeck<>(List.of(StandardCard.ACE_SPADES));
        assertEquals(StandardCard.ACE_SPADES, deck.peekBottom(), "Should return the only card in deck");
    }

    @Test
    @DisplayName("should return empty unmodifiable list for empty deck")
    void exportCardsReturnsEmptyUnmodifiableListWhenEmpty() {
        StandardGameDeck<StandardCard> deck = new StandardGameDeck<>(Collections.emptyList());
        List<StandardCard> cards = deck.exportCards();
        assertTrue(cards.isEmpty(), "Result list should be empty");
        assertThrows(UnsupportedOperationException.class, () -> cards.add(StandardCard.ACE_SPADES),
                "Returned list must be unmodifiable");
    }

    @Test
    @DisplayName("should return unmodifiable list with all cards in correct order for non-empty deck")
    void exportCardsReturnsAllCardsInOrderAndIsUnmodifiable() {
        List<StandardCard> source = List.of(
                StandardCard.ACE_SPADES,
                StandardCard.KING_HEARTS,
                StandardCard.QUEEN_DIAMONDS
        );
        StandardGameDeck<StandardCard> deck = new StandardGameDeck<>(source);

        List<StandardCard> cards = deck.exportCards();
        assertEquals(source, cards, "Returned list should match deck content and order");
        assertThrows(UnsupportedOperationException.class, cards::removeFirst,
                "Returned list must be unmodifiable");
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    @DisplayName("Copy constructor should throw NullPointerException if argument is null")
    void copyConstructorShouldThrowIfArgumentIsNull() {
        assertThrows(NullPointerException.class,
                () -> new StandardGameDeck<>((StandardGameDeck<StandardCard>) null),
                "Copy constructor must throw NullPointerException when argument is null");
    }

    @Test
    @DisplayName("Copy constructor should create a deck with the same cards in the same order")
    void copyConstructorCopiesCardsInOrder() {
        List<StandardCard> cards = List.of(StandardCard.ACE_SPADES, StandardCard.KING_HEARTS, StandardCard.QUEEN_DIAMONDS);
        StandardGameDeck<StandardCard> original = new StandardGameDeck<>(cards);
        StandardGameDeck<StandardCard> copy = new StandardGameDeck<>(original);

        assertEquals(original.size(), copy.size(), "Copy should have same size as original");
        assertEquals(original.exportCards(), copy.exportCards(),
                "Copied deck should have cards in the same order as original");
        assertNotSame(copy, original, "Copy must be a different object instance");
    }

    @Test
    @DisplayName("Mutating one deck does not affect the other (independent deque)")
    void decksShouldBeIndependentAfterCopy() {
        List<StandardCard> cards = List.of(StandardCard.ACE_SPADES, StandardCard.KING_HEARTS);
        StandardGameDeck<StandardCard> original = new StandardGameDeck<>(cards);
        StandardGameDeck<StandardCard> copy = new StandardGameDeck<>(original);

        // Draw from original, should not affect the copy
        StandardCard drawn = original.draw();
        assertEquals(StandardCard.ACE_SPADES, drawn, "Original should draw ACE_SPADES");
        assertEquals(1, original.size(), "Original size should decrease after draw");
        assertEquals(2, copy.size(), "Copy size should remain unchanged");

        // Draw from copy, should not affect the original
        StandardCard copyDrawn = copy.draw();
        assertEquals(StandardCard.ACE_SPADES, copyDrawn, "Copy should also draw ACE_SPADES (shallow copy of cards)");
        assertEquals(1, copy.size(), "Copy size decreases after its own draw");
    }

    @Test
    @DisplayName("Constructor should throw NullPointerException if card list contains null elements")
    void constructorShouldThrowIfCardListContainsNullElements() {
        List<StandardCard> listWithNull = new ArrayList<>();
        listWithNull.add(StandardCard.ACE_SPADES);
        listWithNull.add(null);
        listWithNull.add(StandardCard.KING_HEARTS);
        assertThrows(IllegalArgumentException.class, () -> new StandardGameDeck<>(listWithNull),
                "Constructor must throw if card list contains null");
    }

    @Test
    @DisplayName("peekTop should return the first card in a non-empty deck")
    void peekTopReturnsFirstCard() {
        List<StandardCard> cards = List.of(StandardCard.ACE_SPADES, StandardCard.KING_HEARTS);
        StandardGameDeck<StandardCard> deck = new StandardGameDeck<>(cards);

        assertEquals(StandardCard.ACE_SPADES, deck.peekTop(), "peekTop should return the first card of the deck");
    }

    @Test
    @DisplayName("peekBottom should return the last card in a non-empty deck")
    void peekBottomReturnsLastCard() {
        List<StandardCard> cards = List.of(StandardCard.ACE_SPADES, StandardCard.KING_HEARTS);
        StandardGameDeck<StandardCard> deck = new StandardGameDeck<>(cards);

        assertEquals(StandardCard.KING_HEARTS, deck.peekBottom(), "peekBottom should return the last card of the deck");
    }

    @Test
    @DisplayName("peekTop should throw GameDeckException for empty deck")
    void peekTopThrowsOnEmptyDeck() {
        StandardGameDeck<StandardCard> emptyDeck = new StandardGameDeck<>(Collections.emptyList());

        assertThrows(
                GameDeckException.class,
                emptyDeck::peekTop,
                "peekTop on empty deck should throw GameDeckException"
        );
    }

    @Test
    @DisplayName("peekBottom should throw GameDeckException for empty deck")
    void peekBottomThrowsOnEmptyDeck() {
        StandardGameDeck<StandardCard> emptyDeck = new StandardGameDeck<>(Collections.emptyList());

        assertThrows(
                GameDeckException.class,
                emptyDeck::peekBottom,
                "peekBottom on empty deck should throw GameDeckException"
        );
    }
}