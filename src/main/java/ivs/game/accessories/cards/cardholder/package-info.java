/**
 * Mutable card holders, read-only query contracts, and count summaries.
 * <p>
 * {@link ivs.game.accessories.cards.cardholder.StandardCardHolder} preserves
 * insertion order and permits duplicate card values. Its supplied comparator
 * controls min/max and relative searches without sorting the stored cards.
 * Suit-based searches ignore stored jokers; reference-card searches require
 * a non-joker reference. Presence queries do not enforce duplicate quantities.
 * Summaries are snapshots counting occurrences by card, rank, suit, and joker.
 * Mutable holders require external synchronization for concurrent modification.
 */
package ivs.game.accessories.cards.cardholder;
