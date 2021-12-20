import java.util.Objects;

public class Pair implements Element {
    private Pair parent = null;
    private boolean isLeftNotRight;
    private Element left;
    private Element right;
    private int depth;

    public Pair(Element left, Element right, int depth) {
        setLeft(left);
        setRight(right);
        if (depth < 0) {
            throw new IllegalArgumentException("depth cannot be negative");
        }
        this.depth = depth;
    }

    @Override
    public void setParent(Pair parent, boolean isLeftNotRight) {
        this.isLeftNotRight = isLeftNotRight;
        this.parent = parent;
    }

    @Override
    public void increaseDepth() {
        depth++;
        left.increaseDepth();
        right.increaseDepth();
    }

    @Override
    public boolean checkExplode() {
        if (depth > 3) {
            //System.out.println("uh oh! exploding the pair " + toString());
            //System.out.println("our depth is " + depth);
            if (parent != null) {
                //System.out.println("and our parent is non-null.");
            } else {
                //System.out.println("and yet our parent is null. curious!");
            }
            explode();
            return true;
        } else {
            return left.checkExplode() || right.checkExplode();
        }
    }

    private void explode() {
        if (parent != null) {
            //System.out.println("parent is non-null.");
            //System.out.println("pushing " + left.getValue() + " left from a " + (isLeftNotRight ? "left" : "right") + " child.");
            //System.out.println("pushing " + right.getValue() + " right from a " + (isLeftNotRight ? "left" : "right") + " child.");
            parent.pushShrapnelLeft(left.getMagnitude(), isLeftNotRight);
            parent.pushShrapnelRight(right.getMagnitude(), isLeftNotRight);
            parent.childExploded(isLeftNotRight);
        }
    }

    private void pushShrapnelLeft(int shrapnel, boolean isChildLeftNotRight) {
        if (isChildLeftNotRight) {
            if (parent != null) {
                parent.pushShrapnelLeft(shrapnel, isLeftNotRight);
            }
        } else {
            left.addShrapnelFromRight(shrapnel);
        }
    }

    private void pushShrapnelRight(int shrapnel, boolean isChildLeftNotRight) {
        if (isChildLeftNotRight) {
            right.addShrapnelFromLeft(shrapnel);
        } else {
            if (parent != null) {
                parent.pushShrapnelRight(shrapnel, isLeftNotRight);
            }
        }
    }

    private void childExploded(boolean isChildLeftNotRight) {
        RegularNumber exploded = new RegularNumber(0, depth + 1);
        if (isChildLeftNotRight) {
            setLeft(exploded);
        } else {
            setRight(exploded);
        }
    }

    @Override
    public void addShrapnelFromLeft(int shrapnel) {
        left.addShrapnelFromLeft(shrapnel);
    }

    @Override
    public void addShrapnelFromRight(int shrapnel) {
        //System.out.println("our left node is " + left.toString() + " and we are pushing shrapnel to it from the right.");
        right.addShrapnelFromRight(shrapnel);
    }

    @Override
    public boolean checkSplit() {
        return left.checkSplit() || right.checkSplit();
    }

    public void childSplit(Pair split, boolean isChildLeftNotRight) {
        if (isChildLeftNotRight) {
            setLeft(split);
        } else {
            setRight(split);
        }
    }

    private void setLeft(Element left) {
        this.left = Objects.requireNonNull(left);
        left.setParent(this, true);
    }

    private void setRight(Element right) {
        this.right = Objects.requireNonNull(right);
        right.setParent(this, false);
    }

    @Override
    public int getMagnitude() {
        return 3 * left.getMagnitude() + 2 * right.getMagnitude();
    }

    @Override
    public String toString() {
        return "[" + left + "," + right + "]";
    }

    @Override
    public Pair duplicate() {
        return new Pair(left.duplicate(), right.duplicate(), depth);
    }
}
