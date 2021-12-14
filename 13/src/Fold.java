public class Fold {
    private enum Axis {
        X,
        Y
    }

    private final Axis axis;
    private final int pos;

    public Fold(char axis, int pos) {
        this.axis = Axis.valueOf(String.valueOf(axis).toUpperCase());
        this.pos = pos;
    }

    public boolean isXAxis() {
        return axis == Axis.X;
    }

    public boolean isYAxis() {
        return axis == Axis.Y;
    }

    public int position() {
        return pos;
    }
}
