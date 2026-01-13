package ivs.game.accessories.cards.gamedeck.dealer.impl;

import ivs.game.accessories.cards.gamedeck.dealer.Recipient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class StandardDealRequestTest {
    @Mock
    Recipient recipient;

    @SuppressWarnings("DataFlowIssue")
    @Test
    @DisplayName("Constructor should throw NullPointerException when recipient is null")
    void constructorThrowsNullPointerExceptionIfRecipientIsNull() {
        assertThrows(NullPointerException.class, () -> new StandardDealRequest<>(null, 2, true));
        assertThrows(NullPointerException.class, () -> new StandardDealRequest<>(null, 2, false));
    }

    @Test
    @DisplayName("Constructor should throw IllegalArgumentException when amount is negative")
    void constructorThrowsIllegalArgumentExceptionIfAmountNegative() {
        assertThrows(IllegalArgumentException.class, () -> new StandardDealRequest<>(recipient, -1, true));
        assertThrows(IllegalArgumentException.class, () -> new StandardDealRequest<>(recipient, -1, false));
    }

    @Test
    @DisplayName("strictOf: throws on null recipient or negative amount")
    void strictOfThrowsOnNullOrNegativeAmount() {
        assertThrows(NullPointerException.class, () -> StandardDealRequest.strictOf(null, 2));
        assertThrows(IllegalArgumentException.class, () -> StandardDealRequest.strictOf(recipient, -1));
    }

    @Test
    @DisplayName("lenientOf: throws on null recipient or negative amount")
    void lenientOfThrowsOnNullOrNegativeAmount() {
        assertThrows(NullPointerException.class, () -> StandardDealRequest.lenientOf(null, 2));
        assertThrows(IllegalArgumentException.class, () -> StandardDealRequest.lenientOf(recipient, -1));
    }

    @Test
    @DisplayName("strictOf returns StandardDealRequest with strict=true")
    void strictOfReturnsStrictDealRequest() {
        var request = StandardDealRequest.strictOf(recipient, 5);
        assertEquals(recipient, request.getRecipient());
        assertEquals(5, request.getAmount());
        assertTrue(request.isStrict());
    }

    @Test
    @DisplayName("lenientOf returns StandardDealRequest with strict=false")
    void lenientOfReturnsLenientDealRequest() {
        var request = StandardDealRequest.lenientOf(recipient, 7);
        assertEquals(recipient, request.getRecipient());
        assertEquals(7, request.getAmount());
        assertFalse(request.isStrict());
    }
}