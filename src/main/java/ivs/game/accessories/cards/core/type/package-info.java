/**
 * Immutable playing-card values, ranks, suits, and colors.
 * <p>
 * {@link ivs.game.accessories.cards.core.type.PlayingCard} is sealed and
 * implemented by the standard-card and joker enums. Standard cards provide
 * rank and suit; joker rank and suit access throws
 * {@link java.lang.UnsupportedOperationException}. Repeated occurrences of a
 * card in a multi-deck collection may share the same enum constant.
 * {@link ivs.game.accessories.cards.core.type.CardExportable} describes card export.
 */
package ivs.game.accessories.cards.core.type;
