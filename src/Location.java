public class Location {
    
    private int x, y;

    /**
     * Stores an x and a y position.
     */
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void shift(int x, int y) {
        this.x += x;
        this.y += y;
    }
}
