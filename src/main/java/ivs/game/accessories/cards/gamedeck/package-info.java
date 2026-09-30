/**
 * Deck templates and ordered, mutable game decks.
 * <p>
 * {@link ivs.game.accessories.cards.gamedeck.DeckTemplate} produces unique card
 * sets with no guaranteed iteration order. Templates contain 24, 32, 36, 52,
 * 54, or 56 cards; the 56-card template contains one full deck and four jokers.
 * {@link ivs.game.accessories.cards.gamedeck.StandardGameDeck} copies its input
 * collection in iteration order, draws from the first element, and exports an
 * unmodifiable snapshot. Insufficient draws and empty single-card peeks throw
 * {@link ivs.game.accessories.cards.gamedeck.GameDeckException}.
 * The read-only deck interface does not guarantee an immutable backing object.
 */
package ivs.game.accessories.cards.gamedeck;
