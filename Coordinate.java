public class Coordinate {
    private int i;
    private int j;
    private Cluster<Integer[]> c;
    public Coordinate(int i, int j) {
        this.i = i;
        this.j = j;
        this.c = null;
    }

    public void setCluster(Cluster<Integer[]> c) {
        this.c = c;
    }

    public Cluster<Integer[]> getCluster() {
        return c;
    }

    @Override
    public String toString() {
        return "[" + i + ", " + j + "]";
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (other instanceof Coordinate) {
            Coordinate otherCoordinate = (Coordinate) other;

            if (otherCoordinate.getRow() == this.getRow() && otherCoordinate.getColumn() == this.getColumn()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return 31 * i * j;  // 31 is prime, thus reduces the number of collisions (not many numbers go in to 31 evenly)
    }

    public int getRow() {
        return i;
    }

    public int getColumn() {
        return j;
    }
    
}
