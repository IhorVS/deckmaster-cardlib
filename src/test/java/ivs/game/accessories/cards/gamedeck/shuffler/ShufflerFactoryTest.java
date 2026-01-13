package ivs.game.accessories.cards.gamedeck.shuffler;

import ivs.game.accessories.cards.core.type.PlayingCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class ShufflerFactoryTest {

    @Test
    @DisplayName("getInPlaceShuffler should return a non-null StandardCardShuffler")
    void getInPlaceShufflerReturnsNonNull() {
        InPlaceShuffler<PlayingCard> shuffler = ShufflerFactory.getInPlaceShuffler();
        assertNotNull(shuffler, "getInPlaceShuffler() must not return null");
        assertEquals(StandardCardShuffler.class, shuffler.getClass(), "Should return StandardCardShuffler instance");
    }

    @Test
    @DisplayName("getCopyingShuffler should return a non-null StandardCardShuffler")
    void getCopyingShufflerReturnsNonNull() {
        CopyingShuffler<PlayingCard> shuffler = ShufflerFactory.getCopyingShuffler();
        assertNotNull(shuffler, "getCopyingShuffler() must not return null");
        assertEquals(StandardCardShuffler.class, shuffler.getClass(), "Should return StandardCardShuffler instance");
    }

    @Test
    @DisplayName("Different calls should produce different instances")
    void eachCallReturnsNewInstance() {
        InPlaceShuffler<PlayingCard> shuffler1 = ShufflerFactory.getInPlaceShuffler();
        InPlaceShuffler<PlayingCard> shuffler2 = ShufflerFactory.getInPlaceShuffler();
        assertNotSame(shuffler1, shuffler2, "Each getInPlaceShuffler() should return a new instance");
    }
}