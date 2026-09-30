/**
 * Contracts for dealing cards to recipients using allocation requests.
 * <p>
 * Requests specify a recipient, amount, and strictness. Results associate
 * recipients with allocated card lists. Recipient implementations must provide
 * stable equality and hash codes for use as map keys.
 * Standard implementations are provided in the {@code dealer.impl} subpackage.
 */
package ivs.game.accessories.cards.gamedeck.dealer;
