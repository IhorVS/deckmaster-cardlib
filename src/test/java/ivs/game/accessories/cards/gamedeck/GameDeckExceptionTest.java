package ivs.game.accessories.cards.gamedeck;

import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.core.type.StandardCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("EmptyDeckException")
class GameDeckExceptionTest {

    @Test
    @DisplayName("no-arg constructor should produce exception with null message and null cause")
    void noArgConstructorShouldHaveNoMessageOrCause() {
        GameDeckException exception = new GameDeckException();
        assertNull(exception.getMessage(), "Message should be null");
        assertNull(exception.getCause(), "Cause should be null");
    }

    @Test
    @DisplayName("constructor with message should set message and null cause")
    void constructorWithMessageShouldReturnMessage() {
        String msg = "DeckTemplate is empty";
        GameDeckException exception = new GameDeckException(msg);
        assertEquals(msg, exception.getMessage(), "Should return message passed to constructor");
        assertNull(exception.getCause(), "Cause should be null");
    }

    @Test
    @DisplayName("constructor with message and cause should set both fields correctly")
    void constructorWithMessageAndCauseShouldReturnBoth() {
        String msg = "DeckTemplate is empty";
        Throwable cause = new RuntimeException("Root");
        GameDeckException exception = new GameDeckException(msg, cause);

        assertEquals(msg, exception.getMessage(), "Should return message passed to constructor");
        assertEquals(cause, exception.getCause(), "Should return cause passed to constructor");
    }

    @Test
    @DisplayName("constructor with cause should set only cause")
    void constructorWithCauseShouldReturnCause() {
        Throwable cause = new RuntimeException("Root");
        GameDeckException exception = new GameDeckException(cause);
        assertEquals(cause, exception.getCause(), "Should return cause passed to constructor");
    }

    @Nested
    @DisplayName("validateDeckSize for GameDeck with overloaded variant")
    class DeprecatedValidateDeckSizeTests {
        @Test
        @DisplayName("should throw when deck is empty")
        void validateDeckSizeShouldThrowIfDeckIsEmpty() {
            @SuppressWarnings("unchecked")
            GameDeck<PlayingCard> emptyDeck = Mockito.mock(GameDeck.class);
            Mockito.when(emptyDeck.isEmpty()).thenReturn(true);

            assertThrows(GameDeckException.class, () -> GameDeckException.validateDeckSize(emptyDeck));
        }

        @Test
        @DisplayName("should not throw when deck is not empty")
        void validateDeckSizeShouldNotThrowIfDeckIsNotEmpty() {
            @SuppressWarnings("unchecked")
            GameDeck<PlayingCard> notEmptyDeck = Mockito.mock(GameDeck.class);
            Mockito.when(notEmptyDeck.isEmpty()).thenReturn(false);

            assertDoesNotThrow(() -> GameDeckException.validateDeckSize(notEmptyDeck));
        }
    }

    @Nested
    @DisplayName("validateDeckSize(GameDeck, int): check deck size against drawCount")
    class ValidateDeckSizeWithDrawCountTests {
        @Test
        @DisplayName("does not throw when deck contains enough cards for drawCount")
        void doesNotThrowWhenEnoughCards() {
            StandardGameDeck<StandardCard> deck = new StandardGameDeck<>(List.of(StandardCard.ACE_SPADES, StandardCard.KING_HEARTS));
            assertDoesNotThrow(() -> GameDeckException.validateDeckSize(deck, 2));
            assertDoesNotThrow(() -> GameDeckException.validateDeckSize(deck, 1));
        }

        @Test
        @DisplayName("throws EmptyDeckException with default message for empty deck")
        void throwsWhenDeckIsEmpty() {
            StandardGameDeck<StandardCard> deck = new StandardGameDeck<>(Collections.emptyList());
            assertThrows(GameDeckException.class, () -> GameDeckException.validateDeckSize(deck, 1));
        }

        @Test
        @DisplayName("throws EmptyDeckException with details when not enough cards in deck")
        void throwsWhenNotEnoughCards() {
            StandardGameDeck<StandardCard> deck = new StandardGameDeck<>(List.of(StandardCard.ACE_SPADES));
            GameDeckException ex = assertThrows(GameDeckException.class, () -> GameDeckException.validateDeckSize(deck, 2));
            assertTrue(ex.getMessage().contains("Not enough cards in deck"),
                    "Error message must mention not enough cards in deck");
            assertTrue(ex.getMessage().contains("requested: 2"),
                    "Error message must mention requested amount");
            assertTrue(ex.getMessage().contains("available: 1"),
                    "Error message must mention available count");
        }

        @Test
        @DisplayName("does not throw if draw count is zero")
        void doesNotThrowWhenDrawCountIsZero() {
            StandardGameDeck<StandardCard> deck = new StandardGameDeck<>(List.of(StandardCard.ACE_SPADES));
            assertDoesNotThrow(() -> GameDeckException.validateDeckSize(deck, 0));
        }
    }
}