public class Basin {
    private int size = 0;
    private int lowpoint = Integer.MAX_VALUE;

    public void addPoint(int height) {
        size++;
        lowpoint = Math.min(lowpoint, height);
    }

    public int size() {
        return this.size;
    }

    public int lowpointRiskLevel() {
        return lowpoint + 1;
    }
}
