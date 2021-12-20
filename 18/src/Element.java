public interface Element {
    public void setParent(Pair parent, boolean isLeftNotRight);

    public void increaseDepth();

    public boolean checkExplode();

    public void addShrapnelFromLeft(int shrapnel);

    public void addShrapnelFromRight(int shrapnel);

    public boolean checkSplit();

    public int getMagnitude();

    public Element duplicate();
}
