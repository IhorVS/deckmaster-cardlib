/**
 * Ordered card shoes with a remaining-card cut threshold.
 * <p>
 * {@link ivs.game.accessories.cards.gamedeck.cardshoe.StandardCardShoe} copies
 * supplied cards, supports repeated card values, and delegates drawing to an
 * internal game deck. Multiple decks must be assembled by the caller.
 * The cut card is out when the number of remaining cards is strictly less
 * than {@code cutCardPosition}; zero disables this signal. Reaching the
 * threshold neither prevents drawing nor triggers automatic reshuffling.
 * {@link ivs.game.accessories.cards.gamedeck.cardshoe.CutCardCalculator}
 * produces a randomized position from a base fraction and deviation.
 * When passed to a shoe, the result is a remaining-card threshold.
 * The read-only interface does not guarantee an immutable backing object.
 */
package ivs.game.accessories.cards.gamedeck.cardshoe;
