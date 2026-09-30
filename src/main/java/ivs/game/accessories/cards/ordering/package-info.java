/**
 * Configurable ordering of suits, ranks, and playing cards.
 * <p>
 * Weight comparators support explicit weights or an ascending sequence of
 * supported values. Values absent from a custom order cannot be compared.
 * {@link ivs.game.accessories.cards.ordering.PlayingCardComparator} compares
 * suit first, then rank. Comparison involving a joker throws
 * {@link java.lang.UnsupportedOperationException} unless its protected
 * {@code compareWithJoker} method is overridden.
 */
package ivs.game.accessories.cards.ordering;
