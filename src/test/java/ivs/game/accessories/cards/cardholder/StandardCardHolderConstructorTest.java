package ivs.game.accessories.cards.cardholder;

import ivs.game.accessories.cards.core.type.PlayingCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class StandardCardHolderConstructorTest {
    @SuppressWarnings("DataFlowIssue")
    @Test
    @DisplayName("constructor throws NullPointerException if comparator is null")
    void constructorThrowsIfComparatorIsNull() {
        assertThrows(
                NullPointerException.class,
                () -> new StandardCardHolder(null),
                "Should throw NPE when trying to create holder with null comparator"
        );
    }

    @Test
    @DisplayName("constructor with comparator does not throw and returns non-null instance")
    void constructorWithMockedComparatorDoesNotThrowAndReturnsNonNull() {
        @SuppressWarnings("unchecked")
        Comparator<PlayingCard> mockedComparator = mock(Comparator.class);

        StandardCardHolder holder = assertDoesNotThrow(
                () -> new StandardCardHolder(mockedComparator),
                "Should not throw when comparator is provided"
        );
        assertNotNull(holder, "Constructor should return a non-null StandardCardHolder");
    }

}