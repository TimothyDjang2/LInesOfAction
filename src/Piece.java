/**
 * Used to store coordinates of renderable objects, (tokens and valid move squares.)
 * Makes it so I don't have to iterate over a grid of 64 possibly empty items, but rather a smaller 1D-array of definitely renderable items.
 */
public class Piece {

    private int x, y;
    private int type;

    /**
     * Constructor for Piece.
     * @param type - 0 for black, 1 for white, 2 for move selection.
     */
    public Piece(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getType() {
        return type;
    }

}