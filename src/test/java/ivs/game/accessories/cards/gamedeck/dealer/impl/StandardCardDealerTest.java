package ivs.game.accessories.cards.gamedeck.dealer.impl;

import ivs.game.accessories.cards.core.type.JokerCard;
import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.core.type.StandardCard;
import ivs.game.accessories.cards.gamedeck.GameDeck;
import ivs.game.accessories.cards.gamedeck.StandardGameDeck;
import ivs.game.accessories.cards.gamedeck.dealer.DealRequest;
import ivs.game.accessories.cards.gamedeck.dealer.Recipient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@SuppressWarnings({"DataFlowIssue", "rawtypes", "unchecked"})
@ExtendWith(MockitoExtension.class)
class StandardCardDealerTest {
    @Mock
    Recipient recipient1;
    @Mock
    Recipient recipient2;
    @Mock
    Recipient recipient3;


    @Test
    @DisplayName("deal() should throw NullPointerException when deck is null")
    void dealShouldThrowWhenDeckIsNull() {
        StandardCardDealer<GameDeck<PlayingCard>, Recipient> dealer = new StandardCardDealer<>();
        List<DealRequest<Recipient>> requests = Collections.emptyList();

        assertThrows(NullPointerException.class, () -> dealer.deal(null, requests)
        );
    }

    @Test
    @DisplayName("deal() should throw NullPointerException when requests is null")
    void dealShouldThrowWhenRequestsIsNull() {
        StandardCardDealer<GameDeck<PlayingCard>, Recipient> dealer = new StandardCardDealer<>();
        GameDeck deck = mock(GameDeck.class);

        assertThrows(NullPointerException.class, () -> dealer.deal(deck, null)
        );
    }

    @Test
    @DisplayName("deal() should throw when not enough cards for strict request")
    void shouldThrowOnStrictRequestWithNotEnoughCards() {
        // DeckTemplate mock: only 3 cards left, then isEmpty returns true
        GameDeck<PlayingCard> deck = new StandardGameDeck<>(List.of(
                StandardCard.ACE_SPADES,
                StandardCard.TWO_HEARTS,
                StandardCard.THREE_CLUBS
        ));

        // Each requests 2 cards, strict = true
        DealRequest<Recipient> req1 = new StandardDealRequest<>(recipient1, 2, true);
        DealRequest<Recipient> req2 = new StandardDealRequest<>(recipient2, 2, true);
        DealRequest<Recipient> req3 = new StandardDealRequest<>(recipient3, 2, true);

        List<DealRequest<Recipient>> requests = Arrays.asList(req1, req2, req3);

        StandardCardDealer<GameDeck<PlayingCard>, Recipient> dealer = new StandardCardDealer<>();
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> dealer.deal(deck, requests));
        assertEquals("Not enough cards for recipient recipient2 (requested: 2, cards left: 1)", e.getMessage());
    }

    @Test
    @DisplayName("deal() should distribute all cards strictly with zero cards left in the deck")
    void shouldDealStrictlyWithExactEnoughCards() {
        // 6 standard cards – three aces and three twos
        GameDeck<PlayingCard> deck = new StandardGameDeck<>(List.of(
                StandardCard.ACE_SPADES,
                StandardCard.ACE_HEARTS,
                StandardCard.ACE_CLUBS,
                StandardCard.TWO_SPADES,
                StandardCard.TWO_HEARTS,
                JokerCard.JOKER_1
        ));

        // Each recipient requests exactly 2 cards, strict mode enabled
        DealRequest<Recipient> req1 = new StandardDealRequest<>(recipient1, 2, true);
        DealRequest<Recipient> req2 = new StandardDealRequest<>(recipient2, 2, true);
        DealRequest<Recipient> req3 = new StandardDealRequest<>(recipient3, 2, true);

        List<DealRequest<Recipient>> requests = List.of(req1, req2, req3);

        StandardCardDealer<GameDeck<PlayingCard>, Recipient> dealer = new StandardCardDealer<>();
        var result = dealer.deal(deck, requests);
        Map<Recipient, List<PlayingCard>> allocations = result.getAllocations();

        // Check that each recipient received exactly 2 cards
        assertEquals(2, allocations.get(recipient1).size());
        assertEquals(2, allocations.get(recipient2).size());
        assertEquals(2, allocations.get(recipient3).size());

        // Check that each recipient received expected cards
        assertEquals(List.of(StandardCard.ACE_SPADES, StandardCard.ACE_HEARTS), allocations.get(recipient1));
        assertEquals(List.of(StandardCard.ACE_CLUBS, StandardCard.TWO_SPADES), allocations.get(recipient2));
        assertEquals(List.of(StandardCard.TWO_HEARTS, JokerCard.JOKER_1), allocations.get(recipient3));

        // The deck should now have zero cards left
        assertTrue(deck.isEmpty());
        assertEquals(0, deck.size());
    }

    @Test
    @DisplayName("deal() should distribute all cards when not enough cards and requests are non-strict")
    void shouldDistributeAllCardsWithNonStrictRequests() {
        // The deck contains only 3 cards
        GameDeck<PlayingCard> deck = new StandardGameDeck<>(List.of(
                StandardCard.ACE_SPADES,
                StandardCard.TWO_HEARTS,
                StandardCard.THREE_CLUBS
        ));

        // Three requests: for 2, 5, and 100 cards, all with strict = false
        DealRequest<Recipient> req1 = new StandardDealRequest<>(recipient1, 2, false);
        DealRequest<Recipient> req2 = new StandardDealRequest<>(recipient2, 5, false);
        DealRequest<Recipient> req3 = new StandardDealRequest<>(recipient3, 100, false);

        List<DealRequest<Recipient>> requests = Arrays.asList(req1, req2, req3);

        StandardCardDealer<GameDeck<PlayingCard>, Recipient> dealer = new StandardCardDealer<>();

        var result = dealer.deal(deck, requests);
        Map<Recipient, List<PlayingCard>> allocations = result.getAllocations();

        // The first recipient should get 2 cards
        assertEquals(2, allocations.get(recipient1).size());
        // The second recipient should get the remaining 1 card
        assertEquals(1, allocations.get(recipient2).size());
        // The third recipient should get no cards
        assertEquals(0, allocations.get(recipient3).size());

        // Check that recipients received the expected specific cards
        assertEquals(List.of(StandardCard.ACE_SPADES, StandardCard.TWO_HEARTS), allocations.get(recipient1));
        assertEquals(List.of(StandardCard.THREE_CLUBS), allocations.get(recipient2));
        assertEquals(List.of(), allocations.get(recipient3));

        // The deck should be empty after dealing
        assertTrue(deck.isEmpty());
        assertEquals(0, deck.size());
    }

    @Test
    @DisplayName("deal() should deal 8 cards to three recipients in two rounds, one card per round")
    void shouldDealInRoundsToThreeRecipientsWithEightCards() {
        // Create a deck: 2-9 of Spades (8 cards in order)
        GameDeck<PlayingCard> deck = new StandardGameDeck<>(List.of(
                StandardCard.TWO_SPADES,
                StandardCard.THREE_SPADES,
                StandardCard.FOUR_SPADES,
                StandardCard.FIVE_SPADES,
                StandardCard.SIX_SPADES,
                StandardCard.SEVEN_SPADES,
                StandardCard.EIGHT_SPADES,
                StandardCard.NINE_SPADES
        ));

        // Each recipient requests 1 cards, strict mode
        DealRequest<Recipient> req1 = new StandardDealRequest<>(recipient1, 1, true);
        DealRequest<Recipient> req2 = new StandardDealRequest<>(recipient2, 1, true);
        DealRequest<Recipient> req3 = new StandardDealRequest<>(recipient3, 1, true);

        List<DealRequest<Recipient>> requests = List.of(
                req1, req2, req3,  // first round of dealing
                req1, req2, req3   // second round (same recipients, different requests)
        );

        StandardCardDealer<GameDeck<PlayingCard>, Recipient> dealer = new StandardCardDealer<>();
        var result = dealer.deal(deck, requests);
        Map<Recipient, List<PlayingCard>> allocations = result.getAllocations();

        // Each recipient should get exactly 2 cards (total dealt: 6 of 8 cards)
        assertEquals(2, allocations.get(recipient1).size());
        assertEquals(2, allocations.get(recipient2).size());
        assertEquals(2, allocations.get(recipient3).size());

        // Verify cards are dealt round-robin in deck order:
        // recipient1: TWO_SPADES, FIVE_SPADES
        // recipient2: THREE_SPADES, SIX_SPADES
        // recipient3: FOUR_SPADES, SEVEN_SPADES
        // Cards left: EIGHT_SPADES, NINE_SPADES
        assertEquals(List.of(StandardCard.TWO_SPADES, StandardCard.FIVE_SPADES), allocations.get(recipient1));
        assertEquals(List.of(StandardCard.THREE_SPADES, StandardCard.SIX_SPADES), allocations.get(recipient2));
        assertEquals(List.of(StandardCard.FOUR_SPADES, StandardCard.SEVEN_SPADES), allocations.get(recipient3));

        // Two cards should remain in the deck: EIGHT_SPADES, NINE_SPADES (order preserved)
        assertEquals(2, deck.size());

        List<PlayingCard> deckLeft = deck.exportCards();
        assertEquals(StandardCard.EIGHT_SPADES, deckLeft.get(0));
        assertEquals(StandardCard.NINE_SPADES, deckLeft.get(1));
    }

    @Test
    @DisplayName("deal() should handle zero amount in requests")
    void shouldHandleZeroAmountInRequest() {
        GameDeck<PlayingCard> deck = new StandardGameDeck<>(List.of(
                StandardCard.ACE_SPADES,
                StandardCard.TWO_HEARTS,
                StandardCard.THREE_CLUBS
        ));

        DealRequest<Recipient> req1 = new StandardDealRequest<>(recipient1, 0, true);
        DealRequest<Recipient> req2 = new StandardDealRequest<>(recipient2, 0, false);

        List<DealRequest<Recipient>> requests = Arrays.asList(req1, req2);

        StandardCardDealer<GameDeck<PlayingCard>, Recipient> dealer = new StandardCardDealer<>();
        var result = dealer.deal(deck, requests);
        Map<Recipient, List<PlayingCard>> allocations = result.getAllocations();

        assertNotNull(allocations.get(recipient1));
        assertTrue(allocations.get(recipient1).isEmpty());

        assertNotNull(allocations.get(recipient2));
        assertTrue(allocations.get(recipient2).isEmpty());

        assertEquals(3, deck.size());

        List<PlayingCard> cardsLeft = deck.exportCards();
        assertEquals(StandardCard.ACE_SPADES, cardsLeft.get(0));
        assertEquals(StandardCard.TWO_HEARTS, cardsLeft.get(1));
        assertEquals(StandardCard.THREE_CLUBS, cardsLeft.get(2));
    }

    @Test
    @DisplayName("deal() should handle zero amount in requests for empty deck")
    void shouldHandleZeroAmountInRequestForEmptyDeck() {
        GameDeck<PlayingCard> deck = new StandardGameDeck<>(List.of());

        DealRequest<Recipient> req1 = new StandardDealRequest<>(recipient1, 0, true);
        DealRequest<Recipient> req2 = new StandardDealRequest<>(recipient2, 0, false);

        List<DealRequest<Recipient>> requests = Arrays.asList(req1, req2);

        StandardCardDealer<GameDeck<PlayingCard>, Recipient> dealer = new StandardCardDealer<>();
        var result = dealer.deal(deck, requests);
        Map<Recipient, List<PlayingCard>> allocations = result.getAllocations();

        assertNotNull(allocations.get(recipient1));
        assertTrue(allocations.get(recipient1).isEmpty());

        assertNotNull(allocations.get(recipient2));
        assertTrue(allocations.get(recipient2).isEmpty());

        assertTrue(deck.isEmpty());
    }
}