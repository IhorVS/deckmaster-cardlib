package ivs.game.accessories.cards.gamedeck.cardshoe;

import ivs.game.accessories.cards.core.type.StandardCard;
import ivs.game.accessories.cards.gamedeck.GameDeck;
import ivs.game.accessories.cards.gamedeck.StandardGameDeck;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ivs.game.accessories.cards.core.type.StandardCard.ACE_SPADES;
import static ivs.game.accessories.cards.core.type.StandardCard.KING_HEARTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StandardCardShoeConstructorTest {

    private static final List<StandardCard> CARDS_WITH_NULL;

    static {
        CARDS_WITH_NULL = new ArrayList<>();
        CARDS_WITH_NULL.add(ACE_SPADES);
        CARDS_WITH_NULL.add(null);
        CARDS_WITH_NULL.add(KING_HEARTS);
    }

    @Test
    @DisplayName("Constructor should throw NullPointerException if card collection is null")
    void constructorShouldThrowIfCardCollectionIsNull() {
        assertThrows(NullPointerException.class,
                () -> new StandardCardShoe<StandardCard>((Collection<StandardCard>) null, 2),
                "Constructor must throw if card collection is null");
    }

    @Test
    @DisplayName("Constructor should throw IllegalArgumentException if card collection contains null element")
    void constructorShouldThrowIfCardCollectionContainsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new StandardCardShoe<>(CARDS_WITH_NULL, 1),
                "Constructor must throw IllegalArgumentException if collection contains null element");
    }

    @Test
    @DisplayName("Constructor should throw IllegalArgumentException if cut-card position is negative")
    void constructorShouldThrowIfCutCardPositionNegative() {
        List<StandardCard> cards = List.of(ACE_SPADES, KING_HEARTS);
        assertThrows(IllegalArgumentException.class,
                () -> new StandardCardShoe<>(cards, -1),
                "Constructor must throw if cut-card position is negative");
    }

    @Test
    @DisplayName("Modifying shoe does not affect the original card collection")
    void modifyingShoeDoesNotAffectOriginalCollection() {
        List<StandardCard> originalCards = new ArrayList<>();
        originalCards.add(ACE_SPADES);
        originalCards.add(KING_HEARTS);
        StandardCardShoe<StandardCard> shoe = new StandardCardShoe<>(originalCards, 1);

        // Draw a card from the shoe
        shoe.draw();

        // Check that the original collection is unchanged
        assertEquals(2, originalCards.size(), "Original collection should not change after modifications in shoe");
        assertEquals(ACE_SPADES, originalCards.get(0));
        assertEquals(KING_HEARTS, originalCards.get(1));
    }

    // ----------- Tests for the second constructor (GameDeck) ------------

    @Test
    @DisplayName("Constructor should throw NullPointerException if gameDeck is null")
    void constructorShouldThrowIfGameDeckIsNull() {
        assertThrows(NullPointerException.class,
                () -> new StandardCardShoe<StandardCard>((GameDeck<StandardCard>) null, 2),
                "Constructor must throw if gameDeck is null"
        );
    }

    @Test
    @DisplayName("Constructor should throw IllegalArgumentException if gameDeck contains null element")
    void constructorShouldThrowIfGameDeckContainsNull() {
        GameDeck<StandardCard> deckWithNull = mock(GameDeck.class);
        when(deckWithNull.exportCards()).thenReturn(CARDS_WITH_NULL);

        assertThrows(IllegalArgumentException.class,
                () -> new StandardCardShoe<>(deckWithNull, 2),
                "Constructor must throw IllegalArgumentException if gameDeck contains null element"
        );
    }

    @Test
    @DisplayName("Constructor should throw IllegalArgumentException if cut-card position is negative (GameDeck)")
    void constructorShouldThrowIfCutCardPositionNegativeForGameDeck() {
        List<StandardCard> cards = List.of(ACE_SPADES, KING_HEARTS);
        StandardGameDeck<StandardCard> deck = new StandardGameDeck<>(cards);

        assertThrows(IllegalArgumentException.class,
                () -> new StandardCardShoe<>(deck, -5),
                "Constructor must throw if cut-card position is negative"
        );
    }

    @Test
    @DisplayName("Modifying shoe does not affect the original GameDeck")
    void modifyingShoeDoesNotAffectOriginalGameDeck() {
        List<StandardCard> cards = List.of(ACE_SPADES, KING_HEARTS);
        StandardGameDeck<StandardCard> originalDeck = new StandardGameDeck<>(cards);

        StandardCardShoe<StandardCard> shoe = new StandardCardShoe<>(originalDeck, 1);

        // Draw from shoe
        shoe.draw();

        // The original deck should be unchanged
        assertEquals(2, originalDeck.size(), "Original deck should not change after modifications in shoe");
        assertEquals(ACE_SPADES, originalDeck.exportCards().get(0));
        assertEquals(KING_HEARTS, originalDeck.exportCards().get(1));
    }

    // ----------- Tests for the copy constructor ------------

    @Test
    @DisplayName("Copy constructor should create an identical shoe with the same cards and cutCardPosition")
    void copyConstructorShouldCreateIdenticalShoe() {
        List<StandardCard> cards = new ArrayList<>();
        cards.add(ACE_SPADES);
        cards.add(KING_HEARTS);
        int cutCardPosition = 1;
        StandardCardShoe<StandardCard> original = new StandardCardShoe<>(cards, cutCardPosition);

        StandardCardShoe<StandardCard> copy = new StandardCardShoe<>(original);

        // Cards must be equal
        assertEquals(original.exportCards(), copy.exportCards(), "Copied shoe must have the same cards in the same order");
        // cutCardPosition must be equal
        assertEquals(cutCardPosition, copy.getCutCardPosition(), "Copied shoe must have the same cutCardPosition");
        // Modifying the copy should not affect the original
        copy.draw();
        assertEquals(2, original.exportCards().size(), "Original shoe should be unaffected by modifications in the copy");
        assertEquals(1, copy.exportCards().size(), "Copy should be modified independently");
    }

    @Test
    @DisplayName("Copy constructor should throw NullPointerException if original shoe is null")
    void copyConstructorShouldThrowIfOriginalIsNull() {
        assertThrows(NullPointerException.class,
                () -> new StandardCardShoe<StandardCard>((StandardCardShoe<StandardCard>) null),
                "Copy constructor must throw if original shoe is null"
        );
    }
}