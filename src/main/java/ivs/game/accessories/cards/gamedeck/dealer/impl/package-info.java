/**
 * Standard implementations of dealing requests, results, and sequential dealing.
 * <p>
 * Requests are processed in list order, drawing consecutive cards for each
 * request. Repeated recipients accumulate allocations. Strict requests fail
 * with {@link java.lang.IllegalStateException} when insufficient cards remain;
 * lenient requests draw up to the available amount. A later failure does not
 * roll back earlier draws. Round-robin dealing can be expressed as repeated
 * one-card requests.
 * Standard requests are immutable; standard results copy their allocation
 * map and lists into unmodifiable containers. Dealing returns allocations
 * without modifying recipient objects or card holders.
 */
package ivs.game.accessories.cards.gamedeck.dealer.impl;
