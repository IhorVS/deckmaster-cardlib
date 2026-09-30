/**
 * Card shuffling contracts and factory methods.
 * <p>
 * {@link ivs.game.accessories.cards.gamedeck.shuffler.InPlaceShuffler} changes a
 * supplied mutable list, while
 * {@link ivs.game.accessories.cards.gamedeck.shuffler.CopyingShuffler} creates a
 * shuffled list without changing the source collection. Obtain standard
 * implementations through
 * {@link ivs.game.accessories.cards.gamedeck.shuffler.ShufflerFactory}.
 * The standard implementation uses thread-local randomness and exposes no
 * seed configuration. Concurrent access to a mutated list remains the caller's
 * responsibility.
 */
package ivs.game.accessories.cards.gamedeck.shuffler;
