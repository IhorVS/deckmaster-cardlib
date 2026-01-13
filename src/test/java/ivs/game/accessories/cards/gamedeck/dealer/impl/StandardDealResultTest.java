package ivs.game.accessories.cards.gamedeck.dealer.impl;

import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.core.type.StandardCard;
import ivs.game.accessories.cards.gamedeck.dealer.DealResult;
import ivs.game.accessories.cards.gamedeck.dealer.Recipient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class StandardDealResultTest {

    @Mock
    Recipient recipient1;
    @Mock
    Recipient recipient2;

    @Test
    @DisplayName("Constructor throws NullPointerException if allocations map is null")
    void constructorThrowsIfAllocationsIsNull() {
        assertThrows(NullPointerException.class, () -> new StandardDealResult<>(null));
    }

    @Test
    @DisplayName("Constructor throws NullPointerException if a key in allocations is null")
    void constructorThrowsIfAllocationsHasNullKey() {
        Map<Recipient, List<PlayingCard>> map = new HashMap<>();
        map.put(null, List.of(StandardCard.ACE_SPADES));
        assertThrows(NullPointerException.class, () -> new StandardDealResult<>(map));
    }

    @Test
    @DisplayName("Constructor throws NullPointerException if a value in allocations is null")
    void constructorThrowsIfAllocationsHasNullList() {
        Map<Recipient, List<PlayingCard>> map = new HashMap<>();
        map.put(recipient1, null);
        assertThrows(NullPointerException.class, () -> new StandardDealResult<>(map));
    }

    @Test
    @DisplayName("Constructor throws NullPointerException if a list contains null")
    void constructorThrowsIfListHasNullCard() {
        Map<Recipient, List<PlayingCard>> map = new HashMap<>();
        map.put(recipient2, Arrays.asList(StandardCard.TWO_HEARTS, null));
        assertThrows(IllegalArgumentException.class, () -> new StandardDealResult<>(map));
    }

    @Test
    @DisplayName("Constructor works with empty allocations map")
    void constructorWorksWithEmptyMap() {
        Map<Recipient, List<PlayingCard>> map = new HashMap<>();
        assertDoesNotThrow(() -> new StandardDealResult<>(map));
    }

    @Test
    @DisplayName("Constructor works with valid recipients and cards")
    void constructorWorksWithValidInput() {
        Map<Recipient, List<PlayingCard>> map = new HashMap<>();
        map.put(recipient1, List.of(StandardCard.SEVEN_CLUBS, StandardCard.TWO_SPADES));
        map.put(recipient2, List.of(StandardCard.ACE_HEARTS));
        assertDoesNotThrow(() -> new StandardDealResult<>(map));
    }

    @Test
    @DisplayName("getAllocations() returns unmodifiable map")
    void getAllocationsReturnsUnmodifiableMap() {
        Map<Recipient, List<PlayingCard>> map = new HashMap<>();
        List<PlayingCard> cards = new ArrayList<>();
        cards.add(StandardCard.TWO_HEARTS);
        map.put(recipient1, cards);

        DealResult<Recipient> result = new StandardDealResult<>(map);

        Map<Recipient, List<PlayingCard>> allocations = result.getAllocations();

        assertThrows(UnsupportedOperationException.class, () -> allocations.put(recipient2, List.of(StandardCard.THREE_HEARTS)));
        assertThrows(UnsupportedOperationException.class, () -> allocations.get(recipient1).add(StandardCard.THREE_HEARTS));
    }

    @Test
    @DisplayName("Result allocations map is a different object than input and is not affected by input changes")
    void resultMapIsIndependentFromInputMap() {
        List<PlayingCard> cards = new ArrayList<>();
        cards.add(StandardCard.TWO_HEARTS);

        Map<Recipient, List<PlayingCard>> map = new HashMap<>();
        map.put(recipient1, cards);

        DealResult<Recipient> result = new StandardDealResult<>(map);

        assertNotSame(map, result.getAllocations());

        map.put(recipient2, List.of(StandardCard.THREE_HEARTS));
        cards.add(StandardCard.FOUR_HEARTS);

        Map<Recipient, List<PlayingCard>> allocations = result.getAllocations();
        assertFalse(allocations.containsKey(recipient2));
        assertEquals(List.of(StandardCard.TWO_HEARTS), allocations.get(recipient1));
    }
}