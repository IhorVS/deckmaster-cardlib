/**
 * Parsing and formatting of card, rank, suit, and joker symbols.
 * <p>
 * Standard cards use rank followed by suit, such as {@code AS} or {@code TH};
 * Ten is {@code T}, and suits are {@code S}, {@code C}, {@code D}, {@code H}.
 * Jokers use {@code R1} through {@code R4}.
 * {@link ivs.game.accessories.cards.core.id.format.CardSymbol} handles standard
 * cards, while {@link ivs.game.accessories.cards.core.id.format.DeckSymbol}
 * handles both standard cards and jokers. Symbols are case-sensitive.
 * Bulk parsing uses the supplied delimiter as a regular expression.
 */
package ivs.game.accessories.cards.core.id.format;
