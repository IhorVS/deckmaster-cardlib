package ivs.game.accessories.cards.core.type;

import ivs.game.accessories.cards.core.id.ColorId;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Represents the color of a playing card.
 * In a standard deck, cards can be either black (spades and clubs) or red (hearts and diamonds).
 */
@Getter
@RequiredArgsConstructor
public enum Color {
    BLACK(ColorId.BLACK),
    RED(ColorId.RED);

    private final int id;

    /**
     * Returns the Color enum constant associated with the given color ID.
     *
     * @param colorId the color ID to look up
     * @return the Color constant for the given ID
     * @throws IllegalArgumentException if the color ID is invalid
     */
    public static Color getById(int colorId) {
        return switch (colorId) {
            case ColorId.BLACK -> BLACK;
            case ColorId.RED -> RED;
            default -> throw new IllegalArgumentException("Invalid color ID: " + colorId);
        };
    }

    /**
     * Returns the opposite color.
     *
     * @param color the color to get the opposite for
     * @return RED for BLACK, or BLACK for RED
     */
    public static Color getOpposite(@NonNull Color color) {
        return switch (color) {
            case BLACK -> RED;
            case RED -> BLACK;
        };
    }
}