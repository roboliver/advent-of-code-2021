public class RegularNumber implements Element {
    private Pair parent;
    private int value;
    private int depth;
    private boolean isLeftNotRight;

    public RegularNumber(int value, int depth) {
        this.value = value;
        this.depth = depth;
    }

    @Override
    public void setParent(Pair parent, boolean isLeftNotRight) {
        this.parent = parent;
        this.isLeftNotRight = isLeftNotRight;
    }

    @Override
    public void increaseDepth() {
        depth++;
    }

    @Override
    public boolean checkExplode() {
        return false;
    }

    @Override
    public void addShrapnelFromLeft(int shrapnel) {
        this.value += shrapnel;
    }

    @Override
    public void addShrapnelFromRight(int shrapnel) {
        this.value += shrapnel;
    }

    @Override
    public boolean checkSplit() {
        if (value > 9) {
            split();
            return true;
        } else {
            return false;
        }
    }

    private void split() {
        RegularNumber left = new RegularNumber(value / 2, depth + 1);
        RegularNumber right = new RegularNumber(left.value * 2 == value ? left.value : left.value + 1, depth + 1);
        parent.childSplit(new Pair(left, right, depth), isLeftNotRight);
    }

    @Override
    public int getMagnitude() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public RegularNumber duplicate() {
        return new RegularNumber(value, depth);
    }
}
