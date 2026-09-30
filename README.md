# cards-core

Java building blocks for card games: playing cards, deck templates, shuffling,
dealing, multi-deck shoes, and cardholders with search and counting operations.

Use these components to manage cards while implementing game rules, turns,
scoring, and win conditions in your application.

## Requirements

- Java 21
- Maven

The project uses Apache Commons Lang and Lombok. Lombok is a build-time dependency
with `provided` scope.

## Build and use locally

Run from the project root:

```shell
mvn clean install
```

Then add the locally installed artifact to your Maven project:

```xml
<dependency>
    <groupId>ivs.games.accessories</groupId>
    <artifactId>cards-core</artifactId>
    <version>1.0.0</version>
</dependency>
```

These coordinates match the project's `pom.xml`; they do not imply availability
in a public Maven repository.

## Components

Package names below are relative to `ivs.game.accessories.cards`.

| Package                | Main types                                                              | Purpose                                                    |
| ---------------------- | ----------------------------------------------------------------------- | ---------------------------------------------------------- |
| `core.type`            | `PlayingCard`, `StandardCard`, `JokerCard`, `Rank`, `Suit`, `Color`     | Immutable card values and their properties                 |
| `core.id`              | `CardId`, `RankId`, `SuitId`, `JokerId`, `ColorId`                      | Numeric identifiers, validation, and conversions           |
| `core.id.format`       | `CardSymbol`, `DeckSymbol`, `RankSymbol`, `SuitSymbol`, `JokerSymbol`   | Parse and format text symbols                              |
| `ordering`             | `PlayingCardComparator`, `RankWeightComparator`, `SuitWeightComparator` | Configure suit and rank ordering                           |
| `cardholder`           | `CardHolder`, `CardViewer`, `CardSummary`, `StandardCardHolder`         | Store cards, search within a suit, and count occurrences   |
| `gamedeck`             | `DeckTemplate`, `GameDeck`, `StandardGameDeck`                          | Create card sets and draw from an ordered deck             |
| `gamedeck.shuffler`    | `ShufflerFactory`, `InPlaceShuffler`, `CopyingShuffler`                 | Shuffle a mutable list or create a shuffled copy           |
| `gamedeck.dealer`      | `CardDealer`, `Recipient`, `DealRequest`, `DealResult`                  | Describe dealing requests and allocations                  |
| `gamedeck.dealer.impl` | `StandardCardDealer`, `StandardDealRequest`, `StandardDealResult`       | Sequential dealing and immutable request/result containers |
| `gamedeck.cardshoe`    | `CardShoe`, `StandardCardShoe`, `CutCardCalculator`                     | Draw from a shoe and monitor a cut-card threshold          |

## Cards and symbols

`PlayingCard` is a sealed interface implemented by the `StandardCard` and
`JokerCard` enums. A card value identifies a card type; repeated occurrences in a
multi-deck shoe can reference the same enum constant.

| Property          | Representation                                          |
| ----------------- | ------------------------------------------------------- |
| Ranks             | `2`–`9`, `T`, `J`, `Q`, `K`, `A`                        |
| Suits             | `S` (spades), `C` (clubs), `D` (diamonds), `H` (hearts) |
| Standard cards    | Rank followed by suit: `AS`, `TH`, `7D`                 |
| Jokers            | `R1`, `R2`, `R3`, `R4`                                  |
| Standard card IDs | `0`–`51`, grouped by suit, then rank from Two to Ace    |
| Joker IDs         | `52`–`55`                                               |

```java
import ivs.game.accessories.cards.core.id.format.DeckSymbol;
import ivs.game.accessories.cards.core.type.JokerCard;
import ivs.game.accessories.cards.core.type.StandardCard;

StandardCard ace = StandardCard.getBySymbol("AS");
JokerCard joker = JokerCard.getBySymbol("R1");
int aceId = DeckSymbol.parse("AS");
String symbol = DeckSymbol.format(aceId); // AS
```

Jokers have a color, but no rank or suit. Calling `getRank()` or `getSuit()` on
`JokerCard` throws `UnsupportedOperationException`; check `isJoker()` first.

## Deck templates

| Template   | Cards | Contents                               |
| -----------| ----: | -------------------------------------- |
| `MAXIMAL`  |    56 | Full deck plus all four jokers         |
| `EXTENDED` |    54 | Full deck plus `JOKER_1` and `JOKER_2` |
| `FULL`     |    52 | Two through Ace, all four suits        |
| `SHORT`    |    36 | Six through Ace, all four suits        |
| `SMALL`    |    32 | Seven through Ace, all four suits      |
| `TINY`     |    24 | Nine through Ace, all four suits       |

`DeckTemplate.get()` returns a set of unique card values with no guaranteed
iteration order. Custom templates can be created with
`DeckTemplate.get(fromRank, toRank, jokers...)`.

## Create, shuffle, and draw

```java
import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.gamedeck.DeckTemplate;
import ivs.game.accessories.cards.gamedeck.StandardGameDeck;
import ivs.game.accessories.cards.gamedeck.shuffler.ShufflerFactory;
import java.util.List;

List<PlayingCard> shuffled = ShufflerFactory
        .<PlayingCard>getCopyingShuffler()
        .shuffleCopy(DeckTemplate.FULL.get());

StandardGameDeck<PlayingCard> deck = new StandardGameDeck<>(shuffled);
PlayingCard top = deck.peekTop();
List<PlayingCard> hand = deck.draw(5);
int remaining = deck.size(); // 47
```

The first input element becomes the top card. Construction copies the collection;
drawing removes cards from the deck. `exportCards()` returns an unmodifiable
snapshot in top-first order. `draw(count)` requires the full requested amount;
an insufficient deck causes `GameDeckException` before any cards are removed.
Drawing or peeking a single card from an empty deck also causes `GameDeckException`.

For an explicitly ordered, unshuffled full deck, use
`new StandardGameDeck<>(StandardCard.stream().toList())`.

## Deal cards to recipients

```java
import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.gamedeck.DeckTemplate;
import ivs.game.accessories.cards.gamedeck.StandardGameDeck;
import ivs.game.accessories.cards.gamedeck.dealer.DealResult;
import ivs.game.accessories.cards.gamedeck.dealer.Recipient;
import ivs.game.accessories.cards.gamedeck.dealer.impl.StandardCardDealer;
import ivs.game.accessories.cards.gamedeck.dealer.impl.StandardDealRequest;
import ivs.game.accessories.cards.gamedeck.shuffler.ShufflerFactory;
import java.util.List;

record Player(String name) implements Recipient {}

Player alice = new Player("Alice");
Player bob = new Player("Bob");
var deck = new StandardGameDeck<PlayingCard>(ShufflerFactory
        .<PlayingCard>getCopyingShuffler()
        .shuffleCopy(DeckTemplate.FULL.get()));
var dealer = new StandardCardDealer<StandardGameDeck<PlayingCard>, Player>();

DealResult<Player> result = dealer.deal(deck, List.of(
        StandardDealRequest.strictOf(alice, 5),
        StandardDealRequest.strictOf(bob, 5)
));
List<PlayingCard> aliceCards = result.getAllocations().get(alice);
```

Requests are processed in list order: Alice receives five consecutive cards,
then Bob receives five. For round-robin dealing, supply repeated one-card requests
in the desired recipient order. Multiple requests for one recipient accumulate.

- A strict request throws `IllegalStateException` if not enough cards remain.
- A lenient request receives as many cards as remain, up to its requested amount.
- Validation occurs per request. A later failure does not undo earlier draws.
- Allocations are returned as an unmodifiable map of unmodifiable lists; the dealer
  does not automatically insert them into cardholders.

Recipients must have stable, correct `equals()` and `hashCode()` implementations.

## Hold, query, and count cards

```java
import ivs.game.accessories.cards.cardholder.CardSummary;
import ivs.game.accessories.cards.cardholder.StandardCardHolder;
import ivs.game.accessories.cards.core.type.StandardCard;
import ivs.game.accessories.cards.core.type.Suit;
import ivs.game.accessories.cards.ordering.PlayingCardComparator;
import ivs.game.accessories.cards.ordering.RankWeightComparator;
import ivs.game.accessories.cards.ordering.SuitWeightComparator;
import java.util.List;

var comparator = new PlayingCardComparator(
        SuitWeightComparator.ofOrderSCDH(),
        RankWeightComparator.aceLowOrder());
var holder = new StandardCardHolder(comparator);
holder.addAll(List.of(StandardCard.ACE_SPADES, StandardCard.KING_SPADES));

var lowestSpade = holder.findMin(Suit.SPADES); // Ace with this rank order
var nextSpade = holder.findClosestHigher(StandardCard.ACE_SPADES); // King
CardSummary summary = holder.getSummary();
int spades = summary.getSuitQty(Suit.SPADES); // 2
```

The holder preserves insertion order and accepts duplicate card values. Its
comparator controls search operations; it does not sort the stored list when
cards are added. Summaries are snapshots and count repeated occurrences.
`containsAll()` checks presence, not duplicate quantities; use counts when
multiplicity matters.

`PlayingCardComparator` compares suit first, then rank. Joker comparison requires
an override of its protected `compareWithJoker()` method. Holder searches by suit
exclude stored jokers; methods taking a reference card require a non-joker reference.

## Multi-deck shoes

```java
import ivs.game.accessories.cards.core.type.PlayingCard;
import ivs.game.accessories.cards.gamedeck.DeckTemplate;
import ivs.game.accessories.cards.gamedeck.cardshoe.StandardCardShoe;
import ivs.game.accessories.cards.gamedeck.shuffler.ShufflerFactory;
import java.util.ArrayList;
import java.util.List;

List<PlayingCard> cards = new ArrayList<>();
for (int i = 0; i < 6; i++) {
    cards.addAll(DeckTemplate.FULL.get());
}
var shuffled = ShufflerFactory.<PlayingCard>getCopyingShuffler().shuffleCopy(cards);
var shoe = new StandardCardShoe<>(shuffled, 52);
PlayingCard next = shoe.draw();
boolean cutCardOut = shoe.isCutCardOut();
```

Here the shoe starts with 312 cards. A cut-card position of 52 means
`isCutCardOut()` becomes true when **fewer than 52 cards remain**. It is a remaining-card
threshold, not a count of cards drawn from the top. `NO_CUT_CARD` (0) disables the
signal. Reaching the threshold does not stop drawing or reshuffle automatically.

`CutCardCalculator` generates a randomized integer from a base fraction and a
deviation (defaults: 0.80 and 0.04). When its result is used as `cutCardPosition`,
that number is interpreted as the remaining-card threshold described above.

## State and concurrency

`ImmutableGameDeck` and `ImmutableCardShoe` expose read-only operations; they do
not guarantee that the underlying object cannot change. Deck and shoe copy
constructors copy their containers while sharing immutable card enum values.

Mutable decks, shoes, and holders require external coordination when shared
across threads. Shufflers do not provide a seed configuration in the current API.
