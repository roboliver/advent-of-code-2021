public class Seafloor {
    private SeaCucumber[][] seaCucumbers;

    public Seafloor(SeaCucumber[][] seaCucumbers) {
        this.seaCucumbers = seaCucumbersClone(seaCucumbers);
    }

    public boolean step() {
        boolean movedEast = stepEast();
        boolean movedSouth = stepSouth();
        return movedEast || movedSouth;
    }

    private boolean stepEast() {
        boolean moved = false;
        SeaCucumber[][] seaCucumbersNew = seaCucumbersClone(seaCucumbers);
        for (int row = 0; row < seaCucumbers.length; row++) {
            for (int col = 0; col < seaCucumbers[row].length; col++) {
                if (seaCucumbers[row][col] == SeaCucumber.EAST) {
                    int colMoveTo = (col == seaCucumbers[row].length - 1) ? 0 : col + 1;
                    if (seaCucumbers[row][colMoveTo] == null) {
                        seaCucumbersNew[row][col] = null;
                        seaCucumbersNew[row][colMoveTo] = SeaCucumber.EAST;
                        moved = true;
                    }
                }
            }
        }
        seaCucumbers = seaCucumbersNew;
        return moved;
    }

    private boolean stepSouth() {
        boolean moved = false;
        SeaCucumber[][] seaCucumbersNew = seaCucumbersClone(seaCucumbers);
        for (int row = 0; row < seaCucumbers.length; row++) {
            for (int col = 0; col < seaCucumbers[row].length; col++) {
                if (seaCucumbers[row][col] == SeaCucumber.SOUTH) {
                    int rowMoveTo = (row == seaCucumbers.length - 1) ? 0 : row + 1;
                    if (seaCucumbers[rowMoveTo][col] == null) {
                        seaCucumbersNew[row][col] = null;
                        seaCucumbersNew[rowMoveTo][col] = SeaCucumber.SOUTH;
                        moved = true;
                    }
                }
            }
        }
        seaCucumbers = seaCucumbersNew;
        return moved;
    }

    private static SeaCucumber[][] seaCucumbersClone(SeaCucumber[][] seaCucumbers) {
        SeaCucumber[][] seaCucumbersNew = new SeaCucumber[seaCucumbers.length][];
        for (int row = 0; row < seaCucumbers.length; row++) {
            seaCucumbersNew[row] = new SeaCucumber[seaCucumbers[row].length];
            for (int col = 0; col < seaCucumbers[row].length; col++) {
                seaCucumbersNew[row][col] = seaCucumbers[row][col];
            }
        }
        return seaCucumbersNew;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (int row = 0; row < seaCucumbers.length; row++) {
            if (row > 0) {
                buf.append('\n');
            }
            for (int col = 0; col < seaCucumbers[row].length; col++) {
                SeaCucumber seaCucumber = seaCucumbers[row][col];
                if (seaCucumber == SeaCucumber.EAST) {
                    buf.append('>');
                } else if (seaCucumber == SeaCucumber.SOUTH) {
                    buf.append('v');
                } else {
                    buf.append('.');
                }
            }
        }
        return buf.toString();
    }
}
