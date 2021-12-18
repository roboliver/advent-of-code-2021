public class Target {
    private final int xMin;
    private final int xMax;
    private final int yMin;
    private final int yMax;

    public Target(int xMin, int xMax, int yMin, int yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    public boolean isXOnTarget(int x) {
        return x >= xMin && x <= xMax;
    }

    public boolean isYOnTarget(int y) {
        return y >= yMin && y <= yMax;
    }

    public boolean isPointOnTarget(int x, int y) {
        return isXOnTarget(x) && isYOnTarget(y);
    }

    public int getXMin() {
        return this.xMin;
    }

    public int getXMax() {
        return this.xMax;
    }

    public int getYMin() {
        return this.yMin;
    }

    public int getYMax() {
        return this.yMax;
    }
}
