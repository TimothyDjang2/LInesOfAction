public class Location {
    
    private int x, y;

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

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void shift(int x, int y) {
        this.x += x;
        this.y += y;
    }

    @Override
    public boolean equals(Object l) {
        if (l == null) return false;
        if (l.getClass() != this.getClass()) return false;
        Location loc = (Location)l;
        if (loc.getX() == this.x && loc.getY() == this.y) return true;
        return false;
    }

}
